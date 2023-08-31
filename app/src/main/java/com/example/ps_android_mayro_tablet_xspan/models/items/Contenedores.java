package com.example.ps_android_mayro_tablet_xspan.models.items;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.LinkedList;
import java.util.List;

public class Contenedores implements Parcelable {
    private final int folio_contenedor;
    private final List<Skus> skusList;

    public Contenedores(int folio_contenedor) {
        this.folio_contenedor = folio_contenedor;
        this.skusList = new LinkedList<>();
    }

    protected Contenedores(Parcel in) {
        this.folio_contenedor = in.readInt();
        this.skusList = new LinkedList<>();
        in.readParcelableList(this.skusList, Skus.class.getClassLoader());
    }

    public static final Creator<Contenedores> CREATOR = new Creator<Contenedores>() {
        @Override
        public Contenedores createFromParcel(Parcel in) {
            return new Contenedores(in);
        }

        @Override
        public Contenedores[] newArray(int size) {
            return new Contenedores[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(folio_contenedor);
        parcel.writeParcelableList(skusList, 0);
    }

    public int getFolio_contenedor() {
        return folio_contenedor;
    }

    public void addSkuFromBd(String sku, String epc, boolean leido) {
        boolean found = false;
        for(Skus s: skusList) {
            if(s.getSku().compareTo(sku)==0) {
                found=true;
                s.addEpcFromBd(epc, leido);
                break;
            }
        }
        if(!found) {
            Skus s = new Skus(sku);
            s.setEnBd(true);
            s.addEpcFromBd(epc, leido);
            skusList.add(s);
        }
    }

    public int addSkuFromXspan(String sku, String epc) {
        boolean found = false;
        int ret = 0;
        for(Skus s: skusList) {
            if(s.getSku().compareTo(sku)==0) {
                found=true;
                ret = s.addEpcFromXspan(epc);
                break;
            }
        }
        if(!found) {
            Skus s = new Skus(sku);
            s.addEpcFromXspan(epc);
            skusList.add(s);
            ret=-1;
        }
        return ret;
    }

    public int removeSkuFromXspan(String sku, String epc) {
        int ret = 0;
        for(Skus s: skusList) {
            if(s.getSku().compareTo(sku)==0) {
                ret = s.removeEpcFromXspan(epc);
                if(ret==-1) {
                    if(s.getEpcsList().size()==0) {
                        skusList.remove(s);
                    }
                }
                break;
            }
        }
        return ret;
    }

    public int getEsperados() {
        int total = 0;
        for(Skus s: skusList) {
            total+=s.getEsperados();
        }
        return total;
    }

    public int getLeidos() {
        int total = 0;
        for(Skus s: skusList) {
            total+=s.getLeidos();
        }
        return total;
    }

    public int getSobrantes() {
        int total = 0;
        for(Skus s: skusList) {
            total+=s.getSobrantes();
        }
        return total;
    }

    public List<Skus> getSkusList() {
        return skusList;
    }

    public boolean ready() {
        if(getSobrantes()==0) {
            if(getEsperados()>0) {
                return getLeidos()==getEsperados();
            }
        }
        return false;
    }
}
