package com.example.ps_android_mayro_tablet_xspan.controller.handler;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import androidx.annotation.NonNull;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.controller.gpio.GPIORespuesta;
import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanRespuesta;
import com.example.ps_android_mayro_tablet_xspan.models.clases.Devices;
import com.example.ps_android_mayro_tablet_xspan.models.clases.GlobalStatus;
import com.example.ps_android_mayro_tablet_xspan.models.clases.Luces;
import com.example.ps_android_mayro_tablet_xspan.models.clases.TipoLuz;
import com.example.ps_android_mayro_tablet_xspan.views.gui.MainActivity;

import java.lang.ref.WeakReference;

public class MainHandler extends Handler {
    private final WeakReference<Activity> activityWeakReference;

    @SuppressWarnings("deprecation")
    public MainHandler(Activity activity) {
        activityWeakReference = new WeakReference<>(activity);
    }

    private int progress = 0;

    @Override
    public void handleMessage(@NonNull Message msg) {
        if(activityWeakReference.get()!=null) {
            Devices devices = Devices.none;
            for (Devices d : Devices.values()) {
                if (d.getCode() == msg.arg1) {
                    devices = d;
                    break;
                }
            }
            switch (devices) {
                case gpio:
                    handleGpioMessage(msg);
                    break;
                case xspan1:
                case xspan2:
                    handleXspanMessage(msg, devices);
                    break;
            }
        }
    }

    private void handleGpioMessage(Message msg) {
        Bundle bundle;
        GPIORespuesta gpioRespuesta = GPIORespuesta.error;
        for(GPIORespuesta gr: GPIORespuesta.values()) {
            if(gr.getCode() == msg.arg2) {
                gpioRespuesta = gr;
                break;
            }
        }
        switch (gpioRespuesta) {
            case error:
                for(TipoLuz tl: TipoLuz.values()) {
                    ((MainActivity)activityWeakReference.get()).setGpioState(-1, tl);
                }
                break;
            case luces_in:
            case luces_out:
                bundle = msg.getData();
                Luces luces = bundle.getParcelable("luces");
                for(int i=0; i<luces.getSize(); i++) {
                    ((MainActivity)activityWeakReference.get()).setGpioState(luces.getAt(i).getState(), luces.getAt(i).getType());
                }
                break;
            case sensor:
            case error_sensor:
                bundle = msg.getData();
                int state = bundle.getInt("estado");
                ((MainActivity)activityWeakReference.get()).setGpioState(state, TipoLuz.sensor);
                if(state!=-1) {
                    ((MainActivity) activityWeakReference.get()).setSensorChanges(state);
                }
            case ready:
                ((MainActivity)activityWeakReference.get()).iniciarTimerGpio();
        }
    }

    private void handleXspanMessage(Message msg, Devices devices) {
        Bundle bundle;
        XspanRespuesta xspanRespuesta = XspanRespuesta.error;
        for(XspanRespuesta xr: XspanRespuesta.values()) {
            if(xr.getCode()==msg.arg2) {
                xspanRespuesta = xr;
                break;
            }
        }
        switch (xspanRespuesta) {
            case error:
                bundle = msg.getData();
                progress+=50;
                if(progress>100) {
                    progress = 100;
                }
                ((MainActivity)activityWeakReference.get()).mostrarMensajeDeError(bundle.getString("errorMsg"), devices.getCode());
                ((MainActivity)activityWeakReference.get()).avanceProgreso(progress, R.string.rfid_error, R.color.status_red);
                break;
            case tags:
                ((MainActivity)activityWeakReference.get()).tagsLeidosXspan(msg.getData());
                break;
            case connected: //25 25
                progress+=25;
                if(progress>=100) {
                    progress=75;
                }
                ((MainActivity)activityWeakReference.get()).avanceProgreso(progress, R.string.busy_ic, R.color.status_gray);
                ((MainActivity)activityWeakReference.get()).setStatusEverything(GlobalStatus.connected, "Xspan conectado", devices.getCode());
                break;
            case disconnected:
                ((MainActivity)activityWeakReference.get()).setStatusEverything(GlobalStatus.unknown, "Xspan desconectado", devices.getCode());
                ((MainActivity)activityWeakReference.get()).finishDesconectar(devices.getCode());
                break;
            case configured: //25 25
                progress+=25;
                if(progress>100) {
                    progress=100;
                }
                ((MainActivity)activityWeakReference.get()).avanceProgreso(progress, R.string.rfid_ok, R.color.status_green);
                ((MainActivity)activityWeakReference.get()).setStatusEverything(GlobalStatus.idle, "Xspan ready", devices.getCode());
                break;
            case reading:
                ((MainActivity)activityWeakReference.get()).setStatusEverything(GlobalStatus.reading, "Xspan leyendo", devices.getCode());
                break;
            case stopreading:
                ((MainActivity)activityWeakReference.get()).setStatusEverything(GlobalStatus.idle, "Xspan ready", devices.getCode());
                break;
            case keepAlive:
                ((MainActivity)activityWeakReference.get()).setStatusEverything(GlobalStatus.idle, "Xspan ready", devices.getCode());
                break;
            case connectionLost:
                bundle = msg.getData();
                ((MainActivity)activityWeakReference.get()).mostrarMensajeDeError(bundle.getString("errorMsg"), devices.getCode());
                break;
            case keepAliveReading:
                ((MainActivity)activityWeakReference.get()).setStatusEverything(GlobalStatus.reading, "Xspan leyendo", devices.getCode());
                break;
            case connectionLostReading:
                bundle = msg.getData();
                ((MainActivity)activityWeakReference.get()).mostrarMensajeDeError(bundle.getString("errorMsg"), devices.getCode());
                break;
        }
        if(xspanRespuesta!=XspanRespuesta.disconnected) {
            ((MainActivity) activityWeakReference.get()).xspanStateMachine(xspanRespuesta, devices.getCode());
        }
    }
}
