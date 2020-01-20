package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.OvershootInterpolator;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.transition.ChangeBounds;
import androidx.transition.Transition;

import com.example.demo1.R;
import com.google.android.material.textfield.TextInputLayout;

public class AltaBajaModificacionDialog extends AppCompatDialogFragment {
    private static final String TAG = "AltaBajaModificacionDia";


    private EditText documentNameEditText, lowOHigh;
    private RadioButton alta, baja, modificacion, radioButton;
    private RadioGroup radioGroup, radioGroupBaja;
    private TextInputLayout otrosTextInputL;

    private Button button;
    private TextInputLayout textInputLayout;
    private ConstraintLayout constraintLayout;
    private ConstraintSet constraintSetAlta = new ConstraintSet();
    private ConstraintSet constraintSetBaja = new ConstraintSet();
    private ConstraintSet constraintSetModificacion = new ConstraintSet();



    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        //todo layout para alta
        View view = inflater.inflate(R.layout.altabajamodificacion_dialog, null);
        builder.setView(view);

        constraintLayout = view.findViewById(R.id.constraintSetToModify);
        constraintSetAlta.clone(constraintLayout);
        constraintSetBaja.clone(getContext(), R.layout.baja_constraintset);
        constraintSetModificacion.clone(getContext(), R.layout.modificacion_constraintset);

        documentNameEditText = view.findViewById(R.id.editTextNombreDocumento);
        lowOHigh = view.findViewById(R.id.edditTextLowOHigh);
        alta = view.findViewById(R.id.altaRadioBtn);
        baja = view.findViewById(R.id.bajaRadioBtn);
        modificacion = view.findViewById(R.id.modificacionRadioBtn);
        radioGroup = view.findViewById(R.id.razonRadioGroup);
        textInputLayout = view.findViewById(R.id.editTextAltaOBaja);
        otrosTextInputL = view.findViewById(R.id.otrosRazonLayout);
        button = view.findViewById(R.id.botonId);


        radioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {
                Transition changeBounds = new ChangeBounds();
                changeBounds.setInterpolator(new OvershootInterpolator());
//                changeBounds.setDuration(1000);
                androidx.transition.TransitionManager.beginDelayedTransition(constraintLayout, changeBounds);
                if (checkedId == R.id.altaRadioBtn){
                    Log.d(TAG, "ALTA");
                    constraintSetAlta.applyTo(constraintLayout);
                    constraintLayout.findViewById(R.id.editTextAltaOBaja).setVisibility(View.VISIBLE);
                    constraintLayout.findViewById(R.id.radioGroupRazonBaja).setVisibility(View.GONE);
                    otrosTextInputL.setVisibility(View.GONE);
//                    constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                } else if (checkedId == R.id.bajaRadioBtn){
                    Log.d(TAG, "BAJA: ");
                    constraintSetBaja.applyTo(constraintLayout);
                    constraintLayout.findViewById(R.id.editTextAltaOBaja).setVisibility(View.GONE);
                    constraintLayout.findViewById(R.id.radioGroupRazonBaja).setVisibility(View.VISIBLE);
                    otrosTextInputL.setVisibility(View.VISIBLE);
//                    constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT));
                } else if (checkedId == R.id.modificacionRadioBtn){
                    Log.d(TAG, "MODIFICACION: ");
                    constraintSetModificacion.applyTo(constraintLayout);
                    constraintLayout.findViewById(R.id.radioGroupRazonBaja).setVisibility(View.GONE);
                    constraintLayout.findViewById(R.id.editTextAltaOBaja).setVisibility(View.GONE);
                    otrosTextInputL.setVisibility(View.GONE);

//                    constraintLayout.setLayoutParams(new ConstraintLayout.LayoutParams(1,1));
                }
            }
        });

//        radioGroupBaja = view.findViewById(R.id.razonRadioGroup);
//        radioGroupBaja.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
//            @Override
//            public void onCheckedChanged(RadioGroup group, int checkedId) {
//               radioButton = getView().findViewById(checkedId);
////                Toast.makeText(view.getContext(), "RButton " + radioButton.getText(), Toast.LENGTH_SHORT).show();
//                if (checkedId== R.id.opcion3){
//                    Log.d(TAG, "onCheckedChanged: boton " + radioButton.getText().toString());
//
//                } else {
//                    Log.d(TAG, "onCheckedChanged: boton " + radioButton.getText().toString());
//                }
//            }
//        });
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarSeleccionProduccto();
            }
        });

        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        return dialog;
    }

    private void guardarSeleccionProduccto(){
        if (alta.isChecked()){

        }

        if (baja.isChecked()){

        }

        if (modificacion.isChecked()){

        }
    }

    public interface AltaBajaModificacionDialogListenar{
        void onAltaSeleccion(String documentName, String high);
        void onBajaSeleccion(String documentName, String reason);
        void onModificacionSeleccion(String documentName);

    }
}
