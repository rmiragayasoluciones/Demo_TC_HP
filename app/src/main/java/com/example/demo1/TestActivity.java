package com.example.demo1;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class TestActivity extends AppCompatActivity {

    private Button btn;
    private ImageView logoEmpresa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);

        Intent intent = getIntent();
        Bitmap bitmap = (Bitmap) intent.getParcelableExtra("logo");
        btn = findViewById(R.id.btnTest);
        logoEmpresa = findViewById(R.id.imageViewId);

        logoEmpresa.setImageBitmap(bitmap);
//        loadImage();

    }

}
