package com.example.ps_android_mayro_tablet_xspan.models.clases;

public enum DialogTypes {
    none(-1),
    xspan(0),
    gpio(1),
    base(2),
    server(3);
    private final int code;

    DialogTypes(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
