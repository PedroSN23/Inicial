package com.example.ps_android_mayro_tablet_xspan.models;

public class UtilsSkuRiver {
    public static String obtener_sku(String epc) {
        return epc.substring(0, 13);
    }

    public static String obtener_oc(String epc) {
        return epc.substring(25, 29);
    }
}