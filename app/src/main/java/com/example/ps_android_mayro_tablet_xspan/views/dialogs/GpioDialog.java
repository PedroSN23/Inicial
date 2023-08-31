package com.example.ps_android_mayro_tablet_xspan.views.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.controller.gpio.GPIOConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.Devices;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;

import java.util.Locale;
import java.util.Objects;

public class GpioDialog extends DialogFragment {
    @SuppressLint("StaticFieldLeak")
    static Context mContext;
    private EditText urlEt;
    private EditText portEt;
    private EditText luzRojaEt;
    private EditText luzAmbarEt;
    private EditText luzVerdeEt;
    private EditText sensorEt;
    static GpioDialogListener listener;

    public static GpioDialog newInstance(Context context, GPIOConf gpioConf, ParametrosConexion parametrosConexion) {
        mContext = context;
        GpioDialog frag = new GpioDialog();
        Bundle args = new Bundle();
        args.putParcelable("gpioConf", gpioConf);
        args.putParcelable("parametros", parametrosConexion);
        frag.setArguments(args);
        return frag;
    }

    private GpioDialog() {
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public void addGpioDialogAdapter(GpioDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(mContext, R.layout.gpio_dialog, null);

        assert getArguments() != null;
        GPIOConf gpioConf = getArguments().getParcelable("gpioConf");
        ParametrosConexion parametrosConexion = getArguments().getParcelable("parametros");

        urlEt= view.findViewById(R.id.url);
        urlEt.setText(parametrosConexion.getUrl());
        portEt = view.findViewById(R.id.puerto);
        portEt.setText(String.format(Locale.getDefault(), "%d", parametrosConexion.getPort()));
        luzRojaEt = view.findViewById(R.id.luzRoja);
        luzRojaEt.setText(String.format(Locale.getDefault(), "%d", gpioConf.getLuzRoja().getPort_id()));
        luzAmbarEt = view.findViewById(R.id.luzAmbar);
        luzAmbarEt.setText(String.format(Locale.getDefault(), "%d", gpioConf.getLuzAmbar().getPort_id()));
        luzVerdeEt = view.findViewById(R.id.luzVerde);
        luzVerdeEt.setText(String.format(Locale.getDefault(), "%d", gpioConf.getLuzVerde().getPort_id()));
        sensorEt = view.findViewById(R.id.sensor);
        sensorEt.setText(String.format(Locale.getDefault(), "%d", gpioConf.getSensor().getPort_id()));

        Button button = view.findViewById(R.id.button1);
        button.setOnClickListener(v -> getGpio());

        button = view.findViewById(R.id.button2);
        button.setOnClickListener(view1 -> {
            if(listener!=null) {
                listener.closeDialog();
            }
        });


        alertDialogBuilder.setView(view);

        return alertDialogBuilder.create();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Objects.requireNonNull(getDialog()).getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    private void getGpio() {
        try {
            String url = urlEt.getText().toString();
            int port = Integer.parseInt(portEt.getText().toString());
            int luzRoja = Integer.parseInt(luzRojaEt.getText().toString());
            int luzAmbar = Integer.parseInt(luzAmbarEt.getText().toString());
            int luzVerde = Integer.parseInt(luzVerdeEt.getText().toString());
            int sensor = Integer.parseInt(sensorEt.getText().toString());
            if (url.isEmpty()) {
                if (listener != null) {
                    listener.emptyparameters();
                }
            } else {
                GPIOConf gpioConf = new GPIOConf();
                gpioConf.setSensorPort(sensor);
                gpioConf.setLuzVerdePort(luzVerde);
                gpioConf.setLuzAmbarPort(luzAmbar);
                gpioConf.setLuzRojaPort(luzRoja);
                ParametrosConexion parametrosConexion = new ParametrosConexion(url, port, Devices.gpio);
                if (listener != null) {
                    listener.changeGpioSettings(gpioConf, parametrosConexion);
                }
            }
        } catch (NumberFormatException e) {
            if(listener!=null) {
                listener.emptyparameters();
            }
        }
    }
}
