package com.example.ps_android_mayro_tablet_xspan.models.clases;

import com.example.ps_android_mayro_tablet_xspan.R;

public enum GlobalStatus {
    error(-1, R.string.status_error, R.color.status_red),
    unknown(0, R.string.status_unknown, R.color.status_gray),
    connected(1, R.string.status_connected, R.color.status_green),
    idle(2, R.string.status_idle, R.color.status_green),
    reading(3, R.string.status_reading, R.color.status_blue),
    busy(4, R.string.status_busy, R.color.status_yellow);

    private int code;
    private int status_icon;
    private int status_color;

    GlobalStatus(int code, int icon, int color) {
        this.code = code;
        this.status_icon = icon;
        this.status_color = color;
    }

    public int getStatus_color() {
        return status_color;
    }

    public int getStatus_icon() {
        return status_icon;
    }
    public int getCode() {
        return code;
    }
}
