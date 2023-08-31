package com.example.ps_android_mayro_tablet_xspan.views.gui;

import android.os.Bundle;

import androidx.fragment.app.FragmentTransaction;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;
import com.example.ps_android_mayro_tablet_xspan.models.items.TransitionTags;
import com.example.ps_android_mayro_tablet_xspan.views.fragments.LecturasFragment;
import com.example.ps_android_mayro_tablet_xspan.views.fragments.LecturasFragmentListener;

import java.util.LinkedList;
import java.util.List;

public class ContentActivity extends ProgressActivity {
    private FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
    private List<TransitionTags> transitionTagsList;
    private LecturasFragment lecturasFragment;
    private int id_embarque;
    private int[] resultados = new int[2];
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        transitionTagsList = new LinkedList<>();

        lecturasFragment = new LecturasFragment();
        lecturasFragment.addLecturasFragmentListener(new LecturasFragmentListener() {
            @Override
            public void buscarEpcEnBd(String epc) {
                onBuscarEnBd(epc);
            }

            @Override
            public void contenedorTerminado(Contenedores contenedores) {
                onRespaldarContenedorBaseServidor(contenedores, id_embarque);
                onPrenderVerde();
            }

            @Override
            public void finalizarEmbarque() {
                onSetFinishVisible(true);
            }
        });

        if(transaction.isEmpty()) {
            transaction.add(R.id.content, lecturasFragment).commit();
        } else {
            transaction = null;
            transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content, lecturasFragment).commit();
        }
    }

    public void clearListContent() {
        onApagarLuces();
        transitionTagsList.clear();
        lecturasFragment.clearContent();
    }

    public void onSetFinishVisible(boolean b) {
    }

    public void onBuscarEnBd(String epc) {
    }

    public void tagsLeidosXspan(Bundle data) {
        String epc = data.getString("epc");
        short lastReadSector = data.getShort("lastReadSector");
        boolean found = false;
        for(TransitionTags tt: transitionTagsList) {
            if(tt.compareTo(epc)==0) {
                found = true;
                if(tt.setLastReadSector(lastReadSector)) { //tag transitioned
                    if(lastReadSector==3) {
                        int ret = lecturasFragment.addTag(tt);
                        if(ret==-1) resultados[0]+=ret;
                        else resultados[1]+=ret;
                        if(resultados[0]<0) {
                            onPrenderRojo();
                        } else {
                            if(ret==1) {
                                onPrenderAmbar();
                            }
                        }
                    } else {
                        int ret = lecturasFragment.removeTag(tt);
                        if(ret==-1) resultados[0]-=ret;
                        else resultados[1]-=ret;
                        if(resultados[0]<0) {
                            onPrenderRojo();
                        } else {
                            if(resultados[0]>0) resultados[0]=0;
                            if(resultados[1]<0) resultados[1]=0;
                            if(ret==1) {
                                onPrenderAmbar();
                            }
                        }
                    }
                }
                break;
            }
        }
        if(!found) {
            TransitionTags tt = new TransitionTags(epc, lastReadSector); //TODO revisar si está dentro para reportar y eso
            transitionTagsList.add(tt);
        }
    }

    public void asyncResultadoBuscar(int id_embarque, List<Contenedores> contenedores) {
        this.id_embarque = id_embarque;
        resultados=lecturasFragment.setEmbarque(id_embarque, contenedores);
        if(resultados[0]<0) {
            onPrenderRojo();
        } else {
            if(resultados[0]>0) resultados[0]=0;
            if(resultados[1]<0) resultados[1]=0;
            onPrenderAmbar();
        }
    }

    public void onPrenderRojo() {
    }

    public void onPrenderAmbar() {
    }

    public void onPrenderVerde() {
    }

    public void onApagarLuces() {
    }

    public void onRespaldarContenedorBaseServidor(Contenedores contenedores, int id_embarque) {
    }
}
