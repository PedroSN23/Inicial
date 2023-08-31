package com.example.ps_android_mayro_tablet_xspan.controller.xspan;

public enum XspanRespuesta {
    error(0),
    tags(1),
    connected(2),
    disconnected(3),
    configured(4),
    reading(5),
    stopreading(6),
    connectionLost(7),
    connectionLostReading(8),
    keepAlive(9),
    keepAliveReading(10);

    private final int code;

    XspanRespuesta(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
