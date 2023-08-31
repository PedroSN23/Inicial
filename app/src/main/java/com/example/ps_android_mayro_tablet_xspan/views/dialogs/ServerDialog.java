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
import com.example.ps_android_mayro_tablet_xspan.models.clases.ServidorConf;

import java.util.Locale;
import java.util.Objects;

public class ServerDialog extends DialogFragment {
    @SuppressLint("StaticFieldLeak")
    static Context mContext;
    private EditText urlEt;
    private EditText userEt;
    private EditText domainEt;
    private EditText passwordEt;
    private EditText soapActEt;
    private EditText portEt;
    private EditText serviEt;
    static ServerDialogListener listener;

    public static ServerDialog newInstance(Context context, ServidorConf servidorConf) {
        mContext = context;
        ServerDialog frag = new ServerDialog();
        Bundle args = new Bundle();
        args.putParcelable("servidorConf", servidorConf);
        frag.setArguments(args);
        return frag;
    }

    private ServerDialog() {
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public void addServerDialogAdapter(ServerDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
        View view = View.inflate(mContext, R.layout.servidor_dialog, null);

        assert getArguments() != null;
        ServidorConf servidorConf = getArguments().getParcelable("servidorConf");

        urlEt= view.findViewById(R.id.url);
        urlEt.setText(servidorConf.getUrl());
        domainEt = view.findViewById(R.id.domain);
        domainEt.setText(servidorConf.getDomain());
        userEt = view.findViewById(R.id.user);
        userEt.setText(servidorConf.getUser());
        passwordEt = view.findViewById(R.id.password);
        passwordEt.setText(servidorConf.getPassword());
        soapActEt = view.findViewById(R.id.soapact);
        soapActEt.setText(servidorConf.getSoapaction());
        serviEt = view.findViewById(R.id.servicio);
        serviEt.setText(servidorConf.getSoapaction());
        portEt = view.findViewById(R.id.port);
        portEt.setText(String.format(Locale.getDefault(), "%d", servidorConf.getPort()));

        Button button = view.findViewById(R.id.button1);
        button.setOnClickListener(v -> getServer());

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

    private void getServer() {
        String url = urlEt.getText().toString();
        String domain = domainEt.getText().toString();
        String user = userEt.getText().toString();
        String password = passwordEt.getText().toString();
        String soapAction = soapActEt.getText().toString();
        String servicio = serviEt.getText().toString();
        try {
            int port = Integer.parseInt(portEt.getText().toString());
            if (url.isEmpty() || domain.isEmpty() || user.isEmpty() || password.isEmpty() || soapAction.isEmpty() || servicio.isEmpty()) {
                if (listener != null) {
                    listener.emptyparameters();
                }
            } else {
                ServidorConf servidorConf = new ServidorConf(url, domain, user, password, soapAction, port, servicio);
                if (listener != null) {
                    listener.changeServerSettings(servidorConf);
                }
            }
        } catch (NumberFormatException e) {
            if (listener != null) {
                listener.emptyparameters();
            }
        }
    }
}
