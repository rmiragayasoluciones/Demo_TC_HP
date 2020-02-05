package com.soluciones.demoKit.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.soluciones.demoKit.R;

public class FinalizacionDeTrabajo extends AppCompatDialogFragment {
    private static final String TAG = "FinalizacionDeTrabajo";

    private FinalizacionDeTrabajoListener listener;
    private Button si, no;
    private TextView trabajo;
    private String mesajePrincipal;

    public FinalizacionDeTrabajo() {
    }

    public FinalizacionDeTrabajo(String mesajePrincipal) {
        this.mesajePrincipal = mesajePrincipal;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateDialog: call");

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View v = inflater.inflate(R.layout.finalizacion_trabajo_layout, null);

        trabajo = v.findViewById(R.id.texto1Id);
        si = v.findViewById(R.id.btnPositivo);
        no = v.findViewById(R.id.btnNegativo);
        //todo agarrar el nombre del archivo y ponerlo en "trabajo" (trabajo.setText(nombre de archivo subido))
        if (this.mesajePrincipal!=null){
            trabajo.setText(this.mesajePrincipal);
        } else {
            trabajo.setText("Los archivos fueron subidos correctamente");
        }

        trabajo.setGravity(Gravity.CENTER);

        builder.setView(v);

        si.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: SIIIIIIIIIIIIII");
                listener.realizarOtroTrabajo(true);
                dismiss();

            }
        });

        no.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "onClick: NOOOOOOOOO");
                listener.realizarOtroTrabajo(false);
                dismiss();
            }
        });

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);

        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (FinalizacionDeTrabajoListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement FinalizacionDeTrabajoListener");
        }
    }

    public interface FinalizacionDeTrabajoListener {
        void realizarOtroTrabajo(boolean siOno);
    }
}
