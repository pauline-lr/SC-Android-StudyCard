package be.henallux.studycard.database;

import android.content.Context;
import android.content.SharedPreferences;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;

import java.util.Date;

import com.squareup.moshi.adapters.Rfc3339DateJsonAdapter;

import be.henallux.studycard.utils.ConnectivityCheckInterceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitConfigurationService {
    private static final String BASE_URL = "http://10.0.2.2:3001";

    private Retrofit retrofitClient;

    private static StudyCardWebService studyCardWebService = null;

    private RetrofitConfigurationService(Context context) {
        initializeRetrofit(context);
    }

    private void initializeRetrofit(Context context) {
        SharedPreferences sharedPreferences = context.getSharedPreferences("login", Context.MODE_PRIVATE);

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();

        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);

        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityCheckInterceptor(context))
                .addInterceptor(logging)
                .addInterceptor(chain -> {
                    Request newRequest = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer " + sharedPreferences.getString("token", "vide"))
                            .build();
                    return chain.proceed(newRequest);
                })
                .build();


        Moshi moshiConverter = new Moshi.Builder()
                .add(new KotlinJsonAdapterFactory())
                .add(Date.class, new Rfc3339DateJsonAdapter().nullSafe())
                .build();

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        this.retrofitClient = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
                .baseUrl(BASE_URL)
                .build();
    }

    public static RetrofitConfigurationService getInstance(Context context) {
        return new RetrofitConfigurationService(context);
    }


    public StudyCardWebService mStudyCardWebService() {
        if (studyCardWebService == null) {
            studyCardWebService = retrofitClient.create(StudyCardWebService.class);
        }
        return studyCardWebService;
    }
}
