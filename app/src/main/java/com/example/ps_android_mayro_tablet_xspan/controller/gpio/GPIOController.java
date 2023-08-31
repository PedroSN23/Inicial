package com.example.ps_android_mayro_tablet_xspan.controller.gpio;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.ps_android_mayro_tablet_xspan.models.clases.Luces;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;
import com.example.ps_android_mayro_tablet_xspan.models.clases.TipoLuz;

public class GPIOController extends Thread {
    private static GPIOController gpioController;
    private Handler mainHandler;
    private ParametrosConexion parametrosConexion;
    private GPIOHandler gpioHandler;

    public static GPIOController getInstance(Handler mainHandler, ParametrosConexion parametrosConexion) {
        if(gpioController==null) {
            gpioController = new GPIOController(mainHandler);
            gpioController.updateParametrosConexion(parametrosConexion);
            gpioController.start();
        } else {
            gpioController.updateParametrosConexion(parametrosConexion);
            gpioController.updateMainHandler(mainHandler);
        }
        return gpioController;
    }

    public GPIOController(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    public void stopThread() {
        Message message = mainHandler.obtainMessage();
        message.arg1 = GPIOInstruction.stop.getCode();
        gpioHandler.sendMessage(message);
    }

    public static void destroyInstance() {
        gpioController=null;
    }

    private void updateMainHandler(Handler mainHandler) {
        this.mainHandler = mainHandler;
        gpioHandler.updateMainHandler(mainHandler);
    }

    public void updateParametrosConexion(ParametrosConexion parametrosConexion) {
        this.parametrosConexion = parametrosConexion;
    }

    @Override
    public void run() {
        Looper.prepare();
        gpioHandler = new GPIOHandler(mainHandler);
        Message message = gpioHandler.obtainMessage();
        message.arg1 = GPIOInstruction.init.getCode();
        gpioHandler.sendMessage(message);
        Looper.loop();
    }

    public void getSensorState(int port) {
        Message message = mainHandler.obtainMessage();
        message.arg1 = GPIOInstruction.sensor.getCode();
        Bundle bundle = new Bundle();
        bundle.putInt("sensor", port);
        bundle.putParcelable("parametrosConexion", parametrosConexion);
        message.setData(bundle);
        gpioHandler.sendMessage(message);
    }

    public void setLuces(Luces luces) {
        Message message = mainHandler.obtainMessage();
        message.arg1 = GPIOInstruction.luces_out.getCode();
        Bundle bundle = new Bundle();
        bundle.putParcelable("luces", luces);
        bundle.putParcelable("parametrosConexion", parametrosConexion);
        message.setData(bundle);
        gpioHandler.sendMessage(message);
    }

    public void getLuces(Luces luces) {
        Message message = mainHandler.obtainMessage();
        message.arg1 = GPIOInstruction.luces_in.getCode();
        Bundle bundle = new Bundle();
        bundle.putParcelable("luces", luces);
        bundle.putParcelable("parametrosConexion", parametrosConexion);
        message.setData(bundle);
        gpioHandler.sendMessage(message);
    }

    public void setLuzAmbar(GPIOConf gpioConf) {
        Luces luces = new Luces();
        luces.addModifyPort(gpioConf.getLuzAmbar().getPort_id(), 1, TipoLuz.ambar);
        luces.addModifyPort(gpioConf.getLuzVerde().getPort_id(), 0, TipoLuz.verde);
        luces.addModifyPort(gpioConf.getLuzRoja().getPort_id(), 0, TipoLuz.roja);
        setLuces(luces);
    }

    public void setLuzRoja(GPIOConf gpioConf) {
        Luces luces = new Luces();
        luces.addModifyPort(gpioConf.getLuzAmbar().getPort_id(), 0, TipoLuz.ambar);
        luces.addModifyPort(gpioConf.getLuzVerde().getPort_id(), 0, TipoLuz.verde);
        luces.addModifyPort(gpioConf.getLuzRoja().getPort_id(), 1, TipoLuz.roja);
        setLuces(luces);
    }

    public void setLuzVerde(GPIOConf gpioConf) {
        Luces luces = new Luces();
        luces.addModifyPort(gpioConf.getLuzAmbar().getPort_id(), 0, TipoLuz.ambar);
        luces.addModifyPort(gpioConf.getLuzVerde().getPort_id(), 1, TipoLuz.verde);
        luces.addModifyPort(gpioConf.getLuzRoja().getPort_id(), 0, TipoLuz.roja);
        setLuces(luces);
    }

    public void apgarLuces(GPIOConf gpioConf) {
        Luces luces = new Luces();
        luces.addModifyPort(gpioConf.getLuzAmbar().getPort_id(), 0, TipoLuz.ambar);
        luces.addModifyPort(gpioConf.getLuzVerde().getPort_id(), 0, TipoLuz.verde);
        luces.addModifyPort(gpioConf.getLuzRoja().getPort_id(), 0, TipoLuz.roja);
        setLuces(luces);
    }
}
