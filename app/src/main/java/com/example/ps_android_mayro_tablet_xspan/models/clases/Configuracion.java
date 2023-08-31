package com.example.ps_android_mayro_tablet_xspan.models.clases;

import android.os.Parcel;
import android.os.Parcelable;

public class Configuracion implements Parcelable {
    private final int id;
    private String url;
    private double txPower;

    public Configuracion(int id, String url, double txPower) {
        this.id = id;
        this.url = url;
        this.txPower = txPower;
    }

    protected Configuracion(Parcel in) {
        this.id = in.readInt();
        this.url = in.readString();
        this.txPower = in.readDouble();
    }


    public int getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public double getTxPower() {
        return txPower;
    }

    public void setTxPower(double txPower) {
        this.txPower = txPower;
    }

    public static final Creator<Configuracion> CREATOR = new Creator<Configuracion>() {
        @Override
        public Configuracion createFromParcel(Parcel in) {
            return new Configuracion(in);
        }

        @Override
        public Configuracion[] newArray(int size) {
            return new Configuracion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(this.id);
        parcel.writeString(this.url);
        parcel.writeDouble(this.txPower);
    }

    public boolean checkChanges(String urlTxt, double powVal) {
        if(this.url.compareTo(urlTxt)!=0) {
            this.url = urlTxt;
            if(this.txPower!=powVal) {
                this.txPower = powVal;
            }
            return true;
        } else {
            if(this.txPower!=powVal) {
                this.txPower = powVal;
                return true;
            }
        }
        return false;
    }
}
