package com.lfkj.dt;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

public interface Translator {

    void translate(String wordOrParagraph);

    OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .build();

    class IgnoreEmptyString2Url implements JsonDeserializer<URL> {
        @Override
        public URL deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
            String str = jsonElement.getAsString();
            if (!isNull(str) && !str.isEmpty())
                try {
                    return new URL(str);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                }
            return null;
        }
    }

    Gson gson = new GsonBuilder().registerTypeAdapter(URL.class, new IgnoreEmptyString2Url()).create();

    GsonConverterFactory factory = GsonConverterFactory.create(gson);

}
