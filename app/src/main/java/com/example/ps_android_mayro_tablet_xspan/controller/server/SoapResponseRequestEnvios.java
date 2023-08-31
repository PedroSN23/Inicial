package com.example.ps_android_mayro_tablet_xspan.controller.server;

import java.util.ArrayList;
import java.util.Locale;

public class SoapResponseRequestEnvios {
    private final ArrayList<XmlElement> elementos;

    public SoapResponseRequestEnvios() {
        elementos = new ArrayList<>();
    }

    public void agregarTransferBarcode(String transfer, String barcode, int qty) {
        ArrayList<XmlElement> temp = new ArrayList<>();
        temp.add(new XmlElement("dyn", "itemBarCode", barcode, 5));
        temp.add(new XmlElement("dyn", "qty", String.format(Locale.getDefault(), "%d", qty), 5));
        temp.add(new XmlElement("dyn", "transferId", "TRF-"+transfer, 5));
        elementos.add(new XmlElement("dyn", "GRWRFIDRecepcionSucContract", temp, 4));
    }

    public XmlElement returnElement() {
        XmlElement e1 = new XmlElement("tem", "_contractLine", elementos, 3);
        return new XmlElement("tem", "GRWRFIDRecepcionSucSendInfoListRequest", e1, 2);
    }
}
