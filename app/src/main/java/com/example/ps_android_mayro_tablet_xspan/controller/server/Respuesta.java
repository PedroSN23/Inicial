package com.example.ps_android_mayro_tablet_xspan.controller.server;

import java.util.ArrayList;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Respuesta {
    private String httpMsg;
    private String httpCode;
    private final ArrayList<String> headers;
    private final ArrayList<String> values;
    private final StringBuilder body;
    private boolean headerRdy;
    private boolean headerFirst;
    private final Pattern p;
    private final Pattern p2;
    public SoapConstruct soapConstruct;
    private String msg;


    public Respuesta(SoapConstruct soapConstruct) {
        this.soapConstruct = soapConstruct;
        this.body = new StringBuilder();
        headerRdy = false;
        headerFirst = false;
        values = new ArrayList<>();
        headers = new ArrayList<>();
        httpMsg="";
        p = Pattern.compile("^HTTP/1\\.1 ([0-9]{3}\\.?[0-9]{0,3}) (.+)$");
        p2 = Pattern.compile("^(?:[a-z]:)?([a-zA-Z]+)(?:[^>]+)>(.+)$");
    }

    /*public String getMessage() {
        return httpMsg;
    }*/

    public void agregarMsg(String msg) {
        if(!headerRdy) {
            String[] headBody = msg.split("\\r\\n\\r\\n"); //separar header de body

            String[] headRows = headBody[0].split("\\r\\n");

            for (String rowH : headRows) {
                if(!headerFirst) {
                    Matcher m = p.matcher(headRows[0]);
                    if(m.matches()) {
                        headerFirst=true;
                        httpCode = m.group(1);
                        httpMsg = m.group(2);
                    }
                } else {
                    String[] headCols = rowH.split(": ");
                    headers.add(headCols[0]);
                    StringBuilder sb = new StringBuilder();
                    if (headCols.length > 1) {
                        for (int i = 1; i < headCols.length; i++) {
                            if (i > 1) sb.append(": ");
                            sb.append(headCols[i]);
                        }
                        values.add(sb.toString());
                    } else {
                        values.add("");
                    }
                }
            }
            if (headBody.length > 1) {
                headerRdy = true;
                for (int i = 1; i < headBody.length; i++) {
                    if (i > 1) body.append("\r\n\r\n");
                    body.append(headBody[i]);
                }
            }
        } else {
            body.append(msg);
        }

        //return expBody-body.toString().length();
    }

    public String getBody() {
        return this.body.toString();
    }

    public String getMsg() {
        return msg;
    }

    @SuppressWarnings("ConstantConditions")
    public boolean checkAnswer() {
        boolean ret = false;
        String[] array = this.body.toString().split("<");
        for(String s: array) {
            Matcher m = p2.matcher(s);
            if(m.matches()) {
                if(m.groupCount()>2) {
                    if (m.group(1).compareTo("Statu") == 0) {
                        try {
                            int val = Integer.parseInt(m.group(2));
                            if (val == 0) {
                                msg = "Mensaje errone del servidor";
                                return false;
                            } else {
                                ret = true;
                            }
                        } catch (NumberFormatException e) {
                            msg = e.getMessage();
                            return false;
                        }
                    } else {
                        if (m.group(1).compareTo("Messag") == 0) {
                            msg = m.group(2);
                            return false;
                        } else {
                            if (m.group(1).compareTo("faultstring") == 0) {
                                msg = m.group(2);
                                return false;
                            }
                        }
                    }
                }
            }
        }
        return ret;
    }


    /*public String header(String header) {
        for(int i=0; i<headers.size(); i++) {
            if(headers.get(i).compareTo(header)==0) {
                return values.get(i);
            }
        }
        return "empty";
    }*/

    /*public ArrayList<String> headers(String header) {
        ArrayList<String> headResp = new ArrayList<>();
        for(int i=0; i<headers.size(); i++) {
            if(headers.get(i).compareTo(header)==0) {
                headResp.add(values.get(i));
            }
        }
        return headResp;
    }*/

    public boolean validateNtlmMessage() {
        int resp=0;
        for(int i=0; i<headers.size(); i++) {
            if(headers.get(i).compareTo("WWW-Authenticate")==0) {
                if(values.get(i).compareToIgnoreCase("Negotiate")==0) {
                    resp|=1;
                } else {
                    if (values.get(i).compareToIgnoreCase("NTLM") == 0) {
                        resp |= 2;
                    }
                }
                if(resp==3) return true;
            }
        }
        return false;
    }

    public String obtainNtlmMsg2() {
        Pattern p = Pattern.compile("Negotiate (.*)");
        for(int i=0; i<headers.size(); i++) {
            if(headers.get(i).compareTo("WWW-Authenticate")==0) {
                Matcher m = p.matcher(values.get(i));
                if(m.matches()) {
                    return m.group(1);
                }
            }
        }
        return "";
    }

    public double getHttpCode() {
        double ret;
        try {
            ret = Double.parseDouble(this.httpCode);
        } catch (NumberFormatException e) {
            ret = -666.0;
        }
        return ret;
    }

    public String firstHeader() {
        return String.format(Locale.getDefault(), "HTTP/1.1 %s %s", this.httpCode, this.httpMsg);
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(String.format(Locale.getDefault(), "HTTP/1.1 %s %s\r\n", this.httpCode, this.httpMsg));
        for(int i=0; i<this.headers.size(); i++) {
            if(this.values.get(i).isEmpty()) {
                sb.append(String.format(Locale.getDefault(), "%s\r\n", this.headers.get(i)));
            } else {
                sb.append(String.format(Locale.getDefault(), "%s: %s\r\n", this.headers.get(i), this.values.get(i)));
            }
        }
        sb.append("\r\n");
        if(!this.body.toString().isEmpty()) {
            sb.append(this.body);
        }
        return sb.toString();
    }
}
