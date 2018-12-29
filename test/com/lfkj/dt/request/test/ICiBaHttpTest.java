package com.lfkj.dt.request.test;

import com.lfkj.dt.request.ICiBaHttp;
import com.lfkj.dt.translator.Translator;
import okhttp3.OkHttpClient;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

public class ICiBaHttpTest {

    private ICiBaHttp iCiBa;

    @Before
    public void init() {
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
        iCiBa = retrofit.create(ICiBaHttp.class);
    }

    @Test
    public void testICiBa() throws IOException {
        iCiBaHttpHandler("word");
        iCiBaHttpHandler("word_not_find");
        iCiBaHttpHandler("other");
        // 当输入传入时，有必要将首字母小写，否则会向下面这样
        iCiBaHttpHandler("Word");
    }

    private void iCiBaHttpHandler(String word) throws IOException {
        Call<ICiBaHttp.Translation> call = iCiBa.translateAPI(word, "json", ICiBaHttp.KEY);
        ICiBaHttp.Translation body = call.execute().body();
        System.out.println(body);
    }

}
