package be.henallux.studycard.ui.home;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

import be.henallux.studycard.repositories.web.RetrofitConfigurationService;
import be.henallux.studycard.repositories.web.StudyCardWebService;
import be.henallux.studycard.services.mappers.DeckMapper;
import be.henallux.studycard.models.Deck;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.repositories.web.dto.DeckDto;
import be.henallux.studycard.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeViewModel extends AndroidViewModel {
    private MutableLiveData<List<Deck>> _decks = new MutableLiveData<>();
    private LiveData<List<Deck>> decks = _decks;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private StudyCardWebService mStudyCardWebService;
    private DeckMapper mDeckMapper;

    public HomeViewModel(@NonNull Application application) {
        super(application);
        this.mStudyCardWebService = RetrofitConfigurationService.getInstance(application).mStudyCardWebService();
        this.mDeckMapper = DeckMapper.getInstance();
    }

    public void getAllDecksFromUser(String pseudo){
        mStudyCardWebService.getDecksUser(pseudo).enqueue(new Callback<List<DeckDto>>() {
            @Override
            public void onResponse(@NotNull Call<List<DeckDto>> call, @NotNull Response<List<DeckDto>> response) {
                if (response.isSuccessful()) {
                    List<Deck> newList = new ArrayList<>();
                    response.body().forEach((deck -> newList.add(mDeckMapper.mapToDeck(deck))));
                    _decks.setValue(newList);
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(@NotNull Call<List<DeckDto>> call, @NotNull Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }
            }
        });
    }

    public LiveData<List<Deck>> getDecks() {
        return decks;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
