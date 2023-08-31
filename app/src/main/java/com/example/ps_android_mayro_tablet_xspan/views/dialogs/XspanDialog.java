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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.Devices;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;
import com.impinj.octane.DirectionFieldOfView;
import com.impinj.octane.DirectionMode;

import java.util.Locale;
import java.util.Objects;

public class XspanDialog extends DialogFragment {
    @SuppressLint("StaticFieldLeak")
    static Context mContext;
    private final EditText[] urlEt = new EditText[2];
    private EditText tagAgeEt;
    private EditText updateIntEt;
    private CheckBox entryRepCb;
    private CheckBox updateRepCb;
    private CheckBox exitRepCb;
    private CheckBox maxPowCb;
    private EditText powerEt;
    private Spinner dirModeS;
    private Spinner fofS;
    static XspanDialogListener listener;

    public static XspanDialog newInstance(Context context, XspanConf xspanConf, ParametrosConexion[] parametrosConexion) {
        mContext = context;
        XspanDialog frag = new XspanDialog();
        Bundle args = new Bundle();
        args.putParcelable("xspanConf", xspanConf);
        args.putParcelableArray("parametros", parametrosConexion);
        frag.setArguments(args);
        return frag;
    }

    private XspanDialog() {
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public void addXspanDialogAdapter(XspanDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(mContext, R.layout.xspan_dialog, null);

        assert getArguments() != null;
        XspanConf xspanConf = getArguments().getParcelable("xspanConf");
        ParametrosConexion[] parametrosConexion = (ParametrosConexion[]) getArguments().getParcelableArray("parametros");

        urlEt[0]= view.findViewById(R.id.url1);
        urlEt[0].setText(parametrosConexion[0].getUrl());
        urlEt[1]= view.findViewById(R.id.url2);
        urlEt[1].setText(parametrosConexion[1].getUrl());

        tagAgeEt = view.findViewById(R.id.tagAge);
        tagAgeEt.setText(String.format(Locale.getDefault(), "%d", xspanConf.getTagAgeIntervalSeconds()));
        updateIntEt = view.findViewById(R.id.updateInt);
        updateIntEt.setText(String.format(Locale.getDefault(), "%d", xspanConf.getUpdateIntervalSeconds()));
        entryRepCb = view.findViewById(R.id.entryRep);
        entryRepCb.setChecked(xspanConf.isEntryReportEnabled());
        updateRepCb = view.findViewById(R.id.updateRep);
        updateRepCb.setChecked(xspanConf.isUpdateReportEnabled());
        exitRepCb = view.findViewById(R.id.exitRep);
        exitRepCb.setChecked(xspanConf.isExitReporrtEnabled());
        maxPowCb = view.findViewById(R.id.maxPow);
        maxPowCb.setChecked(xspanConf.isSetMaxTxPower());
        powerEt = view.findViewById(R.id.powVal);
        powerEt.setText(String.format(Locale.getDefault(), "%.02f", xspanConf.getTxPowerinDbm()));
        dirModeS = view.findViewById(R.id.dirMode);
        ArrayAdapter<DirectionMode> dirM = new ArrayAdapter<>(getContext(), R.layout.mode_spinner_layout, DirectionMode.values());
        dirM.setDropDownViewResource(R.layout.spinner_drop_down_list);
        dirModeS.setAdapter(dirM);
        for(int i=0; i<DirectionMode.values().length; i++) {
            DirectionMode dm = DirectionMode.values()[i];
            if(dm.equals(xspanConf.getDirectionMode())) {
                dirModeS.setSelection(i);
                break;
            }
        }
        fofS = view.findViewById(R.id.dirFov);
        ArrayAdapter<DirectionFieldOfView> dirF = new ArrayAdapter<>(getContext(), R.layout.fov_spinner_layout, DirectionFieldOfView.values());
        dirF.setDropDownViewResource(R.layout.spinner_drop_down_list);
        fofS.setAdapter(dirF);
        for(int i=0; i<DirectionFieldOfView.values().length; i++) {
            DirectionFieldOfView dm = DirectionFieldOfView.values()[i];
            if(dm.equals(xspanConf.getDirectionFieldOfView())) {
                fofS.setSelection(i);
                break;
            }
        }

        Button button = view.findViewById(R.id.button1);
        button.setOnClickListener(v -> getXspan());

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

    private void getXspan() {
        try {
            String[] url = new String[2];
            url[0] = urlEt[0].getText().toString();
            url[1] = urlEt[1].getText().toString();
            int tagAge = Integer.parseInt(tagAgeEt.getText().toString());
            int updtInt = Integer.parseInt(updateIntEt.getText().toString());
            boolean entrRp = entryRepCb.isChecked();
            boolean updtRp = updateRepCb.isChecked();
            boolean extRp = exitRepCb.isChecked();
            boolean maxPow = maxPowCb.isChecked();
            double power = Double.parseDouble(powerEt.getText().toString());
            DirectionMode drmod = (DirectionMode) dirModeS.getSelectedItem();
            DirectionFieldOfView dfov = (DirectionFieldOfView) fofS.getSelectedItem();
            if (url[0].isEmpty()||url[1].isEmpty()) {
                if (listener != null) {
                    listener.emptyparameters();
                }
            } else {
                XspanConf xspanConf = new XspanConf();
                xspanConf.setDirectionMode(drmod);
                xspanConf.setDirectionFieldOfView(dfov);
                xspanConf.setTxPowerinDbm(power);
                xspanConf.setSetMaxTxPower(maxPow);
                xspanConf.setExitReporrtEnabled(extRp);
                xspanConf.setEntryReportEnabled(entrRp);
                xspanConf.setUpdateReportEnabled(updtRp);
                xspanConf.setTagAgeIntervalSeconds((short) tagAge);
                xspanConf.setUpdateIntervalSeconds((short) updtInt);
                ParametrosConexion[] parametrosConexion = new ParametrosConexion[2];
                parametrosConexion[0] = new ParametrosConexion(url[0], 0, Devices.xspan1);
                parametrosConexion[1] = new ParametrosConexion(url[1], 1, Devices.xspan2);
                if (listener != null) {
                    listener.changeXspanSettings(xspanConf, parametrosConexion);
                }
            }
        } catch (NumberFormatException e) {
            if(listener!=null) {
                listener.emptyparameters();
            }
        }
    }
}
