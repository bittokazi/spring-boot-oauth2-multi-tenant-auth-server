package com.bittokazi.oauth2.auth.server.app.models.master;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

@Setter
@Getter
@Builder
public class TenantInfo implements Serializable {
    private Boolean cpanel = true;
    private Boolean enabledConfigPanel = true;
    private String name = "";
}
