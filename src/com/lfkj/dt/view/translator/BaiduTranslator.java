package com.lfkj.dt.view.translator;

import com.lfkj.dt.request.BaiduHttp;
import com.lfkj.dt.request.Request;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import javax.annotation.Nonnull;

import static com.lfkj.util.Context.nullOrThen;
import static java.util.Objects.nonNull;

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
                BaiduHttp.Translation.Result[] results = nullOrThen(response.body(), b -> b.result);
                meanings.setText(nonNull(results) && results.length > 0 ? results[0].meanings : "未收到翻译");
            }

            @Override
            public void onFailure(@Nonnull Call<BaiduHttp.Translation> call, @Nonnull Throwable throwable) {
                meanings.setText("请求失败 原因：" + throwable.getCause());
            }
        });
    }

    @Override
    public String getTittle() {
        return "百度翻译";
    }
}
