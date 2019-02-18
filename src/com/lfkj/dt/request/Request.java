package com.lfkj.dt.request;

import com.google.gson.*;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import static java.util.Objects.isNull;

/**
 * 使用单列模式创建请求类
 * 封装了一堆让人脑阔疼的框架调用
 */
public class Request {

    private Request() {
    }

    private static Request request;

    private static Request getInstance() {
        if (isNull(request))
            synchronized (Request.class) {
                if (isNull(request))
                    request = new Request();
            }
        return request;
    }

    private static ICiBaHttp iCiBaRequest;

    public static ICiBaHttp iCiBaRequest() {
        if (isNull(iCiBaRequest))
            synchronized (Request.class) {
                if (isNull(iCiBaRequest))
                    iCiBaRequest = new Retrofit.Builder()
                            .baseUrl(ICiBaHttp.HOST_NAME)
                            .addConverterFactory(getInstance().factory)
                            .client(getInstance().client)
                            .build()
                            .create(ICiBaHttp.class);
            }
        return iCiBaRequest;
    }

    private static BaiduHttp baiduRequest;

    public static BaiduHttp baiduRequest() {
        if (isNull(baiduRequest))
            synchronized (Request.class) {
                if (isNull(baiduRequest))
                    baiduRequest = new Retrofit.Builder()
                            .baseUrl(BaiduHttp.HOST_NAME)
                            .addConverterFactory(getInstance().factory)
                            .client(getInstance().client)
                            .build()
                            .create(BaiduHttp.class);
            }
        return baiduRequest;
    }

    private OkHttpClient client = new OkHttpClient.Builder()
            .connectTimeout(5, TimeUnit.SECONDS)
            .readTimeout(5, TimeUnit.SECONDS)
            .callTimeout(5, TimeUnit.SECONDS)
            .build();

    private class IgnoreEmptyString2Url implements JsonDeserializer<URL> {
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

    private Gson gson = new GsonBuilder().registerTypeAdapter(URL.class, new IgnoreEmptyString2Url()).create();

    private GsonConverterFactory factory = GsonConverterFactory.create(gson);
}
