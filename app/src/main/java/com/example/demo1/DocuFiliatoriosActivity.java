package com.example.demo1;

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

import com.example.demo1.Dialogs.DigitalizarDocuFiliatoriosDialog;
import com.example.demo1.Dialogs.FinalizacionDeTrabajo;
import com.example.demo1.Task.CreateDocument;
import com.example.demo1.Task.InitializationTask;
import com.example.demo1.Task.JobCompleteReciever;
import com.example.demo1.Task.ScanToDestinationTask;
import com.example.demo1.UserClass.CreateDocumentViewModel;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.ScanOptionsSelected;
import com.example.demo1.UserClass.ScanUserAttriputes;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.job.JobInfo;
import com.hp.jetadvantage.link.api.job.JobService;
import com.hp.jetadvantage.link.api.job.JobletAttributes;
import com.hp.jetadvantage.link.api.job.ScanJobData;
import com.hp.jetadvantage.link.api.scanner.ScanAttributes;
import com.hp.jetadvantage.link.api.scanner.ScanAttributesCaps;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class DocuFiliatoriosActivity extends AppCompatActivity implements FinalizacionDeTrabajo.FinalizacionDeTrabajoListener,
                                                                    CreateDocument.OnCreateDocumentsListener {
    private static final String TAG = "DocuFiliatoriosActivity";

    private ConstraintLayout layout;
    CardView jobBuilderCardV, scanPrevieweCardV, paperSizeCardV, removeBlankPagesCardV;
    private ArrayList<String> blackImageRemovalEntries = new ArrayList<>();
    private ArrayList<String> paperSize = new ArrayList<>();
    private ArrayList<String> jobassemblymode = new ArrayList<>();
    private ArrayList<String> scanpreview = new ArrayList<>();
    private String[] JOBBUILDER;
    private String[] SCANPREVIEW;
    private String[] PAPERSIZE;
    private String[] BLANKPAGES;
    private Button siguiente;
    private DigitalizarDocuFiliatoriosDialog dialog;
    private int dialogNumero = 0;

    private TextView paperSizeSelected;

    private List<String> listFilesPath = new ArrayList<>();
    private List<String> listFilesNames = new ArrayList<>();

//    private String job_builder_selected;
    private SwitchCompat jobBuilderSwitch, scanPreviewSwitch, blankPagesSwitch;
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

    /* ScanCount */
    private int scanCount = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_docu_filiatorios2);

        blankPagesSwitch = findViewById(R.id.blankPagesId);
        jobBuilderSwitch = findViewById(R.id.jobBuilderSwitch);
        scanPreviewSwitch = findViewById(R.id.scanPreviewSwitch);

        cargarOpcionesaBotones();

        coverView = findViewById(R.id.viewcoverid);
        progressBar = findViewById(R.id.llProgressBar);

        /* JobObserver */
        mJobObserver = new JobObserver(new Handler());

        layout = findViewById(R.id.layoutforSnack);

        jobBuilderCardV = findViewById(R.id.jobBuilder);
        scanPrevieweCardV = findViewById(R.id.scanPreview);
        paperSizeCardV = findViewById(R.id.paperSize);
        removeBlankPagesCardV = findViewById(R.id.removeBlankPages);
        siguiente = findViewById(R.id.siguienteBtnId);
        paperSizeSelected = findViewById(R.id.paperSelectedId);


        siguiente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveOptionsSelected();
                openDialogDigitalizacion(dialogNumero);
            }
        });


//        jobBuilderCardV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DocuFiliatoriosActivity.this, "Job Builder", Toast.LENGTH_SHORT).show();
////                showJobBuilderDialog();
//            }
//        });

//        scanPrevieweCardV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DocuFiliatoriosActivity.this, "scanPrevieweCardV", Toast.LENGTH_SHORT).show();
////                showScanPreviewDialog();
//            }
//        });

        paperSizeCardV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Toast.makeText(DocuFiliatoriosActivity.this, "paperSizeCardV", Toast.LENGTH_SHORT).show();
                showPaperSizeDialog();
            }
        });

