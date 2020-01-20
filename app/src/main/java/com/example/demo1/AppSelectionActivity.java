package com.example.demo1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

public class AppSelectionActivity extends AppCompatActivity {

    private static final String TAG = "AppSelectionActivity";

    private static final int MAX_STEP = 3;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private View cargandoProgresBar;


    private String about_title_array[] = {
            "Apertura de Cuenta",
            "Clasificación de Documentos",
            "Captura de Firma"
    };
    private String about_description_array[] = {
            "Cargue la documentación de sus clientes de forma rápida y sencilla.",
            "Direccione sus documentos de forma dinámica mediante la lectura de códigos de Barra/QR.",
            "Recorte la firma de sus clientes plasmadas en formularios de manera dinámica."

    };
    private String about_images_array[] = {
            "abrir_cuenta.json",
            "qr.json",
            "firma.json"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selection);


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


//        Intent intent = getIntent();
//        if (intent.hasExtra("logo")){
//            Log.d(TAG, "Intent Has Extra!!!");
//            logo = intent.getParcelableExtra("logo");
//        }
//        if (intent.hasExtra("nombre")){
//            Log.d(TAG, "Intent Has Nombre!!!");
//            clientName = intent.getStringExtra("nombre");
//        }

        cargandoProgresBar = findViewById(R.id.selectAppProgressDialog);

        viewPager = findViewById(R.id.view_pager);
        // adding bottom dots
        bottomProgressDots(0);

        myViewPagerAdapter = new MyViewPagerAdapter();
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);

    }


    private void bottomProgressDots(int current_index) {
        LinearLayout dotsLayout = findViewById(R.id.layoutDots);
        ImageView[] dots = new ImageView[MAX_STEP];

        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new ImageView(this);
            int width_height = 15;
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(new ViewGroup.LayoutParams(width_height, width_height));
            params.setMargins(10, 10, 10, 10);
            dots[i].setLayoutParams(params);
            dots[i].setImageResource(R.drawable.shape_circle);
            dots[i].setColorFilter(Color.WHITE, PorterDuff.Mode.SRC_IN);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0) {
            dots[current_index].setImageResource(R.drawable.shape_circle);
            dots[current_index].setColorFilter(getResources().getColor(R.color.colorPrimary), PorterDuff.Mode.SRC_IN);
        }
    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(final int position) {
            bottomProgressDots(position);
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }
    };

    /**
     * View pager adapter
     */
    public class MyViewPagerAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Button btnNext;

        public MyViewPagerAdapter() {
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);

            View view = layoutInflater.inflate(R.layout.item_card_wizard_light, container, false);
            ((TextView) view.findViewById(R.id.title)).setText(about_title_array[position]);
            ((TextView) view.findViewById(R.id.description)).setText(about_description_array[position]);
            ((LottieAnimationView) view.findViewById(R.id.image)).setAnimation(about_images_array[position]);


            btnNext = view.findViewById(R.id.btn_next);

            btnNext.setText("Abrir");


            //cada slide abre cada app!!!
            btnNext.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    cargandoDialog();
//                    /** test */
//                    Intent intent = new Intent(v.getContext(), AperturaCuentaMainActivity.class);
//                    startActivity(intent);
//                    finish();
//                    /** test */


                    int current = viewPager.getCurrentItem();
                    Intent intent = new Intent();
                    switch (current) {
                        case 0:
                            intent = new Intent(v.getContext(), AperturaCuentaMainActivity.class);
                            break;
                        case 1:
                            intent = new Intent(v.getContext(), CodigoBarraYQRActivity.class);
                            break;
                        case 2:
                            intent = new Intent(v.getContext(), RecorteDeFirmaActivity.class);
                            break;
                    }

                    startActivity(intent);
                    finish();
                }

            });

            container.addView(view);
            return view;
        }

        @Override
        public int getCount() {
            return about_title_array.length;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    private void cargandoDialog(){
        if (cargandoProgresBar.getVisibility() == View.GONE) {
            cargandoProgresBar.setVisibility(View.VISIBLE);
        } else {
            cargandoProgresBar.setVisibility(View.GONE);
        }
    }

}
