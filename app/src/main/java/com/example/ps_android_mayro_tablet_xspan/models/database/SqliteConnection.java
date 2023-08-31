package com.example.ps_android_mayro_tablet_xspan.models.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteConnection extends SQLiteOpenHelper {
    public SqliteConnection(Context context) {
        super(context, "expo.db", null, 3);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String cadena = "create table if not exists xspan (_id integer primary key autoincrement, " +
                "url1 text not null, " +
                "url2 text not null, " +
                "tagAgeInterval integer not null, " +
                "updateinterval integer not null, " +
                "entryReport integer not null, " +
                "updateReport integer not null, " +
                "exitReport integer not null, " +
                "maxTxPower integer not null, " +
                "txPower text not null, " +
                "directionMode text not null, " +
                "directionField text not null);";
        db.execSQL(cadena);
        cadena = "create table if not exists gpio (_id integer primary key autoincrement, " +
                "url text not null, " +
                "port integer not null, " +
                "luzRojaPort integer not null, " +
                "luzAmbarPort integer not null, " +
                "luzVerdePort integer not null, " +
                "sensorPort integer not null);";
        db.execSQL(cadena);
        cadena = "create table if not exists servidor (_id integer primary key autoincrement, " +
                "url text not null, " +
                "domain text not null, " +
                "user text not null, " +
                "password text not null, " +
                "soapaction text not null, " +
                "port integer not null, " +
                "servicio text not null);";
        db.execSQL(cadena);
        cadena = "create table if not exists base (_id integer primary key autoincrement, " +
                "url text not null, " +
                "user text not null, " +
                "password text not null, " +
                "name text not null);";
        db.execSQL(cadena);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        if(i!=i1) {
            String cadenaUpdate = "drop table if exists xspan;";
            db.execSQL(cadenaUpdate);
            cadenaUpdate = "drop table if exists gpio;";
            db.execSQL(cadenaUpdate);
            cadenaUpdate = "drop table if exists servidor;";
            db.execSQL(cadenaUpdate);
            cadenaUpdate = "drop table if exists base;";
            db.execSQL(cadenaUpdate);
            onCreate(db);
        }
    }
}
