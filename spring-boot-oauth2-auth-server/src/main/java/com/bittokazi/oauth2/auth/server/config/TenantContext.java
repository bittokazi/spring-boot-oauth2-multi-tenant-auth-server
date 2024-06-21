package com.bittokazi.oauth2.auth.server.config;

public class TenantContext {

    private static ThreadLocal<String> currentTenant = new ThreadLocal<String>();

    private static ThreadLocal<String> currentClient = new ThreadLocal<String>();

    private static ThreadLocal<String> currentDataTenant = new ThreadLocal<String>();

    private static ThreadLocal<String> currentIssuer = new ThreadLocal<String>();

    public static void setCurrentTenant(String tenant) {
        currentTenant.set(tenant);
    }

    public static String getCurrentTenant() {
        return currentTenant.get();
    }

    public static void clear() {
        currentTenant.set(null);
    }

    public static void setCurrentClient(String client) {
        currentClient.set(client);
    }

    public static String getCurrentClient() {
        return currentClient.get();
    }

    public static void clearClient() {
        currentClient.set(null);
    }

    public static void setCurrentIssuer(String client) {
        currentIssuer.set(client);
    }

    public static String getCurrentIssuer() {
        return currentIssuer.get();
    }

    public static void clearCurrentIssuer() {
        currentIssuer.set(null);
    }

    public static void setCurrentDataTenant(String client) {
        currentDataTenant.set(client);
    }

    public static String getCurrentDataTenant() {
        return currentDataTenant.get();
    }

    public static void clearCurrentDataTenant() {
        currentDataTenant.set(null);
    }

}
