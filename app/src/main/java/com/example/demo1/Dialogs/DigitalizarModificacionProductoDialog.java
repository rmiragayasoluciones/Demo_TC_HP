package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.demo1.R;
import com.google.android.material.textfield.TextInputEditText;

public class DigitalizarModificacionProductoDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarModificacion";

    private DigitalizarModificacionDialogListener listener;
    private TextInputEditText nombreDocumento;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.modificacion_producto_dialog, null);
        builder.setView(view);

        nombreDocumento = view.findViewById(R.id.modificacionNombredocumentoEdittext);

        Button btn = view.findViewById(R.id.digitalizacionBtnId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (validarCampos()){
                    listener.onDigitalizacionModificacionDialogRespons(nombreDocumento.getText().toString().toLowerCase().trim());
                    dismiss();
                }

            }
        });

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private boolean validarCampos(){
        if (nombreDocumento.length() == 0){
            nombreDocumento.setError("Campo obligatorio");
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DigitalizarModificacionDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarModificacionDialogListener");
        }
    }

    public interface DigitalizarModificacionDialogListener {
        void onDigitalizacionModificacionDialogRespons(String documentName);
    }

}
