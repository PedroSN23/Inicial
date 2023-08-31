package com.example.ps_android_mayro_tablet_xspan.controller.server;

public class SoapHeader {
    private final XmlElement head;

    public SoapHeader() {
        XmlElement n1 = new XmlElement("dat", "Company", "RIVE", 3);
        XmlElement n2 = new XmlElement("dat", "CallContext", n1, 2);
        this.head = new XmlElement("soapenv", "Header", n2, 1);
    }

    @Override
    public String toString() {
        return head.toString();
    }
}
