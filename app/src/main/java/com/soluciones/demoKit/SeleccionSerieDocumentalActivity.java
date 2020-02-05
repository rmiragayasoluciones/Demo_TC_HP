package com.soluciones.demoKit;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

public class SeleccionSerieDocumentalActivity extends AppCompatActivity {

    private CardView docuFiliatorios, productos;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seleccion_serie_documental);

        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        docuFiliatorios = findViewById(R.id.docuFiliatoriosId);
        docuFiliatorios.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionSerieDocumentalActivity.this, "Documentos Filiatorios", Toast.LENGTH_SHORT).show();
                startDocuFliliatorioActivity();
            }
        });

        productos = findViewById(R.id.productosId);
        productos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(SeleccionSerieDocumentalActivity.this, "Producto", Toast.LENGTH_SHORT).show();
                startProductosIdActivity();
            }
        });

    }

    private void startDocuFliliatorioActivity(){
        Intent intent = new Intent(this, DocuFiliatoriosActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void startProductosIdActivity(){
        Intent intent = new Intent(this, ProductoActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AppSelectionActivity.class);
        startActivity(intent);
        finish();
    }
}
