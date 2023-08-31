package com.example.ps_android_mayro_tablet_xspan.views.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;
import com.example.ps_android_mayro_tablet_xspan.models.items.Epcs;
import com.example.ps_android_mayro_tablet_xspan.models.items.Skus;
import com.example.ps_android_mayro_tablet_xspan.models.items.TransitionTags;
import com.example.ps_android_mayro_tablet_xspan.views.adapters.ContenedoresAdapter;
import com.example.ps_android_mayro_tablet_xspan.views.adapters.PalletAdapter;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.atomic.AtomicBoolean;


public class LecturasFragment extends Fragment {
    private TextView pl_number_tv;
    private TextView cont_number_tv;
    private TextView pares_cantidad_tv;
    private ContenedoresAdapter contenedoresAdapter;
    private PalletAdapter palletAdapter;
    private final AtomicBoolean buscandoEnBd = new AtomicBoolean(false);
    private List<Contenedores> contenedoresList;
    private Contenedores contenedores=null;
    private final List<TransitionTags> tagsPendientes = new LinkedList<>();
    private LecturasFragmentListener listener;
    private final AtomicBoolean tablaDerVacia=new AtomicBoolean(true);
    private final AtomicBoolean embarqueTerminado = new AtomicBoolean(false);
    public LecturasFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_lecturas, container, false);
        pl_number_tv = v.findViewById(R.id.pl_num);
        cont_number_tv = v.findViewById(R.id.cont_num);
        pares_cantidad_tv = v.findViewById(R.id.pairs);

        contenedoresList = new LinkedList<>();

        ListView listaIzq = v.findViewById(R.id.lista_izq);
        contenedoresAdapter = new ContenedoresAdapter(getActivity(), contenedoresList);
        listaIzq.setAdapter(contenedoresAdapter);

        ListView listaDer = v.findViewById(R.id.lista_der);
        palletAdapter = new PalletAdapter(getActivity());
        listaDer.setAdapter(palletAdapter);

        return v;
    }

    public void addLecturasFragmentListener(LecturasFragmentListener listener) {
        this.listener = listener;
    }

    public int removeTag(TransitionTags tag) {
        int ret = 0;
        if(!embarqueTerminado.get()) {
            boolean found = false;
            if (contenedoresList.size() == 0 && tagsPendientes.size()>0) {
                tagsPendientes.add(tag);
            } else {
                if (tablaDerVacia.get()) {
                    for (Contenedores c : contenedoresList) {
                        for (Skus s : c.getSkusList()) {
                            if (s.getSku().compareTo(tag.getSku()) == 0) {
                                for (Epcs e : s.getEpcsList()) {
                                    if (e.getEpc().compareTo(tag.getEpc()) == 0) {
                                        found = true;
                                        e.setLeido(false);
                                        ret = 1;
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        if (found) {
                            tablaDerVacia.set(false);
                            contenedores = c;
                            palletAdapter.setSkusList(contenedores.getSkusList());
                            contenedoresAdapter.notifyDataSetChanged();
                            cont_number_tv.setText(String.format(Locale.getDefault(), "Contenedor %d", c.getFolio_contenedor()));
                            pares_cantidad_tv.setText(String.format(Locale.getDefault(), "%d", c.getEsperados()));
                            break;
                        }
                    }
                } else {
                    ret = contenedores.removeSkuFromXspan(tag.getSku(), tag.getEpc());
                    if (contenedores.ready()) {
                        if (listener != null) {
                            tablaDerVacia.set(true);
                            listener.contenedorTerminado(contenedores);
                        }
                        boolean notReady = false;
                        for (Contenedores c : contenedoresList) {
                            if (!c.ready()) {
                                notReady = true;
                                break;
                            }
                        }
                        if (!notReady) {
                            embarqueTerminado.set(true);
                            if (listener != null) {
                                listener.finalizarEmbarque();
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    public int addTag(TransitionTags tag) {
        int ret = 0;
        if(!embarqueTerminado.get()) {
            boolean found = false;
            if (contenedoresList.size() == 0) {
                if (!buscandoEnBd.get()) {
                    buscandoEnBd.set(true);
                    if (listener != null) {
                        listener.buscarEpcEnBd(tag.getEpc());
                    }
                }
                tagsPendientes.add(tag);
            } else {
                if (tablaDerVacia.get()) {
                    for (Contenedores c : contenedoresList) {
                        for (Skus s : c.getSkusList()) {
                            if (s.getSku().compareTo(tag.getSku()) == 0) {
                                for (Epcs e : s.getEpcsList()) {
                                    if (e.getEpc().compareTo(tag.getEpc()) == 0) {
                                        found = true;
                                        e.setLeido(true);
                                        ret = 1;
                                        break;
                                    }
                                }
                                break;
                            }
                        }
                        if (found) {
                            tablaDerVacia.set(false);
                            contenedores = c;
                            palletAdapter.setSkusList(contenedores.getSkusList());
                            contenedoresAdapter.notifyDataSetChanged();
                            cont_number_tv.setText(String.format(Locale.getDefault(), "Contenedor %d", c.getFolio_contenedor()));
                            pares_cantidad_tv.setText(String.format(Locale.getDefault(), "%d", c.getEsperados()));
                            break;
                        }
                    }
                } else {
                    ret = contenedores.addSkuFromXspan(tag.getSku(), tag.getEpc());
                    if (contenedores.ready()) {
                        if (listener != null) {
                            tablaDerVacia.set(true);
                            listener.contenedorTerminado(contenedores);
                        }
                        boolean notReady = false;
                        for (Contenedores c : contenedoresList) {
                            if (!c.ready()) {
                                notReady = true;
                                break;
                            }
                        }
                        if (!notReady) {
                            embarqueTerminado.set(true);
                            if (listener != null) {
                                listener.finalizarEmbarque();
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }

    public int[] setEmbarque(int id_embarque, List<Contenedores> contenedores) {
        buscandoEnBd.set(false);
        int[] ret=new int[2];
        pl_number_tv.setText(String.format(Locale.getDefault(), "%d", id_embarque));
        contenedoresList.clear();
        contenedoresList.addAll(contenedores);
        for(TransitionTags tt: tagsPendientes) {
            if(tt.getLastReadSector()==3) {
                int r=addTag(tt);
                if(r==-1) ret[0]+=r;
                else ret[1]+=r;
            } else {
                int r=removeTag(tt);
                if(r==-1) ret[0]-=r;
                else ret[1]-=r;
            }
        }
        tagsPendientes.clear();
        return ret;
    }

    public void clearContent() {
        pl_number_tv.setText("");
        cont_number_tv.setText("");
        pares_cantidad_tv.setText("");
        palletAdapter.setSkusList(new LinkedList<>());
        contenedoresList.clear();
        contenedoresAdapter.notifyDataSetChanged();
        buscandoEnBd.set(false);
        contenedores=null;
        tagsPendientes.clear();
        tablaDerVacia.set(true);
        embarqueTerminado.set(false);
    }
}
