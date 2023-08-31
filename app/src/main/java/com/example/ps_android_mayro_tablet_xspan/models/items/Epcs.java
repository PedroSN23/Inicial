package com.example.ps_android_mayro_tablet_xspan.models.items;

import android.os.Parcel;
import android.os.Parcelable;

public class Epcs implements Parcelable {
    private final String epc;
    private boolean leido;
    private boolean enBd;

    public Epcs(String epc) {
        this.epc = epc;
        this.leido=false;
        this.enBd=false;
    }

    protected Epcs(Parcel in) {
        this.epc = in.readString();
        this.leido = in.readInt()==1;
        this.enBd = in.readInt()==1;
    }

    public static final Creator<Epcs> CREATOR = new Creator<Epcs>() {
        @Override
        public Epcs createFromParcel(Parcel in) {
            return new Epcs(in);
        }

        @Override
        public Epcs[] newArray(int size) {
            return new Epcs[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(epc);
        parcel.writeInt(leido? 1:0);
        parcel.writeInt(enBd? 1:0);
    }
    public String getEpc() {
        return epc;
    }

    public boolean isLeido() {
        return leido;
    }

    public void setLeido(boolean leido) {
        this.leido = leido;
    }

    public boolean isEnBd() {
        return enBd;
    }

    public void setEnBd(boolean enBd) {
        this.enBd = enBd;
    }
}
