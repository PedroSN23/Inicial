package com.example.ps_android_mayro_tablet_xspan.controller.gpio;

import android.os.Parcel;
import android.os.Parcelable;

import com.example.ps_android_mayro_tablet_xspan.models.clases.Luz;
import com.example.ps_android_mayro_tablet_xspan.models.clases.TipoLuz;

public class GPIOConf implements Parcelable {
    private final Luz luzRoja;
    private final Luz luzVerde;
    private final Luz luzAmbar;
    private final Luz sensor;

    public GPIOConf() {
        luzRoja = new Luz(1, 0, TipoLuz.roja);
        luzAmbar = new Luz(2, 0, TipoLuz.ambar);
        luzVerde = new Luz(3, 0, TipoLuz.verde);
        sensor = new Luz(1, 0, TipoLuz.verde);
    }

    protected GPIOConf(Parcel in) {
        this.luzRoja = in.readParcelable(Luz.class.getClassLoader());
        this.luzAmbar = in.readParcelable(Luz.class.getClassLoader());
        this.luzVerde = in.readParcelable(Luz.class.getClassLoader());
        this.sensor = in.readParcelable(Luz.class.getClassLoader());
    }

    public static final Creator<GPIOConf> CREATOR = new Creator<GPIOConf>() {
        @Override
        public GPIOConf createFromParcel(Parcel in) {
            return new GPIOConf(in);
        }

        @Override
        public GPIOConf[] newArray(int size) {
            return new GPIOConf[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(luzRoja, 1);
        parcel.writeParcelable(luzAmbar, 1);
        parcel.writeParcelable(luzVerde, 1);
        parcel.writeParcelable(sensor, 1);
    }

    public Luz getLuzRoja() {
        return luzRoja;
    }

    public void setLuzRojaPort(int port) {
        this.luzRoja.setPort_id(port);
    }

    public void setLuzRojaState(int state) {
        this.luzRoja.setState(state);
    }

    public Luz getLuzVerde() {
        return luzVerde;
    }

    public void setLuzVerdePort(int port) {
        this.luzVerde.setPort_id(port);
    }

    public void setLuzVerdeState(int state) {
        this.luzVerde.setState(state);
    }

    public Luz getLuzAmbar() {
        return luzAmbar;
    }

    public void setLuzAmbarPort(int port) {
        this.luzAmbar.setPort_id(port);
    }

    public void setLuzAmbarState(int state) {
        this.luzAmbar.setState(state);
    }

    public Luz getSensor() {
        return sensor;
    }

    public void setSensorPort(int port) {
        this.sensor.setPort_id(port);
    }

    public void setSensorState(int state) {
        this.sensor.setState(state);
    }
}
