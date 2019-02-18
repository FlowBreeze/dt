package com.lfkj.dt.request;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;

import static com.lfkj.dt.Constant.CONF;

public interface BaiduHttp {

    String HOST_NAME = "http://api.fanyi.baidu.com/";
    long APPID = CONF.baiduAppId();
    String PRIVATE_KEY = CONF.baiduPrivateKey();

    @GET("api/trans/vip/translate")
    Call<Translation> translateAPI(
            @Query("from") String from,
            @Query("to") String to,
            @Query("q") String q,
            @Query("salt") double rand,
            @Query("appid") long appid,
            @Query("sign") String md5);

    class Translation {
        @SerializedName("trans_result")
        public Result[] result;

        @Override
        public String toString() {
            return "Translation{" +
                    "result=" + Arrays.toString(result) +
                    '}';
        }

        public class Result {
            @SerializedName("dst")
            public String meanings;

            @Override
            public String toString() {
                return "Result{" +
                        "meanings='" + meanings + '\'' +
                        '}';
            }
        }
    }

    static String mkMd5(String q, double rand) {
        return mkMd5(APPID, q, rand, PRIVATE_KEY);
    }

    static String mkMd5(long appid, String q, double rand, String privateKey) {
        return getMD5Str(appid + q + rand + privateKey);
    }

    static String getMD5Str(String str) {
        MessageDigest messageDigest = null;
        try {
            messageDigest = MessageDigest.getInstance("MD5");
            messageDigest.reset();
            messageDigest.update(str.getBytes(StandardCharsets.UTF_8));
        } catch (NoSuchAlgorithmException e) {
            System.out.println("NoSuchAlgorithmException caught!");
            System.exit(-1);
        }

        byte[] byteArray = messageDigest.digest();

        StringBuilder md5StrBuff = new StringBuilder();

        for (byte aByteArray : byteArray) {
            String hex = Integer.toHexString(0xFF & aByteArray);
            if (hex.length() == 1)
                md5StrBuff.append("0").append(hex);
            else
                md5StrBuff.append(hex);
        }
        return md5StrBuff.toString();
    }
}
