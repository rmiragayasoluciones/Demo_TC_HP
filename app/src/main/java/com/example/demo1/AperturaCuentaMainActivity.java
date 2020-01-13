package com.example.demo1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.DialogFragment;

import com.example.demo1.Dialogs.BuscarIdDialog;
import com.example.demo1.Dialogs.DatePickerFragment;
import com.example.demo1.Dialogs.VolleyErrorResponseDialog;
import com.example.demo1.Task.GetDemoClientMetadata;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.MetadataCliente;
import com.example.demo1.Utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;

public class AperturaCuentaMainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener,
        BuscarIdDialog.BuscarIdDialogListener,
        DatePickerDialog.OnDateSetListener {

    private static final String TAG = "AperturaCuentaMainActiv";

    private ArrayList<PaisItem> paisItemArrayList;
    private PaisAdapter paisAdapter;

    private EditText razonSocial, mail;
    private RadioButton femenino, masculino, noEspecifica;
    private Spinner spinner;
    private boolean rotate = false;
    private View lyt_mic, lyt_call, back_drop;
    private FloatingActionButton fab_add;

    private View cargandoProgresBar;
    private Button calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apertura_cuenta_card_overlaps_layout);

//        Intent intent = getIntent();
//        if (intent.hasExtra("logo")){
//            Log.d(TAG, "Intent Has Logo!!!");
//            Bitmap bitmap = (Bitmap) intent.getParcelableExtra("logo");
//            ImageView logoImageView = findViewById(R.id.logoHPOEmpresaId);
//            logoImageView.setImageBitmap(bitmap);
//        }
//
//        if (intent.hasExtra("nombre")){
//            Log.d(TAG, "Intent Has Nombre!!!");
//            String nombreEmpresa = intent.getStringExtra("nombre");
//            TextView nombreEmpresaTextView = findViewById(R.id.tituloEmpresaId);
//            nombreEmpresaTextView.setText(nombreEmpresa);
//        }

        //todo cargar imagen y nombre ed empresa
        DemoViewModelSingleton demoViewModelSingleton = DemoViewModelSingleton.getInstance();
        String nombreEmpresa = demoViewModelSingleton.getDemoViewModelGuardado().getClientName();
        String logoEnString = demoViewModelSingleton.getDemoViewModelGuardado().getLogo();

        if (nombreEmpresa!=null){
            TextView nombreEmpresaTextView = findViewById(R.id.tituloEmpresaId);
            nombreEmpresaTextView.setText(nombreEmpresa);
        }
        if (logoEnString!= null){
            ImageView logoImageView = findViewById(R.id.logoHPOEmpresaId);
            logoImageView.setImageBitmap(loadImage(logoEnString));
        }




        initToolbar();
        razonSocial = findViewById(R.id.editTextRazonSocialId);
        mail = findViewById(R.id.edittextMailId);
        femenino = findViewById(R.id.femeninoRadioBtn);
        masculino = findViewById(R.id.masculinoRadioBtn);
        noEspecifica = findViewById(R.id.noEspecificaRadioBtn);
        noEspecifica.setChecked(true);
        back_drop = findViewById(R.id.back_drop);
        cargandoProgresBar = findViewById(R.id.llProgressBar);
        calendar = findViewById(R.id.calendarBtn);

        spinner = findViewById(R.id.spinnerPaisesId);
        fab_add = findViewById(R.id.fab_add);
        lyt_mic = findViewById(R.id.lyt_mic);
        lyt_call = findViewById(R.id.lyt_call);
    }

    @Override
    protected void onResume() {
        super.onResume();

        initList();
        selectActualDate();

        paisAdapter = new PaisAdapter(this, paisItemArrayList);
        spinner.setAdapter(paisAdapter);
        spinner.setOnItemSelectedListener(this);

        ViewAnimation.initShowOut(lyt_mic);
        ViewAnimation.initShowOut(lyt_call);

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        lyt_mic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click en el layout");
//                Toast.makeText(getApplicationContext(), "Buscar", Toast.LENGTH_SHORT).show();
                toggleFabMode(fab_add);
                openSearchIdDialog();
            }
        });

        lyt_call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d(TAG, "click en el layout");
                toggleFabMode(fab_add);
                guardarInputs();
                startSerieDocuActivity();
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });

        calendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment datePickerFragt = new DatePickerFragment();
                datePickerFragt.show(getSupportFragmentManager(), "date Picker");
            }
        });

    }


    private void toggleFabMode(View v) {
        rotate = ViewAnimation.rotateFab(v, !rotate);
        if (rotate) {
            ViewAnimation.showIn(lyt_mic);
            ViewAnimation.showIn(lyt_call);
            back_drop.setVisibility(View.VISIBLE);
        } else {
            ViewAnimation.showOut(lyt_mic);
            ViewAnimation.showOut(lyt_call);
            back_drop.setVisibility(View.GONE);
        }
    }


    private void initList() {
        paisItemArrayList = new ArrayList<>();
        paisItemArrayList.add(new PaisItem("Argentina", R.drawable.bandera_argentina));
        paisItemArrayList.add(new PaisItem("Brasil", R.drawable.bandera_brasil));
        paisItemArrayList.add(new PaisItem("Chile", R.drawable.bandera_chile));
        paisItemArrayList.add(new PaisItem("Colombia", R.drawable.bandera_colombia));
        paisItemArrayList.add(new PaisItem("Uruguay", R.drawable.bandera_uruguay));
        paisItemArrayList.add(new PaisItem("Peru", R.drawable.bandera_peru));
        paisItemArrayList.add(new PaisItem("Venezuela", R.drawable.bandera_venezuela));
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_menu);
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("Apertura de Cuenta");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void openSearchIdDialog() {
        Log.d(TAG, "dialog CALL");
        BuscarIdDialog buscarIdDialog = new BuscarIdDialog();
        buscarIdDialog.show(getSupportFragmentManager(), "buscarId Dialog");
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            Intent intent = new Intent(this, AppSelectionActivity.class);
            startActivity(intent);
            overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void buscarId(String idCliente) {
        //aca llama al Async
        closeKeyboard();
        new GetDemoClientMetadata(this, idCliente).execute();
        cargandoDialog();
    }

    public void onClientCorrect(MetadataCliente demoCliente) {
        ConstraintLayout maxLayout =findViewById(R.id.maxLayout);

        Snackbar snackbar = Snackbar.make(maxLayout,"Ususario " + demoCliente.getBusinessName() + " encontrado", Snackbar.LENGTH_SHORT);
        snackbar.getView().setBackgroundColor(ContextCompat.getColor(this, R.color.colorPrimary));
        snackbar.show();
        closeKeyboard();
        cargandoDialog();
        llenarCampos(demoCliente);
    }

    private void llenarCampos(MetadataCliente demoCliente) {
        if (demoCliente.getBusinessName() != null) {
            razonSocial.setText(demoCliente.getBusinessName());
        }
        if (demoCliente.getEmail() != null) {
            mail.setText(demoCliente.getEmail());
        }
        if (demoCliente.getCountry() != null) {
            checkPaisSpinerSelection(demoCliente.getCountry());
        }
        if (demoCliente.getSex() != null) {
            checkSexoRadiobtn(demoCliente.getSex());
        } else {
            noEspecifica.setChecked(true);
        }
    }

    private void checkPaisSpinerSelection(String pais) {

        for (int i = 0; i < paisItemArrayList.size(); i++) {
            if (paisItemArrayList.get(i).getmPaisNombre().equalsIgnoreCase(pais)) {
                spinner.setSelection(i);
                return;
            }
        }
    }

    private void checkSexoRadiobtn(String sexoCliente) {
        switch (sexoCliente.toLowerCase()) {
            case "femenino":
                femenino.setChecked(true);
                break;
            case "masculino":
                masculino.setChecked(true);
                break;
            default:
                noEspecifica.setChecked(true);
                break;
        }
    }

    private void guardarInputs() {
        String razonSocialIngresad = razonSocial.getText().toString();
        String mailIngresado = mail.getText().toString();
        String sexoIngresado = getRadioBtnSelected();
        PaisItem paisSeleccionadoObj = (PaisItem) spinner.getSelectedItem();
        String paisSeleccionado = paisSeleccionadoObj.getmPaisNombre();
        String fecha = calendar.getText().toString();

        MetadataCliente metadataCliente = new MetadataCliente(razonSocialIngresad,mailIngresado,sexoIngresado, paisSeleccionado,null, null, null, fecha);
        DemoViewModelSingleton.getInstance().setMetadataCliente(metadataCliente);

        Log.d(TAG, " RazonSocial " + razonSocialIngresad +
                " Mail " + mailIngresado +
                " Sexo " + sexoIngresado +
                " Pais " + paisSeleccionado +
                " Fecha " + fecha);
    }

    private String getRadioBtnSelected() {
        if (femenino.isChecked()) {
            Log.d(TAG, "femenino");
            return femenino.getText().toString();
        } else if (masculino.isChecked()) {
            Log.d(TAG, "masculino");
            return masculino.getText().toString();
        } else return noEspecifica.getText().toString();

    }

    private void selectActualDate() {
        Calendar c = Calendar.getInstance();
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        calendar.setText(fecha);
    }

    @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
        Calendar c = Calendar.getInstance();
        c.set(Calendar.YEAR, year);
        c.set(Calendar.MONTH, month);
        c.set(Calendar.DAY_OF_MONTH, dayOfMonth);
        String fecha = DateFormat.getDateInstance(DateFormat.FULL).format(c.getTime());
        calendar.setText(fecha);
    }

    private void closeKeyboard() {
        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void cargandoDialog() {
        if (cargandoProgresBar.getVisibility() == View.GONE) {
            cargandoProgresBar.setVisibility(View.VISIBLE);
        } else {
            cargandoProgresBar.setVisibility(View.GONE);
        }
    }

    public void onClientError(String errorMsg) {
        cargandoDialog();
        Toast.makeText(this, errorMsg, Toast.LENGTH_SHORT).show();
    }

    public void volleyResponseError(String mensajeError){
        cargandoDialog();
        setAllInputsEmpty();
        if (mensajeError == null){
            VolleyErrorResponseDialog volleyErrorResponseDialog = new VolleyErrorResponseDialog(this, null);
            volleyErrorResponseDialog.show(getSupportFragmentManager(), "noConfigLoaded");
        } else {
            VolleyErrorResponseDialog volleyErrorResponseDialog = new VolleyErrorResponseDialog(this, mensajeError);
            volleyErrorResponseDialog.show(getSupportFragmentManager(), "noConfigLoaded");
        }
    }

    public void setAllInputsEmpty(){
        razonSocial.setText("");
        mail.setText("");
        noEspecifica.setChecked(true);
        spinner.setSelection(0);
    }

    private void startSerieDocuActivity(){
        Intent intent = new Intent(this, SeleccionSerieDocumentalActivity.class);
        startActivity(intent);
        finish();
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent(this, AppSelectionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);
        finish();

    }

    private Bitmap loadImage(String logoEnString){

        byte[] decodeString = Base64.decode(logoEnString, Base64.DEFAULT);
        Bitmap decodedByte = BitmapFactory.decodeByteArray(decodeString, 0 , decodeString.length);

        return  decodedByte;
    }

    //    @Override
//    public void finish() {
//        super.finish();
//        Intent intent = new Intent(this, AppSelectionActivity.class);
//        startActivity(intent);
//        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
//        finish();
//    }
}
