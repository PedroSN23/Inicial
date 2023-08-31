package com.example.ps_android_mayro_tablet_xspan.controller.server;

import java.util.Locale;

public class SoapConstruct {
    private final SoapHeader header;
    private final SoapBody body;
    private final String servicio;
    private final String action;
    private final String host;
    private final int port;
    private int negoState;
    private String ntlm_init;
    private String ntml_fin;
    private final boolean getPost;

    public SoapConstruct(XmlElement element, String servicio, String action, String host, int port, boolean getPost) {
        this.header=new SoapHeader();
        this.body=new SoapBody(element);
        this.servicio = servicio;
        this.action = action;
        this.host = host;
        this.port = port;
        this.negoState=0;
        this.getPost=getPost;
    }

    public void setNtlm_init(String ntml_init) {
        this.ntlm_init = ntml_init;
    }

    public void setNtml_fin(String ntml_fin) {
        this.ntml_fin = ntml_fin;
    }

    public void nextNegoState() {
        negoState++;
    }

    @Override
    public String toString() {
        String resp;
        switch (negoState) {
            case 0:
                if(getPost){
                    resp = firstAproach();
                } else {
                    resp = firstAproachGet();
                }
                break;
            case 1:
                if(getPost) {
                    resp = secondAproach();
                } else {
                    resp = secondAproachGet();
                }
                break;
            case 2:
                if(getPost) {
                    resp = getSoapString();
                } else {
                    resp = thirdAproachGet();
                }
                break;
            default:
                resp="";
                break;
        }
        return resp;
    }

    private String firstAproachGet() {
        return String.format(Locale.getDefault(), "GET %sL HTTP/1.1\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port), this.servicio) +
                "User-Agent: IntuiLog/1.0\r\n" +
                "Accept: */*\r\n" +
                "Cache-Control: no-cache\r\n" +
                String.format(Locale.getDefault(), "Host: %s%s\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port)) +
                "accept-encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n\r\n";
    }

    private String secondAproachGet() {
        return String.format(Locale.getDefault(), "GET http://%s%s/%s?WSDL HTTP/1.1\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port), this.servicio) +
                "User-Agent: IntuiLog/1.0\r\n" +
                "Accept: */*\r\n" +
                "Cache-Control: no-cache\r\n" +
                String.format(Locale.getDefault(), "Host: %s%s\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port)) +
                "accept-encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                String.format(Locale.getDefault(), "Authorization: Negotiate %s\r\n\r\n", this.ntlm_init);
    }

    private String thirdAproachGet() {
        return String.format(Locale.getDefault(), "GET http://%s%s/%s?WSDL HTTP/1.1\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port), this.servicio) +
                "User-Agent: IntuiLog/1.0\r\n" +
                "Accept: */*\r\n" +
                "Cache-Control: no-cache\r\n" +
                String.format(Locale.getDefault(), "Host: %s%s\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port)) +
                "accept-encoding: gzip, deflate\r\n" +
                "Connection: keep-alive\r\n" +
                String.format(Locale.getDefault(), "Authorization: Negotiate %s\r\n\r\n", this.ntml_fin);
    }

    private String firstAproach() {
        return String.format(Locale.getDefault(), "POST http://%s%s/%s HTTP/1.1\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port), this.servicio) +
                "Content-Type: text/xml\r\n" +
                String.format(Locale.getDefault(), "SOAPAction: %s\r\n", this.action) +
                "User-Agent: IntuiLog/1.0\r\n" +
                "Accept: */*\r\n" +
                "Cache-Control: no-cache\r\n" +
                String.format(Locale.getDefault(), "Host: %s%s\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port)) +
                "accept-encoding: gzip, deflate\r\n" +
                "content-length: 0\r\n" +
                "Connection: keep-alive\r\n\r\n";
    }

    private String secondAproach() {

        return String.format(Locale.getDefault(), "POST http://%s%s/%s HTTP/1.1\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port), this.servicio) +
                "Content-Type: text/xml\r\n" +
                String.format(Locale.getDefault(), "SOAPAction: %s\r\n", this.action) +
                "User-Agent: IntuiLog/1.0\r\n" +
                "Accept: */*\r\n" +
                "Cache-Control: no-cache\r\n" +
                String.format(Locale.getDefault(), "Host: %s%s\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port)) +
                "accept-encoding: gzip, deflate\r\n" +
                "content-length: 0\r\n" +
                "Connection: keep-alive\r\n" +
                String.format(Locale.getDefault(), "Authorization: Negotiate %s\r\n\r\n", this.ntlm_init);
    }

    private String getSoapString() {
        StringBuilder sbCuerpo = new StringBuilder();
        //sbCuerpo.append("<?xml version=\"1.0\" encoding=\"utf-8\"?>\r\n");
        sbCuerpo.append("<soapenv:Envelope xmlns:soapenv=\"http://schemas.xmlsoap.org/soap/envelope/\" xmlns:dat=\"http://schemas.microsoft.com/dynamics/2010/01/datacontracts\" xmlns:arr=\"http://schemas.microsoft.com/2003/10/Serialization/Arrays\" xmlns:tem=\"http://tempuri.org\" xmlns:dyn=\"http://schemas.datacontract.org/2004/07/Dynamics.Ax.Application\">\r\n");
        sbCuerpo.append(header.toString());
        sbCuerpo.append(body.toString());
        sbCuerpo.append("</soapenv:Envelope>");

        return String.format(Locale.getDefault(), "POST http://%s%s/%s HTTP/1.1\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port), this.servicio) +
                "Content-Type: text/xml; charset=utf-8\r\n" +
                String.format(Locale.getDefault(), "SOAPAction: %s\r\n", this.action) +
                "User-Agent: IntuiLog/1.0\r\n" +
                "Accept: */*\r\n" +
                "Cache-Control: no-cache\r\n" +
                String.format(Locale.getDefault(), "Host: %s%s\r\n", this.host, (this.port == 80) ? "" : String.format(Locale.getDefault(), ":%d", this.port)) +
                "accept-encoding: gzip, deflate\r\n" +
                String.format(Locale.getDefault(), "content-length: %d\r\n", sbCuerpo.toString().length()) +
                "Connection: keep-alive\r\n" +
                String.format(Locale.getDefault(), "Authorization: Negotiate %s\r\n", this.ntml_fin) +
                "\r\n" +
                sbCuerpo.toString();
    }
}
