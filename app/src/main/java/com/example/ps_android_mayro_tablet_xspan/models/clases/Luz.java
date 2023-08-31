package com.example.ps_android_mayro_tablet_xspan.models.clases;

import android.os.Parcel;
import android.os.Parcelable;

public class Luz implements Parcelable {
    private int port_id;
    private int state;
    private TipoLuz type;

    public Luz(int port_id, int state, TipoLuz type) {
        this.port_id = port_id;
        this.state = state;
        this.type =type;
    }

    protected Luz(Parcel in) {
        this.port_id = in.readInt();
        this.state = in.readInt();
        this.type = TipoLuz.valueOf(in.readString());
    }

    public static final Creator<Luz> CREATOR = new Creator<Luz>() {
        @Override
        public Luz createFromParcel(Parcel in) {
            return new Luz(in);
        }

        @Override
        public Luz[] newArray(int size) {
            return new Luz[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(port_id);
        parcel.writeInt(state);
        parcel.writeString(type.toString());
    }
    public int getPort_id() {
        return port_id;
    }

    public void setPort_id(int port_id) {
        this.port_id = port_id;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }

    public TipoLuz getType() {
        return type;
    }
}
