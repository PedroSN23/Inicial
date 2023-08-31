package com.example.ps_android_mayro_tablet_xspan.models.clases;

import com.example.ps_android_mayro_tablet_xspan.R;

public enum TipoLuz {
    roja(3, R.drawable.ic_luz_roja),
    verde(1, R.drawable.ic_luz_verde),
    ambar(2, R.drawable.ic_luz_ambar),
    sensor(0, R.drawable.sensor_on);

    private final int code;
    private final int drawable;

    TipoLuz(int code, int drawable) {
        this.code = code;
        this.drawable = drawable;
    }

    public int getDrawable() {
        return drawable;
    }

    public int getCode() {
        return code;
    }
}
