package com.example.ps_android_mayro_tablet_xspan.views.fragments;

import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;

public interface LecturasFragmentListener {
    void buscarEpcEnBd(String epc);
    void contenedorTerminado(Contenedores contenedores);
    void finalizarEmbarque();
}
