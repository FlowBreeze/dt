package com.lfkj.dt.translator;

import com.lfkj.dt.request.ICiBaHttp;
import javafx.fxml.FXML;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.applet.Applet;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Function;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.isNull;

public class ICiBaTranslator implements Translator {
    @FXML
    private TextField word;
    @FXML
    private TextField enSymbols;
    @FXML
    private TextField amSymbols;
    @FXML
    private TextArea meanings;


    private static final String right = " ]";
    private static final String left = "[ ";

    private ICiBaHttp iCiBa;
    private ICiBaHttp.Translation translation;

    @FXML
    public void initialize() {
        iCiBa = new Retrofit.Builder()
                .baseUrl(ICiBaHttp.HOST_NAME)
                .addConverterFactory(factory)
                .client(client)
                .build()
                .create(ICiBaHttp.class);
    }

    @Override
    public void translate(String wordOrParagraph) {
        word.setText("Searching... ...");
        Function<String, String> firstAlphabetToLower = s -> {
            StringBuffer stringBuffer = new StringBuffer();
            Matcher matcher = Pattern.compile("^(\\s*\\w)").matcher(s);
            if (matcher.find()) {
                matcher.appendReplacement(stringBuffer, matcher.group().toLowerCase()).appendTail(stringBuffer);
                return stringBuffer.toString();
            }
            return s;
        };
        Call<ICiBaHttp.Translation> call = iCiBa.translateAPI(firstAlphabetToLower.apply(wordOrParagraph), "json", ICiBaHttp.KEY);
        call.enqueue(new Callback<ICiBaHttp.Translation>() {
            @Override
            public void onResponse(Call<ICiBaHttp.Translation> call, Response<ICiBaHttp.Translation> response) {
                translation = response.body();
                Logger.getLogger(ICiBaTranslator.class.getSimpleName()).info("translation get " + translation);
                word.setText(wordOrParagraph);
                ICiBaHttp.Translation.Nature nature = Objects.requireNonNull(translation).nature[0];
                if (isNull(nature.meanings)) {
                    meanings.setText("can not find");
                    enSymbols.setText("");
                    amSymbols.setText("");
                    return;
                }
                enSymbols.setText(left + nature.enSymbols + right);
                amSymbols.setText(left + nature.amSymbols + right);
                final Function<ICiBaHttp.Translation.Nature.Meaning[], String> transformMeaning = arr -> {
                    StringBuilder stringBuilder = new StringBuilder();
                    for (ICiBaHttp.Translation.Nature.Meaning meaning : arr) {
                        stringBuilder.append(meaning.type)
                                .append(":")
                                .append(String.join(",", Arrays.asList(meaning.meanings)))
                                .append("\n");
                    }
                    return stringBuilder.toString();
                };
                meanings.setText(transformMeaning.apply(nature.meanings));
            }

            @Override
            public void onFailure(Call<ICiBaHttp.Translation> call, Throwable throwable) {
                throwable.printStackTrace();
                word.setText("error on http request");
                meanings.setText("message:\n" + throwable.getMessage() + "\n for more details please check console");
            }
        });
    }

    @Override
    public String getTittle() {
        return "爱词霸";
    }

    public void readEnglish() {
        Applet.newAudioClip(translation.nature[0].enPronunciation).play();
    }

    public void readAmerican() {
        Applet.newAudioClip(translation.nature[0].amPronunciation).play();
    }

}
