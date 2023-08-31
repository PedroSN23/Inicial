package com.example.ps_android_mayro_tablet_xspan.models.clases;

import android.os.Parcel;
import android.os.Parcelable;

public class ServidorConf implements Parcelable {
    private String url;
    private String domain;
    private int port;
    private String user;
    private String password;
    private String soapaction;
    private String servicio;

    public ServidorConf(String url, String domain, String user, String password, String soapaction, int port, String servicio) {
        this.url = url;
        this.domain = domain;
        this.user = user;
        this.password = password;
        this.soapaction = soapaction;
        this.port = port;
        this.servicio = servicio;
    }

    protected ServidorConf(Parcel in) {
        this.url = in.readString();
        this.domain = in.readString();
        this.user = in.readString();
        this.password = in.readString();
        this.soapaction = in.readString();
        this.port = in.readInt();
        this.servicio = in.readString();
    }

    public static final Creator<ServidorConf> CREATOR = new Creator<ServidorConf>() {
        @Override
        public ServidorConf createFromParcel(Parcel in) {
            return new ServidorConf(in);
        }

        @Override
        public ServidorConf[] newArray(int size) {
            return new ServidorConf[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(domain);
        parcel.writeString(user);
        parcel.writeString(password);
        parcel.writeString(soapaction);
        parcel.writeInt(port);
        parcel.writeString(servicio);
    }

    public String getDomain() {
        return domain;
    }

    public void setDomain(String domain) {
        this.domain = domain;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getUser() {
        return user;
    }

    public void setUser(String user) {
        this.user = user;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSoapaction() {
        return soapaction;
    }

    public void setSoapaction(String soapaction) {
        this.soapaction = soapaction;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getServicio() {
        return servicio;
    }

    public void setServicio(String servicio) {
        this.servicio=servicio;
    }
}
