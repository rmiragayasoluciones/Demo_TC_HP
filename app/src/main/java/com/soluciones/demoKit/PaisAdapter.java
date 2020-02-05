package com.soluciones.demoKit;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.ArrayList;

public class PaisAdapter extends ArrayAdapter<PaisItem> {

    public PaisAdapter (Context context, ArrayList<PaisItem> paisList){
        super(context, 0, paisList);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return initView(position, convertView, parent);
    }

    private View initView(int position, View convertView, ViewGroup parent){
        if (convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(
                    R.layout.pais_spinner_layout, parent, false
            );
        }

        ImageView imageViewBandera = convertView.findViewById(R.id.banderaImagen);
        TextView textViewPais = convertView.findViewById(R.id.nombrePais);

        PaisItem currentPaisItem = getItem(position);

        if (currentPaisItem != null){
            imageViewBandera.setImageResource(currentPaisItem.getmPaisBandera());
            textViewPais.setText(currentPaisItem.getmPaisNombre());
        }

        return convertView;
    }
}
