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

public class DigitalizarAltaProductoDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarAltaProducto";

    private DigitalizarAltaProductoDialogListener listener;
    private TextInputEditText editText, nombreDocumento;


    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //todo layout para alta
        View view = inflater.inflate(R.layout.alta_producto_alta_dialog, null);
        builder.setView(view);

        editText = view.findViewById(R.id.textEditaltaId);
        nombreDocumento = view.findViewById(R.id.editTextDocumentName);

        Button btn = view.findViewById(R.id.digitalizacionBtnId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //si los campos estan llenos
                if (validarCampos()) {
                    dismiss();
                    listener.onDigitalizacionAltaDialogRespons(editText.getText().toString(), nombreDocumento.getText().toString());
                }
            }
        });


        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }


    private boolean validarCampos(){
        if (editText.length() == 0){
            editText.setError("Campo obligatorio");
            return false;
        } else if (nombreDocumento.length() == 0){
            nombreDocumento.setError("Campo obligatorio");
            return false;
        }
        return true;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DigitalizarAltaProductoDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarAltaProductoDialogListener");
        }
    }

    public interface DigitalizarAltaProductoDialogListener {
        void onDigitalizacionAltaDialogRespons(String codigoAlta, String documentName);
    }
}
