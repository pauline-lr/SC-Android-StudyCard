package be.henallux.studycard.ui.deck;

import android.app.Application;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.repositories.web.RetrofitConfigurationService;
import be.henallux.studycard.repositories.web.StudyCardWebService;
import be.henallux.studycard.repositories.web.dto.CardDto;
import be.henallux.studycard.services.mappers.CardMapper;
import be.henallux.studycard.utils.errors.NoConnectivityException;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class RevisionDeckViewModel extends AndroidViewModel {
    private MutableLiveData<List<Card>> _cardsToStudy = new MutableLiveData<>();
    private LiveData<List<Card>> cardsToStudy = _cardsToStudy;

    private MutableLiveData<List<Card>> _cardsAcquired = new MutableLiveData<>();
    private LiveData<List<Card>> cardsAcquired = _cardsAcquired;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private StudyCardWebService studyCardWebService;
    private CardMapper cardMapper;

    public RevisionDeckViewModel(@NonNull Application application) {
        super(application);
        this.studyCardWebService = RetrofitConfigurationService.getInstance(application).mStudyCardWebService();
        this.cardMapper = CardMapper.getInstance();
    }

    public void getAllCardsToStudyRevisionDeck(Integer revisionDeck_id) {
        studyCardWebService.getCardsToStudyDeck(revisionDeck_id).enqueue(new Callback<List<CardDto>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call<List<CardDto>> call, @NotNull Response<List<CardDto>> response) {
                if (response.isSuccessful()) {
                    List<Card> newList = new ArrayList<>();
                    response.body().forEach((card -> newList.add(cardMapper.mapToCard(card))));
                    _cardsToStudy.setValue(newList);
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

    public void getAllCardsAcquiredRevisionDeck(Integer revisionDeck_id) {
        studyCardWebService.getCardsAcquiredOfDeck(revisionDeck_id).enqueue(new Callback<List<CardDto>>() {
            @RequiresApi(api = Build.VERSION_CODES.N)
            @Override
            public void onResponse(@NotNull Call<List<CardDto>> call, @NotNull Response<List<CardDto>> response) {
                if (response.isSuccessful()) {
                    List<Card> newList = new ArrayList<>();
                    response.body().forEach((card -> newList.add(cardMapper.mapToCard(card))));
                    _cardsAcquired.setValue(newList);
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

    public LiveData<List<Card>> getCardsToStudy() {
        return cardsToStudy;
    }

    public LiveData<List<Card>> getCardsAcquired() {
        return cardsAcquired;
    }


    public LiveData<NetworkError> getError() {
        return error;
    }
}
