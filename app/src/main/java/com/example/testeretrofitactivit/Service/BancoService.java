package com.example.testeretrofitactivit.Service;

import okhttp3.Headers;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface BancoService {

    @POST("estudo1/ApiInserirUsuario.php")
    Call<ResponseBody> inserirUsuario(@Header("Authorization") String authorizationHeader, @Body RequestBody requestBody);

}
