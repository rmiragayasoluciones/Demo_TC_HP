package com.example.demo1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.airbnb.lottie.L;
import com.example.demo1.Retro.JsonPlaceHolderAPI;
import com.example.demo1.Retro.Post;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiResponse extends AppCompatActivity {
    private static final String TAG = "ApiResponse";

    private TextView textViewResult;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_api_response);

        Intent intent = getIntent();
        String respuestaApi = intent.getStringExtra("respuestaAPI");
        Log.d(TAG, "onCreate: llego la respuesta " + respuestaApi);

        textViewResult = findViewById(R.id.text_view_result);
        textViewResult.setText(respuestaApi);

//        Retrofit retrofit = new Retrofit.Builder()
//                .baseUrl("https://jsonplaceholder.typicode.com/")
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        JsonPlaceHolderAPI jsonPlaceHolderAPI = retrofit.create(JsonPlaceHolderAPI.class);
//
//        Call<List<Post>> call = jsonPlaceHolderAPI.getDemo();
//
//        call.enqueue(new Callback<List<Post>>() {
//            @Override
//            public void onResponse(Call<List<Post>> call, Response<List<Post>> response) {
//                Log.d(TAG, "onResponse: CALL");
//                if (!response.isSuccessful()){
//                    textViewResult.setText("code " + response.code());
//                    return;
//                }
//
//                List<Post> posts = response.body();
//
//                for (Post post : posts){
//                    String content = "";
//                    content += "ID: " + post.getId() + "\n";
//                    content += "UserId: " + post.getUserId() + "\n";
//                    content += "Title: " + post.getTitle() + "\n";
//                    content += "Text: " + post.getText() + "\n";
//
//                    textViewResult.append(content);
//                }
//
//            }
//
//            @Override
//            public void onFailure(Call<List<Post>> call, Throwable t) {
//                Log.d(TAG, "onFailure: CALL");
//                textViewResult.setText(t.getMessage());
//            }
//        });
    }
}
