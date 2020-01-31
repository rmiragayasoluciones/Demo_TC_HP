package com.example.demo1;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
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
import com.example.demo1.Fragments.EjemplosFragment;
import com.example.demo1.Fragments.ProductosFragment;
import com.example.demo1.Fragments.QRBarcodeFragment;
import com.example.demo1.Fragments.RecorteFirmaFragment;
import com.example.demo1.Fragments.ViewPagerAdapter;
import com.example.demo1.Task.InitializationPrintTask;
import com.example.demo1.Task.InputStreamVolleyRequest;
import com.example.demo1.Task.JobCompleteReciever;
import com.example.demo1.Task.LoadPrintCapabilitiesTask;
import com.example.demo1.Task.RequestPrintTask;
import com.example.demo1.Task.VolleySingleton;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.Documents;
import com.example.demo1.UserClass.GetDocumentViewModel;
import com.example.demo1.Utils.ImagenManipulation;
import com.example.demo1.Utils.Tools;
import com.github.barteksc.pdfviewer.PDFView;
import com.github.barteksc.pdfviewer.listener.OnLoadCompleteListener;
import com.github.barteksc.pdfviewer.listener.OnPageChangeListener;
import com.github.barteksc.pdfviewer.listener.OnPageErrorListener;
import com.github.barteksc.pdfviewer.scroll.DefaultScrollHandle;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.job.JobletAttributes;
import com.hp.jetadvantage.link.api.printer.PrintAttributesCaps;
import com.hp.jetadvantage.link.api.printer.PrinterService;

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
        EjemplosFragment.EjemplosFragmentListener,
        OnPageChangeListener, OnLoadCompleteListener,
        OnPageErrorListener,
        DetallePdfPreviewDialog.OnImprimirBtnPressListener {

    private static final String TAG = "DocumentPreviewActivity";

    private ConstraintLayout prevPdf;
    private TabLayout tabLayout;
    private ViewPager viewPager;
    private PDFView pdfView;
    private String fileName;
    private Documents documentSing;
    private List<Documents> documentsList = new ArrayList<>();
    private List<Documents> documentsEjemploList = new ArrayList<>();
    private File file;
    final String token = DemoViewModelSingleton.getInstance().getDemoViewModelGuardado().getToken();
    private int tabSelected = 0;

    /* Background task for JetAdvantageLink API initialization */
    private InitializationPrintTask mInitializationTask;
    private PrintAttributesCaps mCapabilities;
    private JobObserver mJobObserver = null;
    private String mJobId = null;
    private String mRid = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_document_preview);
        prevPdf = findViewById(R.id.cargarPdfPrevId);
        tabLayout = findViewById(R.id.tabDocumentsId);
        viewPager = findViewById(R.id.document_view_pager);
        pdfView = findViewById(R.id.pdfPreview);

        new LoadPrintCapabilitiesTask(this).execute();


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
        documentsEjemploList = bundle.getParcelableArrayList("documentosEjemplo");
        Log.d(TAG, "onCreate: documentListTene " + documentsList.size() + " documentos");


        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(), FragmentPagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);


        // todo le agregamos las listas de Documentos
        viewPagerAdapter.addFragment(new DocuFiliaFragment(documentsList), "Filiatorios");
        viewPagerAdapter.addFragment(new ProductosFragment(documentsList), "Productos");
        viewPagerAdapter.addFragment(new QRBarcodeFragment(documentsList), "QR Y Barcode");
        viewPagerAdapter.addFragment(new RecorteFirmaFragment(documentsList), "Recorte de Firma");
        viewPagerAdapter.addFragment(new EjemplosFragment(), "Ejemplos");

        //adapter Setup
        viewPager.setAdapter(viewPagerAdapter);
        tabLayout.setupWithViewPager(viewPager);
        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                Log.d(TAG, "onTabSelected: Tab " + tab.getText());
                Log.d(TAG, "onTabSelected: Position " +  tab.getPosition());

                if (tab.getPosition() != tabSelected){
                    prevPdf.setVisibility(View.VISIBLE);
                }

                tabSelected = tab.getPosition();

            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });

        pdfView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (documentSing != null){
                    Log.d(TAG, "onClick: abrir detalle docu " + documentSing.getId());
                    //todo cargando
                    //todo get info del cdocumento
                    Log.d(TAG, "onclick con la tab " + tabSelected);
                    getDocument(documentSing.getId());
                    //abre dialog con preview del pdf
//                    openDetallePdf(file);
                }
            }
        });

        mJobObserver = new JobObserver(new Handler());

    }

    @Override
    protected void onResume() {
        super.onResume();

        mJobObserver.register(getApplicationContext());

        mInitializationTask = new InitializationPrintTask(this);
        mInitializationTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        // Unregister JobObserver
        mJobObserver.unregister(getApplicationContext());

        mInitializationTask.cancel(true);
        mInitializationTask = null;
    }


    /**
     * Launches Print job
     */
    private void executePrint(String filePath) {
        Toast.makeText(this, "Iniciando Impresión", Toast.LENGTH_SHORT).show();
        mJobId = null;
        mRid = null;
        new RequestPrintTask(this, filePath).execute();
    }

    @Override
    public void onDocuClick(Documents documentSeleccionado) {
        Log.d(TAG, "descargar y mostrar el : " + documentSeleccionado.getId());
        documentSing = documentSeleccionado;
        deleteAllFiles();
        getDocumentFile(documentSeleccionado.getId(),"/Documents/GetDocumentFile/");
        //todo iniciar loading
    }

    public void getDocumentFile(final String id, String urlGetDocument){
        Log.d(TAG, "getDocumentFile: call");
        String preUrl = Tools.getUrlFromConfirg(this);

        String mUrl = preUrl + urlGetDocument + id;
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

        String preUrl = Tools.getUrlFromConfirg(this);

        final JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, preUrl + "/Documents/GetDocument/" + idDocument, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d(TAG, "onResponse: call");
                        Gson gson = new Gson();
                        GetDocumentViewModel documentViewModel = gson.fromJson(response.toString(), GetDocumentViewModel.class);


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
            Log.d(TAG, "openDetallePdf: file es null");
            return;
        }
        DetallePdfPreviewDialog dialog = new DetallePdfPreviewDialog(file, documentViewModel, tabSelected);
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(), "detalle Pdf");

    }

    @Override
    public void loadComplete(int nbPages) {
        Log.d(TAG, "loadComplete: call, pages= " + nbPages);
        prevPdf.setVisibility(View.GONE);
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
        executePrint(pdfFile.getAbsolutePath());

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

    @Override
    public void onEjemploDocuClick(String ejemploDocuIc) {
        deleteAllFiles();
        documentSing = new Documents("id", null, null, null, null);
        getDocumentFile(ejemploDocuIc, "/Documents/GetCoverFile/");
    }


    /**
     * Observer for submitted job
     */
    private class JobObserver extends JobService.AbstractJobletObserver {

        public JobObserver(final Handler handler) {
            super(handler);
        }

        public void onProgress(final String rid, final JobInfo jobInfo) {
            Log.i(TAG, "onProgress: Received rid=" + rid);
            Log.i(TAG, "JobInfo=");
            if (rid.equals(mRid)) {
                if (mJobId == null) {
                    if (jobInfo.getJobId() != null) {
                        mJobId = jobInfo.getJobId();

                        Log.i(TAG, "Received jobId=" + mJobId);
//                        showSnackBar(getString(R.string.job_id, mJobId));

                        SharedPreferences mPrefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                        // Store Job Id in order to verify it in the Broadcast Receiver
                        mPrefs.edit().putString("pref_currentJobId", mJobId).apply();

                        final Intent intent = new Intent(getApplicationContext(), JobCompleteReciever.class);
                        intent.setAction("com.hp.jetadvantage.link.sample.printsample.ACTION_PRINT_COMPLETED");
                        intent.putExtra(JobCompleteReciever.RID_EXTRA, rid);
                        intent.putExtra(JobCompleteReciever.JOB_ID_EXTRA, mJobId);

                        final boolean showProgress = true;

                        // Monitor the job completion
                        final JobletAttributes taskAttributes =
                                new JobletAttributes.Builder().setShowUi(showProgress).build();

                        final String jrid = JobService.monitorJobInForeground(DocumentPreviewActivity.this, mJobId,
                                taskAttributes, intent);

                        Log.i(TAG, "MonitorJob request: " + jrid);

                    }
                }
            }
        }

        @Override
        public void onComplete(final String rid, final JobInfo jobInfo) {
            Log.i(TAG, "onComplete: Received rid=" + rid);
            Log.i(TAG, "JobInfo=" );
            if (jobInfo.getJobType() == JobInfo.JobType.PRINT) {
                Toast.makeText(DocumentPreviewActivity.this, "Impresión Finalizada", Toast.LENGTH_SHORT).show();
//                showSnackBar(getString(R.string.job_completed, jobInfo.getJobName()));
            }
        }

        @Override
        public void onFail(final String rid, final Result result) {
            Log.e(TAG, "onFail: Received rid=" + rid);
//            showResult(getString(R.string.job_failed), result);
        }

        @Override
        public void onCancel(final String rid) {
            Log.i(TAG, "onCancel: Received rid=" + rid);
//            showSnackBar(getString(R.string.job_cancelled));
        }
    }

    public PrintAttributesCaps requestCaps(final Context context, Result result) {
        if (result == null) {
            result = new Result();
        }

        // cache capabilities for building PrintAttributes
        mCapabilities = PrinterService.getCapabilities(context, result);


        return mCapabilities;
    }


    public void showResult(final String msg) {
        showResult(msg, null);
    }

    public void setRid(String rid) {
        this.mRid = rid;
    }



    public void showResult(final String msg, final Result result) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                String resultMsg;
                resultMsg = msg;
                if (result == null) {
                    Log.i(TAG, resultMsg);
                } else if (result.getCode() == Result.RESULT_OK) {
                    resultMsg += "\nCode: RESULT_OK";
                    Log.i(TAG, resultMsg);
                } else if (result.getCode() == Result.RESULT_FAIL) {
                    resultMsg += "\nCode: RESULT_FAIL" + "\n"
                            + "ErrorCode: " + result.getErrorCode() + "\n"
                            + "Cause: " + result.getCause();
                    Log.e(TAG, resultMsg);
                }
                Toast.makeText(getApplicationContext(), resultMsg, Toast.LENGTH_SHORT).show();
            }
        });
    }

}
