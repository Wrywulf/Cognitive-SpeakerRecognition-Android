package com.microsoft.cognitive.speakerrecognition.contract;

import com.microsoft.cognitive.speakerrecognition.contract.verification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Enrollment;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Profile;

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

public interface VerificationProfileApi {

    @POST("verificationProfiles")
    Call<CreateProfileResponse> createProfile(@Body ProfileLocale locale);

    @DELETE("verificationProfiles/{verificationProfileId}")
    Call<Void> deleteProfile(@Path("verificationProfileId") String verificationProfileId);

    @GET("verificationProfiles/{verificationProfileId}")
    Call<Profile> getProfile(@Path("verificationProfileId") String verificationProfileId);

    @GET("verificationProfiles")
    Call<List<Profile>> getProfiles();

    @Multipart
    @POST("verificationProfiles/{verificationProfileId}/enroll")
    Call<Enrollment> enroll(@Part("enrollmentData") RequestBody audio, @Path("verificationProfileId") String verificationProfileId);

    @POST("verificationProfiles/{verificationProfileId}/reset")
    Call<Void> resetEnrollments(@Path("verificationProfileId") String verificationProfileId);
}
