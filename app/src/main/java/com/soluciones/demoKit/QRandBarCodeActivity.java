package com.soluciones.demoKit;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SwitchCompat;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.preference.PreferenceManager;

import com.soluciones.demoKit.Dialogs.DigitalizarQroBarcodeDialog;
import com.soluciones.demoKit.Dialogs.FinalizacionDeTrabajo;
import com.soluciones.demoKit.Dialogs.VolleyErrorResponseDialog;
import com.soluciones.demoKit.Task.CreateDocument;
import com.soluciones.demoKit.Task.InitializationTask;
import com.soluciones.demoKit.Task.JobCompleteReciever;
import com.soluciones.demoKit.Task.ScanToDestinationTask;
import com.soluciones.demoKit.UserClass.CreateDocumentViewModel;
import com.soluciones.demoKit.UserClass.DemoViewModelSingleton;
import com.soluciones.demoKit.UserClass.ScanOptionsSelected;
import com.soluciones.demoKit.UserClass.ScanUserAttriputes;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.job.JobletAttributes;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;
import com.soluciones.demoKit.Utils.Tools;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class QRandBarCodeActivity extends AppCompatActivity implements FinalizacionDeTrabajo.FinalizacionDeTrabajoListener,
                                                                    CreateDocument.OnCreateDocumentsListener,
                                                                DigitalizarQroBarcodeDialog.DigitalizarQroBarcodeDialogListener,
