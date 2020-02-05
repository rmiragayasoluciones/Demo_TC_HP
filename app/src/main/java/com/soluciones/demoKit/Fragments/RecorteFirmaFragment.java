package com.soluciones.demoKit.Fragments;

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

import com.soluciones.demoKit.DocumentRecyclerAdapter;
import com.soluciones.demoKit.R;
import com.soluciones.demoKit.UserClass.Documents;

import java.util.ArrayList;
import java.util.List;

public class RecorteFirmaFragment extends Fragment implements DocumentRecyclerAdapter.OnItemClickListener {
    private static final String TAG = "RecorteFirmaFragment";

    private RecyclerView recyclerView;
    private DocumentRecyclerAdapter adapter;
    private List<Documents> documentsList;
    private RecorteFragmentListener mListener;

    public interface RecorteFragmentListener{
        void onDocuClick(Documents documentSeleccionado);
    }

    public RecorteFirmaFragment(List<Documents> documentsList) {
        this.documentsList = separarCategorias(documentsList);
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
        mListener.onDocuClick(documentsList.get(position));
        //abrir el Fragment con el Archivo PDF
    }

    private List<Documents> separarCategorias(List<Documents> documentsList){
        List<Documents> documentsSeparados = new ArrayList<>();
        for (Documents d : documentsList){
            if (d.getSerieName().toLowerCase().equalsIgnoreCase("signature")){
                documentsSeparados.add(d);
            }
        }
        return documentsSeparados;
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof RecorteFragmentListener){
            mListener = (RecorteFragmentListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implements RecorteFragmentListener");
        }
    }
}
