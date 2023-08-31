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
import com.example.ps_android_mayro_tablet_xspan.models.clases.BaseConf;

import java.util.Objects;

public class BaseDialog extends DialogFragment {
    @SuppressLint("StaticFieldLeak")
    static Context mContext;
    private EditText urlEt;
    private EditText userEt;
    private EditText passwordEt;
    private EditText nameEt;
    static BaseDialogListener listener;

    public static BaseDialog newInstance(Context context, BaseConf baseConf) {
        mContext = context;
        BaseDialog frag = new BaseDialog();
        Bundle args = new Bundle();
        args.putParcelable("baseConf", baseConf);
        frag.setArguments(args);
        return frag;
    }

    private BaseDialog() {
    }

    @SuppressWarnings("AccessStaticViaInstance")
    public void addBaseDialogAdapter(BaseDialogListener listener) {
        this.listener = listener;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        View view = View.inflate(mContext, R.layout.base_dialog, null);

        assert getArguments() != null;
        BaseConf baseConf = getArguments().getParcelable("baseConf");

        urlEt= view.findViewById(R.id.url);
        urlEt.setText(baseConf.getUrl());
        userEt = view.findViewById(R.id.user);
        userEt.setText(baseConf.getUser());
        passwordEt = view.findViewById(R.id.password);
        passwordEt.setText(baseConf.getPassword());
        nameEt = view.findViewById(R.id.name);
        nameEt.setText(baseConf.getName());

        Button button = view.findViewById(R.id.button1);
        button.setOnClickListener(v -> getBase());

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

    private void getBase() {
        String url = urlEt.getText().toString();
        String user = userEt.getText().toString();
        String password = passwordEt.getText().toString();
        String name = nameEt.getText().toString();
        if(url.isEmpty() || user.isEmpty() || password.isEmpty() || name.isEmpty()) {
            if(listener!=null) {
                listener.emptyparameters();
            }
        } else {
            BaseConf baseConf = new BaseConf(url, user, password, name);
            if(listener!=null) {
                listener.changeBaseSettings(baseConf);
            }
        }
    }
}