VolleyErrorResponseDialog.IntentarReconectListener{

    private static final String TAG = "ProductoActivity";

    private TextView tituloActivity;

    private ConstraintLayout layout, layoutCardsViewForShowcase;
    CardView jobBuilderCardV, scanPrevieweCardV, paperSizeCardV, removeBlankPagesCardV, duplexCardView;
    private ArrayList<String> blackImageRemovalEntries = new ArrayList<>();
    private ArrayList<String> paperSize = new ArrayList<>();
    private ArrayList<String> jobassemblymode = new ArrayList<>();
    private ArrayList<String> scanpreview = new ArrayList<>();
    private ArrayList<String> duplex = new ArrayList<>();
    private String[] JOBBUILDER;
    private String[] SCANPREVIEW;
    private String[] PAPERSIZE;
    private String[] BLANKPAGES;
    private String[] DUPLEX;

    private Button siguiente;

    private TextView paperSizeSelected;

    private String filePath, fileName, idCliente;

    //    private String job_builder_selected;
    private SwitchCompat jobBuilderSwitch, scanPreviewSwitch, blankPagesSwitch, duplexSwitch;
    //    private String scan_preview_selected;
    private String paper_size_selected = "A4";

    /* Background task for JetAdvantageLink API initialization */
    private InitializationTask mInitializationTask;
    private JobObserver mJobObserver = null;
    private String mJobId = null;
    private String mRid = null;

    /* View cover */
    private View coverView;
    private ConstraintLayout progressBar;

    /* Es QR o BarCode */
    private String qrOBarcode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docu_filiatorios2);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);

        //Seleccionaron QR o Barcode
        qrOBarcode = getIntent().getStringExtra("TIPO");
        Log.d(TAG, "usuario seleccionó: " + qrOBarcode);

        openDialogs();

        tituloActivity = findViewById(R.id.textView13);
        setTitulo();

        blankPagesSwitch = findViewById(R.id.blankPagesId);
        jobBuilderSwitch = findViewById(R.id.jobBuilderSwitch);
        scanPreviewSwitch = findViewById(R.id.scanPreviewSwitch);
        duplexSwitch = findViewById(R.id.duplexSwitchId);


        cargarOpcionesaBotones();

        coverView = findViewById(R.id.viewcoverid);
        progressBar = findViewById(R.id.llProgressBar);

        /* JobObserver */
        mJobObserver = new JobObserver(new Handler());

        layout = findViewById(R.id.layoutforSnack);
        layoutCardsViewForShowcase = findViewById(R.id.groupConstraintLayoutId);

        paperSizeCardV = findViewById(R.id.paperSize);
        jobBuilderCardV = findViewById(R.id.jobBuilder);
        jobBuilderCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                jobBuilderSwitch.performClick();
            }
        });
        scanPrevieweCardV = findViewById(R.id.scanPreview);
        scanPrevieweCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                scanPreviewSwitch.performClick();
            }
        });
        removeBlankPagesCardV = findViewById(R.id.removeBlankPages);
        removeBlankPagesCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                blankPagesSwitch.performClick();
            }
        });
        siguiente = findViewById(R.id.siguienteBtnId);
        paperSizeSelected = findViewById(R.id.paperSelectedId);
        duplexCardView = findViewById(R.id.duplexCardV);
        duplexCardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                duplexSwitch.performClick();
            }
        });

        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (idCliente == null){
                    openDialogs();
                }else {
                    saveOptionsSelected();
                    scanToDestination("qr-bardoce");
                    Tools.showSnackbar(coverView);
                }


            }
        });

        paperSizeCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPaperSizeDialog();

            }
        });

        //test this
        paperSizeCardV.setVisibility(View.GONE);
    }

    private void openDialogs() {
        DigitalizarQroBarcodeDialog dialog;
        if (qrOBarcode.equalsIgnoreCase("QR")){
            dialog = new DigitalizarQroBarcodeDialog(true);
        } else {
            dialog = new DigitalizarQroBarcodeDialog(false);
        }
        dialog.setCancelable(true);
        dialog.show(getSupportFragmentManager(), "digitalizar Qr o Barcode");
    }

    private void setTitulo() {
        switch (qrOBarcode){
            case "QR":
                tituloActivity.setText("QR");
                break;
            case "Barcode":
                tituloActivity.setText("Código de Barras");
                break;
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        // Register JobObserver
        mJobObserver.register(this);

        mInitializationTask = new InitializationTask(this);
        mInitializationTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();
        // Unregister JobObserver
        mJobObserver.unregister(this);

        mInitializationTask.cancel(true);
        mInitializationTask = null;
    }

    /**
     * Prepares {@link com.hp.jetadvantage.link.api.scanner.ScanAttributes} and submits scan job.
     */
    private void scanToDestination(String filename) {
        mJobId = null;
        mRid = null;
        desButton();
        new ScanToDestinationTask(this, filename).execute();
        Toast.makeText(this, "Iniciando escaneo", Toast.LENGTH_SHORT).show();
    }

    public void desButton() {
        coverView.setVisibility(View.VISIBLE);
        coverView.setClickable(true);
        coverView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(v.getContext(), "Escaneo en curso", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showPaperSizeDialog() {
        //get prev selecction
        int checkedItem = paperSize.indexOf(paper_size_selected);

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Tamaño de hoja");
        builder.setSingleChoiceItems(PAPERSIZE, checkedItem, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                paper_size_selected = PAPERSIZE[i];
            }
        });
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Snackbar.make(findViewById(android.R.id.content), paper_size_selected + " seleccionado", Snackbar.LENGTH_SHORT).show();
                paperSizeSelected.setText(paper_size_selected);
            }
        });
        builder.show();
    }

    private void saveOptionsSelected() {
        Log.d(TAG, "paperSize: " + paper_size_selected);
        Log.d(TAG, "blankPages isCheck: " + blankPagesSwitch.isChecked());
        Log.d(TAG, "scanPreview isCheck: " + scanPreviewSwitch.isChecked());
        Log.d(TAG, "JobBuilder isCheck: " + jobBuilderSwitch.isChecked());
        Log.d(TAG, "Dulpex usCheck: " + duplexSwitch.isChecked());

        String blank_pages_selected;
        if (blankPagesSwitch.isChecked()){
            blank_pages_selected = BLANKPAGES[1];
            Log.d(TAG, "blank_pages_selected: " + blank_pages_selected);
        } else {
            blank_pages_selected = BLANKPAGES[2];
            Log.d(TAG, "blank_pages_selected: " + blank_pages_selected);
        }

        String scan_preview_selected;
        if (scanPreviewSwitch.isChecked()){
            scan_preview_selected = SCANPREVIEW[2];
            Log.d(TAG, "scan_preview_selected: " + scan_preview_selected);
        } else {
            scan_preview_selected = SCANPREVIEW[1];
            Log.d(TAG, "scan_preview_selected: " + scan_preview_selected);
        }

        String job_builder_selected;
        if (jobBuilderSwitch.isChecked()){
            job_builder_selected = JOBBUILDER[2];
            Log.d(TAG, "job_builder_selected: " + job_builder_selected);
        } else {
            job_builder_selected = JOBBUILDER[1];
            Log.d(TAG, "job_builder_selected: " + job_builder_selected);
        }

        String duplex_selected;
        if (duplexSwitch.isChecked()){
            duplex_selected = DUPLEX[2];
            Log.d(TAG, "duplex_selected: " + duplex_selected);
        } else {
            duplex_selected = DUPLEX[1];
            Log.d(TAG, "duplex_selected: " + duplex_selected);
        }


        ScanOptionsSelected scanOptionsSelected = ScanOptionsSelected.getInstance();
        scanOptionsSelected.setPaperSize(paper_size_selected);

        scanOptionsSelected.setBlankPagesSelected(blank_pages_selected);
        scanOptionsSelected.setScanPreviewSelected(scan_preview_selected);
        scanOptionsSelected.setJobBuilderSelected(job_builder_selected);
        scanOptionsSelected.setDuplexSelected(duplex_selected);

    }

    private void cargarOpcionesaBotones() {

        ScanAttributesCaps mCapabilities = ScanUserAttriputes.getInstance().getCaps();
        Object[] objArr;

        for (ScanAttributes.BlankImageRemovalMode blankImageRemovalMode : mCapabilities.getBlankImageRemovalModeList()) {
            blackImageRemovalEntries.add(blankImageRemovalMode.name()); //name
        }

        BLANKPAGES = new String[blackImageRemovalEntries.size()];

        objArr = blackImageRemovalEntries.toArray();

        int i = 0;
        for (Object obj : objArr) {
            BLANKPAGES[i++] = (String) obj;
        }

        //**********************************

        for (ScanAttributes.ScanSize os : mCapabilities.getScanSizeList()) {
            paperSize.add(os.name());
        }

        PAPERSIZE = new String[paperSize.size()];

        objArr = paperSize.toArray();

        int x = 0;
        for (Object obj : objArr) {
            PAPERSIZE[x++] = (String) obj;
        }

        //**********************************

        for (ScanAttributes.JobAssemblyMode jobAssemblyMode : mCapabilities.getJobAssemblyModeList()) {
            jobassemblymode.add(jobAssemblyMode.name());
        }

        JOBBUILDER = new String[jobassemblymode.size()];

        objArr = jobassemblymode.toArray();

        int z = 0;
        for (Object obj : objArr) {
            JOBBUILDER[z++] = (String) obj;
        }

        //**********************************

        for (ScanAttributes.ScanPreview scanPreview : mCapabilities.getScanPreviewList()) {
            scanpreview.add(scanPreview.name());
        }

        SCANPREVIEW = new String[scanpreview.size()];

        objArr = scanpreview.toArray();

        int w = 0;
        for (Object obj : objArr) {
            SCANPREVIEW[w++] = (String) obj;
        }

        //**********************************

        for (ScanAttributes.Duplex duplex : mCapabilities.getDuplexList()) {
            this.duplex.add(duplex.name());
        }

        DUPLEX = new String[this.duplex.size()];

        objArr = this.duplex.toArray();

        int d = 0;
        for (Object obj : objArr) {
            DUPLEX[d++] = (String) obj;
        }

        //**********************************

        jobBuilderSwitch.setChecked(false);
        scanPreviewSwitch.setChecked(false);
        blankPagesSwitch.setChecked(false);
        duplexSwitch.setChecked(false);

    }

    /**
     * CallBack de FinalizacionTrabajo Dialog
     * */
    @Override
    public void realizarOtroTrabajo(boolean siOno) {
        if (siOno){
            finish();
        } else {
            closeApp();
        }
    }

    private void closeApp() {
        Intent intent = new Intent(getApplicationContext(), CodigoBarraYQRActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    @Override
    public void finish() {
        super.finish();
        switch (qrOBarcode){
            case "QR":
                overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                break;
            case "BARCODE":
                overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
                break;
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        deleteAllFiles();
        finish();
    }

    /**
     * Calback de QR o Barcode Dialog
     * */
    @Override
    public void onDigitalizarQroBarcodeDialog(String idCliente) {
        //todo guardar el ID cliente
        Log.d(TAG, "onDigitalizarQroBarcodeDialog: idCliente " + idCliente);
        this.idCliente = idCliente;
//        showcaseEjemplo();
    }

    @Override
    public void onCreateDocumentComplete() {
        cartelSubirALaNube();
        cartelFinalizacionTRabajo();
    }

    @Override
    public void onCreateDocumentError(String volleyError) {
        //todo: aca cortar toddo y meter cartel de error con la info de NetworkResponse
        cartelSubirALaNube();
        VolleyErrorResponseDialog volleyErrorResponseDialog = new VolleyErrorResponseDialog(volleyError);
        volleyErrorResponseDialog.show(getSupportFragmentManager(), "noConfigLoaded");
    }

    public void cartelFinalizacionTRabajo(){
        FinalizacionDeTrabajo finalizacionDeTrabajo = new FinalizacionDeTrabajo("La documentación fue subida correctamente");
        finalizacionDeTrabajo.setCancelable(false);
        finalizacionDeTrabajo.show(getSupportFragmentManager(), "finalizacion fialog");
    }

    @Override
    public void reconectarYsubirArchivo() {
        //todo volver a la pantalla pincipal
        menuPrincipal();
    }


    private class JobObserver extends JobService.AbstractJobletObserver {
        private static final String TAG = "JobObserver";


        public JobObserver(final Handler handler) {
            super(handler);
        }

        @Override
        public void onProgress(String rid, JobInfo jobInfo) {
            Log.d(TAG, "Received onProgress for rid " + rid);
            Log.d(TAG, "Received onProgress jobInfo " + jobInfo);

            if (mJobId == null) {
                if (jobInfo.getJobId() != null) {
                    mJobId = jobInfo.getJobId();

                    Log.d(TAG, "Received jobID as " + mJobId);

                    final SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
                    final boolean monitorJob = true;

                    if (monitorJob) {
                        prefs.edit().putString("pref_currentJobId", mJobId).apply();

                        final boolean showProgress = prefs.getBoolean("pref_showJobProgress", true);

                        // Monitor the job completion
                        final JobletAttributes taskAttributes = new JobletAttributes.Builder().setShowUi(showProgress).build();

                        final Intent intent = new Intent(getApplicationContext(), JobCompleteReciever.class);
                        intent.setAction("com.hp.jetadvantage.link.sample.scansample.ACTION_SCAN_COMPLETED");
                        intent.putExtra(JobCompleteReciever.RID_EXTRA, rid);
                        intent.putExtra(JobCompleteReciever.JOB_ID_EXTRA, mJobId);

                        final String jrid = JobService.monitorJobInForeground(QRandBarCodeActivity.this, mJobId, taskAttributes, intent);

                        Log.d(TAG, "MonitorJob request: " + jrid);
                    }
                }
            }
        }

        @Override
        public void onComplete(String rid, JobInfo jobInfo) {
            Log.d(TAG, "onComplete");
            Log.d(TAG, "Received onComplete for rid " + rid);
            Log.d(TAG, "Received onComplete jobInfo " + jobInfo);

            ScanJobData scanJobData = jobInfo.getJobData();
            final List<String> images = scanJobData.getFileNames();

            if (images != null && images.size() > 0) {
                Log.d(TAG, "Images: " + Arrays.toString(images.toArray()));
            }


            /** NUEVO*/
            String ruta = scanJobData.getFileNames().get(0);
            Log.d(TAG, "onComplete Ruta: " + ruta);
            String splitBy = "/" + jobInfo.getJobId() + "/";
            String file = ruta.split(splitBy)[1];
            Log.d(TAG, "onComplete file: " + file);

            //guarda la ruta del archivo
            fileName = file;
            filePath = ruta;

            /** NUEVO*/

            // terminado el scan llama al metodo para cambiar el dialog
            onScannResponse();

        }

        @Override
        public void onFail(String s, Result result) {
            Log.d(TAG, "onFail: CALL");
            Toast.makeText(QRandBarCodeActivity.this, "Espere unos segundos", Toast.LENGTH_SHORT).show();
            Log.d(TAG, "result.getCause()=" + result.getCause());
            Log.d(TAG, "result.getErrorCode()= " + result.getErrorCode());
            Log.d(TAG, "result.toString()= " + result.toString());
            Log.d(TAG, "result.getCode()= " + result.getCode());
            Log.d(TAG, "s= " + s);

            //borra todos los elementos dela carpeta
//            deleteAllFiles();
            // vuelve a llamar al metodo de scaneo
//            scanProgresDialogEnds();
//            scanToDestination();
            // deshabilita los botones
//            desButton();
        }

        @Override
        public void onCancel(String s) {
            Log.d(TAG, "onCancel: CALL");
            Toast.makeText(QRandBarCodeActivity.this, "Escaneo Cancelado", Toast.LENGTH_SHORT).show();
            deleteAllFiles();
            restartActivity();

        }
    }

    private void onScannResponse() {
        Log.d(TAG, "onScannResponse: va a subir el archivo");
        cartelSubirALaNube();
        subirArchivo(filePath);
    }

    private void subirArchivo(String filePath) {
        Log.d(TAG, "******************");
        Log.d(TAG, "filePath: " + filePath);
        Log.d(TAG, "fileName: " + fileName);
        Log.d(TAG, "******************");
        //todo pasar el objeto a string
        new CreateDocument(this,filePath, fileName, convertJsonBojectToString()).execute();
    }

    private String convertJsonBojectToString() {

        DemoViewModelSingleton demoViewModelSingleton = DemoViewModelSingleton.getInstance();
        demoViewModelSingleton.borrarMetadataCliente();
        int demoId = demoViewModelSingleton.getDemoViewModelGuardado().getId();

        Log.d(TAG, "idClient " + this.idCliente);

        String newDocumentName = fileName.split("-001")[0];
        Log.d(TAG, "document viene " + fileName + " y lo paso a " + newDocumentName);
        demoViewModelSingleton.getMetadataCliente().setDocumentName(newDocumentName);

        CreateDocumentViewModel createDocumentViewModel = new CreateDocumentViewModel(qrOBarcode, demoId, this.idCliente, demoViewModelSingleton.getMetadataCliente());

        Gson gson = new Gson();

        return gson.toJson(createDocumentViewModel);

    }

    public void cartelSubirALaNube() {
        Log.d(TAG, "progressDialodNubeUploadVISIBLE: CALL");

        if (progressBar.getVisibility() == View.INVISIBLE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            });
        } else if (progressBar.getVisibility() == View.VISIBLE){
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressBar.setVisibility(View.INVISIBLE);
                }
            });
        }
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

    private void restartActivity() {
        Intent i = getIntent();
        finish();
        startActivity(i);
    }

    private void menuPrincipal(){
        Intent intent = new Intent(getApplicationContext(), AppSelectionActivity.class);
        startActivity(intent);
        finish();
    }
}
