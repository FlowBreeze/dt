package com.lfkj.dt.translator;

import com.lfkj.dt.request.BaiduHttp;
import com.lfkj.dt.request.Request;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.Nonnull;
import java.util.Objects;

/**
 * 通过 {@link retrofit2.Retrofit} 与百度翻译 API 交互，返回结果到 javafx 中
 */
public class BaiduTranslator implements Translator {

    @FXML
    private TextArea meanings;

    @Override
    public void translate(String wordOrParagraph) {
        String q = wordOrParagraph.replaceAll("\\n", "");
        double rand = Math.random();
        Call<BaiduHttp.Translation> call = Request.baiduRequest().translateAPI("en", "zh", q, rand, BaiduHttp.APPID, BaiduHttp.mkMd5(q, rand));
        call.enqueue(new Callback<BaiduHttp.Translation>() {
            @Override
            public void onResponse(@Nonnull Call<BaiduHttp.Translation> call, @Nonnull Response<BaiduHttp.Translation> response) {
                meanings.setText(Objects.requireNonNull(response.body()).result[0].meanings);
            }

            @Override
            public void onFailure(@Nonnull Call<BaiduHttp.Translation> call, @Nonnull Throwable throwable) {
                meanings.setText("error on http request\nmessage:\n" + throwable.getMessage() + "\n for more details please check console");
            }
        });
    }

    @Override
    public String getTittle() {
        return "百度翻译";
    }
}
