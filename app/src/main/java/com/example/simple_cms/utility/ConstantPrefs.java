package com.example.simple_cms.utility;

public enum ConstantPrefs {

    SHARED_PREFS("sharedPrefs"), TRY_TO_RECONNECT("tryingReconnect"), URI_TEXT("uriText");

    private final String name;

    ConstantPrefs(String name){
        this.name = name;
    }
}
