package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.demo1.R;

public class VolleyErrorResponseDialog extends AppCompatDialogFragment {
    private static final String TAG = "VolleyErrorResponseDial";

    private IntentarReconectListener listener;
    private final String menssaje;

    public VolleyErrorResponseDialog(String menssajeError) {
        this.menssaje = menssajeError;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        //todo crear nuevo layout
        View view = inflater.inflate(R.layout.volley_response_error_dialog, null);
        builder.setView(view);

        Button button = view.findViewById(R.id.okBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.reconectarYsubirArchivo();
            }
        });

        TextView texto = view.findViewById(R.id.tituloDialog);

        //si mensaje es nulo, se carga por default, sino se carga la respuesta de la api
        if (this.menssaje == null || this.menssaje.equalsIgnoreCase("")){
            texto.setText(R.string.config_error);
        } else {
            texto.setText(this.menssaje);
        }

        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;

        return dialog;
    }


    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (IntentarReconectListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement IntentarReconectListener");
        }
    }

    public interface IntentarReconectListener{
        void reconectarYsubirArchivo();
    }
}