//        removeBlankPagesCardV.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Toast.makeText(DocuFiliatoriosActivity.this, "removeBlankPagesCardV", Toast.LENGTH_SHORT).show();
////                showBlankPagesDialog();
//            }
//        });

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

    private void openDialogDigitalizacion(int numeroDeDialog) {
        dialog = new DigitalizarDocuFiliatoriosDialog(this, numeroDeDialog);
        dialog.show(getSupportFragmentManager(), "digitalizar fragment");
//        dialog.setCancelable(false);
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

        jobBuilderSwitch.setChecked(false);
        scanPreviewSwitch.setChecked(false);
        blankPagesSwitch.setChecked(false);

    }

//    private void showJobBuilderDialog() {
//        //get prev selecction
//        int checkedItem;
//        if (job_builder_selected != null) {
//            checkedItem = jobassemblymode.indexOf(job_builder_selected);
//        } else {
//            checkedItem = 0;
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Job Builder");
//        builder.setSingleChoiceItems(JOBBUILDER, checkedItem, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                job_builder_selected = JOBBUILDER[i];
//            }
//        });
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Snackbar.make(layout, "selected : " + job_builder_selected, Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }
//
//
//    private void showScanPreviewDialog() {
//        //get prev selecction
//        int checkedItem;
//        if (scan_preview_selected != null) {
//            checkedItem = scanpreview.indexOf(scan_preview_selected);
//        } else {
//            checkedItem = 0;
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Scan Preview");
//        builder.setSingleChoiceItems(SCANPREVIEW, checkedItem, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                scan_preview_selected = SCANPREVIEW[i];
//            }
//        });
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Snackbar.make(layout, "selected : " + scan_preview_selected, Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }
    //    private void showBlankPagesDialog() {
