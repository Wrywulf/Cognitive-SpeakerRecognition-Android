package com.microsoft.cognitive.speakerrecognition;

import com.microsoft.cognitive.speakerrecognition.contract.IdentificationProfileApi;
import com.microsoft.cognitive.speakerrecognition.contract.SpeakerRecognitionApi;
import com.microsoft.cognitive.speakerrecognition.contract.VerificationPhraseApi;
import com.microsoft.cognitive.speakerrecognition.contract.VerificationProfileApi;
import com.squareup.moshi.JsonAdapter;
import com.squareup.moshi.JsonReader;
import com.squareup.moshi.JsonWriter;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import java.io.IOException;
import java.util.Date;
import java.util.UUID;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RecognitionFactory {

    private final ObjectGraph objectGraph;

    private RecognitionFactory(final String apiKey, HttpLoggingInterceptor.Logger logger) {
        objectGraph = ObjectGraph.getInstance(apiKey, logger);
    }

    public static class Builder {

        private String apiKey;
        private HttpLoggingInterceptor.Logger logger;

        public Builder(final String apiKey) {
            this.apiKey = apiKey;
        }

        public Builder setLogger(HttpLoggingInterceptor.Logger logger) {
            this.logger = logger;
            return this;
        }

        public RecognitionFactory build() {
            return new RecognitionFactory(apiKey, logger);
        }
    }

    public SpeakerIdentificationClient createSpeakerIdentificationClient() {
        return new SpeakerIdentificationRetrofitClient(objectGraph.getIdentificationProfileApi(),
                objectGraph.getSpeakerRecognitionApi(), objectGraph.getMoshi());
    }

    public SpeakerVerificationClient createSpeakerVerificationClient() {
        return new SpeakerVerificationRetrofitClient(objectGraph.getVerificationProfileApi(),
                objectGraph.getVerificationPhraseApi(),
                objectGraph.getSpeakerRecognitionApi(),
                objectGraph.getMoshi());
    }

    private static class ObjectGraph {

        private final String apiKey;
        private final HttpLoggingInterceptor.Logger logger;
        private Moshi moshi;
        private OkHttpClient okHttpClient;
        private Retrofit retrofit;

        private static ObjectGraph INSTANCE;

        public static ObjectGraph getInstance(final String apiKey, HttpLoggingInterceptor.Logger logger) {
            if (INSTANCE == null) {
                INSTANCE = new ObjectGraph(apiKey, logger);
            }
            return INSTANCE;
        }

        private ObjectGraph(String apiKey, HttpLoggingInterceptor.Logger logger) {
            this.apiKey = apiKey;
            this.logger = logger;
        }

        public Moshi getMoshi() {
            if (moshi == null) {
                moshi = new Moshi.Builder()
                        .add(UUID.class, getUUIDAdapter())
                        .add(Date.class, new Rfc3339DateJsonAdapter())
                        .build();
            }
            return moshi;
        }

        public OkHttpClient getOkHttpClient() {
            if (okHttpClient == null) {
                okHttpClient = new OkHttpClient.Builder()
                        .addInterceptor(new Interceptor() {
                            @Override
                            public okhttp3.Response intercept(Chain chain) throws IOException {
                                Request newRequest = chain.request()
                                        .newBuilder()
                                        .addHeader("Ocp-Apim-Subscription-Key", apiKey)
                                        .build();
                                return chain.proceed(newRequest);
                            }
                        })
                        .addInterceptor(getLogger())
                        .build();
            }
            return okHttpClient;
        }

        public HttpLoggingInterceptor getLogger() {
            if (logger != null) {
                return new HttpLoggingInterceptor(logger).setLevel(HttpLoggingInterceptor.Level.HEADERS);
            } else {
                return new HttpLoggingInterceptor();
            }
        }

        public Retrofit getRetrofit() {
            if (retrofit == null) {
                retrofit = new Retrofit.Builder()
                        .addConverterFactory(MoshiConverterFactory.create(getMoshi()))
                        .client(getOkHttpClient())
                        .baseUrl(BaseUrls.COGNITIVE_SERVICES_BASE_URL)
                        .build();
            }
            return retrofit;
        }

        public IdentificationProfileApi getIdentificationProfileApi() {
            return getRetrofit().create(IdentificationProfileApi.class);
        }

        public VerificationProfileApi getVerificationProfileApi() {
            return getRetrofit().create(VerificationProfileApi.class);
        }

        public VerificationPhraseApi getVerificationPhraseApi() {
            return getRetrofit().create(VerificationPhraseApi.class);
        }

        public SpeakerRecognitionApi getSpeakerRecognitionApi() {
            return getRetrofit().create(SpeakerRecognitionApi.class);
        }


        public UUIDJsonAdapter getUUIDAdapter() {
            return new UUIDJsonAdapter();
        }
    }

    public static class UUIDJsonAdapter extends JsonAdapter<UUID> {

        @Override
        public synchronized UUID fromJson(JsonReader reader) throws IOException {
            String string = reader.nextString();
            return UUID.fromString(string);
        }

        @Override
        public synchronized void toJson(JsonWriter writer, UUID value) throws IOException {
            writer.value(value.toString());
        }
    }

}
