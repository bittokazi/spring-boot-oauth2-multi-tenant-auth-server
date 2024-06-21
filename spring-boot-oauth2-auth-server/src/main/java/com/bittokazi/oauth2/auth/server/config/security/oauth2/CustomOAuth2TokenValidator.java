package com.bittokazi.oauth2.auth.server.config.security.oauth2;

import com.bittokazi.oauth2.auth.server.config.TenantContext;
import org.springframework.security.oauth2.core.OAuth2Error;
import org.springframework.security.oauth2.core.OAuth2Token;
import org.springframework.security.oauth2.core.OAuth2TokenValidator;
import org.springframework.security.oauth2.core.OAuth2TokenValidatorResult;
import org.springframework.util.Assert;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;

public class CustomOAuth2TokenValidator <T extends OAuth2Token> implements OAuth2TokenValidator<T> {
    private final Collection<OAuth2TokenValidator<T>> tokenValidators;

    public CustomOAuth2TokenValidator(Collection<OAuth2TokenValidator<T>> tokenValidators) {
        Assert.notNull(tokenValidators, "tokenValidators cannot be null");
        this.tokenValidators = new ArrayList(tokenValidators);
    }

    @SafeVarargs
    public CustomOAuth2TokenValidator(OAuth2TokenValidator<T>... tokenValidators) {
        this((Collection) Arrays.asList(tokenValidators));
    }

    public OAuth2TokenValidatorResult validate(T token) {
        Collection<OAuth2Error> errors = new ArrayList();
        Iterator var3 = this.tokenValidators.iterator();

        while(var3.hasNext()) {
            OAuth2TokenValidator<T> validator = (OAuth2TokenValidator)var3.next();
            errors.addAll(validator.validate(token).getErrors());
        }

        System.out.println("T> "+ TenantContext.getCurrentTenant()+" t>"+token);

        return OAuth2TokenValidatorResult.failure(errors);
    }
}

