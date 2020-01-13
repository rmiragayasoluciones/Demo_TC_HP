package com.example.demo1.Dialogs;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.demo1.R;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class BottomSheetDialogProducto extends BottomSheetDialogFragment {
    private BottomSheetAltaBajaListener mListener;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getDialog().setOnShowListener(new DialogInterface.OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog){
            BottomSheetDialog d = (BottomSheetDialog) dialog;
            View bottomSheetInternal = d.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            BottomSheetBehavior.from(bottomSheetInternal).setState(BottomSheetBehavior.STATE_EXPANDED);
        }
        });
        View v = inflater.inflate(R.layout.bottom_sheet_productoaltabaja_layout, container, false);

        Button altaBtn = v.findViewById(R.id.altaBtn);
        Button bajaBtn = v.findViewById(R.id.bajaBtn);

        altaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAltaBajaClick("alta");
                dismiss();
            }
        });

        bajaBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onAltaBajaClick("baja");
                dismiss();
            }
        });

        return v;
    }

    public interface BottomSheetAltaBajaListener {
        void onAltaBajaClick(String text);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);

        try {
            mListener = (BottomSheetAltaBajaListener) context;
        } catch (ClassCastException e){
            throw new ClassCastException(context.toString()
                    + " debe implementar BottomSheetAltaBajaListener");
        }

    }
}
