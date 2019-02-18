package com.lfkj.dt.request.test;

import com.lfkj.dt.request.ICiBaHttp;
import com.lfkj.dt.request.Request;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

import java.io.IOException;

public class ICiBaHttpTest {

    private ICiBaHttp iCiBa;

    @Before
    public void init() {
        iCiBa = Request.iCiBaRequest();
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
