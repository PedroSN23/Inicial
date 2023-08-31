package com.example.ps_android_mayro_tablet_xspan.controller.xspan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;

public class XspanController extends Thread {
    private static final XspanController[] xspanController = new XspanController[2];
    private Handler mainHandler;
    private ParametrosConexion parametrosConexion;
    private XspanHandler xspanHandler;
    private boolean desconect = false;

    public static XspanController[] getInstance(Handler mainHandler, ParametrosConexion[] parametrosConexion) {
        for(int i=0; i<2; i++) {
            if (xspanController[i] == null) {
                xspanController[i] = new XspanController(mainHandler);
                xspanController[i].updateParametrosConexion(parametrosConexion[i]);
                xspanController[i].start();
            } else {
                xspanController[i].updateParametrosConexion(parametrosConexion[i]);
                xspanController[i].updateMainHandler(mainHandler);
                xspanController[i].conectar();
            }
        }
        return xspanController;
    }

    public static void destroyInstance(int index) {
        xspanController[index]=null;
    }

    private void updateMainHandler(Handler mainHandler) {
        this.mainHandler = mainHandler;
        xspanHandler.updateMainHandler(mainHandler);
    }

    public boolean updateParametrosConexion(ParametrosConexion parametrosConexion) {
        if(this.parametrosConexion!=null && this.parametrosConexion.getUrl().compareTo(parametrosConexion.getUrl())==0) {
            return false;
        }
        this.parametrosConexion = parametrosConexion;
        return true;
    }

    private XspanController(Handler mainHandler) {
        this.mainHandler = mainHandler;
    }

    @Override
    public void run() {
        Looper.prepare();
        xspanHandler = new XspanHandler(mainHandler, parametrosConexion.getDevice());
        conectar();
        Looper.loop();
    }

    public void conectar() {
        if(!desconect) {
            Message message = mainHandler.obtainMessage();
            message.arg1 = XspanInstruction.conectar.getCode();
            Bundle bundle = new Bundle();
            bundle.putString("url", parametrosConexion.getUrl());
            bundle.putInt("index", parametrosConexion.getPort());
            message.setData(bundle);
            xspanHandler.sendMessage(message);
        }
    }

    public void configurar(XspanConf xspanConf) {
        if(!desconect) {
            Message message = mainHandler.obtainMessage();
            message.arg1 = XspanInstruction.configurar.getCode();
            Bundle bundle = new Bundle();
            bundle.putParcelable("xspanConf", xspanConf);
            message.setData(bundle);
            xspanHandler.sendMessage(message);
        }
    }

    public void iniciarLectura() {
        if(!desconect) {
            Message message = mainHandler.obtainMessage();
            message.arg1 = XspanInstruction.start.getCode();
            xspanHandler.sendMessage(message);
        }
    }

    public void finalizarLectura() {
        if(!desconect) {
            Message message = mainHandler.obtainMessage();
            message.arg1 = XspanInstruction.stop.getCode();
            xspanHandler.sendMessage(message);
        }
    }

    public void desconectar() {
        if(!desconect) {
            desconect=true;
            Message message = xspanHandler.obtainMessage();
            message.arg1 = XspanInstruction.desconectar.getCode();
            xspanHandler.sendMessage(message);
        }
    }

    public void cambiarConexion(XspanConf xspanConf, boolean changes) {
        if(!desconect) {
            Message message = xspanHandler.obtainMessage();
            message.arg1 = XspanInstruction.cambiarConexion.getCode();
            Bundle bundle = new Bundle();
            bundle.putString("url", parametrosConexion.getUrl());
            bundle.putBoolean("changes", changes);
            bundle.putParcelable("xspanConf", xspanConf);
            message.setData(bundle);
            xspanHandler.sendMessage(message);
        }
    }
}
