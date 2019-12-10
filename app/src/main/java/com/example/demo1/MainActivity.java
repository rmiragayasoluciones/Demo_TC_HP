package com.example.demo1;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;


import com.example.demo1.Retro.JsonPlaceHolderAPI;
import com.example.demo1.Retro.Post;
import com.example.demo1.Retro.TLSSocketFactory;
import com.example.demo1.Retro.UnsafeOkHttpClient;


import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;


import javax.net.ssl.SSLContext;
import javax.net.ssl.X509TrustManager;

import okhttp3.ConnectionSpec;
import okhttp3.OkHttpClient;
import okhttp3.TlsVersion;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MainActivity extends AppCompatActivity implements  BlankFragment.OnFragmentInteractionListener{
    private static final String TAG = "MainActivity";

    private ImageView logo;
    private TextView poweredBy;
    private FrameLayout fragmentContainer;
//    private LoginFragment loginFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fragmentContainer = findViewById(R.id.loginFragment);
        logo = findViewById(R.id.logohp);
        poweredBy = findViewById(R.id.textoPoweredBySoluciones);



//        brand = BrandInfo.getInstance();
//        String imageUrl = "https://cdn.pixabay.com/photo/2017/01/11/08/32/icon-1971135_960_720.png";
//        String imageUrl = "https://cdn.pixabay.com/photo/2016/08/31/00/49/google-1632434_960_720.png";
//        String imageUrl = "https://cdn.pixabay.com/photo/2015/09/15/21/26/cat-941821_960_720.png";

//        final Target target = new Target() {
//            @Override
//            public void onBitmapLoaded(Bitmap bitmap, Picasso.LoadedFrom from) {
//                Log.d(TAG, "onBitmapLoaded: call");
//                logoImagen = bitmap;
////                logo.setImageBitmap(bitmap);
//                brand.setLogoMarca(bitmap);
//                en2IniciaFragment += 1;
//                openLoginFragment();
//            }
//
//            @Override
//            public void onBitmapFailed(Exception e, Drawable errorDrawable) {
//                Log.d(TAG, "onBitmapFailed: call");
//                e.printStackTrace();
//                e.getMessage();
//                e.getCause();
//            }
//
//            @Override
//            public void onPrepareLoad(Drawable placeHolderDrawable) {
//                Log.d(TAG, "onPrepareLoad: call");
//            }
//        };
//
//
//        Picasso.get().load(imageUrl).noPlaceholder().error(R.drawable.hplogo).into(target);

//        Glide.with(this).asBitmap().load(imageUrl).listener(new RequestListener<Bitmap>() {
//            @Override
//            public boolean onLoadFailed(@Nullable GlideException e, Object o, Target<Bitmap> target, boolean b) {
//                Log.d(TAG, "onLoadFailed: call");
//                Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();
//                return false;
//            }
//
//            @Override
//            public boolean onResourceReady(Bitmap bitmap, Object o, Target<Bitmap> target, DataSource dataSource, boolean b) {
//                Log.d(TAG, "onResourceReady: call");
//                brand.setLogoMarca(bitmap);
//                en2IniciaFragment += 1;
//                openLoginFragment();
//                return false;
//            }
//        }
//        ).submit();


        splashAnimation();


    }


    private void openLoginFragment( ) {

        Log.d(TAG, "openLoginFragment: CALL");
//        loginFragment = LoginFragment.newInstance();
//        FragmentManager fm = getSupportFragmentManager();
//        FragmentTransaction transaction = fm.beginTransaction();
//        transaction.setCustomAnimations(R.anim.from_bottom, R.anim.from_bottom);
//        transaction.add(R.id.loginFragment, loginFragment, "LOGIN_FRAGMENT").commit();

        BlankFragment fragment = BlankFragment.newInstance();
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transition = fm.beginTransaction();
        transition.setCustomAnimations(R.anim.from_bottom,R.anim.from_bottom,R.anim.from_bottom,R.anim.from_bottom );
        transition.add(R.id.loginFragment, fragment, "Blank Fragment").commit();

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
                                //Do something after 1000ms
                                openLoginFragment();
                            }
                        }, 1000);

                    }
                });
    }

