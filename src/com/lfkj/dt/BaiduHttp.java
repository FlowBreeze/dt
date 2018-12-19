package com.lfkj.dt;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public interface BaiduHttp {

    String HOST_NAME = "http://api.fanyi.baidu.com/";
    long APPID = 20181216000249121L;
    String PRIVATE_KEY = "i7roLjgbkCP3O4fq9OEr";

    @GET("api/trans/vip/translate")
    Call<ResponseBody> translateAPI(
            @Query("from") String from,
            @Query("to") String to,
            @Query("q") String q,
            @Query("salt") double rand,
            @Query("appid") long appid,
            @Query("sign") String md5);


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
