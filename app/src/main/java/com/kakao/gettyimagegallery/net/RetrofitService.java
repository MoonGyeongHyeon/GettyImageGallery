package com.kakao.gettyimagegallery.net;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

/**
 * Created by khan.moon on 2018. 3. 8..
 */

public interface RetrofitService {

    @GET(".")
    Call<ResponseBody> getHtml();

}
