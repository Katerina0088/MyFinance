package com.dz.finansist.utils;

import java.util.concurrent.ConcurrentHashMap;

public class AuthorizationController {
    private static final String USER_NAME = "user";
    private static ConcurrentHashMap<String, String> yserStorage = new ConcurrentHashMap<>();

    private static final String TOKEN_KEY = "userToken";
    private static ConcurrentHashMap<String, String> tokenStorage = new ConcurrentHashMap<>();

    public void setAuthToken(String token) {
        tokenStorage.put(TOKEN_KEY, token);
    }

    public String getAuthToken() {
        return tokenStorage.getOrDefault(TOKEN_KEY, null);
    }

    public void setUserName(String userName) {
        yserStorage.put(USER_NAME, userName);
    }

    public String getUserName() {
        return yserStorage.getOrDefault(USER_NAME, null);
    }
}
