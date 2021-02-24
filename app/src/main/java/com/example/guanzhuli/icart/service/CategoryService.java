package com.example.guanzhuli.icart.service;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface CategoryService {
    @GET("/category/findAll/{idSessao}")
    Call <ResponseBody> listCategories(@Path("idSessao") String idSessao);
}
