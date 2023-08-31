package com.example.ps_android_mayro_tablet_xspan.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.models.items.Contenedores;
import com.example.ps_android_mayro_tablet_xspan.views.clases.ViewHolder;

import java.util.List;
import java.util.Locale;

public class ContenedoresAdapter extends BaseAdapter {
    private final Activity activity;
    private final List<Contenedores> contenedoresList;

    public ContenedoresAdapter(Activity activity, List<Contenedores> contenedoresList) {
        this.activity = activity;
        this.contenedoresList = contenedoresList;
    }

    @Override
    public int getCount() {
        return contenedoresList.size();
    }

    @Override
    public Contenedores getItem(int i) {
        return contenedoresList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        ViewHolder viewHolder;
        if(rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_contenedores, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.padreLl = rowView.findViewById(R.id.padre);
            viewHolder.titulo = rowView.findViewById(R.id.titulo);
            viewHolder.contenedorTv = rowView.findViewById(R.id.cont_val);
            viewHolder.esperadosTv = rowView.findViewById(R.id.esp_val);
            viewHolder.leidosTv = rowView.findViewById(R.id.lei_val);
            viewHolder.resultadoFa = rowView.findViewById(R.id.result);
            viewHolder.views[0] = rowView.findViewById(R.id.linea1);
            viewHolder.views[1] = rowView.findViewById(R.id.linea2);
            viewHolder.views[2] = rowView.findViewById(R.id.linea3);
            rowView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        viewHolder.titulo.setText(activity.getResources().getString(R.string.num_contenedor));
        viewHolder.contenedorTv.setText(String.format(Locale.getDefault(), "Contenedor %d", contenedoresList.get(position).getFolio_contenedor()));
        viewHolder.esperadosTv.setText(String.format(Locale.getDefault(), "%d", contenedoresList.get(position).getEsperados()));
        viewHolder.leidosTv.setText(String.format(Locale.getDefault(), "%d", contenedoresList.get(position).getLeidos()));

        if(contenedoresList.get(position).getSobrantes()>0) {
            viewHolder.padreLl.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_errores));
            for(int i=0; i<viewHolder.views.length; i++) {
                viewHolder.views[i].setBackgroundColor(ContextCompat.getColor(activity, R.color.status_red));
            }
        } else {
            if(contenedoresList.get(position).getEsperados()>contenedoresList.get(position).getLeidos()) {
                viewHolder.padreLl.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_faltante));
                for(int i=0; i<viewHolder.views.length; i++) {
                    viewHolder.views[i].setBackgroundColor(ContextCompat.getColor(activity, R.color.status_yellow));
                }
            } else {
                viewHolder.padreLl.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_correcto));
                for(int i=0; i<viewHolder.views.length; i++) {
                    viewHolder.views[i].setBackgroundColor(ContextCompat.getColor(activity, R.color.status_green));
                }
            }
        }
        return rowView;
    }
}
