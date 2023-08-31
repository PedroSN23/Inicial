package com.example.ps_android_mayro_tablet_xspan.models.clases;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ServerResponse {
    private int code;
    private final StringBuilder response;
    private final Pattern p = Pattern.compile("^HTTP/1.1 (\\d+) .+$");
    private final Pattern cont = Pattern.compile("Content-Length: (\\d+)");

    private String content="";

    public ServerResponse() {
        this.code = -1;
        this.response = new StringBuilder();
    }

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public void addResponse(String txt) {
        this.response.append(txt);
    }

    public String getResponse() {
        return response.toString();
    }

    public JSONObject getResponseJson() throws JSONException {
        return new JSONObject(content);
    }

    public String getResponseString() {
        return response.toString();
    }

    @SuppressWarnings("ConstantConditions")
    public void checkResponse() {
        String[] lines = response.toString().split("\\r?\\n");
        for(String l: lines) {
            Matcher m = p.matcher(l);
            if(m.matches()) {
                code = Integer.parseInt(m.group(1));
            }
            m = cont.matcher(l);
            if(m.matches()) {
                content = response.substring(response.length()-Integer.parseInt(m.group(1))).replaceAll("\\R", "");
            }
        }
    }
}
