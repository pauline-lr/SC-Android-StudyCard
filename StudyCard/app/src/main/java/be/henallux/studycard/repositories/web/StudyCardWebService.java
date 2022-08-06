package be.henallux.studycard.repositories.web;

import java.util.List;

import be.henallux.studycard.models.Login;
import be.henallux.studycard.repositories.web.dto.CardDto;
import be.henallux.studycard.repositories.web.dto.ClientDto;
import be.henallux.studycard.repositories.web.dto.DeckDto;
import be.henallux.studycard.repositories.web.dto.RevisionCategoryDto;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface StudyCardWebService {
    // se connecter avec son compte
    @POST("v1/client/login")
    Call<String> login(@Body Login login);

    // obtenir tous les decks d'un utilisateur avec son pseudo
    // @Path car on l'obtient avec l'url et non le body
    @GET("/v1/deck/all/{pseudo}")
    Call<List<DeckDto>> getDecksUser(@Path("pseudo") String pseudo);

    @GET("decks")
    Call<List<DeckDto>> getDecks();

    @GET("v1/decks/{id}")
    Call<DeckDto> getDeck(@Path("id") Integer id);

    @GET("cards")
    Call<List<DeckDto>> getCards();

    @GET("/v1/card/{id}")
    Call<CardDto> getCard(@Path("id") Integer id);

    @GET("/v1/card/all/{id}")
    Call<List<CardDto>> getCardsDeck(@Path("id") Integer id);

    @GET("/v1/card/all/to-study/{id}")
    Call<List<CardDto>> getCardsToStudyDeck(@Path("id") Integer id);

    @GET("/v1/card/all/acquired/{id}")
    Call<List<CardDto>> getCardsAcquiredOfDeck(@Path("id") Integer id);

    @GET("/v1/card/all/{id}/{pos}")
    Call<CardDto> getCardByPosition(@Path("id") Integer id, @Path("pos") Integer pos);

    @GET("v1/category/{id}")
    Call<RevisionCategoryDto> getCategory(@Path("id") Integer id);

    @GET("/v1/client/{pseudo}")
    Call<ClientDto> getClient(@Path("pseudo") String pseudo);

    @PATCH("/v1/card/updateCategory/{id}/{category_id}")
    Call<CardDto> updateCard(@Path("id") Integer id, @Path("category_id") Integer category_id);
}
