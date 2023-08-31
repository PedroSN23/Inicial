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
import com.example.ps_android_mayro_tablet_xspan.models.clases.DialogTypes;

public class LoginDialog extends DialogFragment {
    private final Context context;
    private LoginDialogListener listener;
    private DialogTypes types = DialogTypes.none;

    public static LoginDialog getInstance(Context context, DialogTypes dialogTypes) {
        LoginDialog frag = new LoginDialog(context);
        Bundle args = new Bundle();
        args.putInt("type", dialogTypes.getCode());
        frag.setArguments(args);
        return frag;
    }

    public LoginDialog(Context context) {
        this.context = context;
    }

    public void addLoginDialogListener(LoginDialogListener listener) {
        this.listener = listener;
    }

    @SuppressLint("WrongConstant")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        View promptsView = View.inflate(context, R.layout.dialog_autentificar_administrador, null);
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());

        Bundle bundle = getArguments();
        if(bundle!=null) {
            int temp = bundle.getInt("type");
            for(DialogTypes dt: DialogTypes.values()) {
                if(dt.getCode()==temp) {
                    types=dt;
                    break;
                }
            }
        }

        final EditText usuario = promptsView.findViewById(R.id.admUser);
        final EditText password = promptsView.findViewById(R.id.admPass);
        @SuppressLint("CutPasteId") Button button1 = promptsView.findViewById(R.id.button1);
        Button button2 = promptsView.findViewById(R.id.button2);


        button1.setOnClickListener(view -> {
            String user = usuario.getText().toString();
            String pass = password.getText().toString();
            if (user.length() > 0 && pass.length() > 0 && user.compareTo("root")==0 && pass.compareTo("acm1ptn0w")==0) {
                if(listener!=null) {
                    listener.abrirDialogo(types);
                }
            } else {
                if(listener!=null) {
                    listener.dialogWarning("Usuario y/o contraseña erroneos");
                }
            }
        });

        button2.setOnClickListener(view -> {
            if(listener!=null) {
                listener.cerrarDialogo();
            }
        });

        alertDialogBuilder.setView(promptsView);

        return alertDialogBuilder.create();
    }

    @SuppressWarnings("ConstantConditions")
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        return super.onCreateView(inflater, container, savedInstanceState);
    }
}
