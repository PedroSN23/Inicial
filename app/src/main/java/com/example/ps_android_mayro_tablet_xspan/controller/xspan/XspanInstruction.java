package com.example.ps_android_mayro_tablet_xspan.controller.xspan;

public enum XspanInstruction {
    none(0),
    conectar(1),
    configurar(2),
    start(3),
    stop(4),
    desconectar(5),
    cambiarConexion(6);

    private final int code;

    XspanInstruction(int code) {
        this.code = code;
    }

    public int getCode() {
        return this.code;
    }
}
