package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.demo1.R;

public class BuscarIdDialog extends AppCompatDialogFragment {
    private static final String TAG = "BuscarIdDialog";

    private BuscarIdDialogListener mListener;
    private EditText idABuscar;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.buscar_id_dialog, null);
        builder.setView(view);

        idABuscar = view.findViewById(R.id.editTextBuscarClienteId);
        Button buscarBtn = view.findViewById(R.id.buscarBtnId);
        buscarBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarId(idABuscar.getText().toString())){
                    mListener.buscarId(idABuscar.getText().toString());
                    dismiss();
                }
            }
        });

        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }

    private boolean validarId(String idIngresado){

        if (idIngresado.length() < 2){
            idABuscar.setError("id incompleto");
            return false;
        }
        return true;
    }


    public interface BuscarIdDialogListener{
        void buscarId(String idCliente);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BuscarIdDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "hay que implementar BuscarIdDialogListener");
        }
    }
}
