package com.kakao.gettyimagegallery.net;

import com.kakao.gettyimagegallery.Environment;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by khan.moon on 2018. 3. 8..
 */

public class Network {
    private static Network instance = new Network(Environment.baseUrl + Environment.mainUrl);
    private RetrofitService service;

    private Network(String baseUrl) {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        service = retrofit.create(RetrofitService.class);
    }

    public static Network getInstance() {
        return instance;
    }

    public void getHtml(Callback callback) {
        Call<ResponseBody> call =  service.getHtml();
        call.enqueue(callback);
    }
}
