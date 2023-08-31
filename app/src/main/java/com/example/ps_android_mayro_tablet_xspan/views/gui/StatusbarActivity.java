package com.example.ps_android_mayro_tablet_xspan.views.gui;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.core.content.ContextCompat;

import com.example.ps_android_mayro_tablet_xspan.BuildConfig;
import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.models.clases.GlobalStatus;
import com.example.ps_android_mayro_tablet_xspan.models.clases.TipoLuz;
import com.example.ps_android_mayro_tablet_xspan.views.subclases.StatusIcon;

import java.util.Locale;

public class StatusbarActivity extends ToolbarActivity {
    private final TextView[] texto = new TextView[2];
    private final StatusIcon[] icono = new StatusIcon[2];
    private final ImageView[] imageView = new ImageView[4];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        texto[0] = findViewById(R.id.texto1);
        texto[1] = findViewById(R.id.texto2);
        icono[0] = findViewById(R.id.icono1);
        icono[1 ] = findViewById(R.id.icono2);
        TextView version = findViewById(R.id.version);
        version.setText(String.format(Locale.getDefault(), "V %s", BuildConfig.VERSION_NAME));
        imageView[0] = findViewById(R.id.sensor);
        imageView[1] = findViewById(R.id.semaforo_v);
        imageView[2] = findViewById(R.id.semaforo_a);
        imageView[3] = findViewById(R.id.semaforo_r);
    }

    public void setStatusIcon(GlobalStatus status, int index) {
        icono[index].setText(getResources().getString(status.getStatus_icon()));
        icono[index].setTextColor(ContextCompat.getColor(this, status.getStatus_color()));
    }

    public void setStatusTexto(String text, int index) {
        texto[index].setText(text);
    }

    public void setGpioState(int state, TipoLuz type) {
        if(state==1) {
            imageView[type.getCode()].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), type.getDrawable()));
        } else {
            if(state==0) {
                imageView[type.getCode()].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.apagado));
            } else {
                imageView[type.getCode()].setImageDrawable(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_error));
            }
        }
    }
}