//        //get prev selecction
//        int checkedItem;
//        if (blank_pages_selected != null) {
//            checkedItem = blackImageRemovalEntries.indexOf(blank_pages_selected);
//        } else {
//            checkedItem = 0;
//        }
//
//        AlertDialog.Builder builder = new AlertDialog.Builder(this);
//        builder.setTitle("Remove Blank Pages");
//        builder.setSingleChoiceItems(BLANKPAGES, checkedItem, new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                blank_pages_selected = BLANKPAGES[i];
//            }
//        });
//        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                Snackbar.make(layout, "selected : " + blank_pages_selected, Snackbar.LENGTH_SHORT).show();
//            }
//        });
//        builder.show();
//    }

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

        ScanOptionsSelected scanOptionsSelected = ScanOptionsSelected.getInstance();
        scanOptionsSelected.setPaperSize(paper_size_selected);

        scanOptionsSelected.setBlankPagesSelected(blank_pages_selected);
        scanOptionsSelected.setScanPreviewSelected(scan_preview_selected);
        scanOptionsSelected.setJobBuilderSelected(job_builder_selected);

    }

    public void onDigitalizacionDialogResponse() {
       dialog.getDialog().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
       switch (dialogNumero){
           case 0:
               scanToDestination("1scan");
               break;
           case 1:
               scanToDestination("2scan");
               break;
           case 2:
               scanToDestination("3scan");
               break;
           case 3:

       }
//       scanToDestination(DemoViewModelSingleton.getInstance().getMetadataCliente().getBusinessName().trim().toLowerCase().replace(" ", "_"));
    }

    private void onScannResponse() {
        dialogNumero++;
        if (dialogNumero == 3){

            Log.d(TAG, "onScannResponse: se digitalizaron los archivos, debieramos subirlos y después cerrar la app");
            for (String i : listFilesPath){
                Log.d(TAG, "path " + i);
            }
            for (String i : listFilesNames){
                Log.d(TAG, "path " + i);
            }
            cartelSubirALaNube();
            //todo metodo volley para subir archivos, call CREATEDOCUMENTS
            subirArchivos(listFilesPath.get(0), listFilesNames.get(0));

            return;
        }
        openDialogDigitalizacion(dialogNumero);
    }

    private void subirArchivos(String filePath, String filename) {

        new CreateDocument(this,filePath, filename, convertJsonBojectToString()).execute();
        /** Async Simula la subida a la web*/
//        new AsyncTask<Void, Void, Void>(){
//            @Override
//            protected void onPostExecute(Void aVoid) {
//                super.onPostExecute(aVoid);
//                cartelSubirALaNube();
//
//            }
//
//            @Override
//            protected Void doInBackground(Void... voids) {
//                try {
//                    Thread.sleep(4000);
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//                return null;
//            }
//        }.execute();
        /** Async Simula la subida a la web*/
    }

    @Override
    public void onCreateDocumentComplete() {
        Log.d(TAG, "onCreateDocumentsFinish: scanCount es: " + scanCount);
        switch (scanCount){
            case 0:
                subirArchivos(listFilesPath.get(1), listFilesNames.get(1));
                break;
            case 1:
                subirArchivos(listFilesPath.get(2), listFilesNames.get(2));
                break;
            case 2:
                cartelSubirALaNube();
                cartelFinalizacionTRabajo();
                break;
        }
        scanCount++;
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

    public void cartelFinalizacionTRabajo(){
        FinalizacionDeTrabajo finalizacionDeTrabajo = new FinalizacionDeTrabajo();
        finalizacionDeTrabajo.setCancelable(false);
        finalizacionDeTrabajo.show(getSupportFragmentManager(), "finalizacion fialog");
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

    
    /** CallBack de FinalizacionTrabajo Dialog */
    @Override
    public void realizarOtroTrabajo(boolean siOno) {
        if (siOno){
            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            closeApp();
        }
    }

    private void closeApp() {
        Intent intent = new Intent(getApplicationContext(), SeleccionSerieDocumentalActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

    private String convertJsonBojectToString(){

        DemoViewModelSingleton demoViewModelSingleton = DemoViewModelSingleton.getInstance();


        String serieName = "Filiation";
        int demoId = demoViewModelSingleton.getDemoViewModelGuardado().getId();
        String client = demoViewModelSingleton.getDemoViewModelGuardado().getClientName();

        CreateDocumentViewModel createDocumentViewModel = new CreateDocumentViewModel(serieName, demoId, client, demoViewModelSingleton.getMetadataCliente());

        Gson gson = new Gson();

        return gson.toJson(createDocumentViewModel);

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
//            int numeroDeHojaEscaneada = Integer.parseInt(jobInfo.getJobData().toString().substring(jobInfo.getJobData().toString().indexOf("imagesScanned=") + 14, jobInfo.getJobData().toString().indexOf(", imagesProcessed")));
//            if (numeroDeHojaEscaneada > 0) {
//                scanProgressDialog.changeText("Escaneando hoja " + numeroDeHojaEscaneada);
//            }

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

                        final String jrid = JobService.monitorJobInForeground(DocuFiliatoriosActivity.this, mJobId, taskAttributes, intent);

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
            String ruta = scanJobData.getFileNames().get(0).toLowerCase();
            Log.d(TAG, "onComplete Ruta: " + ruta);
            String splitBy = "/" + jobInfo.getJobId() + "/";
            String file = ruta.split(splitBy)[1];
            Log.d(TAG, "onComplete file: " + file);

            listFilesNames.add(file);
            listFilesPath.add(ruta);

            /** NUEVO*/

            // terminado el scan llama al metodo para cambiar el dialog
            onScannResponse();

        }

        @Override
        public void onFail(String s, Result result) {
            Log.d(TAG, "onFail: CALL");
            Toast.makeText(DocuFiliatoriosActivity.this, "Espere unos segundos", Toast.LENGTH_SHORT).show();
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
            Toast.makeText(DocuFiliatoriosActivity.this, "Escaneo Cancelado", Toast.LENGTH_SHORT).show();
            deleteAllFiles();
            restartActivity();

        }


    }

    @Override
    protected void onStop() {
        super.onStop();
        deleteAllFiles();
        finish();
    }

    private void restartActivity() {
        Intent i = getIntent();
        finish();
        startActivity(i);
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
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

//    public enum DialogStatus {
//        SOLICITA_ID,
//        SOLICITA_CONSTANCIA,
//        SOLICITA_OTRADOCU,
//        TERMINA
//    }
}
