package com.example.ps_android_mayro_tablet_xspan.models.clases;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class Luces implements Parcelable {
    private ArrayList<Luz> luces;

    public Luces() {
        luces = new ArrayList<>();
    }

    protected Luces(Parcel in) {
        luces = new ArrayList<>();
        this.luces = in.readArrayList(Luces.class.getClassLoader());
    }

    public static final Creator<Luces> CREATOR = new Creator<Luces>() {
        @Override
        public Luces createFromParcel(Parcel in) {
            return new Luces(in);
        }

        @Override
        public Luces[] newArray(int size) {
            return new Luces[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeList(luces);
    }

    public void addModifyPort(int port, int state, TipoLuz type) {
        boolean found = false;
        for(Luz l: luces) {
            if(l.getPort_id()==port) {
                l.setState(state);
                found = true;
                break;
            }
        }
        if(!found) {
            luces.add(new Luz(port, state, type));
        }
    }

    public int getSize() {
        return luces.size();
    }

    public Luz getAt(int position) {
        if(position<luces.size()) {
            return luces.get(position);
        }
        return null;
    }
}
