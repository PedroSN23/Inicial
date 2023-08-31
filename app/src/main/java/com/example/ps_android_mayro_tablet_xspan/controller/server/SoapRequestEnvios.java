package com.example.ps_android_mayro_tablet_xspan.controller.server;

import java.util.ArrayList;

public class SoapRequestEnvios {
    private final ArrayList<XmlElement> elementos;

    public SoapRequestEnvios() {
        elementos = new ArrayList<>();
    }

    public void agregarContenedor(String sku, String oc, String cantidad) {
        ArrayList<XmlElement> temp = new ArrayList<>();
        temp.add(new XmlElement("dyn", "ItemBarCode", sku, 4));
        temp.add(new XmlElement("dyn", "PurchId", oc, 4));
        temp.add(new XmlElement("dyn", "QtyPack", cantidad, 4));
        temp.add(new XmlElement("dyn", "Type", "E", 4));
        elementos.add(new XmlElement("dyn", "GRWRFIDRecepcionOCContract", temp, 3));
    }

    public XmlElement returnElement() {
        XmlElement e1 = new XmlElement("tem", "_lineList", elementos, 3);
        return new XmlElement("tem", "GRWRFIDRecepcionOCServSendInfoListRequest", e1, 2);
    }
}