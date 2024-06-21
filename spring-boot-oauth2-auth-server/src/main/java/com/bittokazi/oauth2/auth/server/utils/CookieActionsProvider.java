package com.bittokazi.oauth2.auth.server.utils;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.function.Function;

@Getter
@Setter
public class CookieActionsProvider {

    private Function<CookieValue, Void> updateFunction;

    public void updateCookie(CookieValue cookieValue) {
        updateFunction.apply(cookieValue);
    }

    public static Function<CookieValue, Void> updateCookieFunc(HttpServletResponse httpServletResponse) {
        return cookieValue -> {
            Cookie cookie = new Cookie(cookieValue.getKey(), cookieValue.getValue());
            cookie.setPath("/");
            cookie.setMaxAge(63072000);
            httpServletResponse.addCookie(cookie);
            return null;
        };
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class CookieValue {
        private String key;
        private String value;
    }
}
