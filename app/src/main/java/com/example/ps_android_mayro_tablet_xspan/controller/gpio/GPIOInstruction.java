package com.example.ps_android_mayro_tablet_xspan.controller.gpio;

public enum GPIOInstruction {
    none(0),
    init(1),
    sensor(2),
    luces_out(3),
    luces_in(4),
    stop(5);

    private final int code;

    GPIOInstruction(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
