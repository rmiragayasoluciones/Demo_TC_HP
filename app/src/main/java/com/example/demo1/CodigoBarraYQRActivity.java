package com.example.demo1;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;

import com.example.demo1.UserClass.DemoViewModelSingleton;

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

        //todo cargar imagen y nombre ed empresa
        DemoViewModelSingleton demoViewModelSingleton = DemoViewModelSingleton.getInstance();
        String nombreEmpresa = demoViewModelSingleton.getDemoViewModelGuardado().getClient();
        String logoEnString = demoViewModelSingleton.getDemoViewModelGuardado().getLogo();
//        String logoEnString = getResources().getString(R.string.long_string);

        if (nombreEmpresa!=null){
            TextView nombreEmpresaTextView = findViewById(R.id.nombreMarcaEmpresaQRYBarcodeId);
            nombreEmpresaTextView.setText(nombreEmpresa);
        }
        if (logoEnString!= null && !logoEnString.isEmpty()){
            ImageView logoImageView = findViewById(R.id.logoMarcaQrYBarcodeId);
            logoImageView.setImageBitmap(resize(loadImage(logoEnString), 70, 70));
        }

        qrCardView = findViewById(R.id.qrCardViewId);
        qrCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "QR", Toast.LENGTH_SHORT).show();
                startQRActivity();
            }
        });

        barCodeCardView = findViewById(R.id.cardCodeCardId);
        barCodeCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(v.getContext(), "BARCODE", Toast.LENGTH_SHORT).show();
                startBarCodeActivity();
            }
        });

        onCreateAnimation();
    }

    private Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;

            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > ratioBitmap) {
                finalWidth = (int) ((float)maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float)maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

    private Bitmap loadImage(String logoEnString){

        byte[] decodeString = Base64.decode(logoEnString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString, 0 , decodeString.length);

        return  decodedByte;
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
