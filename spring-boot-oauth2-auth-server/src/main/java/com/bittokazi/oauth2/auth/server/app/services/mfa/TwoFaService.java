package com.bittokazi.oauth2.auth.server.app.services.mfa;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.bittokazi.oauth2.auth.server.app.models.tenant.User;
import com.bittokazi.oauth2.auth.server.app.models.tenant.mfa.TwoFASecretPayload;
import com.bittokazi.oauth2.auth.server.app.models.tenant.mfa.UserTrustedDevice;
import com.bittokazi.oauth2.auth.server.app.models.tenant.mfa.UserTwoFaSecret;
import com.bittokazi.oauth2.auth.server.app.repositories.master.TenantRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.UserRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.mfa.UserTrustedDeviceRepository;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.mfa.UserTwoFaSecretRepository;
import com.bittokazi.oauth2.auth.server.app.services.base.RestResponseGenerator;
import com.bittokazi.oauth2.auth.server.config.TenantContext;
import com.bittokazi.oauth2.auth.server.utils.Utils;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.warrenstrange.googleauth.GoogleAuthenticator;
import com.warrenstrange.googleauth.GoogleAuthenticatorKey;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TwoFaService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserTwoFaSecretRepository userTwoFaSecretRepository;

    @Autowired
    private UserTrustedDeviceRepository userTrustedDeviceRepository;

    public Object generateSecret(HttpServletRequest httpServletRequest) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
        Optional<UserTwoFaSecret> faSecret = userTwoFaSecretRepository.findByUserId(userOptional.get().getId());
        if (faSecret.isPresent()) {
            return ResponseEntity.status(403).build();
        }
        List<String> scratchCodes = new ArrayList<String>();
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        final GoogleAuthenticatorKey key = gAuth.createCredentials();
        for (int i = 0; i < 5; i++) {
            scratchCodes.add(Utils.randomNumberGenerator(20));
        }
        return new TwoFASecretPayload().setSecret(key.getKey()).setEnabled(userOptional.get().getTwoFaEnabled())
                .setTenantName(TenantContext.getCurrentTenant())
                .setScratchCodes(scratchCodes);
    }

    public Object regenerateScratchCode(HttpServletRequest httpServletRequest,
                                        HttpServletResponse httpServletResponse) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
        Optional<UserTwoFaSecret> faSecret = userTwoFaSecretRepository.findByUserId(userOptional.get().getId());
        if (faSecret.isPresent()) {
            List<String> scratchCodes = new ArrayList<String>();
            for (int i = 0; i < 5; i++) {
                scratchCodes.add(Utils.randomNumberGenerator(20));
            }
            UserTwoFaSecret userTwoFaSecret = faSecret.get();
            userTwoFaSecret.setScratchCodes(
                    new Gson().toJson(scratchCodes.stream().map(code -> BCrypt.hashpw(code, BCrypt.gensalt())).toList())
            );
            userTwoFaSecret = userTwoFaSecretRepository.save(userTwoFaSecret);
            return new TwoFASecretPayload().setScratchCodes(scratchCodes);
        }
        return RestResponseGenerator.notFound(httpServletResponse);
    }

    public Object enable2FA(TwoFASecretPayload twoFASecretPayload, HttpServletRequest httpServletRequest,
                            HttpServletResponse httpServletResponse) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());

        if(userOptional.get().getTwoFaEnabled()) return ResponseEntity.status(403).build();

        UserTwoFaSecret userTwoFaSecret = new UserTwoFaSecret();
        userTwoFaSecret.setUser(userOptional.get());
        userTwoFaSecret.setSecret(twoFASecretPayload.getSecret());
        userTwoFaSecret.setScratchCodes(new Gson().toJson(twoFASecretPayload.getScratchCodes().stream().map(code -> BCrypt.hashpw(code, BCrypt.gensalt())).toList()));
        User user = userOptional.get();
        if (validate2FA(twoFASecretPayload.getCode(), userTwoFaSecret.getSecret())) {
            userTwoFaSecretRepository.save(userTwoFaSecret);
            user.setTwoFaEnabled(true);
            userRepository.save(user);
        }
        return user;
    }

    @Transactional
    public Object disable2FA(HttpServletRequest httpServletRequest) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
        Optional<UserTwoFaSecret> faSecret = userTwoFaSecretRepository.findByUserId(userOptional.get().getId());
        User user = userOptional.get();
        user.setTwoFaEnabled(false);
        if (faSecret.isPresent()) {
            userTwoFaSecretRepository.delete(faSecret.get());
        }
        this.userTrustedDeviceRepository.deleteAllByUserId(user.getId());
        return userRepository.save(user);
    }

    public Boolean validate2FA(Integer code, HttpServletRequest httpServletRequest,
                               HttpServletResponse httpServletResponse) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getParameter("username"));
        if (userOptional.isPresent() && code != null) {
            Optional<UserTwoFaSecret> faSecret = userTwoFaSecretRepository.findByUserId(userOptional.get().getId());
            return validate2FA(code, faSecret.get().getSecret());
        }
        return false;
    }

    public Boolean validate2FAScratchCode(String code, HttpServletRequest httpServletRequest) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getParameter("username"));
        if (userOptional.isPresent()) {
            Optional<UserTwoFaSecret> faSecret = userTwoFaSecretRepository.findByUserId(userOptional.get().getId());
            if (faSecret.isPresent()) {
                Type listType = new TypeToken<ArrayList<String>>() {
                }.getType();
                List<String> scratchCodes;
                if (faSecret.get().getScratchCodes() != null) {
                    scratchCodes = new Gson().fromJson(faSecret.get().getScratchCodes(), listType);

                    BCryptPasswordEncoder bCryptPasswordEncoder = new BCryptPasswordEncoder();
                    List<String> tmp = scratchCodes.stream().filter(scratchCode -> bCryptPasswordEncoder.matches(code, scratchCode)).collect(Collectors.toList());
                    if (tmp.size() > 0) {
                        scratchCodes.remove(tmp.get(0));
                        UserTwoFaSecret userTwoFaSecret = faSecret.get();
                        userTwoFaSecret.setScratchCodes(new Gson().toJson(scratchCodes));
                        userTwoFaSecretRepository.save(userTwoFaSecret);
                        return true;
                    }
                }
            }
        }
        return false;
    }

    public Boolean validate2FA(int code, String secret) {
        GoogleAuthenticator gAuth = new GoogleAuthenticator();
        return gAuth.authorize(secret, code);
    }

    public Boolean isTrustedDevice(String deviceId, User user) {
        return userTrustedDeviceRepository.findAllByUserIdandInstanceId(user.getId(), deviceId).size() > 0;
    }

    public void saveTrustedDevice(String deviceId, User user, String userAgent, String ip) {
        if (userTrustedDeviceRepository.findAllByUserIdandInstanceId(user.getId(), deviceId).size() < 1) {
            UserTrustedDevice userTrustedDevice = new UserTrustedDevice();
            userTrustedDevice.setInstanceId(deviceId);
            userTrustedDevice.setUser(user);
            userTrustedDevice.setUserAgent(userAgent);
            userTrustedDevice.setDeviceIp(ip);
            userTrustedDeviceRepository.save(userTrustedDevice);
        }
    }

    public Object selfGetAllTrustedDeviceOfUser(HttpServletRequest httpServletRequest) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
        return userTrustedDeviceRepository.findAllByUserId(userOptional.get().getId());
    }

    public Object selfDeleteTrustedDeviceById(Long id, HttpServletRequest httpServletRequest,
                                              HttpServletResponse httpServletResponse) {
        Optional<User> userOptional = userRepository.findOneByUsername(httpServletRequest.getUserPrincipal().getName());
        Optional<UserTrustedDevice> userTrustedDeviceOptional = userTrustedDeviceRepository.findById(id);
        if (!userTrustedDeviceOptional.isPresent()) {
            return RestResponseGenerator.notFound(httpServletResponse);
        }
        if (!userTrustedDeviceOptional.get().getUser().getId().equals(userOptional.get().getId())) {
            return RestResponseGenerator.accessDenied(httpServletResponse);
        }
        userTrustedDeviceRepository.deleteById(id);
        return userTrustedDeviceOptional.get();
    }

}

