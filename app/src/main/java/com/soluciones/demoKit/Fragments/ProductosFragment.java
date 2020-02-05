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

public class ProductosFragment extends Fragment implements DocumentRecyclerAdapter.OnItemClickListener {
    private static final String TAG = "ProductosFragment";

    private RecyclerView recyclerView;
    private DocumentRecyclerAdapter adapter;
    private List<Documents> documentsList;
    private ProductFragmentListener mListener;

    public interface ProductFragmentListener{
        void onDocuClick(Documents documentSeleccionado);
    }

    public ProductosFragment(List<Documents> documentsList) {
        this.documentsList = separarCategorias(documentsList);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.producto_document_fragment, container, false);
        recyclerView = v.findViewById(R.id.productorecyclerfragment);
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
            if (d.getSerieName().toLowerCase().equalsIgnoreCase("high")
            || d.getSerieName().toLowerCase().equalsIgnoreCase("low")
            || d.getSerieName().toLowerCase().equalsIgnoreCase("modification")){
                documentsSeparados.add(d);
            }
        }
        return documentsSeparados;

    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof ProductFragmentListener){
            mListener = (ProductFragmentListener)context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implements ProductFragmentListener");
        }
    }
}
