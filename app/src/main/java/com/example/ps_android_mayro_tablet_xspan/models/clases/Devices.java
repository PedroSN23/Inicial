package com.example.ps_android_mayro_tablet_xspan.models.clases;

import androidx.annotation.NonNull;

public enum Devices {
    none(-1, "NONE"),
    gpio(2, "GPIO"),
    xspan1(0, "XSPAN"),
    xspan2(1, "XSPAN");
    private final int code;
    private final String name;

    Devices(int code, String name) {
        this.code = code;
        this.name = name;
    }

    public int getCode() {
        return this.code;
    }


    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
