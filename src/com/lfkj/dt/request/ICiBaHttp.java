package com.lfkj.dt.request;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.net.URL;
import java.util.Arrays;

import static com.lfkj.dt.Constant.CONF;

public interface ICiBaHttp {
    String HOST_NAME = "http://dict-co.iciba.com/";
    String KEY = CONF.iCiBaKey();

    @POST("api/dictionary.php")
    Call<Translation> translateAPI(
            @Query("w") String word,
            @Query("type") String type,
            @Query("key") String k
    );

    class Translation {
        @SerializedName("symbols")
        public
        Nature[] nature;

        @Override
        public String toString() {
            return "Translation{" +
                    "nature=" + Arrays.toString(nature) +
                    '}';
        }

        public class Nature {
            @SerializedName("ph_en")
            public
            String enSymbols;
            @SerializedName("ph_am")
            public
            String amSymbols;
            @SerializedName("ph_en_mp3")
            public
            URL enPronunciation;
            @SerializedName("ph_am_mp3")
            public
            URL amPronunciation;
            @SerializedName("parts")
            public
            Meaning[] meanings;

            public class Meaning {
                @SerializedName("part")
                public
                String type; // vi. vt. n.
                @SerializedName("means")
                public
                String[] meanings;

                @Override
                public String toString() {
                    return "Meaning{" +
                            "type='" + type + '\'' +
                            ", meanings=" + Arrays.toString(meanings) +
                            '}';
                }
            }

            @Override
            public String toString() {
                return "Nature{" +
                        "enSymbols='" + enSymbols + '\'' +
                        ", amSymbols='" + amSymbols + '\'' +
                        ", enPronunciation=" + enPronunciation +
                        ", amPronunciation=" + amPronunciation +
                        ", meanings=" + Arrays.toString(meanings) +
                        '}';
            }

        }
    }

}
