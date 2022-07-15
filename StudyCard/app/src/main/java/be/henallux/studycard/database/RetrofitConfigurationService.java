package be.henallux.studycard.database;

import android.content.Context;

import com.squareup.moshi.Moshi;
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory;
import be.henallux.studycard.utils.ConnectivityCheckInterceptor;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RetrofitConfigurationService {
    private static final String BASE_URL = "https://henallux.free.beeceptor.com/";

    // Retrofit client creation
    private Retrofit retrofitClient;

    private static StudyCardWebService pizzaWebService = null;

    private RetrofitConfigurationService(Context context) {
        initializeRetrofit(context);
    }

    private void initializeRetrofit(Context context) {
        OkHttpClient client = new OkHttpClient.Builder()
                .addInterceptor(new ConnectivityCheckInterceptor(context))
                .build();

        Moshi moshiConverter = new Moshi.Builder()
                .add(new KotlinJsonAdapterFactory())
                .build();

        this.retrofitClient = new Retrofit.Builder()
                .client(client)
                .addConverterFactory(MoshiConverterFactory.create(moshiConverter))
                .baseUrl(BASE_URL)
                .build();
    }

    public static RetrofitConfigurationService getInstance(Context context) {
        return new RetrofitConfigurationService(context);
    }

    public StudyCardWebService mStudyCardWebService() {
        if (pizzaWebService == null) {
            pizzaWebService = retrofitClient.create(StudyCardWebService.class);
        }
        return pizzaWebService;
    }
}
