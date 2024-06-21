package com.bittokazi.oauth2.auth.server.app.services.client;

import com.bittokazi.oauth2.auth.server.app.models.tenant.OauthClient;
import com.bittokazi.oauth2.auth.server.app.repositories.tenant.OauthClientRepository;
import com.bittokazi.oauth2.auth.server.app.services.base.RestResponseGenerator;
import com.bittokazi.oauth2.auth.server.config.TenantContext;
import com.bittokazi.oauth2.auth.server.utils.Utils;
import com.nimbusds.jose.shaded.gson.Gson;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class ClientService {

    private OauthClientRepository oauthClientRepository;

    public ClientService(OauthClientRepository oauthClientRepository) {
        this.oauthClientRepository = oauthClientRepository;
    }

    public ResponseEntity<?> saveOauthClient(OauthClient oauthClient, HttpServletResponse httpServletResponse) {
//        oauthClient.getAdditionalInformation().put(constants.DEFAULT_OAUTH_CLIENT_IDENTIFIER,
//                constants.DEFAULT_OAUTH_EXTERNAL_CLIENT);
        oauthClient.setAdditionalInformation("{ \"client\": \"user_generated\" }");
        oauthClient.setClientId(UUID.randomUUID().toString());
        String newSecret = Utils.randomNumberGenerator(90);
        oauthClient.setClientSecret(newSecret);
        oauthClient.setNewSecret(newSecret);
        if(!TenantContext.getCurrentTenant().equals("public")) oauthClient.setScope(String.join(",", oauthClient.getScope().stream().filter(s -> !(s.equals("tenant:read") || s.equals("tenant:write"))).collect(Collectors.toList())));
        oauthClient = oauthClientRepository.save(oauthClient);
        oauthClient.setNewSecret(newSecret);
        return ResponseEntity.ok(oauthClient);
    }

    public ResponseEntity<?>  getAllOauthClients() {
        return ResponseEntity.ok(oauthClientRepository.findAll());
    }

    public ResponseEntity<?> getOauthClient(String id, HttpServletResponse httpServletResponse) {
        Optional<OauthClient> clientEntityOptional = oauthClientRepository.findById(id);
        if (clientEntityOptional.isPresent()) {
            return ResponseEntity.ok(clientEntityOptional.get());
        }
        return ResponseEntity.status(404).build();
    }

    public ResponseEntity<?> updateOauthClient(OauthClient oauthClient, HttpServletResponse httpServletResponse) {
        Optional<OauthClient> clientEntityOptional = oauthClientRepository.findById(oauthClient.getId());

        if (clientEntityOptional.isPresent()) {
            OauthClient clientEntityToUpdate = clientEntityOptional.get();

            if (clientEntityToUpdate.getAdditionalInformation() != null
                    && clientEntityToUpdate.getAdditionalInformation()
                    .containsKey("client")
                    && clientEntityToUpdate.getAdditionalInformation().get("client")
                    .equals("default")) {
                oauthClient.setAdditionalInformation(new Gson().toJson(clientEntityToUpdate.getAdditionalInformation()));
            } else {
                oauthClient.setAdditionalInformation("{ \"client\": \"user_generated\" }");
            }
            if(!TenantContext.getCurrentTenant().equals("public")) oauthClient.setScope(String.join(",", oauthClient.getScope().stream().filter(s -> !(s.equals("tenant:read") || s.equals("tenant:write"))).collect(Collectors.toList())));
            String newSecret = "";
            if(oauthClient.getGenerateSecret()) {
                newSecret = Utils.randomNumberGenerator(90);
                oauthClient.setClientSecret(newSecret);
            }
            else {
                oauthClient.setClientSecret(clientEntityToUpdate.getClientSecret());
            }
            oauthClient = oauthClientRepository.save(oauthClient);
            if(!newSecret.equals("")) oauthClient.setNewSecret(newSecret);
            return ResponseEntity.ok(oauthClient);
        }
        return ResponseEntity.status(404).build();
    }

    public ResponseEntity<?> deleteOauthClient(String id, HttpServletResponse httpServletResponse) {
        Optional<OauthClient> clientEntityOptional = oauthClientRepository.findById(id);
        if (clientEntityOptional.isPresent()) {
            if (clientEntityOptional.get().getAdditionalInformation() != null
                    && clientEntityOptional.get().getAdditionalInformation()
                    .containsKey("client")
                    && clientEntityOptional.get().getAdditionalInformation()
                    .get("client")
                    .equals("default")) {
                return ResponseEntity.status(403).body("Delete Not Permitted");
            } else {
                oauthClientRepository.deleteById(id);
                return ResponseEntity.ok(clientEntityOptional.get());
            }
        }
        return ResponseEntity.status(404).build();
    }
}
