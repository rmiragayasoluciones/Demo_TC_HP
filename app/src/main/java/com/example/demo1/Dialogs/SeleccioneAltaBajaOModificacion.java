package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.demo1.R;

public class SeleccioneAltaBajaOModificacion  extends AppCompatDialogFragment  {

    private SeleccionesAltaBajaModListener mListener;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.seleccione_alta_baja_mod, null);
        builder.setView(view);
        Button altaBtn = view.findViewById(R.id.altaBtn);
        Button bajaBtn = view.findViewById(R.id.bajaBtn);
        Button modificacionBtn = view.findViewById(R.id.modificacionBtn);

        altaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAltaBajaModificacionClick("alta");
                dismiss();
            }
        });

        bajaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAltaBajaModificacionClick("baja");
                dismiss();
            }
        });

        modificacionBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAltaBajaModificacionClick("modificacion");
                dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    public void onResume() {
        super.onResume();

        getDialog().setOnKeyListener(new DialogInterface.OnKeyListener() {
            @Override
            public boolean onKey(DialogInterface dialog, int keyCode, KeyEvent event) {

                if ((keyCode ==  android.view.KeyEvent.KEYCODE_BACK))
                {
                    mListener.volverActivityAnterior();
                    return true;
                }
                else
                    return false;

            }
        });
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (SeleccionesAltaBajaModListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement SeleccionesAltaBajaModListener");
        }
    }

    public interface SeleccionesAltaBajaModListener{
        void onAltaBajaModificacionClick(String text);
        void volverActivityAnterior();
    }



}
