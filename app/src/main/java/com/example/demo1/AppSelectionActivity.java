package com.example.demo1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.os.Bundle;
import android.os.Parcelable;
import android.util.Log;
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
import com.android.volley.AuthFailureError;
import com.android.volley.DefaultRetryPolicy;
import com.android.volley.NoConnectionError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.demo1.Dialogs.VolleyErrorResponseDialog;
import com.example.demo1.Task.VolleySingleton;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.Documents;
import com.example.demo1.Utils.Tools;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class AppSelectionActivity extends AppCompatActivity implements VolleyErrorResponseDialog.IntentarReconectListener {

    private static final String TAG = "AppSelectionActivity";

    private static final int MAX_STEP = 4;
    private ViewPager viewPager;
    private MyViewPagerAdapter myViewPagerAdapter;
    private View cargandoProgresBar;
    private List<Documents> documentsList = new ArrayList<>();
    private List<Documents> documentsEjemplosList = new ArrayList<>();

    private String about_title_array[] = {
            "Apertura de Cuenta",
            "Clasificación de Documentos",
            "Captura de Firma",
            "Impresión de Documentos"
    };
    private String about_description_array[] = {
            "Cargue la documentación de sus clientes de forma rápida y sencilla.",
            "Direccione sus documentos de forma dinámica mediante la lectura de códigos de Barra/QR.",
            "Recorte la firma de sus clientes plasmadas en formularios de manera dinámica.",
            "Accede a la cuenta e imprime la documentación"

    };
    private String about_images_array[] = {
            "abrir_cuenta.json",
            "qr.json",
            "firma.json",
            "print.json"
    };


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_selection);


        if (getIntent().getBooleanExtra("EXIT", false)) {
            finish();
        }


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

    @Override
    public void reconectarYsubirArchivo() {
        // es necesario hacer algo mas?
        Log.d(TAG, "reconectarYsubirArchivo: call");
    }

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
                    Intent intent;
                    switch (current) {
                        case 0:
                            intent = new Intent(v.getContext(), AperturaCuentaMainActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 1:
                            intent = new Intent(v.getContext(), CodigoBarraYQRActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 2:
                            intent = new Intent(v.getContext(), RecorteDeFirmaActivity.class);
                            startActivity(intent);
                            finish();
                            break;
                        case 3:
                            //carga documentos para luego mandarlos a Preview Activity
                            getListaDocumentos();
                            break;
                    }

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

    private void getListaDocumentos(){
        Log.d(TAG, "getListaDocumentos: CALL");

        final String token = DemoViewModelSingleton.getInstance().getDemoViewModelGuardado().getToken();

        final RequestQueue queue = VolleySingleton.getInstance(this).getmRequestQueue();
        String preUrl = Tools.getUrlFromConfirg(this);

        final JsonArrayRequest request = new JsonArrayRequest(Request.Method.GET, preUrl + "/Documents/GetDocuments/" + token, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, "onResponse: call");
                        try {

                            for (int i = 0; i< response.length();i++){
                                JSONObject documento = response.getJSONObject(i);

                                createAndAddToDocumentList(documento.getString("id")
                                        ,documento.getString("serieName")
                                        ,documento.getString("demoId")
                                        ,documento.getString("filePath")
                                        ,documento.getString("client"));

                            }
                            documentsList = sortArraylist(documentsList);

                            //guarda la lista de documentos
                            //TODO MANDAR A DESCARGAR LOS DOCUMENTOS DE EJEMPLO Y DESPUES LLAMAR STARTDOCUMENTS  startDocumentosActivity();
                            getDocumentosEjemplo();

                        } catch (JSONException e){
                            e.printStackTrace();
                        }

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: call");

                if (error instanceof TimeoutError || error instanceof NoConnectionError) {
                    Log.d(TAG, "TimeOut error o No ConnectionError");
                    errorVolley(getString(R.string.error_conexion));
                    return;
                }
                error.printStackTrace();
            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Token", token);
                return params;
            }
        };

        //setea el timeout y los retry
        request.setRetryPolicy(new DefaultRetryPolicy(
                5000,
                DefaultRetryPolicy.DEFAULT_MAX_RETRIES,
                DefaultRetryPolicy.DEFAULT_BACKOFF_MULT));
        queue.add(request);
    }

    private void getDocumentosEjemplo(){
        //todo cuando este el metodo, aca bajar los archivos ejemplo y cargarlos a "documentsEjemplosList" y después llamar:
        startDocumentosActivity();
    }

    private void createAndAddToDocumentList(String id, String serieName, String demoId, String filePath, String client){
        Documents d = new Documents(id,serieName, demoId, filePath, client);
        documentsList.add(d);
    }

    private void startDocumentosActivity(){
        Log.d(TAG, "startDocumentosActivity: call");
        Intent intent = new Intent(this, DocumentPreviewActivity.class);
        Bundle bundle = new Bundle();
        bundle.putParcelableArrayList("listaDocumentos", (ArrayList<? extends Parcelable>) this.documentsList);
        bundle.putParcelableArrayList("documentosEjemplo", (ArrayList<? extends Parcelable>) this.documentsEjemplosList);
        intent.putExtras(bundle);
        startActivity(intent);
        finish();


    }

    private List<Documents> sortArraylist(List<Documents> arrayList){

        Collections.sort(arrayList, new Comparator<Documents>() {
            @Override
            public int compare(Documents o1, Documents o2) {
                return extractInt(o1.getDemoId()) - extractInt(o2.getDemoId());
            }
        });

        Collections.reverse(arrayList);

        if (arrayList.size()>100){
            Log.d(TAG, "Array mayor a 100, tiene " + arrayList.size() + " documentos");
            arrayList = limitarLista(arrayList);
        }


        return arrayList;
    }

    private List<Documents> limitarLista(List<Documents> arrayList) {
        ArrayList<Documents> solo100 = new ArrayList();

        for (int i = 0 ; i < 100; i++ ){
            solo100.add(arrayList.get(i));
        }

        return solo100;
    }

    private int extractInt(String s){
        return s.isEmpty() ? 0 : Integer.parseInt(s);
    }

    private void errorVolley(String volleyError){
        cargandoDialog();
        VolleyErrorResponseDialog volleyErrorResponseDialog = new VolleyErrorResponseDialog(volleyError);
        volleyErrorResponseDialog.setCancelable(false);
        volleyErrorResponseDialog.show(getSupportFragmentManager(), "noConfigLoaded");
    }

}
