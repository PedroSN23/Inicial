package com.example.ps_android_mayro_tablet_xspan.controller.server;

public class SoapBody {
    private final XmlElement elemento;

    public SoapBody(Object elemento) {
        this.elemento = new XmlElement("soapenv", "Body", elemento, 1);
    }

    @Override
    public String toString() {
        return elemento.toString();
    }
}
