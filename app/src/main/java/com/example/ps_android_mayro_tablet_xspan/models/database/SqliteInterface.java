package com.example.ps_android_mayro_tablet_xspan.models.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.util.Log;

import com.example.ps_android_mayro_tablet_xspan.controller.gpio.GPIOConf;
import com.example.ps_android_mayro_tablet_xspan.controller.xspan.XspanConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.BaseConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.Devices;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ServidorConf;
import com.impinj.octane.DirectionFieldOfView;
import com.impinj.octane.DirectionMode;

import java.util.Locale;

public class SqliteInterface {
    SqliteConnection con;
    SQLiteDatabase db;

    public SqliteInterface(Context context) {
        con = new SqliteConnection(context);
        inicializaDB();
    }

    public void open() throws SQLiteException {
        db = con.getWritableDatabase();
    }

    public void close() throws SQLiteException {
        con.close();
    }

    public void inicializaDB() {
        int numRegistros;

        ContentValues content;
        open();

        numRegistros = traeRegistro("xspan");
        if(numRegistros<1) {
            content = new ContentValues();
            content.put("url1", "192.168.100.8");
            content.put("url2", "192.168.100.9");
            content.put("tagAgeInterval", 1);
            content.put("updateinterval", 1);
            content.put("entryReport", 1);
            content.put("updateReport", 1);
            content.put("exitReport", 1);
            content.put("maxTxPower", 0);
            content.put("txPower", "22.0");
            content.put("directionMode", "HighPerformance");
            content.put("directionField", "NARROW");
            db.insert("xspan", null, content);
        }

        numRegistros = traeRegistro("gpio");
        if(numRegistros<1) {
            content = new ContentValues();
            content.put("url", "192.168.100.36");
            content.put("port", 8090);
            content.put("luzRojaPort", 2);
            content.put("luzAmbarPort", 1);
            content.put("luzVerdePort", 0);
            content.put("sensorPort", 0);
            db.insert("gpio", null, content);
        }

        numRegistros = traeRegistro("servidor");
        if(numRegistros<1) {
            content = new ContentValues();
            content.put("url", "192.168.1.230");
            content.put("domain", "");
            content.put("user", "");
            content.put("password", "");
            content.put("soapaction", "");
            content.put("port", 80);
            content.put("servicio", "");
            db.insert("servidor", null, content);
        }

        numRegistros = traeRegistro("base");
        if(numRegistros<1) {
            content = new ContentValues();
            content.put("url", "192.168.1.230");
            content.put("user", "root");
            content.put("password", "impinj");
            content.put("name", "impresion_riverline");
            db.insert("base", null, content);
        }
    }

    public int traeRegistro(String tabla) {
        int ret;
        String consulta = "select _id from "+tabla+";";
        try {
            Cursor c = db.rawQuery(consulta, null);
            ret = c.getCount();
            c.close();
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
            ret = -1;
        }
        return ret;
    }

