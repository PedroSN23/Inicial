package com.example.ps_android_mayro_tablet_xspan.controller.xspan;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import androidx.annotation.NonNull;

import com.example.ps_android_mayro_tablet_xspan.models.clases.Devices;
import com.impinj.octane.ConnectionLostListener;
import com.impinj.octane.DirectionConfig;
import com.impinj.octane.DirectionReport;
import com.impinj.octane.DirectionReportListener;
import com.impinj.octane.ImpinjReader;
import com.impinj.octane.KeepaliveEvent;
import com.impinj.octane.KeepaliveListener;
import com.impinj.octane.OctaneSdkException;
import com.impinj.octane.ReaderMode;
import com.impinj.octane.ReportMode;
import com.impinj.octane.SearchMode;
import com.impinj.octane.Settings;
import com.impinj.octane.SpatialConfig;
import com.impinj.octane.SpatialMode;
import com.impinj.octane.TagData;

import java.lang.ref.WeakReference;
import java.util.concurrent.atomic.AtomicBoolean;

public class XspanHandler extends Handler implements DirectionReportListener, KeepaliveListener, ConnectionLostListener {
    private final ImpinjReader xspan;
    private WeakReference<Handler> mainHandlerWeakReference;
    private final AtomicBoolean reading = new AtomicBoolean(false);
    private final AtomicBoolean connetionLost = new AtomicBoolean(false);
    private final AtomicBoolean desconect = new AtomicBoolean(false);
    private final Devices devices;

    @SuppressWarnings("deprecation")
    public XspanHandler(Handler mainHandler, Devices devices) {
        xspan = new ImpinjReader();
        mainHandlerWeakReference = new WeakReference<>(mainHandler);
        this.devices = devices;
    }

    public void updateMainHandler(Handler mainHandler) {
        mainHandlerWeakReference = new WeakReference<>(mainHandler);
    }

    @Override
    public void handleMessage(@NonNull Message msg) {
        Message msgOut;
        Bundle bundle;
        try {
            XspanInstruction xspanInstruction = XspanInstruction.none;
            for (XspanInstruction ii : XspanInstruction.values()) {
                if (ii.getCode() == msg.arg1) {
                    xspanInstruction = ii;
                    break;
                }
            }
            switch (xspanInstruction) {
                case none:
                    msgOut = mainHandlerWeakReference.get().obtainMessage();
                    msgOut.arg1 = devices.getCode(); //
                    msgOut.arg2 = XspanRespuesta.error.getCode();
                    msgOut.obj = "No se recibió instrucción";
                    mainHandlerWeakReference.get().sendMessage(msgOut);
                    break;
                case conectar:
                    bundle = msg.getData();
                    if (!xspan.isConnected()) {
                        xspan.connect(bundle.getString("url"));
                        xspan.setDirectionReportListener(this);
                        xspan.setKeepaliveListener(this);
                        xspan.setConnectionLostListener(this);
                    }
                    msgOut = mainHandlerWeakReference.get().obtainMessage();
                    msgOut.arg1= devices.getCode(); //
                    msgOut.arg2= XspanRespuesta.connected.getCode();
                    mainHandlerWeakReference.get().sendMessage(msgOut);
                    break;
                case configurar:
                    bundle = msg.getData();
                    configureXspan(bundle.getParcelable("xspanConf"));
                    msgOut = mainHandlerWeakReference.get().obtainMessage();
                    msgOut.arg1= devices.getCode(); //
                    msgOut.arg2= XspanRespuesta.configured.getCode();
                    mainHandlerWeakReference.get().sendMessage(msgOut);
                    break;
                case start:
                    if(!reading.get()) {
                        reading.set(true);
                        xspan.start();
                    }
                    msgOut = mainHandlerWeakReference.get().obtainMessage();
                    msgOut.arg1= devices.getCode(); //
                    msgOut.arg2= XspanRespuesta.reading.getCode();
                    mainHandlerWeakReference.get().sendMessage(msgOut);
                    break;
                case stop:
                    if(reading.get()) {
                        reading.set(false);
                        xspan.stop();
                    }
                    msgOut = mainHandlerWeakReference.get().obtainMessage();
                    msgOut.arg1= devices.getCode(); //
                    msgOut.arg2= XspanRespuesta.stopreading.getCode();
                    mainHandlerWeakReference.get().sendMessage(msgOut);
                    break;
                case desconectar:
                    desconect.set(true);
                    if(xspan.isConnected()) {
                        if(reading.get()) xspan.stop();
                        reading.set(false);
                        xspan.disconnect();
                    }
                    msgOut = mainHandlerWeakReference.get().obtainMessage();
                    msgOut.arg1= devices.getCode(); //
                    msgOut.arg2= XspanRespuesta.disconnected.getCode();
                    mainHandlerWeakReference.get().sendMessage(msgOut);
                    getLooper().quitSafely();
                    break;
                case cambiarConexion:
                    bundle = msg.getData();
                    boolean changes = bundle.getBoolean("changes");
                    if(changes) {
                        if (xspan.isConnected()) {
                            if (reading.get()) xspan.stop();
                            xspan.disconnect();
                        }
                        xspan.connect(bundle.getString("url"));
                        xspan.setDirectionReportListener(this);
                        xspan.setKeepaliveListener(this);
                        xspan.setConnectionLostListener(this);
                    } else {
                        if(reading.get()) xspan.stop();
                    }
                    configureXspan(bundle.getParcelable("xspanConf"));
                    if(reading.get()) {
                        xspan.start();
                    }
                    msgOut = mainHandlerWeakReference.get().obtainMessage();
                    msgOut.arg1= devices.getCode(); //
                    msgOut.arg2= (reading.get())? XspanRespuesta.reading.getCode():XspanRespuesta.configured.getCode();
                    mainHandlerWeakReference.get().sendMessage(msgOut);
                    break;
            }
        } catch (OctaneSdkException ex) {
            msgOut = mainHandlerWeakReference.get().obtainMessage();
            msgOut.arg1= devices.getCode(); //
            msgOut.arg2= (desconect.get())? XspanRespuesta.disconnected.getCode():XspanRespuesta.error.getCode();
            bundle = new Bundle();
            bundle.putString("errorMsg", ex.getMessage());
            msgOut.setData(bundle);
            mainHandlerWeakReference.get().sendMessage(msgOut);
        }
        super.handleMessage(msg);
    }

