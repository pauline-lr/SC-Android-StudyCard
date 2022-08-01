package be.henallux.studycard.ui.personnalInformations;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.jetbrains.annotations.NotNull;

import be.henallux.studycard.database.RetrofitConfigurationService;
import be.henallux.studycard.database.StudyCardWebService;
import be.henallux.studycard.database.mappers.ClientMapper;
import be.henallux.studycard.database.mappers.DeckMapper;
import be.henallux.studycard.models.Client;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.repositories.web.dto.ClientDto;
import be.henallux.studycard.utils.errors.NoConnectivityException;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class PersonnalInformationsViewModel extends AndroidViewModel {
    private MutableLiveData<Client> _account = new MutableLiveData<>();
    private LiveData<Client> account = _account;

    private MutableLiveData<NetworkError> _error = new MutableLiveData<>();
    private LiveData<NetworkError> error = _error;

    private StudyCardWebService studyCardWebService;
    private ClientMapper clientMapper;

    public PersonnalInformationsViewModel(@NonNull Application application) {
        super(application);
        this.studyCardWebService = RetrofitConfigurationService.getInstance(application).mStudyCardWebService();
        this.clientMapper = ClientMapper.getInstance();
    }

    public void getClientFromWeb(String pseudo) {
        studyCardWebService.getClient(pseudo).enqueue(new Callback<ClientDto>() {
            @Override
            public void onResponse(@NotNull Call<ClientDto> call, @NotNull Response<ClientDto> response) {
                if (response.isSuccessful()) {
                    _account.setValue(clientMapper.mapToClient(response.body()));
                    _error.setValue(null);
                } else {
                    _error.setValue(NetworkError.REQUEST_ERROR);
                }
            }

            @Override
            public void onFailure(@NotNull Call<ClientDto> call, @NotNull Throwable t) {
                if (t instanceof NoConnectivityException) {
                    _error.setValue(NetworkError.NO_CONNECTION);
                } else {
                    _error.setValue(NetworkError.TECHNICAL_ERROR);
                }

            }
        });
    }

    public LiveData<Client> getAccount() {
        return account;
    }

    public LiveData<NetworkError> getError() {
        return error;
    }
}
