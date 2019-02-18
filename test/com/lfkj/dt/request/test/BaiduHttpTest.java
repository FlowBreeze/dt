package com.lfkj.dt.request.test;

import com.lfkj.dt.request.BaiduHttp;
import com.lfkj.dt.request.Request;
import org.junit.Before;
import org.junit.Test;
import retrofit2.Call;

import java.io.IOException;

public class BaiduHttpTest {

    private BaiduHttp baidu;

    @Before
    public void init() {

        baidu = Request.baiduRequest();
    }

    @Test
    public void testBaidu() throws IOException {
        baiduHttpHandler("this is a method");
        baiduHttpHandler("Of course, rather than creating an overload method, you could also keep the \"scanner\" and \"output\" as mutable fields in your system under test. I tend to like keeping classes as stateless as possible, but that's not a very big concession if it matters to you or your coworkers/instructor.");
        // 当输入传入时，有必要将 \n 清除，否则会向下面这样
        baiduHttpHandler("Although an interface based on coarse-grained trans-\n" +
                "formations may at first seem limited, RDDs are a good\n" +
                "fit  for  many  parallel  applications,  because\n" +
                "these  appli-\n" +
                "cations naturally apply the same operation to multiple\n" +
                "data items\n" +
                ". Indeed, we show that RDDs can efficiently\n" +
                "express many cluster programming models that have so\n" +
                "far been proposed as separate systems, including MapRe-\n" +
                "duce, DryadLINQ, SQL, Pregel and HaLoop, as well as\n" +
                "new applications that these systems do not capture, like\n" +
                "interactive data mining. The ability of RDDs to accom-\n" +
                "modate computing needs that were previously met only\n" +
                "by introducing new frameworks is, we believe, the most\n" +
                "credible evidence of the power of the RDD abstraction.");
    }

    private void baiduHttpHandler(String text) throws IOException {
        double rand = Math.random();
        Call<BaiduHttp.Translation> call = baidu.translateAPI("en", "zh", text, rand, BaiduHttp.APPID, BaiduHttp.mkMd5(text, rand));
        BaiduHttp.Translation body = call.execute().body();
        System.out.println(body);
    }
}
