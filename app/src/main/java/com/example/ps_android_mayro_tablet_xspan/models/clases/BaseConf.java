package com.example.ps_android_mayro_tablet_xspan.models.clases;

import android.os.Parcel;
import android.os.Parcelable;

public class BaseConf implements Parcelable {
    private String url;
    private String user;
    private String password;
    private String name;

    public BaseConf(String url, String user, String password, String name) {
        this.url = url;
        this.user = user;
        this.password = password;
        this.name = name;
    }

    protected BaseConf(Parcel in) {
        this.url = in.readString();
        this.user = in.readString();
        this.password = in.readString();
        this.name = in.readString();
    }

    public static final Creator<BaseConf> CREATOR = new Creator<BaseConf>() {
        @Override
        public BaseConf createFromParcel(Parcel in) {
            return new BaseConf(in);
        }

        @Override
        public BaseConf[] newArray(int size) {
            return new BaseConf[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(url);
        parcel.writeString(user);
        parcel.writeString(password);
        parcel.writeString(name);
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
