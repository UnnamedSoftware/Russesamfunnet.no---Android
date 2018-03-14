package com.unnamedsoftware.russesamfunnet;

import android.app.Application;

/**
 * Created by HallvardPC on 23.02.2018.
 */

public class MyApplication extends Application {

    private int russId;

    public int getRussId() {
        return russId;
    }

    public void setRussId(int russId) {
        this.russId = russId;
    }
}
