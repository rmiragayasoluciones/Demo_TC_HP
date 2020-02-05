package com.soluciones.demoKit.Task;

import android.os.AsyncTask;
import android.util.Log;

import com.soluciones.demoKit.MainActivity;
import com.soluciones.demoKit.UserClass.TokenCliente;
import com.soluciones.demoKit.Utils.Tools;
import com.google.gson.Gson;
import com.hp.jetadvantage.link.api.Result;
import com.hp.jetadvantage.link.api.config.ConfigService;

import org.json.JSONObject;

import java.lang.ref.WeakReference;
/**
 *
 * Lee la configuracion del equipo
 *
 * */
public class ConfigReaderTask extends AsyncTask<Void, Void, JSONObject> {
    private static final String TAG = "ConfigReaderTask";

    private final WeakReference<MainActivity> mContextRef;
    private Result result;

    public ConfigReaderTask (final MainActivity context){
        this.mContextRef = new WeakReference<>(context);
        this.result = new Result();
    }

    @Override
    protected JSONObject doInBackground(Void... voids) {
        Log.d(TAG, "doInBackground: call");
        // call ConfigService API to retrieve configuration for the application
        JSONObject configuration = ConfigService.getDefaultConfig(mContextRef.get().getApplicationContext(), result);
        Log.d(TAG, "config cargada= " + configuration.toString());

        // check Result first
        if (result.getCode() == Result.RESULT_OK) {
            return configuration;
        }
        return null;
    }

    @Override
    protected void onPostExecute(JSONObject jsonObject) {
        super.onPostExecute(jsonObject);
        if (jsonObject != null && result.getCode() == Result.RESULT_OK) {

            Log.d(TAG, "onPostExecute: OK");
            Gson gson = new Gson();
            TokenCliente tokenCliente = gson.fromJson(jsonObject.toString(), TokenCliente.class);
            Log.d(TAG, "onPostExecute token=" + tokenCliente.getTokenCliente() );
            //todo guardar este url

            /** guarda url */
            Log.d(TAG, "tokenCliente.getUrl(): " + tokenCliente.getUrl());
            Tools.saveUrlonSharedPreference(mContextRef.get(), tokenCliente.getUrl());
            /** guarda url */


            Log.d(TAG, "onPostExecute url=" + tokenCliente.getUrl());

            /** For Debug */
//            tokenCliente.setTokenCliente("j2xcs");
            /** For Debug */

            String tokenClienteString = tokenCliente.getTokenCliente();

            if (tokenClienteString != null){
                new GetDemoFromApi(this.mContextRef, tokenCliente.getTokenCliente()).execute();
            } else {
                mContextRef.get().onConfigError();
            }

        }
        else {
            Log.d(TAG, "onPostExecute: ERROR: " + result);
            mContextRef.get().onConfigError();
        }
    }
}
