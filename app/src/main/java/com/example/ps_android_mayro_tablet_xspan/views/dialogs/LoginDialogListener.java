package com.example.ps_android_mayro_tablet_xspan.views.dialogs;

import com.example.ps_android_mayro_tablet_xspan.models.clases.DialogTypes;

public interface LoginDialogListener {
    void dialogWarning(String s);
    void cerrarDialogo();
    void abrirDialogo(DialogTypes types);
}
