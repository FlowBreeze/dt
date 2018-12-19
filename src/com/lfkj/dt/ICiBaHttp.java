package com.lfkj.dt;

import com.google.gson.annotations.SerializedName;
import retrofit2.Call;
import retrofit2.http.POST;
import retrofit2.http.Query;

import java.net.URL;
import java.util.Arrays;

public interface ICiBaHttp {
    String HOST_NAME = "http://dict-co.iciba.com/";
    String KEY = "3B8BEC6E03BE66DAB129AFFBC6C57380";

    @POST("api/dictionary.php")
    Call<Translation> translateAPI(
            @Query("w") String word,
            @Query("type") String type,
            @Query("key") String k
    );

    class Translation {
        @SerializedName("symbols")
        Nature[] nature;

        @Override
        public String toString() {
            return "Translation{" +
                    "nature=" + Arrays.toString(nature) +
                    '}';
        }

        public class Nature {
            @SerializedName("ph_en")
            String enSymbols;
            @SerializedName("ph_am")
            String amSymbols;
            @SerializedName("ph_en_mp3")
            URL enPronunciation;
            @SerializedName("ph_am_mp3")
            URL amPronunciation;
            @SerializedName("parts")
            Meaning[] meanings;

            public class Meaning {
                @SerializedName("part")
                String type; // vi. vt. n.
                @SerializedName("means")
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
