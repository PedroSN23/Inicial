package com.example.ps_android_mayro_tablet_xspan.views.dialogs;

import com.example.ps_android_mayro_tablet_xspan.models.clases.BaseConf;

public interface BaseDialogListener {
    void closeDialog();

    void emptyparameters();

    void changeBaseSettings(BaseConf baseConf);
}
