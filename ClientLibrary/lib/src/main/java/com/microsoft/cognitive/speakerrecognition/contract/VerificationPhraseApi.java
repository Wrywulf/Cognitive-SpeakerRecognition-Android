package com.microsoft.cognitive.speakerrecognition.contract;

import com.microsoft.cognitive.speakerrecognition.contract.verification.VerificationPhrase;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface VerificationPhraseApi {

    /**
     *
     * @param locale ie. "en-us"
     * @return
     */
    @GET("verificationPhrases")
    Call<List<VerificationPhrase>> getPhrases(@Query("locale") String locale);

}
