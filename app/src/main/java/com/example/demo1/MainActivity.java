package com.example.demo1;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.demo1.Dialogs.NoConfigDialog;
import com.example.demo1.Task.ConfigReaderTask;
import com.example.demo1.Task.InitializationTask;

/** link json ejemplo
 *
 * https://api.myjson.com/bins/j2xcs
 *
 * */

public class MainActivity extends AppCompatActivity{
    private static final String TAG = "MainActivity";
    private static final int METODOS_COMPLETADOS = 2;

    private ImageView logo;
    private TextView poweredBy;
    private int metodos = 0;

    /* HP InitializationTask */
    private InitializationTask mInitializationTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        logo = findViewById(R.id.logohp);
        poweredBy = findViewById(R.id.textoPoweredBySoluciones);

//        /** For Debug */
//        createDemoViewModel();
//        /** For Debug */

        splashAnimation();
    }

    @Override
    protected void onResume() {
        super.onResume();


        mInitializationTask = new InitializationTask(this);
        mInitializationTask.execute();
    }

    @Override
    protected void onPause() {
        super.onPause();

        mInitializationTask.cancel(true);
        mInitializationTask = null;
    }

    private void splashAnimation(){
        logo.setAlpha(0f);
        logo.setVisibility(View.VISIBLE);

        logo.animate()
                .alpha(1f)
                .setDuration(1000)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        textAnimation();
                    }
                });
    }

    private void textAnimation(){
        ObjectAnimator animaton = ObjectAnimator.ofFloat(poweredBy,"translationY", 50f);
        animaton.setDuration(1300);
        animaton.setInterpolator(new AccelerateDecelerateInterpolator());
        poweredBy.setAlpha(0f);
        poweredBy.setVisibility(View.VISIBLE);

        animaton.start();
        poweredBy.animate()
                .alpha(1f)
                .setDuration(1300)
                .setListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                startNextActivity();
                            }
                        }, 1000);

                    }
                });
    }

    public void handleComplete(){
        // el initStatus vuelve correctamente
        Log.d(TAG, "handleComplete: initStatus vuelve correctamente");
        //busca las capabilities del Equipo
        //busca la configCargada
        new ConfigReaderTask(this).execute();
    }

    public void handleException (final Exception e){
        // el initStatus vuelve con Error
        Log.e(TAG, e.getMessage());
    }


    private void startNextActivity() {
        Log.d(TAG, "startNextActivity: call");
        metodos++;
        Log.d(TAG, "startNextActivity: metodos hechos " + metodos);
        if (metodos != METODOS_COMPLETADOS){
            Log.d(TAG, "startNextActivity: return");
            return;
        }
        Log.d(TAG, "startNextActivity: inicia");

        Intent intent = new Intent(this, AppSelectionActivity.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    private void notTokenLoaded(String mensajeError){
        if (mensajeError == null){
            NoConfigDialog noConfigDialog = new NoConfigDialog(this, null);
            noConfigDialog.show(getSupportFragmentManager(), "noConfigLoaded");
        } else {
            NoConfigDialog noConfigDialog = new NoConfigDialog(this, mensajeError);
            noConfigDialog.show(getSupportFragmentManager(), "noConfigLoaded");
        }
    }

    public void onConfigError(){
        Log.d(TAG, "onConfigError: error en la configuracion (no/mal cargada)");
        notTokenLoaded(null);
    }

    public void onTokenError(String mensajeError){
        Log.d(TAG, "onTokenError: error en la respuesta del token (no existe, cenvido, etc)");
        notTokenLoaded(mensajeError);
    }

    //vuelve de buscar la config del equipo
    public void onApiResponseComplete() {
        Log.d(TAG, "llega con el json respuesta de la Api nuestra");
        startNextActivity();
    }





}
