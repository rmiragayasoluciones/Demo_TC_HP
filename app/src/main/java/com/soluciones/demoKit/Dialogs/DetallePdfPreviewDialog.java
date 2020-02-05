package com.soluciones.demoKit.Dialogs;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
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
import androidx.constraintlayout.widget.ConstraintLayout;

import com.soluciones.demoKit.R;
import com.soluciones.demoKit.UserClass.GetDocumentViewModel;
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
    private int tabPosition;
    private ConstraintLayout filiation, procuts, qrBarcode, firma, ejemplos;


    public DetallePdfPreviewDialog(File pdfFile, GetDocumentViewModel documentViewModel, int tabPosition) {
        this.pdfFile = pdfFile;
        this.documentViewModel = documentViewModel;
        this.tabPosition = tabPosition;
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

        filiation = view.findViewById(R.id.filiationConstraint);
        procuts = view.findViewById(R.id.productsConstraint);
        qrBarcode = view.findViewById(R.id.qrBarcodeConstraint);
        firma = view.findViewById(R.id.firmaConstraint);
        ejemplos = view.findViewById(R.id.ejemplosConsrtaint);
//        ejemplos = view.findViewById(R.id.constra);



        switch (tabPosition){
            case 0:
                Log.d(TAG, "Filiation");
                filiation.setVisibility(View.VISIBLE);
                bindFiliationsViews(view);
                break;
            case 1:
                Log.d(TAG, "Productos");
                procuts.setVisibility(View.VISIBLE);
                bindProductosViews(view);
                break;
            case 2:
                Log.d(TAG, "QR/Barcode");
                qrBarcode.setVisibility(View.VISIBLE);
                bindQRBarcodeViews(view);
                break;
            case 3:
                Log.d(TAG, "RecorteFirma");
                firma.setVisibility(View.VISIBLE);
                bindFirmaViews(view);
                break;
            case 4:
                Log.d(TAG, "Ejemplos");
                ejemplos.setVisibility(View.VISIBLE);
//                bindEjemplosViews(view);
                break;
        }


        Button imprimirBtn = view.findViewById(R.id.imprimirBtn);
        imprimirBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onImprimirBtnPress(pdfFile);
                dismiss();
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

    private void bindFiliationsViews(View view){

        TextView cliente = view.findViewById(R.id.clienteId);
        cliente.setText(this.documentViewModel.getClient());


        TextView name = view.findViewById(R.id.bussinesNameId);
        TextView email = view.findViewById(R.id.emailId);
        TextView sexo = view.findViewById(R.id.sexoId);
        TextView pais = view.findViewById(R.id.paisId);

        name.setText(this.documentViewModel.getMetadataViewModel().getBusinessName());
        email.setText(this.documentViewModel.getMetadataViewModel().getEmail());
        sexo.setText(this.documentViewModel.getMetadataViewModel().getSex());
        pais.setText(this.documentViewModel.getMetadataViewModel().getCountry());

    }

    private void bindProductosViews(View view){

        TextView cliente = view.findViewById(R.id.clienteIdp);
        cliente.setText(this.documentViewModel.getClient());


        TextView name = view.findViewById(R.id.bussinesNameIdp);
        TextView email = view.findViewById(R.id.emailIdp);
        TextView sexo = view.findViewById(R.id.sexoIdp);
        TextView pais = view.findViewById(R.id.paisIdp);
        TextView documentoNombre = view.findViewById(R.id.nombreDocumentoIdp);


        name.setText(this.documentViewModel.getMetadataViewModel().getBusinessName());
        email.setText(this.documentViewModel.getMetadataViewModel().getEmail());
        sexo.setText(this.documentViewModel.getMetadataViewModel().getSex());
        pais.setText(this.documentViewModel.getMetadataViewModel().getCountry());
        documentoNombre.setText(this.documentViewModel.getMetadataViewModel().getDocumentName());



        if (this.documentViewModel.getMetadataViewModel().getReason() != null){
            //baja
            View barra = view.findViewById(R.id.barradeRazonOCodigo);
            barra.setVisibility(View.VISIBLE);
            TextView razonIpoCodigo = view.findViewById(R.id.razonIdp);
            TextView razooCodigoTituloIp = view.findViewById(R.id.razonTituloIdp);
            razonIpoCodigo.setVisibility(View.VISIBLE);
            razooCodigoTituloIp.setVisibility(View.VISIBLE);
            razooCodigoTituloIp.setText("Razón");
            razonIpoCodigo.setText(this.documentViewModel.getMetadataViewModel().getReason());


        }

        if (this.documentViewModel.getMetadataViewModel().getCode() != null){
            //alta
            View barra = view.findViewById(R.id.barradeRazonOCodigo);
            barra.setVisibility(View.VISIBLE);
            TextView razonIpoCodigo = view.findViewById(R.id.razonIdp);
            TextView razooCodigoTituloIp = view.findViewById(R.id.razonTituloIdp);
            razonIpoCodigo.setVisibility(View.VISIBLE);
            razooCodigoTituloIp.setVisibility(View.VISIBLE);
            razooCodigoTituloIp.setText("Código");
            razonIpoCodigo.setText(this.documentViewModel.getMetadataViewModel().getCode());
        }

    }

    private void bindQRBarcodeViews(View view){

        TextView cliente = view.findViewById(R.id.clienteIdq);
        cliente.setText(this.documentViewModel.getClient());

        TextView documentName = view.findViewById(R.id.documentNameIdq);
        documentName.setText(this.documentViewModel.getMetadataViewModel().getDocumentName());
    }

    private void bindFirmaViews(View view){
        TextView cliente = view.findViewById(R.id.clienteIdf);
        cliente.setText(this.documentViewModel.getClient());

    }
}