//    private String saveToInternalStorage(Bitmap bitmapImage){
//        ContextWrapper cw = new ContextWrapper(getApplicationContext());
//        // path to /data/data/yourapp/app_data/imageDir
//        String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS).toString() + "/HpDemo";
//        // Create imageDir
//        File mypath=new File(path,"logoTesting.png");
//
//        FileOutputStream fos = null;
//        try {
//            fos = new FileOutputStream(mypath);
//            // Use the compress method on the BitMap object to write image to the OutputStream
//            bitmapImage.compress(Bitmap.CompressFormat.PNG, 100, fos);
//        } catch (Exception e) {
//            e.printStackTrace();
//        } finally {
//            try {
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//        return mypath.getAbsolutePath() ;
//    }

    @Override
    public void onbuscarIdClienteIngresado(String uri) {
        Log.d(TAG, "desde Main llama al Asyntack con el id " + uri);


        OkHttpClient okHttpClient = UnsafeOkHttpClient.getUnsafeOkHttpClient();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://dummy.restapiexample.com/")
                .addConverterFactory(GsonConverterFactory.create())
                .client(okHttpClient)
                .build();

        Log.d(TAG, "reto Build");

        JsonPlaceHolderAPI jsonPlaceHolderApi = retrofit.create(JsonPlaceHolderAPI.class);

        Call<Post> call = jsonPlaceHolderApi.getComments("api/v1/employee/1");
        Log.d(TAG, "reto Call call");

        call.enqueue(new Callback<Post>() {
            @Override
            public void onResponse(Call<Post> call, Response<Post> response) {
                Log.d(TAG, "onResponse: CALL");
                if (!response.isSuccessful()){
                    //mostrar cartel de error
                    Log.d(TAG, "code " + response.code());
                    return;
                }

                Post post = response.body();
                String textoCompleto = "";

                    String content = "";
                    content += "ID: " + post.getId() + "\n";
                    content += "Nombre: " + post.getEmployee_name() + "\n";
                    content += "Salario: " + post.getEmployee_salary() + "\n";
                    content += "Age: " + post.getEmployee_age() + "\n";

                    textoCompleto += content + " ";

                startNextActivity(textoCompleto);

            }

            @Override
            public void onFailure(Call<Post> call, Throwable t) {
                //mostrar cartel de error
                Log.d(TAG, "onFailure: CALL " + t.getMessage());
                t.fillInStackTrace();
                startNextActivity(t.getMessage());
            }
        });
        Log.d(TAG, "call.enque CALL");
    }

    public static OkHttpClient.Builder enableTls12OnPreLollipop(OkHttpClient.Builder client) {
        if (Build.VERSION.SDK_INT >= 16 && Build.VERSION.SDK_INT < 22) {

            X509TrustManager tm = new X509TrustManager() {

                @Override
                public java.security.cert.X509Certificate[] getAcceptedIssuers() {
                    return null;
                }

                @Override
                public void checkClientTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }

                @Override
                public void checkServerTrusted(
                        java.security.cert.X509Certificate[] chain, String authType)
                        throws java.security.cert.CertificateException {

                }
            };

            try {
                SSLContext sc = SSLContext.getInstance("TLSv1.2");
                sc.init(null, null, null);
                client.sslSocketFactory(new TLSSocketFactory(sc.getSocketFactory() ), tm);

                ConnectionSpec cs = new ConnectionSpec.Builder(ConnectionSpec.MODERN_TLS)
                        .tlsVersions(TlsVersion.TLS_1_2)
                        .build();

                List<ConnectionSpec> specs = new ArrayList<>();
                specs.add(cs);
                specs.add(ConnectionSpec.COMPATIBLE_TLS);
                specs.add(ConnectionSpec.CLEARTEXT);

                client.connectionSpecs(specs);
            } catch (Exception exc) {
                Log.e("OkHttpTLSCompat", "Error while setting TLS 1.2", exc);
            }
        }

        return client;
    }

    private OkHttpClient getNewHttpClient() {
        OkHttpClient.Builder client = new OkHttpClient.Builder()
                .followRedirects(true)
                .followSslRedirects(true)
                .retryOnConnectionFailure(true)
                .cache(null)
                .connectTimeout(5, TimeUnit.SECONDS)
                .writeTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS);

        return enableTls12OnPreLollipop(client).build();
    }

    private void startNextActivity(String respuestaDeLaApi) {
        Log.d(TAG, "startNextActivity: call");

        Intent intent = new Intent(this, AppSelectionActivity.class);
//        Intent intent = new Intent(this, ApiResponse.class);
//        if (respuestaDeLaApi != null){
//            intent.putExtra("respuestaAPI", respuestaDeLaApi);
//        }
        startActivity(intent);
        finish();
    }
}
