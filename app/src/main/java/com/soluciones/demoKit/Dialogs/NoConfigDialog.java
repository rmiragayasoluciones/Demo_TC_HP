package com.soluciones.demoKit.Dialogs;

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

import com.soluciones.demoKit.MainActivity;
import com.soluciones.demoKit.R;

import java.lang.ref.WeakReference;

public class NoConfigDialog extends AppCompatDialogFragment {
    private static final String TAG = "NoConfigDialog";

    private final WeakReference<MainActivity> mContext;
    private final String menssaje;

    public NoConfigDialog(MainActivity mContext, String menssajeError) {
        this.mContext = new WeakReference<>(mContext);
        this.menssaje = menssajeError;
    }



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();
        View view = inflater.inflate(R.layout.on_config_error_layout, null);
        builder.setView(view);

        Button button = view.findViewById(R.id.closeAppBtn);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mContext.get().finish();
                dismiss();
            }
        });

        TextView texto = view.findViewById(R.id.carteltituloId);

        //si mensaje es nulo, se carga por default, sino se carga la respuesta de la api
        if (this.menssaje == null || this.menssaje.equalsIgnoreCase("")){
            texto.setText(R.string.config_error);
        } else {
            texto.setText(this.menssaje);
        }

        Dialog dialog = builder.create();

        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.setCanceledOnTouchOutside(false);

        return dialog;
    }
}
