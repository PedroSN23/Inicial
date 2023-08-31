package com.example.ps_android_mayro_tablet_xspan.views.dialogs;

import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;

public interface XspanDialogListener {
    void closeDialog();
    void emptyparameters();
    void changeXspanSettings(XspanConf xspanConf, ParametrosConexion[] parametrosConexion);
}
