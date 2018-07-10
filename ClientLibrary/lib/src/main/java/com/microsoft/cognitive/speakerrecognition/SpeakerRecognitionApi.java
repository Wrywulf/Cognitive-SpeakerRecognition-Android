package com.microsoft.cognitive.speakerrecognition;

import com.microsoft.cognitive.speakerrecognition.contract.identification.EnrollmentOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Url;

public interface SpeakerRecognitionApi {

    @GET
    Call<EnrollmentOperation> checkEnrollmentStatus(@Url String operationLocation);

    @GET
    Call<IdentificationOperation> checkIdentificationStatus(@Url String operationLocation);

    @Multipart
    @POST("verify")
    Call<Void> verify(@Part("verificationData") RequestBody audio, @Query("verificationProfileId") String verificationProfileId);

    @Multipart
    @POST("identify")
    Call<Void> identify(@Part("identificationData") RequestBody audio, @Query("identificationProfileIds") String identificationProfileIds, @Query("shortAudio") boolean shortAudio);

}
