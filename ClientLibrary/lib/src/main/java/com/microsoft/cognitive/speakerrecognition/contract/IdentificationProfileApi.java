package com.microsoft.cognitive.speakerrecognition.contract;

import com.microsoft.cognitive.speakerrecognition.contract.identification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Profile;
import com.microsoft.cognitive.speakerrecognition.contract.ProfileLocale;

import java.util.List;

import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IdentificationProfileApi {

    @POST("identificationProfiles")
    Call<CreateProfileResponse> createProfile(@Body ProfileLocale locale);

    @DELETE("identificationProfiles/{identificationProfileId}")
    Call<Void> deleteProfile(@Path("identificationProfileId") String identificationProfileId);

    @GET("identificationProfiles/{identificationProfileId}")
    Call<Profile> getProfile(@Path("identificationProfileId") String identificationProfileId);

    @GET("identificationProfiles")
    Call<List<Profile>> getProfiles();

    @Multipart
    @POST("identificationProfiles/{identificationProfileId}/enroll")
    Call<Void> enroll(@Part("enrollmentData") RequestBody audio, @Path("identificationProfileId") String identificationProfileId, @Query("shortAudio") boolean shortAudio);

    @POST("identificationProfiles/{identificationProfileId}/reset")
    Call<Void> resetEnrollments(@Path("identificationProfileId") String identificationProfileId);
}
