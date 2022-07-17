package be.henallux.studycard.database;

import java.util.List;

import be.henallux.studycard.models.Login;
import be.henallux.studycard.repositories.web.dto.DeckDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StudyCardWebService {
    // se connecter avec son compte
    @POST("v1/client/login")
    Call<String> login(@Body Login login);

    // obtenir tous les decks d'un utilisateur avec son pseudo
    // @Path car on l'obtient avec l'url et non le body
    @GET("/v1/deck/all/{pseudo}")
    Call<List<DeckDto>> getNextDecksUser(@Path("pseudo") String pseudo);
}
