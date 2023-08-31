package com.example.ps_android_mayro_tablet_xspan.controller.gpio;

import android.util.Log;

import com.example.ps_android_mayro_tablet_xspan.models.clases.ComMethod;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ParametrosConexion;
import com.example.ps_android_mayro_tablet_xspan.models.clases.ServerResponse;

import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.Locale;

public class GPIOUtils {
    public static ServerResponse connectNormal(ParametrosConexion parametrosConexion, ComMethod method, String path, String jsonString) {
        Log.d("SENSOR", "<---------------------connectNormal-------------------->");
        ServerResponse resp = new ServerResponse();
        byte[] lenBytes = new byte[1024];
        int len;
        try {
            InetAddress serverAddr = InetAddress.getByName(parametrosConexion.getUrl());
            Socket socket = new Socket(serverAddr, parametrosConexion.getPort());
            socket.setSoTimeout(5000);
            PrintStream output = new PrintStream(socket.getOutputStream());
            String message = String.format(Locale.getDefault(), "%s %s HTTP/1.1\r\n" +
                    "User-Agent: Astlix/1.0\r\n" +
                    "Accept: application/json\r\n" +
                    "Content-Type: application/json; charset=utf-8\r\n" +
                    "Connection: close\r\n" +
                    "Content-Lenght: %d\r\n\r\n%s", method.toString(), path, jsonString.length(), jsonString);

            Log.d("SENSOR", message);

            output.println(message);
            InputStream stream = socket.getInputStream();

            do {
                if (stream.read(lenBytes, 0, 1024) <= 0) {
                    break;
                }
                resp.addResponse(new String(lenBytes, StandardCharsets.UTF_8).trim());
                len = stream.available();
            } while (len > 0);

            socket.close();
        } catch (IOException ex) {
            resp.addResponse(ex.getMessage());
            ex.printStackTrace();
            Log.d("SENSOR", "IOException "+ex.getMessage());
        }
        Log.d("SENSOR", "</---------------------connectNormal-------------------->");
        return resp;
    }
}
