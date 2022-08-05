package be.henallux.studycard.ui.card;

import android.app.Application;
import android.content.Context;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import be.henallux.studycard.database.mappers.RevisionCategoryMapper;
import be.henallux.studycard.models.Card;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.database.RetrofitConfigurationService;
import be.henallux.studycard.database.StudyCardWebService;
import be.henallux.studycard.models.RevisionCategory;
import be.henallux.studycard.repositories.web.dto.CardDto;
import be.henallux.studycard.database.mappers.CardMapper;
import be.henallux.studycard.repositories.web.dto.RevisionCategoryDto;
import be.henallux.studycard.utils.errors.NoConnectivityException;

import org.jetbrains.annotations.NotNull;

import java.util.Locale;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CardViewModel extends AndroidViewModel {
    private MutableLiveData<Card> _card = new MutableLiveData<>();
    private LiveData<Card> card = _card;

    private MutableLiveData<RevisionCategory> _category = new MutableLiveData<>();
    private LiveData<RevisionCategory> category = _category;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private StudyCardWebService studyCardWebService;
    private CardMapper cardMapper;
    private RevisionCategoryMapper mRevisionCategoryMapper;

    public CardViewModel(@NonNull Application application) {
        super(application);
        this.studyCardWebService = RetrofitConfigurationService.getInstance(application).mStudyCardWebService();
        this.cardMapper = CardMapper.getInstance();
        this.mRevisionCategoryMapper = RevisionCategoryMapper.getInstance();
    }

    public void getCardFromWeb(Integer card_id, Integer position) {
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

    public void getCategoryFromWeb(Integer category_id) {
        studyCardWebService.getCategory(category_id).enqueue(new Callback<RevisionCategoryDto>() {
            @Override
            public void onResponse(@NotNull Call<RevisionCategoryDto> call, @NotNull Response<RevisionCategoryDto> response) {
                if (response.isSuccessful()) {
                    _category.setValue(mRevisionCategoryMapper.mapToRevisionCategory(response.body()));
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(@NotNull Call<RevisionCategoryDto> call, @NotNull Throwable t) {
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

    public LiveData<RevisionCategory> getCategory() {
        return category;
    }

    public LiveData<Card> getCard() {
        return card;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
