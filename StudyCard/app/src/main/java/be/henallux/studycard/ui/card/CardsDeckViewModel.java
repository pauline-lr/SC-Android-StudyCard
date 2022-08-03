package be.henallux.studycard.ui.card;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.database.RetrofitConfigurationService;
import be.henallux.studycard.database.StudyCardWebService;
import be.henallux.studycard.repositories.web.dto.CardDto;
import be.henallux.studycard.database.mappers.CardMapper;
import be.henallux.studycard.utils.errors.NoConnectivityException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CardsDeckViewModel extends AndroidViewModel {
    private MutableLiveData<List<Card>> _cardsCategory = new MutableLiveData<>();
    private LiveData<List<Card>> cardsCategory  = _cardsCategory ;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private StudyCardWebService studyCardWebService;
    private CardMapper cardMapper;

    public CardsDeckViewModel(@NonNull Application application) {
        super(application);
        this.studyCardWebService = RetrofitConfigurationService.getInstance(application).mStudyCardWebService();
        this.cardMapper = CardMapper.getInstance();
    }

    public void getCardsByCategoryDeck(Integer deck_id, boolean isToStudyCards) {
        if(isToStudyCards){
            studyCardWebService.getCardsToStudyDeck(deck_id).enqueue(new Callback<List<CardDto>>() {
                @Override
                public void onResponse(@NotNull Call<List<CardDto>> call, @NotNull Response<List<CardDto>> response) {
                    if (response.isSuccessful()) {
                        List<Card> newList = new ArrayList<>();
                        response.body().forEach((card -> newList.add(cardMapper.mapToCard(card))));
                        _cardsCategory.setValue(newList);
                        _error.setValue(null);
                    } else {
                        _error.setValue(NetworkError.REQUEST_ERROR);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<CardDto>> call, @NotNull Throwable t) {
                    if (t instanceof NoConnectivityException) {
                        _error.setValue(NetworkError.NO_CONNECTION);
                    } else {
                        _error.setValue(NetworkError.TECHNICAL_ERROR);
                    }
                }
            });
        }else{
            studyCardWebService.getCardsAcquiredOfDeck(deck_id).enqueue(new Callback<List<CardDto>>() {
                @Override
                public void onResponse(@NotNull Call<List<CardDto>> call, @NotNull Response<List<CardDto>> response) {
                    if (response.isSuccessful()) {
                        List<Card> newList = new ArrayList<>();
                        response.body().forEach((card -> newList.add(cardMapper.mapToCard(card))));
                        _cardsCategory.setValue(newList);
                        _error.setValue(null);
                    } else {
                        _error.setValue(NetworkError.REQUEST_ERROR);
                    }
                }

                @Override
                public void onFailure(@NotNull Call<List<CardDto>> call, @NotNull Throwable t) {
                    if (t instanceof NoConnectivityException) {
                        _error.setValue(NetworkError.NO_CONNECTION);
                    } else {
                        _error.setValue(NetworkError.TECHNICAL_ERROR);
                    }
                }
            });
        }
    }


    public LiveData<List<Card>> getCards() {
        return cardsCategory;
    }


    public LiveData<NetworkError> getError() {
        return error;
    }
}
