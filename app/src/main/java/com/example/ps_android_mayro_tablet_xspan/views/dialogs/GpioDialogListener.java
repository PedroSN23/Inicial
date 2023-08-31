package com.example.ps_android_mayro_tablet_xspan.views.dialogs;

import com.example.ps_android_mayro_tablet_xspan.controller.gpio.GPIOConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;

public interface GpioDialogListener {
    void closeDialog();
    void emptyparameters();
    void changeGpioSettings(GPIOConf gpioConf, ParametrosConexion parametrosConexion);
}
