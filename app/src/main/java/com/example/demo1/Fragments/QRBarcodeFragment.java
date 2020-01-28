package com.example.demo1.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.demo1.DocumentRecyclerAdapter;
import com.example.demo1.R;
import com.example.demo1.UserClass.Documents;

import java.util.ArrayList;
import java.util.List;

public class QRBarcodeFragment extends Fragment implements DocumentRecyclerAdapter.OnItemClickListener {
    private static final String TAG = "QRBarcodeFragment";

    private RecyclerView recyclerView;
    private DocumentRecyclerAdapter adapter;
    private List<Documents> documentsList;
    private QRBarcodeFragmentListener mListener;

    public interface QRBarcodeFragmentListener{
        void onDocuClick(Documents documentSeleccionado);
    }

    public QRBarcodeFragment(List<Documents> documentsList) {
        this.documentsList = separarCategorias(documentsList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.qrbarcode_document_fragment, container, false);
        recyclerView = v.findViewById(R.id.qrBarcoderecyclerfregmanent);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        adapter =new DocumentRecyclerAdapter(getActivity(), this.documentsList);
        recyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(this);
        return v;
    }

    @Override
    public void onItemClick(int position) {
        Toast.makeText(getActivity(), "Click en " + documentsList.get(position).getId(), Toast.LENGTH_SHORT).show();
        mListener.onDocuClick(documentsList.get(position));
        //abrir el Fragment con el Archivo PDF
    }

    private List<Documents> separarCategorias(List<Documents> documentsList){
        List<Documents> documentsSeparados = new ArrayList<>();
        for (Documents d : documentsList){
            if (d.getSerieName().toLowerCase().equalsIgnoreCase("qr") || d.getSerieName().toLowerCase().equalsIgnoreCase("barcode")){
                documentsSeparados.add(d);
            }
        }
        return documentsSeparados;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof QRBarcodeFragmentListener){
            mListener = (QRBarcodeFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implements QRBarcodeFragmentListener");
        }
    }
}
