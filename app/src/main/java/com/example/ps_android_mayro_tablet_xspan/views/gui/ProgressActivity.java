package com.example.ps_android_mayro_tablet_xspan.views.gui;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.models.database.SqliteInterface;
import com.example.ps_android_mayro_tablet_xspan.views.subclases.ProgressIcons;
import com.google.android.material.progressindicator.CircularProgressIndicator;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;

public class ProgressActivity extends AppCompatActivity {
    private RelativeLayout progreso;
    private boolean progresoVisible=true;
    private CircularProgressIndicator progressIndicator;
    private ProgressIcons progressIcons;
    private Context context;
    private int valor = 0;
    private int nexVal = 0;
    private int maxValor = 0;
    private Timer timer;
    private final AtomicBoolean finalizar = new AtomicBoolean(true);

    public SqliteInterface sqliteInterface;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        context = this;
        progreso = findViewById(R.id.progreso);
        progressIndicator = findViewById(R.id.circleProg);
        progressIcons = findViewById(R.id.iconProg);

        sqliteInterface = new SqliteInterface(context);
    }

    @Override
    protected void onDestroy() {
        sqliteInterface.close();
        super.onDestroy();
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    public boolean isProgresoVisible() {
        return progresoVisible;
    }

    public void setProgresoVisible(boolean b) {
        this.finalizar.set(b);
        this.progresoVisible = true;
        progreso.setVisibility(View.VISIBLE);
        progressIndicator.setProgress(0);
        valor = 0;
        nexVal = 0;
        timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(nexVal>valor) {
                    valor++;
                    runOnUiThread(() -> {
                        progressIndicator.setProgress(valor);
                        if(valor==maxValor && finalizar.get()) {
                            timer.purge();
                            timer.cancel();
                            progresoFinalizado();
                        }
                    });
                }
            }
        }, 10, 10);
        progressIcons.setText(getResources().getString(R.string.busy_ic));
        progressIcons.setTextColor(ContextCompat.getColor(context, R.color.status_gray));
    }

    public void hideProgreso() {
        this.progresoVisible = false;
        progreso.setVisibility(View.GONE);
    }

    public void setMaxVal(int maxVal) {
        maxValor=maxVal;
        progressIndicator.setMax(maxVal);
    }

    public void progresoFinalizado() {
        Log.d("PROGRESO", "progresoFinalizado");
        Timer timer1 = new Timer();
        timer1.schedule(new TimerTask() {
            @Override
            public void run() {
                runOnUiThread(() -> {
                    enableDisableChangePage(true);
                    progresoVisible=false;
                    progreso.setVisibility(View.GONE);
                });
            }
        }, 1000);
    }

    public void avanceProgreso(int val, int icon, int color) {
        if(progresoVisible) {
            nexVal = val;
            progressIcons.setText(getResources().getString(icon));
            progressIcons.setTextColor(ContextCompat.getColor(context, color));
        }
    }

    public void setFinalizar(boolean b) {
        this.finalizar.set(b);
    }

    public void enableDisableChangePage(boolean b) {
    }
}