    private void configureXspan(XspanConf xspanConf) throws OctaneSdkException {
        //CONFIGURE
        Settings settings = xspan.queryDefaultSettings();
        SpatialConfig sc = settings.getSpatialConfig();
        sc.setMode(SpatialMode.Direction);
        sc.getDirection().removeAllSectors();
        sc.getDirection().enableSector((short) 2);
        sc.getDirection().enableSector((short) 3);
        DirectionConfig dc = sc.getDirection();
        dc.setMode(xspanConf.getDirectionMode());
        dc.setFieldOfView(xspanConf.getDirectionFieldOfView());
        dc.setTagAgeIntervalSeconds(xspanConf.getTagAgeIntervalSeconds());
        dc.setUpdateIntervalSeconds(xspanConf.getUpdateIntervalSeconds());

        dc.setEntryReportEnabled(xspanConf.isEntryReportEnabled());
        dc.setUpdateReportEnabled(xspanConf.isUpdateReportEnabled());
        dc.setExitReportEnabled(xspanConf.isExitReporrtEnabled());

        dc.setIsMaxTxPower(xspanConf.isSetMaxTxPower());
        dc.setTxPowerinDbm(xspanConf.getTxPowerinDbm());
        sc.setDirection(dc);

        settings.setSearchMode(SearchMode.DualTarget);
        //noinspection deprecation
        settings.setReaderMode(ReaderMode.AutoSetDenseReaderDeepScan);

        //settings.setRfMode(1002);
        settings.getReport().setIncludeAntennaPortNumber(false);
        settings.getReport().setIncludePeakRssi(false);
        settings.getReport().setMode(ReportMode.Individual);

        settings.getKeepalives().setEnableLinkMonitorMode(true);
        settings.getKeepalives().setEnabled(true);
        settings.getKeepalives().setLinkDownThreshold(8);
        settings.getKeepalives().setPeriodInMs(8000);

        xspan.applySettings(settings);
    }

    @Override
    public void onConnectionLost(ImpinjReader impinjReader) {
        Log.d("KEEPALIVE", "onconnectionlost");
        if(!connetionLost.get()) {
            connetionLost.set(true);
            Message msgOut = mainHandlerWeakReference.get().obtainMessage();
            msgOut.arg1 = devices.getCode();
            msgOut.arg2 = (reading.get())? XspanRespuesta.connectionLostReading.getCode(): XspanRespuesta.connectionLost.getCode();
            Bundle bundle = new Bundle();
            bundle.putString("errorMsg", "Error de desconexión de xspan");
            msgOut.setData(bundle);
            mainHandlerWeakReference.get().sendMessage(msgOut);
        }
    }

    @Override
    public void onDirectionReported(ImpinjReader impinjReader, DirectionReport directionReport) {
        TagData tagData = directionReport.getEpc();
        String epc = tagData.toHexString().replaceAll("\\s", "").toUpperCase();
        if(epc.startsWith("75")&&epc.length()==32) {
            Message msgOut = mainHandlerWeakReference.get().obtainMessage();
            msgOut.arg1 = devices.getCode();
            msgOut.arg2 = XspanRespuesta.tags.getCode();
            Bundle bundle = new Bundle();
            bundle.putString("epc", epc);
            //bundle.putString("reportType", directionReport.getReportType().toString());
            bundle.putShort("lastReadSector", directionReport.getLastReadSector());
            msgOut.setData(bundle);
            mainHandlerWeakReference.get().sendMessage(msgOut);
        }
    }

    @Override
    public void onKeepalive(ImpinjReader impinjReader, KeepaliveEvent keepaliveEvent) {
        Log.d("KEEPALIVE", "keepalive");
        if(connetionLost.get()) {
            connetionLost.set(false);
            Message msgOut = mainHandlerWeakReference.get().obtainMessage();
            msgOut.arg1 = devices.getCode();
            msgOut.arg2 = (reading.get())? XspanRespuesta.keepAliveReading.getCode() : XspanRespuesta.keepAlive.getCode();
            mainHandlerWeakReference.get().sendMessage(msgOut);
        }
    }
}
