package com.example.ps_android_mayro_tablet_xspan.controller.server;

import java.util.ArrayList;
import java.util.Locale;

public class XmlElement {
    private final String type;
    private final String name;
    private final Object element;
    private final int level;
    private final int tipo;

    public XmlElement(String type, String name, Object element, int level) {
        this.type = type;
        this.name = name;
        this.element = element;
        this.level = level;
        String tmp = element.getClass().getName();
        if(tmp.endsWith("String")) this.tipo=1;
        else {
            if(tmp.endsWith("XmlElement")) this.tipo = 2;
            else {
                if(tmp.endsWith("ArrayList")) this.tipo=3;
                else this.tipo=-1;
            }
        }
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<level; i++) sb.append("\t");
        sb.append(String.format(Locale.getDefault(), "<%s:%s>", this.type, this.name));
        switch (this.tipo) {
            case 1: //Value
                sb.append((String)this.element);
                break;
            case 2: //XmlElement
                sb.append("\r\n");
                sb.append(((XmlElement)this.element).toString());
                for(int i=0; i<level; i++) sb.append("\t");
                break;
            case 3: //array
                sb.append("\r\n");
                //noinspection unchecked
                for(XmlElement el: (ArrayList<XmlElement>) element) {
                    sb.append(el.toString());
                }
                for(int i=0; i<level; i++) sb.append("\t");
                break;
        }
        sb.append(String.format(Locale.getDefault(), "</%s:%s>\r\n", this.type, this.name));
        return sb.toString();
    }
}
