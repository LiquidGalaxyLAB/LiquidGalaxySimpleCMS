package com.example.simple_cms.utility;

/**
 * This class is in charge of saving the constants that are use to save in the shared preferences.
 */
public enum ConstantPrefs {

    SHARED_PREFS("sharedPrefs"), TRY_TO_RECONNECT("tryingReconnect"),
    URI_TEXT("uriText"), USER_NAME("userName"),
    USER_PASSWORD("userPassword"), IS_CONNECTED("Connected");

    private final String name;

    ConstantPrefs(String name){
        this.name = name;
    }

    public String getName() {
        return name;
    }

}
