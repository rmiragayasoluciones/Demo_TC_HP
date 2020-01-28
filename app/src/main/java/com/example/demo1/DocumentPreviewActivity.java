package com.example.demo1;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HurlStack;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.demo1.Dialogs.DetallePdfPreviewDialog;
import com.example.demo1.Fragments.DocuFiliaFragment;
import com.example.demo1.Fragments.ProductosFragment;
import com.example.demo1.Fragments.QRBarcodeFragment;
import com.example.demo1.Fragments.RecorteFirmaFragment;
import com.example.demo1.Fragments.ViewPagerAdapter;
import com.example.demo1.Task.InputStreamVolleyRequest;
import com.example.demo1.Task.VolleySingleton;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.Documents;
import com.example.demo1.UserClass.GetDocumentViewModel;
import com.example.demo1.Utils.ImagenManipulation;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import org.json.JSONObject;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class DocumentPreviewActivity extends AppCompatActivity implements DocuFiliaFragment.DocuFiliaFragmentListener,
        ProductosFragment.ProductFragmentListener,
        QRBarcodeFragment.QRBarcodeFragmentListener,
        RecorteFirmaFragment.RecorteFragmentListener,
        OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener,
        DetallePdfPreviewDialog.OnImprimirBtnPressListener {

    private static final String TAG = "DocumentPreviewActivity";

    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PDFView pdfView;
    private String fileName;
    private Documents documentSing;
    private List<Documents> documentsList = new ArrayList<>();
    private File file;
    final String token = DemoViewModelSingleton.getInstance().getDemoViewModelGuardado().getToken();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_preview);
        tabLayout = findViewById(R.id.tabDocumentsId);
        viewPager = findViewById(R.id.document_view_pager);
        pdfView = findViewById(R.id.pdfPreview);


        //carga imagen y nombre de empresa
        DemoViewModelSingleton demoViewModelSingleton = DemoViewModelSingleton.getInstance();
        String nombreEmpresa = demoViewModelSingleton.getDemoViewModelGuardado().getClient();
        String logoEnString = demoViewModelSingleton.getDemoViewModelGuardado().getLogo();

        if (nombreEmpresa!=null && !nombreEmpresa.isEmpty()){
            TextView nombreEmpresaTextView = findViewById(R.id.nombreMarcaEmpresaQRYBarcodeId);
            nombreEmpresaTextView.setText(nombreEmpresa);
        }
        if (logoEnString!= null && !logoEnString.isEmpty()){
            Log.d(TAG, "logoenstring "+ logoEnString);
            ImageView logoImageView = findViewById(R.id.logoHPOEmpresaId);
            Bitmap logo = ImagenManipulation.loadImage(logoEnString);
            if (logo != null){
                logoImageView.setImageBitmap(ImagenManipulation.resize(logo, 70, 70));
            }

        }

        Bundle bundle = getIntent().getExtras();
        documentsList = bundle.getParcelableArrayList("listaDocumentos");


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


        // todo le agregamos las listas de Documentos
        viewPagerAdapter.addFragment(new DocuFiliaFragment(documentsList), "Filiatorios");
        viewPagerAdapter.addFragment(new ProductosFragment(documentsList), "Productos");
        viewPagerAdapter.addFragment(new QRBarcodeFragment(documentsList), "QR Y Barcode");
        viewPagerAdapter.addFragment(new RecorteFirmaFragment(documentsList), "Recorte de Firma");

        //adapter Setup
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);

        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentSing != null){
                    Log.d(TAG, "onClick: abrir detalle docu " + documentSing.getId());
                    //todo cargando
                    //todo get info del cdocumento
                    getDocument(documentSing.getId());
                    //abre dialog con preview del pdf
//                    openDetallePdf(file);
                }
            }
        });

    }


    @Override
    public void onDocuClick(Documents documentSeleccionado) {
        Log.d(TAG, "descargar y mostrar el : " + documentSeleccionado.getId());
        documentSing = documentSeleccionado;
        getDocumentFile(documentSeleccionado.getId());
        //todo iniciar loading
    }

    public void getDocumentFile(final String id){
        Log.d(TAG, "getDocumentFile: call");
        String mUrl = "http://10.13.0.34:5656/api/Documents/GetDocumentFile/" + id;
        InputStreamVolleyRequest request = new InputStreamVolleyRequest(Request.Method.GET, mUrl, new Response.Listener<byte[]>() {
            @Override
            public void onResponse(byte[] response) {
                Log.d(TAG, "onResponse: call");
                Log.d(TAG, "response.toString(): " + response.toString());
                fileName = id + ".pdf";

                FileOutputStream outputStream;
                try {
                    outputStream = openFileOutput(fileName, Context.MODE_PRIVATE);
                    outputStream.write(response);
                    outputStream.close();
                } catch (FileNotFoundException e){
                    Log.d(TAG, "FileNotFoundException: call");
                } catch (IOException ioe){
                    Log.d(TAG, "IOException: call");
                }


                file = getFileStreamPath(fileName);
                Log.d(TAG, "onClick, filename " + file.getName() + " \nfile.getAbsolutePath() " + file.getAbsolutePath() );

                pdfView.fromFile(file)
                        .onPageChange(DocumentPreviewActivity.this)
                        .swipeHorizontal(true)
                        .enableAnnotationRendering(true)
                        .onLoad(DocumentPreviewActivity.this)
                        .scrollHandle(new DefaultScrollHandle(DocumentPreviewActivity.this))
                        .spacing(10) // in dp
                        .onPageError(DocumentPreviewActivity.this)
                        .load();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: call");
                error.printStackTrace();
            }
        }, null){
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                Log.d(TAG, "Headers Agrego Token: homedepotdemo ");
                params.put("Token", token);
                return params;
            }
        };

        RequestQueue mRequestQueue = Volley.newRequestQueue(getApplicationContext(), new HurlStack());
        mRequestQueue.add(request);
    }


    private void getDocument(String idDocument){

        RequestQueue queue = VolleySingleton.getInstance(this).getmRequestQueue();

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, "http://10.13.0.34:5656/api/Documents/GetDocument/" + idDocument, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: call");
                        Gson gson = new Gson();
                        GetDocumentViewModel documentViewModel = gson.fromJson(response.toString(), GetDocumentViewModel.class);
//                        Log.d(TAG, "getClient " + documentViewModel.getClient());
//                        Log.d(TAG, "getDemoId " + documentViewModel.getDemoId());
//                        Log.d(TAG, "getFilePath " + documentViewModel.getFilePath());
//                        Log.d(TAG, "getEmail " + documentViewModel.getMetadataViewModel().getEmail());
//                        Log.d(TAG, "getBusinessName " + documentViewModel.getMetadataViewModel().getBusinessName());
//                        Log.d(TAG, "getCountry " + documentViewModel.getMetadataViewModel().getCountry());
//                        Log.d(TAG, "getCode " + documentViewModel.getMetadataViewModel().getCode());
//                        Log.d(TAG, "getFecha " + documentViewModel.getMetadataViewModel().getFecha());
//                        Log.d(TAG, "getReason " + documentViewModel.getMetadataViewModel().getReason());

                        openDetallePdf(file, documentViewModel);

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d(TAG, "onErrorResponse: call");

            }
        }){

            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Token", token);
                return params;
            }
        };

        queue.add(request);

    }

    private void openDetallePdf(File file, GetDocumentViewModel documentViewModel){
        if (file ==null){
            return;
        }
        DetallePdfPreviewDialog dialog = new DetallePdfPreviewDialog(file, documentViewModel);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(), "detalle Pdf");

    }

    @Override
    public void loadComplete(int nbPages) {
        Log.d(TAG, "loadComplete: call, pages= " + nbPages);
        //todo cortar loading
    }

    @Override
    public void onPageChanged(int page, int pageCount) {
        Log.d(TAG, "onPageChanged: call");
    }

    @Override
    public void onPageError(int page, Throwable t) {
        Log.d(TAG, "onPageError: call");
        t.fillInStackTrace();
    }

    @Override
    public void onImprimirBtnPress(File pdfFile) {
        Log.d(TAG, "onImprimirBtnPress: IMPRIMIR CALL");
        Log.d(TAG, "pdf " + pdfFile.getName());
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(this, AppSelectionActivity.class);
        startActivity(intent);
        finish();
        Log.d(TAG, "onBackPressed: ahora Kill the activity");
    }

    private void deleteAllFiles() {
        Log.d(TAG, "**************************");
        File folder2 = new File(this.getFilesDir().getAbsolutePath());
        String[] children = folder2.list();
        Log.d(TAG, "children.length: " + children.length);
        Log.d(TAG, "folder2.getFreeSpace(): " + folder2.getFreeSpace());
        deleteRecursive(folder2);
        folder2 = new File(this.getFilesDir().getAbsolutePath());
        Log.d(TAG, "children.length: " + children.length);
        Log.d(TAG, "folder2.getFreeSpace(): " + folder2.getFreeSpace());
        Log.d(TAG, "**************************");
    }

    public void deleteRecursive(File fileOrDirectory) {

        if (fileOrDirectory.isDirectory()) {
            for (File child : fileOrDirectory.listFiles()) {
                deleteRecursive(child);
            }
        }
        Log.d(TAG, "fileOrDirectory " + fileOrDirectory.getAbsolutePath() + " will be delete");
        fileOrDirectory.delete();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        deleteAllFiles();
    }

    private Bitmap loadImage(String logoEnString){

        byte[] decodeString = Base64.decode(logoEnString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString, 0 , decodeString.length);

        return  decodedByte;
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
}
