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

public class EjemplosFragment extends Fragment implements DocumentRecyclerAdapter.OnItemClickListener {
    private static final String TAG = "EjemplosFragment";

    private RecyclerView recyclerView;
    private DocumentRecyclerAdapter adapter;
    private List<Documents> documentsList;
    private EjemplosFragmentListener mListener;


    public interface EjemplosFragmentListener{
        void onEjemploDocuClick(String ejemploDocuIc);
    }

    public EjemplosFragment() {
        this.documentsList = new ArrayList<>();
        this.documentsList.add(new Documents("1","QR", "demo id", null, "Carátula"));
        this.documentsList.add(new Documents("2","Barcode", "demo id", null, "Carátula"));
        this.documentsList.add(new Documents("3","Recorte de Firma", "demo id", null, "Carátula"));
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.recorte_firma_fragment, container, false);
        recyclerView = v.findViewById(R.id.recorteFirmaRecyclerFragment);
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
        mListener.onEjemploDocuClick(documentsList.get(position).getId());
        //abrir el Fragment con el Archivo PDF
    }

//    private List<Documents> separarCategorias(List<Documents> documentsList){
//        List<Documents> documentsSeparados = new ArrayList<>();
//        for (Documents d : documentsList){
//            if (d.getSerieName().toLowerCase().equalsIgnoreCase("signature")){
//                documentsSeparados.add(d);
//            }
//        }
//        return documentsSeparados;
//    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof EjemplosFragmentListener){
            mListener = (EjemplosFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implements EjemplosFragmentListener");
        }
    }
}
