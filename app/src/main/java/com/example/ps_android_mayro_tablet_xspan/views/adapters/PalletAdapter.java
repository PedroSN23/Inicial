package com.example.ps_android_mayro_tablet_xspan.views.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import androidx.core.content.ContextCompat;

import com.example.ps_android_mayro_tablet_xspan.R;
import com.example.ps_android_mayro_tablet_xspan.models.items.Skus;

import java.util.LinkedList;
import java.util.List;
import java.util.Locale;

public class PalletAdapter extends BaseAdapter {
    private final Activity activity;
    private List<Skus> skusList;

    public PalletAdapter(Activity activity) {
        this.activity = activity;
        this.skusList = new LinkedList<>();
    }

    public void setSkusList(List<Skus> skusList) {
        this.skusList = skusList;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return skusList.size();
    }

    @Override
    public Skus getItem(int i) {
        return skusList.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View rowView = convertView;
        com.example.ps_android_mayro_tablet_xspan.views.clases.ViewHolder viewHolder;
        if(rowView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            rowView = inflater.inflate(R.layout.list_contenedores, parent, false);
            viewHolder = new com.example.ps_android_mayro_tablet_xspan.views.clases.ViewHolder();
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
            viewHolder = (com.example.ps_android_mayro_tablet_xspan.views.clases.ViewHolder) convertView.getTag();
        }
        viewHolder.titulo.setText(activity.getResources().getString(R.string.sku));
        viewHolder.contenedorTv.setText(String.format(skusList.get(position).getSku()));
        viewHolder.esperadosTv.setText(String.format(Locale.getDefault(), "%d", skusList.get(position).getEsperados()));
        viewHolder.leidosTv.setText(String.format(Locale.getDefault(), "%d", skusList.get(position).getLeidos()));

        if(skusList.get(position).getSobrantes()>0) {
            viewHolder.padreLl.setBackground(ContextCompat.getDrawable(activity, R.drawable.rectangle_errores));
            for(int i=0; i<viewHolder.views.length; i++) {
                viewHolder.views[i].setBackgroundColor(ContextCompat.getColor(activity, R.color.status_red));
            }
        } else {
            if(skusList.get(position).getEsperados()>skusList.get(position).getLeidos()) {
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
