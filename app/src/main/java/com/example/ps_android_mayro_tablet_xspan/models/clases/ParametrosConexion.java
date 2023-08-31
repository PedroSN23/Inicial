package com.example.ps_android_mayro_tablet_xspan.models.clases;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Locale;

public class ParametrosConexion implements Parcelable {
    private String url;
    private int port;
    private final Devices device;

    public ParametrosConexion(String url, int port, Devices device) {
        this.url = url;
        this.port = port;
        this.device = device;
    }

    protected ParametrosConexion(Parcel in) {
        this.url = in.readString();
        this.port = in.readInt();
        this.device = Devices.valueOf(in.readString());
    }

    public static final Creator<ParametrosConexion> CREATOR = new Creator<ParametrosConexion>() {
        @Override
        public ParametrosConexion createFromParcel(Parcel in) {
            return new ParametrosConexion(in);
        }

        @Override
        public ParametrosConexion[] newArray(int size) {
            return new ParametrosConexion[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeInt(port);
        parcel.writeString(device.toString());
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public Devices getDevice() {
        return device;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.getDefault(), "%s=>%s:%d", device.toString(), url, port);
    }
}
