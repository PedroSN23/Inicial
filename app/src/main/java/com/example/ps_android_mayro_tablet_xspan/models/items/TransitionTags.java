package com.example.ps_android_mayro_tablet_xspan.models.items;

import com.example.ps_android_mayro_tablet_xspan.models.UtilsSkuRiver;

public class TransitionTags {
    private final String epc;
    private final String sku;
    private short lastReadSector;
    private final short firstReadSector;
    private boolean transitioned;

    public TransitionTags(String epc, short lastReadSector) {
        this.epc = epc;
        this.sku = UtilsSkuRiver.obtener_sku(epc);
        this.lastReadSector = lastReadSector;
        this.firstReadSector = lastReadSector;
        this.transitioned = false;
    }

    public String getEpc() {
        return epc;
    }

    public String getSku() {
        return sku;
    }

    public short getLastReadSector() {
        return lastReadSector;
    }

    public boolean setLastReadSector(short lastReadSector) {
        if(this.lastReadSector!=lastReadSector) {
            this.transitioned = true;
            this.lastReadSector = lastReadSector;
            return true;
        }
        return false;
    }

    public short getFirstReadSector() {
        return firstReadSector;
    }

    public boolean isTransitioned() {
        return transitioned;
    }

    public int compareTo(String epc) {
        return this.epc.compareTo(epc);
    }
}
