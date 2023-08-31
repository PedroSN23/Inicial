package com.example.ps_android_mayro_tablet_xspan.controller.gpio;

import androidx.annotation.NonNull;

public enum GPIOPaths {
    outputs("/OutputState"),
    inputs("/InputState");

    private final String txt;

    GPIOPaths(String txt) {
        this.txt = txt;
    }


    @NonNull
    @Override
    public String toString() {
        return txt;
    }
}
