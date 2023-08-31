package com.example.ps_android_mayro_tablet_xspan.views.gui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.view.menu.MenuBuilder;
import androidx.fragment.app.FragmentManager;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.controller.gpio.GPIOConf;
import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.BaseConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.DialogTypes;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ServidorConf;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.BaseDialog;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.BaseDialogListener;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.GpioDialog;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.GpioDialogListener;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.LoginDialog;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.LoginDialogListener;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.ServerDialog;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.ServerDialogListener;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.XspanDialog;
import com.example.ps_android_mayro_tablet_xspan.views.dialogs.XspanDialogListener;

public class ToolbarActivity extends ContentActivity {
    private Context context;
    private MenuItem finish;

    @SuppressWarnings("ConstantConditions")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        androidx.appcompat.widget.Toolbar toolbar = findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @SuppressLint("RestrictedApi")
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.operation_menu, menu);
        ((MenuBuilder) menu).setOptionalIconsVisible(true);
        finish = menu.findItem(R.id.finish);
        return true;
    }

    @Override
    public void onSetFinishVisible(boolean b) {
        finish.setVisible(b);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.xspan:
                if(!isProgresoVisible()) {
                    crearDialogoAdministrador(DialogTypes.xspan);
                }
                break;
            case R.id.database:
                if(!isProgresoVisible()) {
                    crearDialogoAdministrador(DialogTypes.base);
                }
                break;
            case R.id.gpio:
                if(!isProgresoVisible()) {
                    crearDialogoAdministrador(DialogTypes.gpio);
                }
                break;
            case R.id.server:
                if(!isProgresoVisible()) {
                    crearDialogoAdministrador(DialogTypes.server);
                }
                break;
            case R.id.finish:
                if(!isProgresoVisible()) {
                    onSetFinishVisible(false);
                    clearListContent();
                }
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void crearDialogoAdministrador(DialogTypes types) {
        FragmentManager fm = getSupportFragmentManager();
        LoginDialog dialog = LoginDialog.getInstance(context,
                types);
        dialog.setCancelable(false);
        dialog.addLoginDialogListener(new LoginDialogListener() {
            @Override
            public void dialogWarning(String s) {
                Toast.makeText(context, s, Toast.LENGTH_LONG).show();
            }

            @Override
            public void cerrarDialogo() {
                dialog.dismiss();
            }

            @Override
            public void abrirDialogo(DialogTypes types) {
                dialog.dismiss();
                switch (types) {
                    case base:
                        onDatabaseDialog();
                        break;
                    case gpio:
                        onGpioDialog();
                        break;
                    case xspan:
                        onXspanDialog();
                        break;
                    case server:
                        onServerDialog();
                        break;
                }
            }

        });
        dialog.show(fm, "dialog");
    }

    private void onXspanDialog() {
        FragmentManager fm = getSupportFragmentManager();
        XspanDialog dialog = XspanDialog.newInstance(context,
                sqliteInterface.obtenerXspan(),
                sqliteInterface.obtenerConexionXspan());
        dialog.setCancelable(false);
        dialog.addXspanDialogAdapter(new XspanDialogListener() {
            @Override
            public void closeDialog() {
                dialog.dismiss();
            }

            @Override
            public void emptyparameters() {
                Toast.makeText(context, "Error: Parámetros vacíos", Toast.LENGTH_LONG).show();
            }

            @Override
            public void changeXspanSettings(XspanConf xspanConf, ParametrosConexion[] parametrosConexion) {
                sqliteInterface.modificarXspan(xspanConf, parametrosConexion);
                dialog.dismiss();
                Toast.makeText(context, "Parámetros guardados", Toast.LENGTH_LONG).show();
                onXspanParamsChanged(xspanConf, parametrosConexion);
            }
        });
        dialog.show(fm, "dialog");
    }

    public void onXspanParamsChanged(XspanConf xspanConf, ParametrosConexion[] parametrosConexion) {
    }

    private void onDatabaseDialog() {
        FragmentManager fm = getSupportFragmentManager();
        BaseDialog dialog = BaseDialog.newInstance(context,
                sqliteInterface.obtenerBase());
        dialog.setCancelable(false);
        dialog.addBaseDialogAdapter(new BaseDialogListener() {
            @Override
            public void closeDialog() {
                dialog.dismiss();
            }

            @Override
            public void emptyparameters() {
                Toast.makeText(context, "Error: Parámetros vacíos", Toast.LENGTH_LONG).show();
            }

            @Override
            public void changeBaseSettings(BaseConf baseConf) {
                sqliteInterface.modificarBase(baseConf);
                dialog.dismiss();
                Toast.makeText(context, "Parámetros guardados", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show(fm, "dialog");
    }

    private void onGpioDialog() {
        FragmentManager fm = getSupportFragmentManager();
        GpioDialog dialog = GpioDialog.newInstance(context,
                sqliteInterface.obtenerGPIO(),
                sqliteInterface.obtenerConexionGPIO());
        dialog.setCancelable(false);
        dialog.addGpioDialogAdapter(new GpioDialogListener() {
            @Override
            public void closeDialog() {
                dialog.dismiss();
            }

            @Override
            public void emptyparameters() {
                Toast.makeText(context, "Error: Parámetros vacíos", Toast.LENGTH_LONG).show();
            }

            @Override
            public void changeGpioSettings(GPIOConf gpioConf, ParametrosConexion parametrosConexion) {
                changeGpios(gpioConf, parametrosConexion);
                sqliteInterface.modificarGPIO(gpioConf, parametrosConexion);
                dialog.dismiss();
                Toast.makeText(context, "Parámetros guardados", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show(fm, "dialog");
    }

    public void changeGpios(GPIOConf gpioConf, ParametrosConexion parametrosConexion) {
    }

    private void onServerDialog() {
        FragmentManager fm = getSupportFragmentManager();
        ServerDialog dialog = ServerDialog.newInstance(context,
                sqliteInterface.obtenerServidor());
        dialog.setCancelable(false);
        dialog.addServerDialogAdapter(new ServerDialogListener() {
            @Override
            public void closeDialog() {
                dialog.dismiss();
            }

            @Override
            public void emptyparameters() {
                Toast.makeText(context, "Error: Parámetros vacíos", Toast.LENGTH_LONG).show();
            }

            @Override
            public void changeServerSettings(ServidorConf servidor) {
                sqliteInterface.modificarServidor(servidor);
                dialog.dismiss();
                Toast.makeText(context, "Parámetros guardados", Toast.LENGTH_LONG).show();
            }
        });
        dialog.show(fm, "dialog");
    }
}
