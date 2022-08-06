package be.henallux.studycard.ui.card;

import android.app.Application;

import androidx.annotation.NonNull;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CardViewModel extends AndroidViewModel {
    private final MutableLiveData<Card> _card = new MutableLiveData<>();
    private final LiveData<Card> card = _card;

    private final MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private final LiveData<NetworkError> error = _error;

    private final StudyCardWebService studyCardWebService;
    private final CardMapper cardMapper;

    public CardViewModel(@NonNull Application application) {
        super(application);
        this.studyCardWebService = RetrofitConfigurationService.getInstance(application).mStudyCardWebService();
        this.cardMapper = CardMapper.getInstance();
    }

    public void getCardByPositionFromWeb(Integer card_id, Integer position) {
        studyCardWebService.getCardByPosition(card_id, position).enqueue(new Callback<CardDto>() {
            @Override
            public void onResponse(@NotNull Call<CardDto> call, @NotNull Response<CardDto> response) {
                if (response.isSuccessful()) {
                    _card.setValue(cardMapper.mapToCard(response.body()));
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CardDto> call, @NotNull Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }

            }
        });
    }

    public void getCardFromWeb(Integer card_id) {
        studyCardWebService.getCard(card_id).enqueue(new Callback<CardDto>() {
            @Override
            public void onResponse(@NotNull Call<CardDto> call, @NotNull Response<CardDto> response) {
                if (response.isSuccessful()) {
                    _card.setValue(cardMapper.mapToCard(response.body()));
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CardDto> call, @NotNull Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }

            }
        });
    }

    public void updateCategoryOfCard(Integer id, Integer category_id) {
        studyCardWebService.updateCard(id, category_id).enqueue(new Callback<CardDto>() {
            @Override
            public void onResponse(@NotNull Call<CardDto> call, @NotNull Response<CardDto> response) {
                if (response.isSuccessful()) {
                    _card.setValue(cardMapper.mapToCard(response.body()));
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(@NotNull Call<CardDto> call, @NotNull Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public LiveData<Card> getCard() {
        return card;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
