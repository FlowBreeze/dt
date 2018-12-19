package com.lfkj.dt.test;

import com.lfkj.dt.ICiBaHttp;
import com.lfkj.dt.Translator;
import okhttp3.OkHttpClient;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.util.concurrent.TimeUnit;

public class ICiBaHttpTest {

    @Test
    public void testICiBaHttp() throws InterruptedException {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.SECONDS)
                .readTimeout(5, TimeUnit.SECONDS)
                .callTimeout(5, TimeUnit.SECONDS)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(ICiBaHttp.HOST_NAME)
                .addConverterFactory(GsonConverterFactory.create(Translator.gson))
                .client(client)
                .build();
        ICiBaHttp iCiBa = retrofit.create(ICiBaHttp.class);
        Call<ICiBaHttp.Translation> call = iCiBa.translateAPI("Ai", "json", ICiBaHttp.KEY);
        System.out.println(call.request().toString());
        call.enqueue(new Callback<ICiBaHttp.Translation>() {
            @Override
            public void onResponse(Call<ICiBaHttp.Translation> call, Response<ICiBaHttp.Translation> response) {
                System.out.println(response.body());
            }

            @Override
            public void onFailure(Call<ICiBaHttp.Translation> call, Throwable throwable) {
                throwable.printStackTrace();
            }
        });
        Thread.sleep(123123);
    }


}
