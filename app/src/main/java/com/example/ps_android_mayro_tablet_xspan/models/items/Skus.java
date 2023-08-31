package com.example.ps_android_mayro_tablet_xspan.models.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public class Skus implements Parcelable {
    private final String sku;
    private final List<Epcs> epcsList;
    private boolean enBd;

    public Skus(String sku) {
        this.sku = sku;
        this.epcsList = new LinkedList<>();
        this.enBd = false;
    }

    protected Skus(Parcel in) {
        this.sku = in.readString();
        this.epcsList = new LinkedList<>();
        in.readParcelableList(this.epcsList, Epcs.class.getClassLoader());
        this.enBd = in.readInt()==1;
    }

    public static final Creator<Skus> CREATOR = new Creator<Skus>() {
        @Override
        public Skus createFromParcel(Parcel in) {
            return new Skus(in);
        }

        @Override
        public Skus[] newArray(int size) {
            return new Skus[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(sku);
        parcel.writeParcelableList(epcsList, 0);
    }

    public String getSku() {
        return sku;
    }

    public boolean isEnBd() {
        return enBd;
    }

    public void setEnBd(boolean enBd) {
        this.enBd = enBd;
    }

    public int getEsperados() {
        int total = 0;
        for(Epcs e: epcsList) {
            if(e.isEnBd()) total++;
        }
        return total;
    }

    public int getLeidos() {
        int total = 0;
        for(Epcs e: epcsList) {
            if(e.isLeido()) total++;
        }
        return total;
    }

    public int getSobrantes() {
        int total = 0;
        for(Epcs e: epcsList) {
            if(!e.isEnBd()) total++;
        }
        return total;
    }

    public void addEpcFromBd(String epc, boolean leido) {
        boolean found = false;
        for(Epcs e: epcsList) {
            if(e.getEpc().compareTo(epc)==0) {
                found=true;
                break;
            }
        }
        if(!found) {
            Epcs e = new Epcs(epc);
            e.setEnBd(true);
            e.setLeido(leido);
            epcsList.add(e);
        }
    }

    public int addEpcFromXspan(String epc) {
        boolean found = false;
        int ret = 0;
        for(Epcs e: epcsList) {
            if(e.getEpc().compareTo(epc)==0) {
                found=true;
                if(!e.isLeido()) {
                    e.setLeido(true);
                    ret=1;
                }
                break;
            }
        }
        if(!found) {
            Epcs e = new Epcs(epc);
            e.setLeido(true);
            epcsList.add(e);
            ret=-1;
        }
        return ret;
    }

    public int removeEpcFromXspan(String epc) {
        int ret = 0;
        for(Epcs e: epcsList) {
            if(e.getEpc().compareTo(epc)==0) {
                if(e.isLeido()) {
                    e.setLeido(false);
                    ret=1;
                    if(!e.isEnBd()) {
                        epcsList.remove(e);
                        ret=-1;
                    }
                }
                break;
            }
        }
        return ret;
    }

    public List<Epcs> getEpcsList() {
        return this.epcsList;
    }
}
