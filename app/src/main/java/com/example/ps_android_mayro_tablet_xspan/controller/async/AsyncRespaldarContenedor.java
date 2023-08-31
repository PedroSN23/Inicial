package com.example.ps_android_mayro_tablet_xspan.controller.async;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.AsyncTask;

import com.example.ps_android_mayro_tablet_xspan.controller.server.NTLMAuthenticator;
import com.example.ps_android_mayro_tablet_xspan.controller.server.Respuesta;
import com.example.ps_android_mayro_tablet_xspan.controller.server.SoapConstruct;
import com.example.ps_android_mayro_tablet_xspan.controller.server.SoapRequestEnvios;
import com.example.ps_android_mayro_tablet_xspan.models.UtilsSkuRiver;
import com.example.ps_android_mayro_tablet_xspan.models.clases.BaseConf;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ServidorConf;
import com.example.ps_android_mayro_tablet_xspan.models.database.MysqlConection;
import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;
import com.example.ps_android_mayro_tablet_xspan.models.items.Skus;
import com.example.ps_android_mayro_tablet_xspan.views.gui.MainActivity;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.sql.SQLException;
import java.util.Locale;

@SuppressWarnings("deprecation")
public class AsyncRespaldarContenedor extends AsyncTask<Integer, Integer, Boolean> {
    @SuppressLint("StaticFieldLeak")
    private final Activity activity;
    private final BaseConf baseConf;
    private final ServidorConf servidorConf;
    private final Contenedores contenedores;
    private String msg;

    public AsyncRespaldarContenedor(Activity activity, BaseConf baseConf, ServidorConf servidorConf, Contenedores contenedores) {
        this.activity = activity;
        this.baseConf = baseConf;
        this.servidorConf = servidorConf;
        this.contenedores = contenedores;
    }

    @Override
    protected Boolean doInBackground(Integer... integers) {
        boolean ret = false;
        try {
            SoapRequestEnvios re1 = new SoapRequestEnvios();
            for(Skus s: contenedores.getSkusList()) {
                re1.agregarContenedor(s.getSku(),
                        UtilsSkuRiver.obtener_oc(s.getEpcsList().get(0).getEpc()),
                        String.format(Locale.getDefault(), "%s", s.getEsperados()));
            }

            SoapConstruct soapConstruct = new SoapConstruct(re1.returnElement(),
                    servidorConf.getServicio(),
                    servidorConf.getSoapaction(), servidorConf.getUrl(),
                    servidorConf.getPort(),
                    true);
            NTLMAuthenticator ntlmAuthenticator = new NTLMAuthenticator(servidorConf.getUser(),
                    servidorConf.getPassword(), "", servidorConf.getDomain());


            Respuesta resp = ejecutarServidor(soapConstruct, ntlmAuthenticator);
            if(resp.checkAnswer()) {
                MysqlConection mysqlConection = new MysqlConection(baseConf.getUrl(),
                        baseConf.getName(),
                        baseConf.getUser(),
                        baseConf.getPassword());
                mysqlConection.modificarEstadoPallet(contenedores.getFolio_contenedor());
                mysqlConection.modificarEtadoEmbarque(integers[0], contenedores.getFolio_contenedor());
                ret = true;
            } else {
                msg = resp.getMsg();
            }
        } catch (SQLException | ClassNotFoundException e) {
            msg = e.getMessage();
        }
        return ret;
    }

    private Respuesta ejecutarServidor(SoapConstruct soapConstruct, NTLMAuthenticator ntlmAuthenticator) {
        Respuesta respuesta=null;
        byte[] lenBytes = new byte[1024];
        int len;

        try {
            InetAddress serverAddr = InetAddress.getByName(servidorConf.getUrl());
            Socket socket = new Socket(serverAddr, servidorConf.getPort());
            PrintStream output = new PrintStream(socket.getOutputStream());
            output.println(servidorConf.getSoapaction());
            InputStream stream = socket.getInputStream();

            respuesta = new Respuesta(soapConstruct);
            do {
                if (stream.read(lenBytes, 0, 1024) <= 0) {
                    break;
                }
                respuesta.agregarMsg(new String(lenBytes, StandardCharsets.UTF_8).trim());
                len = stream.available();
            } while (len > 0);

            socket.close();

            if(respuesta.validateNtlmMessage()) {
                soapConstruct.nextNegoState();
                soapConstruct.setNtlm_init(ntlmAuthenticator.get1Msg());

                socket = new Socket(serverAddr, servidorConf.getPort());
                output = new PrintStream(socket.getOutputStream());
                output.println(soapConstruct);
                stream = socket.getInputStream();
                respuesta = new Respuesta(soapConstruct);
                do {
                    if (stream.read(lenBytes, 0, 1024) <= 0) {
                        break;
                    }
                    respuesta.agregarMsg(new String(lenBytes, StandardCharsets.UTF_8).trim());
                    len = stream.available();
                } while (len > 0);
                String ntlm2 = respuesta.obtainNtlmMsg2();
                if (!ntlm2.isEmpty()) {
                    soapConstruct.nextNegoState();
                    soapConstruct.setNtml_fin(ntlmAuthenticator.get3Msg(ntlm2));
                    output.println(soapConstruct);
                    stream = socket.getInputStream();
                    respuesta = new Respuesta(soapConstruct);
                    do {
                        if (stream.read(lenBytes, 0, 1024) <= 0) {
                            break;
                        }
                        respuesta.agregarMsg(new String(lenBytes, StandardCharsets.UTF_8).trim());
                        len = stream.available();
                    } while (len > 0);
                }
                socket.close();
            }
        } catch (IOException ex) {
            msg=ex.getLocalizedMessage();
            ex.printStackTrace();
        }
        return respuesta;
    }

    @Override
    protected void onPostExecute(Boolean b) {
        if(b) {
            ((MainActivity)activity).mostrarMensajeExito(contenedores.getFolio_contenedor());
        } else {
            ((MainActivity)activity).mostrarMensajeDeErrorDialog(msg);
        }
    }
}
