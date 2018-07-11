package com.microsoft.cognitive.speakerrecognition;

import com.microsoft.cognitive.speakerrecognition.contract.CreateProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.DeleteProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.EnrollmentException;
import com.microsoft.cognitive.speakerrecognition.contract.GetProfileException;
import com.microsoft.cognitive.speakerrecognition.contract.IdentificationProfileApi;
import com.microsoft.cognitive.speakerrecognition.contract.ResetEnrollmentsException;
import com.microsoft.cognitive.speakerrecognition.contract.SpeakerRecognitionApi;
import com.microsoft.cognitive.speakerrecognition.contract.identification.CreateProfileResponse;
import com.microsoft.cognitive.speakerrecognition.contract.identification.EnrollmentOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationException;
import com.microsoft.cognitive.speakerrecognition.contract.identification.IdentificationOperation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.OperationLocation;
import com.microsoft.cognitive.speakerrecognition.contract.identification.Profile;
import com.microsoft.cognitive.speakerrecognition.contract.ProfileLocale;
import com.squareup.moshi.Moshi;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Response;

/**
 * This class abstracts all the identification service calls
 */
public class SpeakerIdentificationRetrofitClient implements SpeakerIdentificationClient {

    /**
     * The operation location header field
     */
    private static final String _OPERATION_LOCATION_HEADER = "Operation-Location";

    private final IdentificationProfileApi identificationProfileApi;
    private final SpeakerRecognitionApi speakerRecognitionApi;
    private Moshi moshi;

    protected SpeakerIdentificationRetrofitClient(IdentificationProfileApi identificationProfileApi, SpeakerRecognitionApi speakerRecognitionApi, Moshi moshi) {
        this.identificationProfileApi = identificationProfileApi;
        this.speakerRecognitionApi = speakerRecognitionApi;
        this.moshi = moshi;
    }

    // The use of input stream leaves closing the stream to the caller of the API... not terribly nice (for OkHttp users)
    // OkHttp needs to be able to read the body content for redirects or retries, so it can't be closed prematurely
    @Override
    public OperationLocation identify(InputStream audioStream, List<UUID> ids) throws IdentificationException, IOException {
        return null;
    }

    // The use of input stream leaves closing the stream to the caller of the API... not terribly nice (for OkHttp users)
    // OkHttp needs to be able to read the body content for redirects or retries, so it can't be closed prematurely
    @Override
    public OperationLocation identify(InputStream audioStream, List<UUID> ids, boolean forceShortAudio) throws IdentificationException, IOException {
        return null;
    }

    @Override
    public OperationLocation identify(File audio, List<UUID> ids, boolean shortAudio) throws IdentificationException, IOException {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody requestBody = RequestBody.create(mediaType, audio);

        Response<Void> response = speakerRecognitionApi.identify(requestBody, buildProfileIdsString(ids), shortAudio).execute();

        if (response.isSuccessful()) {
            OperationLocation opLoc = new OperationLocation();
            opLoc.Url = response.headers().get(_OPERATION_LOCATION_HEADER);
            return opLoc;
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new IdentificationException(errorResponse.error.message);
            } else {
                throw new IdentificationException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public CreateProfileResponse createProfile(String locale) throws CreateProfileException, IOException {
        Response<CreateProfileResponse> response = identificationProfileApi.createProfile(new ProfileLocale(locale)).execute();

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
    public void deleteProfile(UUID id) throws DeleteProfileException, IOException {
        Response<Void> response = identificationProfileApi.deleteProfile(id.toString()).execute();

        if (response.isSuccessful()) {
            return;
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new DeleteProfileException(errorResponse.error.message);
            } else {
                throw new DeleteProfileException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public Profile getProfile(UUID id) throws GetProfileException, IOException {
        Response<Profile> response = identificationProfileApi.getProfile(id.toString()).execute();

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
        Response<List<Profile>> response = identificationProfileApi.getProfiles().execute();

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
    public OperationLocation enroll(InputStream audioStream, UUID id) throws EnrollmentException, IOException {
        // The use of input stream leaves closing the stream to the caller of the API... not terribly nice (for OkHttp users)
        // OkHttp needs to be able to read the body content for redirects or retries, so it can't be closed prematurely
        return null;
    }

    @Override
    public OperationLocation enroll(InputStream audioStream, UUID id, boolean forceShortAudio) throws EnrollmentException, IOException {
        // The use of input stream leaves closing the stream to the caller of the API... not terribly nice (for OkHttp users)
        // OkHttp needs to be able to read the body content for redirects or retries, so it can't be closed prematurely
        return null;
    }

    @Override
    public OperationLocation enroll(File audio, UUID id, boolean forceShortAudio) throws EnrollmentException, IOException {
        MediaType mediaType = MediaType.parse("multipart/form-data");
        RequestBody requestBody = RequestBody.create(mediaType, audio);

        Response<Void> response = identificationProfileApi
                .enroll(requestBody, id.toString(), forceShortAudio)
                .execute();

        if (response.isSuccessful()) {
            OperationLocation opLoc = new OperationLocation();
            opLoc.Url = response.headers().get(_OPERATION_LOCATION_HEADER);
            return opLoc;
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
    public EnrollmentOperation checkEnrollmentStatus(OperationLocation location) throws EnrollmentException, IOException {
        Response<EnrollmentOperation> response = speakerRecognitionApi.checkEnrollmentStatus(location.Url).execute();

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
    public IdentificationOperation checkIdentificationStatus(OperationLocation location) throws IdentificationException, IOException {
        Response<IdentificationOperation> response = speakerRecognitionApi.checkIdentificationStatus(location.Url).execute();

        if (response.isSuccessful()) {
            return response.body();
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new IdentificationException(errorResponse.error.message);
            } else {
                throw new IdentificationException(String.valueOf(response.code()));
            }
        }
    }

    @Override
    public void resetEnrollments(UUID id) throws ResetEnrollmentsException, IOException {
        Response<Void> response = identificationProfileApi.resetEnrollments(id.toString()).execute();

        if (response.isSuccessful()) {
            return;
        } else {
            ErrorResponse errorResponse = moshi.adapter(ErrorResponse.class).fromJson(response.errorBody().source());
            if (errorResponse != null) {
                throw new ResetEnrollmentsException(errorResponse.error.message);
            } else {
                throw new ResetEnrollmentsException(String.valueOf(response.code()));
            }
        }
    }

    /**
     * Converts a list of profile IDs to a single string
     *
     * @param ids List of profile IDs
     * @return String of profile IDs separated by a comma(",")
     */
    String buildProfileIdsString(List<UUID> ids) {
        StringBuilder builder = new StringBuilder();
        Iterator<?> iter = ids.iterator();
        while (iter.hasNext()) {
            builder.append(iter.next());
            if (!iter.hasNext()) {
                break;
            }
            builder.append(",");
        }
        return builder.toString();
    }
}
