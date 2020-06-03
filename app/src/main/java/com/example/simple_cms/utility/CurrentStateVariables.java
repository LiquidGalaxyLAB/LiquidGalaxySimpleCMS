package com.example.simple_cms.utility;

public class CurrentStateVariables {

    private static CurrentStateVariables instance = null;
    private boolean isTryReconnect = false;
    private String uriText = "";

    private CurrentStateVariables() {}

    public static CurrentStateVariables getInstance()
    {
        if (instance == null) instance = new CurrentStateVariables();
        return instance;
    }

    public boolean isTryReconnect() {
        return isTryReconnect;
    }

    public void setTryReconnect(boolean tryReconnect) {
        isTryReconnect = tryReconnect;
    }

    public String getUriText() {
        return uriText;
    }

    public void setUriText(String uriText) {
        this.uriText = uriText;
    }
}
