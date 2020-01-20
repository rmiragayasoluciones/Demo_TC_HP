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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;

import com.example.demo1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DigitalizarRecorteFirmaDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarRecorteFirma";

    private RecorteFirmaDialogListener listener;
    private Button btnSiguiente;
    private TextInputEditText editText;
    private TextInputLayout editTextLayout;
    private TextView subtitulo;
    private String editTextGuardado;

    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.recorte_firma_dialog, null);
        builder.setView(view);

        constraintLayout = view.findViewById(R.id.constraintLayoutRecorteFirmeDialog);
        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        //todo set Views
        editText = view.findViewById(R.id.editTextRecorteFirmaDialog);
        editTextLayout = view.findViewById(R.id.textInputLayoutRecorteFirma);
        subtitulo = view.findViewById(R.id.subtituloRecorteFirma);
        subtitulo.setText("Ingrese ID del Cliente");


        //todo set Buttons y OnclickListener + listener

        btnSiguiente = view.findViewById(R.id.btnSiguienteRecorteFirmaDialog);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                    dismiss();
                    listener.onDigitalizarRecorteFirmaDialog(editText.getText().toString());
            }
        });

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }


//    private void cambiarLayout(){
//        constraintSet.connect(btnSiguiente.getId(), ConstraintSet.TOP, subtitulo.getId(),ConstraintSet.BOTTOM,16);
//        constraintSet.applyTo(constraintLayout);
//        constraintLayout.removeView(editTextLayout);
//        constraintLayout.removeView(editText);
//        subtitulo.setText("Ingrese la documentacion a escanear y presione siguiente");
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (RecorteFirmaDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarQroBarcodeDialogListener");
        }
    }


    public interface RecorteFirmaDialogListener {
        void onDigitalizarRecorteFirmaDialog(String idCliente);
    }
}
