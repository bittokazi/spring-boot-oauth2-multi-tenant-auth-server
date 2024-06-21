package com.bittokazi.oauth2.auth.server.utils;

import jakarta.servlet.http.HttpServletRequest;

public class HttpReqRespUtils {

    private static final String[] IP_HEADER_CANDIDATES = { "X-Forwarded-For", "Proxy-Client-IP", "WL-Proxy-Client-IP",
            "HTTP_X_FORWARDED_FOR", "HTTP_X_FORWARDED", "HTTP_X_CLUSTER_CLIENT_IP", "HTTP_CLIENT_IP",
            "HTTP_FORWARDED_FOR", "HTTP_FORWARDED", "HTTP_VIA", "REMOTE_ADDR" };

    public static String getClientIpAddressIfServletRequestExist(HttpServletRequest httpServletRequest) {
        for (String header : IP_HEADER_CANDIDATES) {
            String ipList = httpServletRequest.getHeader(header);
            if (ipList != null && ipList.length() != 0 && !"unknown".equalsIgnoreCase(ipList)) {
                String ip = ipList.split(",")[0];
                return ip;
            }
        }
        return httpServletRequest.getRemoteAddr();
    }

    public static final String getUserAgent(HttpServletRequest httpServletRequest) {
        return httpServletRequest.getHeader("User-Agent");
    }

}

