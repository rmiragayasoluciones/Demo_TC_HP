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
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.airbnb.lottie.LottieAnimationView;

public class AppSelectionActivity extends AppCompatActivity {

    private static final int MAX_STEP = 3;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private long backPressedTime;
    private View cargandoProgresBar;

    private String about_title_array[] = {
            "Apertura de Cuenta",
            "Ruteo de Documentos\nCodigo de Barra / QR",
            "Recorte de Firmas"
    };
    private String about_description_array[] = {
            "Abre una cuenta de forma maravillosa nunca antes vista por el ojo humano. Utiliza documentacion general y del producto",
            "Selecciona y separa documentos de un lote en distintas particiones basándose en el codigo de barra/QR",
            "Se digitaliza un documento para recortar la firma del cliente desde un formulario de firmas"

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
                    int current = viewPager.getCurrentItem();
                    switch (current) {
                        case 0:
//                            Toast.makeText(AppSelectionActivity.this, "Abrir " + current + " App", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(v.getContext(), AperturaCuentaMainActivity.class);
                            startActivity(intent);
                            break;
                        case 1:
                            Toast.makeText(AppSelectionActivity.this, "Abrir " + current + " App", Toast.LENGTH_SHORT).show();
                            break;
                        case 2:
                            Toast.makeText(AppSelectionActivity.this, "Abrir " + current + " App", Toast.LENGTH_SHORT).show();
                            break;

                    }
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

//    @Override
//    public void onBackPressed() {
//        if(backPressedTime + 2000 > System.currentTimeMillis()){
//            super.onBackPressed();
//            return;
//        } else {
//            showCustomeToast();
//        }
//        backPressedTime = System.currentTimeMillis();
//    }

    private void cargandoDialog(){
        if (cargandoProgresBar.getVisibility() == View.GONE) {
            cargandoProgresBar.setVisibility(View.VISIBLE);
        } else {
            cargandoProgresBar.setVisibility(View.GONE);
        }
    }

    public void showCustomeToast(){
        View layout = getLayoutInflater().inflate(R.layout.custom_toast, (ViewGroup) findViewById(R.id.custom_toast_layout_id));
        TextView text = layout.findViewById(R.id.text);
        text.setTextColor(Color.WHITE);
        text.setText("Presione nuevamente para salir");
        CardView lyt_card = layout.findViewById(R.id.lyt_card);
        lyt_card.setCardBackgroundColor(getResources().getColor(R.color.colorPrimary));

        Toast toast = new Toast(getApplicationContext());
        toast.setDuration(Toast.LENGTH_SHORT);
        toast.setView(layout);
        toast.show();
    }
}
