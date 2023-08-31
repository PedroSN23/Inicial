package com.example.ps_android_mayro_tablet_xspan.controller.gpio;

public enum GPIORespuesta {
    error(0),
    error_sensor(1),
    ready(2),
    sensor(3),
    luces_in(4),
    luces_out(5);

    private final int code;

    GPIORespuesta(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
