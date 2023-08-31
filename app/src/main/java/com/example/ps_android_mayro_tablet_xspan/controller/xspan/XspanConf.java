package com.example.ps_android_mayro_tablet_xspan.controller.xspan;

import android.os.Parcel;
import android.os.Parcelable;

import com.impinj.octane.DirectionFieldOfView;
import com.impinj.octane.DirectionMode;

public class XspanConf implements Parcelable {
    private short tagAgeIntervalSeconds;
    private short updateIntervalSeconds;
    private boolean entryReportEnabled;
    private boolean updateReportEnabled;
    private boolean exitReporrtEnabled;
    private boolean setMaxTxPower;
    private double txPowerinDbm;
    private DirectionMode directionMode;
    private DirectionFieldOfView directionFieldOfView;

    public XspanConf() {
        this.tagAgeIntervalSeconds = 1;
        this.updateIntervalSeconds = 1;
        this.entryReportEnabled = true;
        this.updateReportEnabled = true;
        this.exitReporrtEnabled = true;
        this.setMaxTxPower = false;
        this.txPowerinDbm = 22.0;
        this.directionMode = DirectionMode.HighSensitivity;
        this.directionFieldOfView = DirectionFieldOfView.NARROW;
    }

    protected XspanConf(Parcel in) {
        this.tagAgeIntervalSeconds = (short) in.readInt();
        this.updateIntervalSeconds = (short) in.readInt();
        this.entryReportEnabled = (in.readInt()==1);
        this.updateReportEnabled = (in.readInt()==1);
        this.exitReporrtEnabled = (in.readInt()==1);
        this.setMaxTxPower = (in.readInt()==1);
        this.txPowerinDbm = in.readDouble();
        this.directionMode = DirectionMode.valueOf(in.readString());
        this.directionFieldOfView = DirectionFieldOfView.valueOf(in.readString());
    }

    public static final Creator<XspanConf> CREATOR = new Creator<XspanConf>() {
        @Override
        public XspanConf createFromParcel(Parcel in) {
            return new XspanConf(in);
        }

        @Override
        public XspanConf[] newArray(int size) {
            return new XspanConf[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(tagAgeIntervalSeconds);
        parcel.writeInt(updateIntervalSeconds);
        parcel.writeInt(entryReportEnabled? 1:0);
        parcel.writeInt(updateReportEnabled? 1:0);
        parcel.writeInt(exitReporrtEnabled? 1:0);
        parcel.writeInt(setMaxTxPower? 1:0);
        parcel.writeDouble(txPowerinDbm);
        parcel.writeString(directionMode.toString());
        parcel.writeString(directionFieldOfView.toString());
    }

    public short getTagAgeIntervalSeconds() {
        return tagAgeIntervalSeconds;
    }

    public void setTagAgeIntervalSeconds(short tagAgeIntervalSeconds) {
        this.tagAgeIntervalSeconds = tagAgeIntervalSeconds;
    }

    public short getUpdateIntervalSeconds() {
        return updateIntervalSeconds;
    }

    public void setUpdateIntervalSeconds(short updateIntervalSeconds) {
        this.updateIntervalSeconds = updateIntervalSeconds;
    }

    public boolean isEntryReportEnabled() {
        return entryReportEnabled;
    }

    public void setEntryReportEnabled(boolean entryReportEnabled) {
        this.entryReportEnabled = entryReportEnabled;
    }

    public boolean isUpdateReportEnabled() {
        return updateReportEnabled;
    }

    public void setUpdateReportEnabled(boolean updateReportEnabled) {
        this.updateReportEnabled = updateReportEnabled;
    }

    public boolean isExitReporrtEnabled() {
        return exitReporrtEnabled;
    }

    public void setExitReporrtEnabled(boolean exitReporrtEnabled) {
        this.exitReporrtEnabled = exitReporrtEnabled;
    }

    public boolean isSetMaxTxPower() {
        return setMaxTxPower;
    }

    public void setSetMaxTxPower(boolean setMaxTxPower) {
        this.setMaxTxPower = setMaxTxPower;
    }

    public double getTxPowerinDbm() {
        return txPowerinDbm;
    }

    public void setTxPowerinDbm(double txPowerinDbm) {
        this.txPowerinDbm = txPowerinDbm;
    }

    public DirectionMode getDirectionMode() {
        return directionMode;
    }

    public void setDirectionMode(DirectionMode directionMode) {
        this.directionMode = directionMode;
    }

    public DirectionFieldOfView getDirectionFieldOfView() {
        return directionFieldOfView;
    }

    public void setDirectionFieldOfView(DirectionFieldOfView directionFieldOfView) {
        this.directionFieldOfView = directionFieldOfView;
    }
}
