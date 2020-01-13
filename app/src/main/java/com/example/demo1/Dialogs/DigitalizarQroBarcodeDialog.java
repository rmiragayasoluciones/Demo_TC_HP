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
import com.google.android.material.textfield.TextInputEditText;

public class DigitalizarQroBarcodeDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarQroBarcodeDi";

    private DigitalizarQroBarcodeDialogListener listener;
    private boolean esQr;
    private TextInputEditText editText;
    private TextView subtitulo;

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

        subtitulo = view.findViewById(R.id.subtituloqrBarcode);
        editText = view.findViewById(R.id.textEditDocuIdQrBarcode);

        Button btn = view.findViewById(R.id.digitalizacionBtnIdQrBarcode);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                listener.onDigitalizarQroBarcodeDialog(editText.getText().toString());

            }
        });

        armarSubtitulo();

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

    private void armarSubtitulo(){
        if (esQr){
            subtitulo.setText("ingrese ID del cliente y la ducumentación QR");
        } else {
            subtitulo.setText("ingrese ID del cliente y la ducumentación BarCode");
        }
    }

}
