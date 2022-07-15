package be.henallux.studycard.database;

import be.henallux.studycard.models.Login;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.POST;

public interface StudyCardWebService {
    // routes
    @POST("user/login")
    Call<String> login(@Body Login login);
}
