package com.example.demo1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

/**
 * Actividad donde el usuario selecciona si vaa  ser QR o BArcode
 * */
public class CodigoBarraYQRActivity extends AppCompatActivity {

    private CardView qrCardView, barCodeCardView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_barra_y_qr);

        //todo ver esto de abajo
        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }

        qrCardView = findViewById(R.id.qrCardViewId);
        qrCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "QR", Toast.LENGTH_SHORT).show();
                startQRActivity();
            }
        });

        barCodeCardView = findViewById(R.id.cardCodeCardId);
        barCodeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "BARCODE", Toast.LENGTH_SHORT).show();
                startBarCodeActivity();
            }
        });

        onCreateAnimation();
    }


    private void startQRActivity(){
        Intent intent = new Intent(this, QRandBarCodeActivity.class);
        intent.putExtra("TIPO", "QR");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
    }

    private void startBarCodeActivity(){
        Intent intent = new Intent(this, QRandBarCodeActivity.class);
        intent.putExtra("TIPO", "Barcode");
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }



    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AppSelectionActivity.class);
        startActivity(intent);
        finish();
    }

    private void onCreateAnimation(){
        qrCardView.setAlpha(0f);
        qrCardView.setVisibility(View.VISIBLE);

        qrCardView.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        barCodeCardView.setAlpha(0f);
                        barCodeCardView.setVisibility(View.VISIBLE);
                        barCodeCardView.animate()
                                .alpha(1f)
                                .setDuration(1000);
                    }
                });
    }
}
