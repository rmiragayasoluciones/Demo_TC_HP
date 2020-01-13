package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.demo1.DocuFiliatoriosActivity;
import com.example.demo1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.lang.ref.WeakReference;

public class DigitalizarDocuFiliatoriosDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarDocuFiliator";

    private final WeakReference<DocuFiliatoriosActivity> mContext;
    private TextView titulo;
    private TextView subtitulo;
    private TextInputLayout editTextLayout;
    private TextInputEditText editText;
    private int dialogNumero;

    public DigitalizarDocuFiliatoriosDialog(DocuFiliatoriosActivity context, int dialogNumero) {
        this.mContext = new WeakReference<>(context);
        this.dialogNumero = dialogNumero;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.digitalizar_docu_filiatorios_dialog, null);
        builder.setView(view);

        titulo = view.findViewById(R.id.tituloDialogId);
        subtitulo = view.findViewById(R.id.subtitulo);
        editText = view.findViewById(R.id.textEditDocuId);
        editTextLayout = view.findViewById(R.id.textimputlayout);

        Button btn = view.findViewById(R.id.digitalizacionBtnId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
                mContext.get().onDigitalizacionDialogResponse();
            }
        });

        armarDialog(dialogNumero);

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private void armarDialog(int numeroDialog){
        switch (numeroDialog){
            case 0:
                solicitarId();
                break;
            case 1:
                solicitarIngresoConstancia();
                break;
            case 2:
                solicitarOtraDocu();
                break;
        }
    }

    private void solicitarId(){
        titulo.setText("Ingrese ID");
        subtitulo.setText("Ingrese ID del trabajo");
        //todo borrar el layout
        editTextLayout.setVisibility(View.VISIBLE);
    }

    private void solicitarIngresoConstancia(){
        titulo.setText("Constancia");
        subtitulo.setText("Ingrese Constancia de ingreso y presione Siguiente");
        editTextLayout.setVisibility(View.GONE);
    }

    private void solicitarOtraDocu(){
        titulo.setText("DNI");
        subtitulo.setText("Ingrese DNI y presione Siguiente");
        editTextLayout.setVisibility(View.GONE);
    }

}
