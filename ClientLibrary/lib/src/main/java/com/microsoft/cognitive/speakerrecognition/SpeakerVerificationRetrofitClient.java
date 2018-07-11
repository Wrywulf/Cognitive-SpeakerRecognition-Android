package com.microsoft.cognitive.speakerrecognition;

import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.DeleteProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentException;
import com.microsoft.cognitive.speakerrecognition.contract.GetProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.ProfileLocale;
import com.microsoft.cognitive.speakerrecognition.contract.ResetEnrollmentsException;
import com.microsoft.cognitive.speakerrecognition.contract.SpeakerRecognitionApi;
import com.microsoft.cognitive.speakerrecognition.contract.VerificationPhraseApi;
import com.microsoft.cognitive.speakerrecognition.contract.VerificationProfileApi;
import com.microsoft.cognitive.speakerrecognition.contract.verification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Enrollment;
import com.microsoft.cognitive.speakerrecognition.contract.verification.PhrasesException;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Profile;
import com.microsoft.cognitive.speakerrecognition.contract.verification.Verification;
import com.microsoft.cognitive.speakerrecognition.contract.verification.VerificationException;
import com.microsoft.cognitive.speakerrecognition.contract.verification.VerificationPhrase;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

class SpeakerVerificationRetrofitClient implements SpeakerVerificationClient {

    private final VerificationProfileApi verificationProfileApi;
    private final VerificationPhraseApi verificationPhraseApi;
    private final SpeakerRecognitionApi speakerRecognitionApi;
    private Moshi moshi;

    protected SpeakerVerificationRetrofitClient(VerificationProfileApi verificationProfileApi,
                                                VerificationPhraseApi verificationPhraseApi,
                                                SpeakerRecognitionApi speakerRecognitionApi,
                                                Moshi moshi) {
        this.verificationProfileApi = verificationProfileApi;
        this.verificationPhraseApi = verificationPhraseApi;
        this.speakerRecognitionApi = speakerRecognitionApi;
        this.moshi = moshi;
    }

    @Override
    public CreateProfileResponse createProfile(String locale) throws CreateProfileException, IOException {
        Response<CreateProfileResponse> response = verificationProfileApi.createProfile(new ProfileLocale(locale)).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new CreateProfileException(errorResponse.error.message);
            } else {
                throw new CreateProfileException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public Profile getProfile(UUID id) throws GetProfileException, IOException {
        Response<Profile> response = verificationProfileApi.getProfile(id.toString()).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new GetProfileException(errorResponse.error.message);
            } else {
                throw new GetProfileException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public List<Profile> getProfiles() throws GetProfileException, IOException {
        Response<List<Profile>> response = verificationProfileApi.getProfiles().execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new GetProfileException(errorResponse.error.message);
            } else {
                throw new GetProfileException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public void deleteProfile(UUID id) throws DeleteProfileException, IOException {
        Response<Void> response = verificationProfileApi.deleteProfile(id.toString()).execute();

        if (!response.isSuccessful()) {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new DeleteProfileException(errorResponse.error.message);
            } else {
                throw new DeleteProfileException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public List<VerificationPhrase> getPhrases(String locale) throws PhrasesException, IOException {
        Response<List<VerificationPhrase>> response = verificationPhraseApi.getPhrases(locale).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new PhrasesException(errorResponse.error.message);
            } else {
                throw new PhrasesException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public Enrollment enroll(InputStream audioStream, UUID id) throws EnrollmentException, IOException {
        return null;
    }

    @Override
    public Enrollment enroll(File audio, UUID id) throws EnrollmentException, IOException {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody requestBody = RequestBody.create(mediaType, audio);

        Response<Enrollment> response = verificationProfileApi
                .enroll(requestBody, id.toString())
                .execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new EnrollmentException(errorResponse.error.message);
            } else {
                throw new EnrollmentException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public Verification verify(InputStream audioStream, UUID id) throws VerificationException, IOException {
        return null;
    }

    @Override
    public Verification verify(File audio, UUID id) throws VerificationException, IOException {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody requestBody = RequestBody.create(mediaType, audio);

        Response<Verification> response = speakerRecognitionApi
                .verify(requestBody, id.toString())
                .execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new VerificationException(errorResponse.error.message);
            } else {
                throw new VerificationException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public void resetEnrollments(UUID id) throws ResetEnrollmentsException, IOException {
        Response<Void> response = verificationProfileApi.resetEnrollments(id.toString()).execute();

        if (!response.isSuccessful()) {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new ResetEnrollmentsException(errorResponse.error.message);
            } else {
                throw new ResetEnrollmentsException(String.valueOf(response.code()));
            }
        }
    }
}
