package com.example.ps_android_mayro_tablet_xspan.views.dialogs;


import com.example.ps_android_mayro_tablet_xspan.models.clases.ServidorConf;

public interface ServerDialogListener {
    void emptyparameters();
    void changeServerSettings(ServidorConf servidor);
    void closeDialog();
}
