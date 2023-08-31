package com.example.ps_android_mayro_tablet_xspan.controller.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.example.ps_android_mayro_tablet_xspan.models.clases.BaseConf;
import com.example.ps_android_mayro_tablet_xspan.models.database.MysqlConection;
import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;
import com.example.ps_android_mayro_tablet_xspan.views.gui.MainActivity;

import java.sql.SQLException;
import java.util.List;

@SuppressWarnings("deprecation")
public class AsyncObtenerEmbarque extends AsyncTask<String, Boolean, Integer> {
    private List<Contenedores> contenedores;
    private BaseConf baseConf;
    private String msg;
    @SuppressLint("StaticFieldLeak")
    private final Activity activity;

    public AsyncObtenerEmbarque(Activity activity, BaseConf baseConf) {
        this.activity = activity;
        this.baseConf = baseConf;
    }


    @Override
    protected Integer doInBackground(String... strings) {
        int ret = -1;
        try {
            MysqlConection mysqlConection = new MysqlConection(baseConf.getUrl(),
                    baseConf.getName(),
                    baseConf.getUser(),
                    baseConf.getPassword());
                ret = mysqlConection.getIdEmbarque(strings[0]);
                if(ret>0) {
                    contenedores = mysqlConection.obtenerPLRecibo(ret);
                    if(contenedores.size()==0) {
                        ret=-1;
                        msg = "Error, embarque vacío";
                    }
                } else {
                    msg = "Error al obtener embarque";
                }
        } catch (SQLException | ClassNotFoundException e) {
            msg = e.getMessage();
        }
        return ret;
    }

    @Override
    protected void onPostExecute(Integer integer) {
        if(integer>0) {
            ((MainActivity) activity).asyncResultadoBuscar(integer, contenedores);
        } else {
            ((MainActivity) activity).mostrarMensajeDeErrorDialog(msg);
        }
    }
}
