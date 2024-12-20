package org.example;

public class TokenAndUrlSetter {
    private static String accessToken;
    private static String instanceUrl;

    public static String getAccessToken() {
        return accessToken;
    }

    public static void setAccessToken(String accessToken) {
        TokenAndUrlSetter.accessToken = accessToken;
    }

    public static String getInstanceUrl() {
        return instanceUrl;
    }

    public static void setInstanceUrl(String instanceUrl) {
        TokenAndUrlSetter.instanceUrl = instanceUrl;
    }
}
