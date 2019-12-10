package com.example.demo1.Retro;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Url;

public interface JsonPlaceHolderAPI {

//    //El ejemplo para nuestra app
//    @GET("noimporta.com/ESTOSI")
//    Call<List<Post>> getDemo(String token);
    @GET("posts")
    Call<List<Post>> getDemo();

    @GET
    Call<Post> getComments(@Url String url);

}
