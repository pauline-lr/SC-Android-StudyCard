package be.henallux.studycard.ui.personnalInformations;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.util.Objects;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentPersonnalInformationsBinding;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;

public class PersonnalInformationsFragment extends Fragment {
    private FragmentPersonnalInformationsBinding mPersonnalInformationsBinding;
    private PersonnalInformationsViewModel viewModel;

    public PersonnalInformationsFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(PersonnalInformationsViewModel.class);

        mPersonnalInformationsBinding = FragmentPersonnalInformationsBinding.inflate(inflater, container, false);
        mPersonnalInformationsBinding.setViewModel(viewModel);
        mPersonnalInformationsBinding.setLifecycleOwner(this.getViewLifecycleOwner());

        this.sendRequestGetClient();
        viewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        return mPersonnalInformationsBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarTitle(getString(R.string.personnal_informations));
        super.onResume();
    }

    private void sendRequestGetClient() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        viewModel.getClientFromWeb(pseudo);
        mPersonnalInformationsBinding.progressBar.setVisibility(View.VISIBLE);
        //mPersonnalInformationsBinding.personnalInformationsLayout.setVisibility(View.GONE);
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private void displayErrorScreen(NetworkError error) {
        mPersonnalInformationsBinding.progressBar.setVisibility(View.GONE);

        if (error == null) {
            mPersonnalInformationsBinding.personnalInformationsLayout.setVisibility(View.VISIBLE);
            mPersonnalInformationsBinding.errorLayout.setVisibility(View.GONE);
            changeVisibilityInformationsUser(View.VISIBLE);
            setValues();
            return;
        } else {
            changeVisibilityInformationsUser(View.GONE);
            mPersonnalInformationsBinding.errorLayout.setVisibility(View.VISIBLE);
            mPersonnalInformationsBinding.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(), requireActivity().getTheme()));
            mPersonnalInformationsBinding.errorText.setText(error.getErrorMessage());
        }
    }

    private void changeVisibilityInformationsUser(Integer visibility) {
        mPersonnalInformationsBinding.pseudo.setVisibility(visibility);
        mPersonnalInformationsBinding.pseudoTitle.setVisibility(visibility);
        mPersonnalInformationsBinding.emailAdress.setVisibility(visibility);
        mPersonnalInformationsBinding.emailAdressTitle.setVisibility(visibility);
    }

    private void setValues() {
        String pseudoText = Objects.requireNonNull(viewModel.getAccount().getValue()).pseudo;
        mPersonnalInformationsBinding.pseudo.setText(pseudoText);

        String emailText = viewModel.getAccount().getValue().email;
        mPersonnalInformationsBinding.pseudo.setText(emailText);
    }
}