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

public class DigitalizarQroBarcodeDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarQroBarcodeDi";

    private DigitalizarQroBarcodeDialogListener listener;
    private boolean esQr;
    private TextInputEditText editText;
    private TextInputLayout editTextLayout;
    private TextView subtitulo;
    private String editTextGuardado;
    private Button btnSiguiente;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSet;

    public DigitalizarQroBarcodeDialog(boolean esQr) {
        this.esQr = esQr;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.qr_or_barcode_dialog, null);
        builder.setView(view);

        constraintLayout = view.findViewById(R.id.dialogQrBarcodeConstraintId);
        constraintSet = new ConstraintSet();
        constraintSet.clone(constraintLayout);

        subtitulo = view.findViewById(R.id.subtituloqrBarcode);
        subtitulo.setText("Ingrese ID del cliente");
        editTextLayout =view.findViewById(R.id.textimputlayoutQrBarcode);
        editText = view.findViewById(R.id.textEditDocuIdQrBarcode);

        btnSiguiente = view.findViewById(R.id.digitalizacionBtnIdQrBarcode);
        btnSiguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (editText.getText().length() == 0){
                    editText.setError("Campo requerido");
                    return;
                }
                    listener.onDigitalizarQroBarcodeDialog(editText.getText().toString());
                    dismiss();



            }
        });


        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            listener = (DigitalizarQroBarcodeDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarQroBarcodeDialogListener");
        }
    }

    public interface DigitalizarQroBarcodeDialogListener {
        void onDigitalizarQroBarcodeDialog(String idCliente);
    }

//    private void cambiarLayout(){
//        constraintSet.connect(btnSiguiente.getId(),ConstraintSet.TOP, subtitulo.getId(),ConstraintSet.BOTTOM,16);
//        constraintSet.applyTo(constraintLayout);
//        constraintLayout.removeView(editTextLayout);
//        constraintLayout.removeView(editText);
//        subtitulo.setText("Ingrese la documentacion a escanear y presione siguiente");
//    }
}
