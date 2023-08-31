package com.example.ps_android_mayro_tablet_xspan.models.clases;

import androidx.annotation.NonNull;

public enum ComMethod {
    post("POST"),
    get("GET");

    private final String text;

    ComMethod(String text) {
        this.text = text;
    }

    @NonNull
    @Override
    public String toString() {
        return text;
    }
}
