package com.lfkj.dt.translator;

import com.lfkj.dt.request.BaiduHttp;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.util.Objects;

public class BaiduTranslator implements Translator {

    private BaiduHttp baidu;

    @FXML
    private TextArea meanings;

    @FXML
    public void initialize() {
        baidu = new Retrofit.Builder()
                .baseUrl(BaiduHttp.HOST_NAME)
                .addConverterFactory(factory)
                .client(client)
                .build()
                .create(BaiduHttp.class);
    }

    @Override
    public void translate(String wordOrParagraph) {
        String q = wordOrParagraph.replaceAll("\\n", "");
        double rand = Math.random();
        Call<BaiduHttp.Translation> call = baidu.translateAPI("en", "zh", q, rand, BaiduHttp.APPID, BaiduHttp.mkMd5(q, rand));
        call.enqueue(new Callback<BaiduHttp.Translation>() {
            @Override
            public void onResponse(Call<BaiduHttp.Translation> call, Response<BaiduHttp.Translation> response) {
                meanings.setText(Objects.requireNonNull(response.body()).result[0].meanings);
            }

            @Override
            public void onFailure(Call<BaiduHttp.Translation> call, Throwable throwable) {
                meanings.setText("error on http request\nmessage:\n" + throwable.getMessage() + "\n for more details please check console");
            }
        });
    }

    @Override
    public String getTittle() {
        return "百度翻译";
    }
}
