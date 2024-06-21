package com.bittokazi.oauth2.auth.server.app.models.tenant.mfa;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TwoFASecretPayload {

    private String tenantName;
    private String secret;
    private Integer code;
    private Boolean enabled;
    private List<String> scratchCodes = new ArrayList<String>();

    public String getSecret() {
        return secret;
    }

    public TwoFASecretPayload setSecret(String secret) {
        this.secret = secret;
        return this;
    }

    public Integer getCode() {
        return code;
    }

    public TwoFASecretPayload setCode(Integer code) {
        this.code = code;
        return this;
    }

    public Boolean getEnabled() {
        return enabled;
    }

    public TwoFASecretPayload setEnabled(Boolean enabled) {
        this.enabled = enabled;
        return this;
    }

    public List<String> getScratchCodes() {
        return scratchCodes;
    }

    public TwoFASecretPayload setScratchCodes(List<String> scratchCodes) {
        this.scratchCodes = scratchCodes;
        return this;
    }

    public TwoFASecretPayload setTenantName(String tenantName) {
        this.tenantName = tenantName;
        return this;
    }

}