    /*********************CONFIGURACION XSPAN*************************/
    public ParametrosConexion[] obtenerConexionXspan () {
        open();
        ParametrosConexion[] parametrosConexion=new ParametrosConexion[2];

        String consulta = "select url1, url2 from xspan where _id = 1;";
        try {
            Cursor c = db.rawQuery(consulta, null);
            if(c.moveToFirst()) {
                if(c.getCount() != 0) {
                    if(!c.isAfterLast()) {
                        parametrosConexion[0] = new ParametrosConexion(c.getString(0), 1, Devices.xspan1);
                        parametrosConexion[1] = new ParametrosConexion(c.getString(1), 1, Devices.xspan2);
                    }
                }
            }
            c.close();
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
        return parametrosConexion;
    }

    public void modificarXspan(XspanConf conf, ParametrosConexion[] parametrosConexion) {
        ContentValues content;
        open();
        try {
            content = new ContentValues();
            content.put("url1", parametrosConexion[0].getUrl());
            content.put("url2", parametrosConexion[1].getUrl());
            content.put("tagAgeInterval", conf.getTagAgeIntervalSeconds());
            content.put("updateinterval", conf.getUpdateIntervalSeconds());
            content.put("entryReport", conf.isEntryReportEnabled()? 1:0);
            content.put("updateReport", conf.isUpdateReportEnabled()? 1:0);
            content.put("exitReport", conf.isExitReporrtEnabled()? 1:0);
            content.put("maxTxPower", conf.isSetMaxTxPower()? 1:0);
            content.put("txPower", String.format(Locale.getDefault(), "%.02f", conf.getTxPowerinDbm()));
            content.put("directionMode", conf.getDirectionMode().toString());
            content.put("directionField", conf.getDirectionFieldOfView().toString());
            db.update("xspan", content, "_id=1", null);
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
    }

    public XspanConf obtenerXspan() {
        open();
        XspanConf xspanConf=null;

        String consulta = "select tagAgeInterval, updateinterval, entryReport, updateReport, exitReport, maxTxPower, txPower, directionMode, directionField from xspan WHERE _id = 1;";
        try {
            Cursor c = db.rawQuery(consulta, null);
            if(c.moveToFirst()) {
                if(c.getCount() != 0) {
                    if(!c.isAfterLast()) {
                        xspanConf = new XspanConf();
                        xspanConf.setTagAgeIntervalSeconds(c.getShort(0));
                        xspanConf.setUpdateIntervalSeconds(c.getShort(1));
                        xspanConf.setEntryReportEnabled(c.getInt(2)==1);
                        xspanConf.setUpdateReportEnabled(c.getInt(3)==1);
                        xspanConf.setExitReporrtEnabled(c.getInt(4)==1);
                        xspanConf.setSetMaxTxPower(c.getInt(5)==1);
                        xspanConf.setTxPowerinDbm(Double.parseDouble(c.getString(6)));
                        xspanConf.setDirectionMode(DirectionMode.valueOf(c.getString(7)));
                        xspanConf.setDirectionFieldOfView(DirectionFieldOfView.valueOf(c.getString(8)));
                    }
                }
            }
            c.close();
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
        return xspanConf;
    }

    /*********************CONFIGURACION GPIO*************************/
    public ParametrosConexion obtenerConexionGPIO () {
        open();
        ParametrosConexion parametrosConexion=null;

        String consulta = "select url, port from gpio where _id = 1;";
        try {
            Cursor c = db.rawQuery(consulta, null);
            if(c.moveToFirst()) {
                if(c.getCount() != 0) {
                    if(!c.isAfterLast()) {
                        parametrosConexion = new ParametrosConexion(c.getString(0), c.getInt(1), Devices.gpio);
                    }
                }
            }
            c.close();
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
        return parametrosConexion;
    }

    public void modificarGPIO(GPIOConf conf, ParametrosConexion parametrosConexion) {
        ContentValues content;
        open();
        try {
            content = new ContentValues();
            content.put("url", parametrosConexion.getUrl());
            content.put("port", parametrosConexion.getPort());
            content.put("luzRojaPort", conf.getLuzRoja().getPort_id());
            content.put("luzAmbarPort", conf.getLuzAmbar().getPort_id());
            content.put("luzVerdePort", conf.getLuzVerde().getPort_id());
            content.put("sensorPort", conf.getSensor().getPort_id());
            db.update("gpio", content, "_id=1", null);
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
    }

    public GPIOConf obtenerGPIO() {
        open();
        GPIOConf gpioConf=null;

        String consulta = "select luzRojaPort, luzAmbarPort, luzVerdePort, sensorPort from gpio WHERE _id = 1;";
        try {
            Cursor c = db.rawQuery(consulta, null);
            if(c.moveToFirst()) {
                if(c.getCount() != 0) {
                    if(!c.isAfterLast()) {
                        gpioConf = new GPIOConf();
                        gpioConf.setLuzRojaPort(c.getInt(0));
                        gpioConf.setLuzAmbarPort(c.getInt(1));
                        gpioConf.setLuzVerdePort(c.getInt(2));
                        gpioConf.setSensorPort(c.getInt(3));
                    }
                }
            }
            c.close();
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
        return gpioConf;
    }

    /*********************CONFIGURACION SERVIDOR*************************/
    public ServidorConf obtenerServidor () {
        open();
        ServidorConf servidorConf=null;

        String consulta = "select url, domain, user, password, soapaction, port, servicio from servidor where _id = 1;";
        try {
            Cursor c = db.rawQuery(consulta, null);
            if(c.moveToFirst()) {
                if(c.getCount() != 0) {
                    if(!c.isAfterLast()) {
                        servidorConf = new ServidorConf(c.getString(0), c.getString(1), c.getString(2), c.getString(3), c.getString(4), c.getInt(5), c.getString(6));
                    }
                }
            }
            c.close();
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
        return servidorConf;
    }

    public void modificarServidor(ServidorConf conf) {
        ContentValues content;
        open();
        try {
            content = new ContentValues();
            content.put("url", conf.getUrl());
            content.put("domain", conf.getDomain());
            content.put("user", conf.getUser());
            content.put("password", conf.getPassword());
            content.put("soapaction", conf.getSoapaction());
            content.put("port", conf.getPort());
            db.update("servidor", content, "_id=1", null);
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
    }

    /*********************CONFIGURACION BASE*************************/
    public BaseConf obtenerBase () {
        open();
        BaseConf baseConf=null;

        String consulta = "select url, user, password, name from base where _id = 1;";
        try {
            Cursor c = db.rawQuery(consulta, null);
            if(c.moveToFirst()) {
                if(c.getCount() != 0) {
                    if(!c.isAfterLast()) {
                        baseConf = new BaseConf(c.getString(0), c.getString(1), c.getString(2), c.getString(3));
                    }
                }
            }
            c.close();
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
        return baseConf;
    }

    public void modificarBase(BaseConf conf) {
        ContentValues content;
        open();
        try {
            content = new ContentValues();
            content.put("url", conf.getUrl());
            content.put("user", conf.getUser());
            content.put("password", conf.getPassword());
            content.put("name", conf.getName());
            db.update("base", content, "_id=1", null);
        } catch (SQLiteException ex) {
            Log.e("SQLite", ex.getMessage());
        }
    }
}
