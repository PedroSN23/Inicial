package com.example.ps_android_mayro_tablet_xspan.controller.gpio;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ps_android_mayro_tablet_xspan.models.clases.ComMethod;
import com.example.ps_android_mayro_tablet_xspan.models.clases.Devices;
import com.example.ps_android_mayro_tablet_xspan.models.clases.Luces;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ServerResponse;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.ref.WeakReference;
import java.util.Locale;

import javax.net.ssl.HttpsURLConnection;

public class GPIOHandler extends Handler {
    private WeakReference<Handler> mainHandlerWeakReference;

    @SuppressWarnings("deprecation")
    public GPIOHandler(Handler mainHandler) {
        mainHandlerWeakReference = new WeakReference<>(mainHandler);
    }

    public void updateMainHandler(Handler mainHandler) {
        mainHandlerWeakReference = new WeakReference<>(mainHandler);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Message msgOut;
        Bundle bundle;
        Luces luces;
        GPIOInstruction gpioInstruction = GPIOInstruction.none;
        for (GPIOInstruction ii : GPIOInstruction.values()) {
            if (ii.getCode() == msg.arg1) {
                gpioInstruction = ii;
                break;
            }
        }
        switch (gpioInstruction) {
            case none:
                msgOut = mainHandlerWeakReference.get().obtainMessage();
                msgOut.arg1 = Devices.gpio.getCode(); //
                msgOut.arg2 = GPIORespuesta.error.getCode();
                msgOut.obj = "No se recibió instrucción";
                mainHandlerWeakReference.get().sendMessage(msgOut);
                break;
            case init:
                msgOut = mainHandlerWeakReference.get().obtainMessage();
                msgOut.arg1 = Devices.gpio.getCode(); //
                msgOut.arg2 = GPIORespuesta.ready.getCode();
                mainHandlerWeakReference.get().sendMessage(msgOut);
                break;
            case sensor:
                bundle=msg.getData();
                int temp = revisarSensores(bundle.getInt("sensor"), bundle.getParcelable("parametrosConexion"));
                Log.d("SENSOR", "regreso "+temp);
                msgOut = mainHandlerWeakReference.get().obtainMessage();
                msgOut.arg1= Devices.gpio.getCode();
                bundle = new Bundle();
                bundle.putInt("estado", temp);
                msgOut.setData(bundle);
                if(temp!=-1) {
                    msgOut.arg2= GPIORespuesta.sensor.getCode();
                } else {
                    msgOut.arg2 = GPIORespuesta.error_sensor.getCode();
                    msgOut.obj = "Error al obtener estado de sensor";
                }
                mainHandlerWeakReference.get().sendMessage(msgOut);
                break;
            case luces_out:
                bundle=msg.getData();
                msgOut = mainHandlerWeakReference.get().obtainMessage();
                msgOut.arg1= Devices.gpio.getCode(); //
                luces = bundle.getParcelable("luces");
                if(cambiarLuces(luces, bundle.getParcelable("parametrosConexion"))) {
                    Log.d("SENSOR", "cambiar luces exito");
                    msgOut.arg2= GPIORespuesta.luces_out.getCode();
                    bundle = new Bundle();
                    bundle.putParcelable("luces", luces);
                    msgOut.setData(bundle);
                } else {
                    Log.d("SENSOR", "cambiar luces FRACASO");
                    msgOut.arg2 = GPIORespuesta.error.getCode();
                    msgOut.obj = "Error al cambiar luces";
                }
                mainHandlerWeakReference.get().sendMessage(msgOut);
                break;
            case luces_in:
                bundle=msg.getData();
                luces = revisarLuces(bundle.getParcelable("luces"), bundle.getParcelable("parametrosConexion"));
                msgOut = mainHandlerWeakReference.get().obtainMessage();
                msgOut.arg1= Devices.gpio.getCode();
                if(luces!=null) {
                    msgOut.arg2= GPIORespuesta.luces_in.getCode();
                    bundle = new Bundle();
                    bundle.putParcelable("luces", luces);
                    msgOut.setData(bundle);
                } else {
                    msgOut.arg2 = GPIORespuesta.error.getCode();
                    msgOut.obj = "Error al obtener estado de luces";
                }
                mainHandlerWeakReference.get().sendMessage(msgOut);
                break;
            case stop:
                getLooper().quitSafely();
                break;
        }
    }

    private int revisarSensores(int puerto, ParametrosConexion parametrosConexion) {
        Log.d("SENSOR", "REVISAR SENSORES");
        try {
            JSONObject port = new JSONObject();
            port.put("port_id", String.format(Locale.getDefault(), "%d", puerto));
            JSONArray jsonArray = new JSONArray();
            jsonArray.put(port);
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("io_ports", jsonArray);
            ServerResponse resp = GPIOUtils.connectNormal(parametrosConexion, ComMethod.get, GPIOPaths.inputs.toString(), jsonObject.toString());
            resp.checkResponse();
            Log.d("SENSOR", resp.getResponseString());
            if (resp.getCode() == HttpsURLConnection.HTTP_OK) {
                JSONObject obj = resp.getResponseJson();
                JSONArray arr = (JSONArray) obj.get("io_ports");
                JSONObject obj2 = arr.getJSONObject(0);
                int port_obtained = Integer.parseInt((String)obj2.get("port_id"));
                Log.d("SENSOR", port_obtained+"?"+puerto);
                if(port_obtained==puerto) {
                    return Integer.parseInt((String) obj2.get("state"));
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Log.d("SENSOR", "return -1");
        return -1;
    }
    private boolean cambiarLuces(Luces luces, ParametrosConexion parametrosConexion) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < luces.getSize(); i++) {
                JSONObject element = new JSONObject();
                element.put("port_id", String.format(Locale.getDefault(), "%d", luces.getAt(i).getPort_id()));
                element.put("state", String.format(Locale.getDefault(), "%d", luces.getAt(i).getState()));
                jsonArray.put(element);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("io_ports", jsonArray);
            ServerResponse resp = GPIOUtils.connectNormal(parametrosConexion, ComMethod.post, GPIOPaths.outputs.toString(), jsonObject.toString());
            resp.checkResponse();
            if (resp.getCode() == HttpsURLConnection.HTTP_OK) {
                return true;
            }
            Log.d("SENSOR", "codigo "+resp.getCode());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return false;
    }

    private Luces revisarLuces(Luces luces, ParametrosConexion parametrosConexion) {
        try {
            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < luces.getSize(); i++) {
                JSONObject element = new JSONObject();
                element.put("port_id", String.format(Locale.getDefault(), "%d", luces.getAt(i).getPort_id()));
                jsonArray.put(element);
            }
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("io_ports", jsonArray);
            ServerResponse resp = GPIOUtils.connectNormal(parametrosConexion, ComMethod.get, GPIOPaths.outputs.toString(), jsonObject.toString());
            resp.checkResponse();
            if (resp.getCode() == HttpsURLConnection.HTTP_OK) {
                for(int x=0; x<luces.getSize(); x++) {
                    luces.getAt(x).setState(-1);
                }
                JSONObject obj = resp.getResponseJson();
                JSONArray arr = (JSONArray) obj.get("io_ports");
                for(int i=0; i<arr.length(); i++) {
                    JSONObject obj2 = arr.getJSONObject(i);
                    int port = Integer.parseInt((String) obj2.get("port_id"));
                    int state = Integer.parseInt((String) obj2.get("state"));
                    for(int j=0; j< luces.getSize(); j++) {
                        if(luces.getAt(j).getPort_id()==port) {
                            luces.getAt(j).setState(state);
                            break;
                        }
                    }
                }
                return luces;
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }
}
