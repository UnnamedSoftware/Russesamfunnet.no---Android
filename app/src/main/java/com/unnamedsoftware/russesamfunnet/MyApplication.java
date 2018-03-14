package com.unnamedsoftware.russesamfunnet;

import android.app.Application;

/**
 * Created by HallvardPC on 23.02.2018.
 */

public class MyApplication extends Application {

    private String accessToken;

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }
}
