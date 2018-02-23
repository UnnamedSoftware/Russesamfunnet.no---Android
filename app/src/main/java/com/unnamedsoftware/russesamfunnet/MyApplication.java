package com.unnamedsoftware.russesamfunnet;

import android.app.Application;

import com.unnamedsoftware.russesamfunnet.Entity.RussEntity;

/**
 * Created by HallvardPC on 23.02.2018.
 */

public class MyApplication extends Application {

    private RussEntity russ;

    public RussEntity getRuss() {
        return russ;
    }

    public void setRuss(RussEntity russ) {
        this.russ = russ;
    }
}
