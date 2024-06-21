package com.bittokazi.oauth2.auth.server.app.models.base;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Oauth2Response {

    private String access_token;

    private String refresh_token;

    private String token_type;

    private Long expires_in;

}
