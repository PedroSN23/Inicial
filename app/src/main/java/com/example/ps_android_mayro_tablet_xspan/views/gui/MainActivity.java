package com.example.ps_android_mayro_tablet_xspan.views.gui;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.controller.async.AsyncObtenerEmbarque;
import com.example.ps_android_mayro_tablet_xspan.controller.async.AsyncRespaldarContenedor;
import com.example.ps_android_mayro_tablet_xspan.controller.gpio.GPIOConf;
import com.example.ps_android_mayro_tablet_xspan.controller.gpio.GPIOController;
import com.example.ps_android_mayro_tablet_xspan.controller.handler.MainHandler;
import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanConf;
import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanController;
import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanRespuesta;
import com.example.ps_android_mayro_tablet_xspan.models.clases.EstadosXspan;
import com.example.ps_android_mayro_tablet_xspan.models.clases.GlobalStatus;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;
import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends StatusbarActivity {
    private static final int MY_PERMISSIONS_REQUEST = 1000;
    private XspanController[] xspanController;
    private GPIOController gpioController;
    private boolean dialogShowing=false;
    private MyTimerTaks myTimerTaks;
    private AlertDialog dialog;
    private GPIOConf gpioConf;
    private Window window;

    private static boolean guiOn;

    private final EstadosXspan[] estadosXspan = new EstadosXspan[2];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        window = getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.setStatusBarColor(ContextCompat.getColor(this, R.color.grisAstlix));

        guiOn=true;
        estadosXspan[0] = EstadosXspan.disconnected;
        estadosXspan[1] = EstadosXspan.disconnected;

        revisarListaSeriales();

        checarPermisos();

        MainHandler mainHandler = new MainHandler(this);
        ParametrosConexion[] parametrosConexions = sqliteInterface.obtenerConexionXspan();
        xspanController = XspanController.getInstance(mainHandler, parametrosConexions);
        gpioConf = sqliteInterface.obtenerGPIO();
        gpioController = GPIOController.getInstance(mainHandler, sqliteInterface.obtenerConexionGPIO());

        setMaxVal(100);
        setProgresoVisible(true);
    }

    @Override
    protected void onStart() {
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onStart();
    }

    @Override
    protected void onStop() {
        window.clearFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onStop();
    }

    private void revisarListaSeriales() {
        boolean found=false;

        @SuppressLint("HardwareIds") String serial = Settings.Secure.getString(getContentResolver(), Settings.Secure.ANDROID_ID);
        String [] stringArray = getResources().getStringArray(R.array.serial);

        for (String s : stringArray) {
            if (s.compareTo(serial) == 0) {
                found = true;
                break;
            }
        }
        if(!found) {
            Log.d("Serial", serial);
            Toast.makeText(this, serial, Toast.LENGTH_LONG).show();
            finishAndRemoveTask();
        }
    }

    @Override
    protected void onDestroy() {
        guiOn=false;
        gpioController.stopThread();
        GPIOController.destroyInstance();
        XspanController.destroyInstance(0);
        XspanController.destroyInstance(1);
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        new android.app.AlertDialog.Builder(this)
                .setTitle("Confirmar Salir")
                .setMessage("¿Desar salir de la aplicación?")
                .setPositiveButton(android.R.string.ok, (dialogInterface, i) -> {
                    guiOn=false;
                    boolean finish = false;
                    switch (estadosXspan[0]) {
                        case disconnected:
                            finish=true;
                            break;
                        case connected:
                        case idle:
                        case reading:
                            xspanController[0].desconectar();
                            break;
                    }
                    switch (estadosXspan[1]) {
                        case disconnected:
                            if(finish) {
                                finishAndRemoveTask();
                            }
                            break;
                        case connected:
                        case idle:
                        case reading:
                            xspanController[1].desconectar();
                            break;
                    }
                })
                .setNegativeButton(android.R.string.cancel, (dialogInterface, i) -> {
                })
                .setIcon(android.R.drawable.ic_dialog_alert)
                .show();
    }

    /************************GENERAL*****************************************/
    public void mostrarMensajeDeError(String msg, int index) {
        setStatusIcon(GlobalStatus.error, index);
        setStatusTexto(msg, index);
        mostrarMensajeDeErrorDialog(msg);
    }

    public void mostrarMensajeDeErrorDialog(String msg) {
        if(!dialogShowing) {
            dialogShowing = true;
            Timer timer = new Timer();
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Error")
                    .setMessage(msg)
                    .setIcon(android.R.drawable.ic_dialog_alert)
                    .setCancelable(false);
            myTimerTaks = new MyTimerTaks(timer);
            timer.schedule(myTimerTaks, 1, 500);
            dialog = builder.show();
        } else {
            myTimerTaks.reset();
        }
    }

    public void mostrarMensajeExito(int folio_contenedor) {
        if(!dialogShowing) {
            dialogShowing = true;
            Timer timer = new Timer();
            AlertDialog.Builder builder = new AlertDialog.Builder(this)
                    .setTitle("Éxito")
                    .setMessage(String.format(Locale.getDefault(), "Contenedor %d guardado", folio_contenedor))
                    .setIcon(android.R.drawable.ic_dialog_info)
                    .setCancelable(false);
            myTimerTaks = new MyTimerTaks(timer);
            timer.schedule(myTimerTaks, 1, 500);
            dialog = builder.show();
        } else {
            myTimerTaks.reset();
        }
    }

    public class MyTimerTaks extends TimerTask {
        private int count = 0;
        private final Timer timer;

        public MyTimerTaks (Timer timer) {
            this.timer = timer;
        }

        @Override
        public void run() {
            if(count==5) {
                timer.cancel();
                timer.purge();
                if(dialogShowing) {
                    dialogShowing=false;
                    dialog.dismiss();
                }
            }
            count++;
        }

        public void reset() {
            count=0;
        }
    }
    public void setStatusEverything(GlobalStatus status, String msg, int index) {
        setStatusTexto(msg, index);
        setStatusIcon(status, index);
    }

    /************************XSPAN*****************************************/
    public void finishDesconectar(int index) {
        estadosXspan[index] = EstadosXspan.disconnected;
        if(estadosXspan[0]==estadosXspan[1]) {
            finishAndRemoveTask();
        }
    }

    @Override
    public void onXspanParamsChanged(XspanConf xspanConf, ParametrosConexion[] parametrosConexion) {
        boolean changes = xspanController[0].updateParametrosConexion(parametrosConexion[0]);
        switch (estadosXspan[0]) {
            case disconnected:
                xspanController[0].conectar();
                break;
            case connected:
            case idle:
            case reading:
                xspanController[0].cambiarConexion(xspanConf, changes);
                break;
        }
        changes = xspanController[1].updateParametrosConexion(parametrosConexion[1]);
        switch (estadosXspan[1]) {
            case disconnected:
                xspanController[1].conectar();
                break;
            case connected:
            case idle:
            case reading:
                xspanController[1].cambiarConexion(xspanConf, changes);
                break;
        }
    }

    public void xspanStateMachine(XspanRespuesta xspanRespuesta, int index) {
        switch (estadosXspan[index]) {
            case disconnected:
                switch (xspanRespuesta) {
                    case keepAliveReading:
                        estadosXspan[index] = EstadosXspan.reading;
                        if(gpioConf.getSensor().getState()==0) {
                            xspanController[index].finalizarLectura();
                        }
                        break;
                    case keepAlive:
                        estadosXspan[index] = EstadosXspan.idle;
                        if(gpioConf.getSensor().getState()==1) {
                            xspanController[index].iniciarLectura();
                        }
                        break;
                    case connected:
                        estadosXspan[index] = EstadosXspan.connected;
                        xspanController[index].configurar(sqliteInterface.obtenerXspan());
                        break;
                }
                break;
            case connected:
                switch (xspanRespuesta) {
                    case configured:
                        estadosXspan[index] = EstadosXspan.idle;
                        if(gpioConf.getSensor().getState()==1) {
                            xspanController[index].iniciarLectura();
                        }
                        break;
                    case connectionLost:
                    case error:
                        estadosXspan[index] = EstadosXspan.disconnected;
                        break;
                }
                break;
            case idle:
                switch (xspanRespuesta) {
                    case connectionLost:
                    case error:
                        estadosXspan[index] = EstadosXspan.disconnected;
                        break;
                    case reading:
                        estadosXspan[index] = EstadosXspan.reading;
                        if(gpioConf.getSensor().getState()==0) {
                            xspanController[index].finalizarLectura();
                        }
                        break;
                }
                break;
            case reading:
                switch (xspanRespuesta) {
                    case connectionLostReading:
                    case connectionLost:
                    case error:
                        estadosXspan[index] = EstadosXspan.disconnected;
                        break;
                    case stopreading:
                        estadosXspan[index] = EstadosXspan.idle;
                        if(gpioConf.getSensor().getState()==1) {
                            xspanController[index].iniciarLectura();
                        }
                        break;
                }
        }
    }

    /**************************ASYNC TASKS********************************/
    @SuppressWarnings("deprecation")
    @Override
    public void onBuscarEnBd(String epc) {
        AsyncObtenerEmbarque asyncObtenerEmbarque = new AsyncObtenerEmbarque(this, sqliteInterface.obtenerBase());
        asyncObtenerEmbarque.execute(epc);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onRespaldarContenedorBaseServidor(Contenedores contenedores, int id_embarque) {
        AsyncRespaldarContenedor asyncRespaldarContenedor = new AsyncRespaldarContenedor(this,
                sqliteInterface.obtenerBase(),
                sqliteInterface.obtenerServidor(),
                contenedores);
        asyncRespaldarContenedor.execute(id_embarque);
    }

    /************************GPIO*****************************************/
    public void setSensorChanges(int state) {
        if(gpioConf.getSensor().getState()!=state) {
            gpioConf.getSensor().setState(state);
            if(state==0) { //Puerta cerrada
                if(estadosXspan[0] == EstadosXspan.reading) {
                    xspanController[0].finalizarLectura();
                }
                if(estadosXspan[1] == EstadosXspan.reading) {
                    xspanController[1].finalizarLectura();
                }
            } else {
                if(estadosXspan[0] == EstadosXspan.idle) {
                    xspanController[0].iniciarLectura();
                }
                if(estadosXspan[1] == EstadosXspan.idle) {
                    xspanController[1].iniciarLectura();
                }
            }
        }
    }

    public void iniciarTimerGpio() {
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                if(guiOn) {
                    gpioController.getSensorState(gpioConf.getSensor().getPort_id());
                }
            }
        }, 1000);
    }

    @Override
    public void changeGpios(GPIOConf gpioConf, ParametrosConexion parametrosConexion) {
        gpioController.updateParametrosConexion(parametrosConexion);
        this.gpioConf.setLuzRojaPort(gpioConf.getLuzRoja().getPort_id());
        this.gpioConf.setLuzAmbarPort(gpioConf.getLuzAmbar().getPort_id());
        this.gpioConf.setLuzVerdePort(gpioConf.getLuzVerde().getPort_id());
        this.gpioConf.setSensorPort(gpioConf.getSensor().getPort_id());
    }

    @Override
    public void onPrenderRojo() {
        gpioController.setLuzRoja(gpioConf);
    }

    @Override
    public void onPrenderAmbar() {
        gpioController.setLuzAmbar(gpioConf);
    }

    @Override
    public void onPrenderVerde() {
        gpioController.setLuzVerde(gpioConf);
    }

    @Override
    public void onApagarLuces() {
        gpioController.apgarLuces(gpioConf);
    }

    /***********************PERMISOS**************************/
    private void checarPermisos() {
        ArrayList<String> lista = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(MainActivity.this, Manifest.permission.INTERNET) != PackageManager.PERMISSION_GRANTED) {
            lista.add(Manifest.permission.INTERNET);
        }
        if (lista.size() > 0) {
            String[] strings = new String[lista.size()];
            for (int i = 0; i < lista.size(); i++) {
                strings[i] = lista.get(i);
            }
            ActivityCompat.requestPermissions(MainActivity.this, strings, MY_PERMISSIONS_REQUEST);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if(requestCode==MY_PERMISSIONS_REQUEST) {
            if(grantResults.length>0 && grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                MainActivity.this.finish();
            }
        }
    }
}