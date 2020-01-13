package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.demo1.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class DigitalizarBajaProductoDialog extends AppCompatDialogFragment {
    private static final String TAG = "DigitalizarBajaProducto";

    private DigitalizarBajaDialogListener listener;

    private RadioGroup radioGroup;
    private RadioButton radioButton;
    private TextInputLayout editTextLayout;
    private TextInputEditText editText;

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        final View view = inflater.inflate(R.layout.baja_producto_baja_dialog, null);
        builder.setView(view);

        editText = view.findViewById(R.id.editTextBajaOtro);
        editTextLayout = view.findViewById(R.id.textInputBajaLayout);
        radioGroup = view.findViewById(R.id.radioGroupRazonBaja);
        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                radioButton = view.findViewById(checkedId);
                Toast.makeText(view.getContext(), "RButton " + radioButton.getText(), Toast.LENGTH_SHORT).show();
                if (checkedId== R.id.opcion3){
                    editTextLayout.setVisibility(View.VISIBLE);

                } else {
                    editTextLayout.setVisibility(View.GONE);
                }
            }
        });

        radioGroup.check(R.id.opcion1);
        radioButton = view.findViewById(R.id.opcion1);

        Button btn = view.findViewById(R.id.digitalizacionBtnId);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (editTextLayout.getVisibility() == View.VISIBLE){
                    listener.onDigitalizacionBajaDialogRespons (radioButton.getText().toString().toLowerCase().trim() ,editText.getText().toString());
                } else {
                    listener.onDigitalizacionBajaDialogRespons (radioButton.getText().toString().toLowerCase().trim() ,"");
                }

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
            listener = (DigitalizarBajaDialogListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(context.toString() +
                    "must implement DigitalizarBajaProductoDialog");
        }
    }

    public interface DigitalizarBajaDialogListener {
        void onDigitalizacionBajaDialogRespons(String razonBaja, String otros);
    }


}
