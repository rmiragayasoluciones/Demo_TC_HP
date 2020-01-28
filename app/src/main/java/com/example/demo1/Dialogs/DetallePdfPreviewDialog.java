package com.example.demo1.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;

import com.example.demo1.R;
import com.example.demo1.UserClass.GetDocumentViewModel;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;

import java.io.File;

public class DetallePdfPreviewDialog extends AppCompatDialogFragment implements OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener {
    private static final String TAG = "DetallePdfPreviewDialog";

    private File pdfFile;
    private OnImprimirBtnPressListener mListener;
    private GetDocumentViewModel documentViewModel;


    public DetallePdfPreviewDialog(File pdfFile, GetDocumentViewModel documentViewModel) {
        this.pdfFile = pdfFile;
        this.documentViewModel = documentViewModel;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(STYLE_NO_TITLE, 0);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = getActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.detalle_pdf_dialog, null);
        builder.setView(view);

        TextView cliente = view.findViewById(R.id.clienteId);
        TextView name = view.findViewById(R.id.bussinesNameId);
        TextView email = view.findViewById(R.id.emailId);
        TextView sexo = view.findViewById(R.id.sexoId);
        TextView pais = view.findViewById(R.id.paisId);

        cliente.setText(this.documentViewModel.getClient());
        name.setText(this.documentViewModel.getMetadataViewModel().getBusinessName());
        email.setText(this.documentViewModel.getMetadataViewModel().getEmail());
        sexo.setText(this.documentViewModel.getMetadataViewModel().getSex());
        pais.setText(this.documentViewModel.getMetadataViewModel().getCountry());


        Button imprimirBtn = view.findViewById(R.id.imprimirBtn);
        imprimirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImprimirBtnPress(pdfFile);
            }
        });
        PDFView pdfView = view.findViewById(R.id.pdfDetallePreview);
        pdfView.fromFile(pdfFile)
                .onPageChange(this)
                .swipeHorizontal(true)
                .enableAnnotationRendering(true)
                .onLoad(this)
                .scrollHandle(new DefaultScrollHandle(getActivity()))
                .spacing(10) // in dp
                .onPageError(this)
                .load();


        Dialog dialog = builder.create();
        dialog.getWindow().setBackgroundDrawableResource(android.R.color.transparent);
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        return dialog;
    }

    @Override
    public void onResume() {
        ViewGroup.LayoutParams params = getDialog().getWindow().getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        getDialog().getWindow().setAttributes((WindowManager.LayoutParams) params);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        super.onResume();
    }

    @Override
    public void loadComplete(int nbPages) {

    }

    @Override
    public void onPageChanged(int page, int pageCount) {

    }

    @Override
    public void onPageError(int page, Throwable t) {

    }

    public interface OnImprimirBtnPressListener{
        void onImprimirBtnPress(File pdfFile);

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnImprimirBtnPressListener){
            mListener = (OnImprimirBtnPressListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implements OnImprimirBtnPressListener");
        }
    }
}
