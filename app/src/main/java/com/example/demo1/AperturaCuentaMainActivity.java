package com.example.demo1;

import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.DialogFragment;

import com.example.demo1.Dialogs.BuscarIdDialog;
import com.example.demo1.Dialogs.DatePickerFragment;
import com.example.demo1.Dialogs.VolleyErrorResponseDialog;
import com.example.demo1.Task.GetDemoClientMetadata;
import com.example.demo1.UserClass.DemoClientMetadataViewModel;
import com.example.demo1.UserClass.DemoViewModelSingleton;
import com.example.demo1.UserClass.MetadataCliente;
import com.example.demo1.Utils.ViewAnimation;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

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

    private View lyt_mic;
    private View lyt_call;
    private boolean rotate = false;
    private View back_drop;
    private View buscarString;
    private View sigString;
    private FloatingActionButton fab_add;
    private FloatingActionButton fab_buscarCliente;
    private FloatingActionButton fab_continuar;

    private View cargandoProgresBar;
    private Button calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.apertura_cuenta_card_overlaps_layout);

        initToolbar();
        razonSocial = findViewById(R.id.editTextRazonSocialId);
        mail = findViewById(R.id.edittextMailId);
        femenino = findViewById(R.id.femeninoRadioBtn);
        masculino = findViewById(R.id.masculinoRadioBtn);
        noEspecifica = findViewById(R.id.noEspecificaRadioBtn);
        noEspecifica.setChecked(true);
        back_drop = findViewById(R.id.back_drop);
        fab_buscarCliente = findViewById(R.id.fab_search);
        fab_continuar = findViewById(R.id.fab_continue);
        cargandoProgresBar = findViewById(R.id.llProgressBar);
        calendar = findViewById(R.id.calendarBtn);
        selectActualDate();
        spinner = findViewById(R.id.spinnerPaisesId);
        sigString = findViewById(R.id.escanearCardViewId);
        fab_add = findViewById(R.id.fab_add);
        lyt_mic = findViewById(R.id.lyt_mic);
        lyt_call = findViewById(R.id.lyt_call);
        buscarString = findViewById(R.id.buscarCardViewId);

    }

    @Override
    protected void onResume() {
        super.onResume();

        /** para testing singleton */
        DemoViewModelSingleton demoViewModelSingleton = DemoViewModelSingleton.getInstance();
        Log.d(TAG, "################################");
        Log.d(TAG, "DemoViewModelSingleton: " + demoViewModelSingleton.getDemoViewModelGuardado().toString());
        Log.d(TAG, "################################");
        /** para testing singleton */

        initList();

        paisAdapter = new PaisAdapter(this, paisItemArrayList);
        spinner.setAdapter(paisAdapter);
        spinner.setOnItemSelectedListener(this);

        ViewAnimation.initShowOut(lyt_mic);
        ViewAnimation.initShowOut(lyt_call);
        buscarString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Buscar", Toast.LENGTH_SHORT).show();
                toggleFabMode(fab_add);
                openSearchIdDialog();

            }
        });


        sigString.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                guardarInputs();
                toggleFabMode(fab_add);
            }
        });

        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(v);
            }
        });

        back_drop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
            }
        });

        fab_buscarCliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                toggleFabMode(fab_add);
                openSearchIdDialog();
            }
        });

        fab_continuar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), "Call clicked", Toast.LENGTH_SHORT).show();
                guardarInputs();
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
        paisItemArrayList.add(new PaisItem("Argentina", R.drawable.argentina));
        paisItemArrayList.add(new PaisItem("Chile", R.drawable.chile));
        paisItemArrayList.add(new PaisItem("Brasil", R.drawable.brasil));
        paisItemArrayList.add(new PaisItem("Colombia", R.drawable.colombia));
        paisItemArrayList.add(new PaisItem("Uruguay", R.drawable.uruguay));
        paisItemArrayList.add(new PaisItem("Peru", R.drawable.peru));
        paisItemArrayList.add(new PaisItem("Venezuela", R.drawable.venezuela));
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
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void buscarId(String idCliente) {
        //aca llama al Async
        new GetDemoClientMetadata(this, idCliente).execute();
        closeKeyboard();
        cargandoDialog();
    }

    public void onClientCorrect(DemoClientMetadataViewModel demoCliente) {
//        ConstraintLayout maxLayout =findViewById(R.id.maxLayout);
//        maxLayout.requestFocus();
        closeKeyboard();
        cargandoDialog();
        llenarCampos(demoCliente);
    }

    private void llenarCampos(DemoClientMetadataViewModel demoCliente) {
        if (demoCliente.getBusinessName() != null) {
            razonSocial.setText(demoCliente.getBusinessName());
        }
        if (demoCliente.getMail() != null) {
            mail.setText(demoCliente.getMail());
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

        MetadataCliente metadataCliente = new MetadataCliente(razonSocialIngresad,mailIngresado,sexoIngresado, paisSeleccionado,fecha);
        DemoViewModelSingleton.getInstance().setMetadataCliente(metadataCliente);



        Log.d(TAG, " RazonSocial " + razonSocialIngresad +
                " Mail " + mailIngresado +
                " Sexo " + sexoIngresado +
                " Pais " + paisSeleccionado +
                " Fecha " + fecha);
    }

    private String getRadioBtnSelected() {
        if (femenino.isSelected()) {
            return femenino.getText().toString();
        } else if (masculino.isSelected()) {
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

}
