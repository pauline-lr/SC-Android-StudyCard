package be.henallux.studycard.ui.personnalInformations;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.FragmentPersonnalInformationsBinding;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;
import be.henallux.studycard.ui.login.LoginActivity;

public class PersonnalInformationsFragment extends Fragment {
    private FragmentPersonnalInformationsBinding mPersonnalInformationsBinding;
    private PersonnalInformationsViewModel mInformationsViewModel;

    public PersonnalInformationsFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        mInformationsViewModel = new ViewModelProvider(this).get(PersonnalInformationsViewModel.class);

        mPersonnalInformationsBinding = FragmentPersonnalInformationsBinding.inflate(inflater, container, false);
        mPersonnalInformationsBinding.setViewModel(mInformationsViewModel);
        mPersonnalInformationsBinding.setLifecycleOwner(this);

        this.sendRequestGetCustomer();
        mInformationsViewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        return mPersonnalInformationsBinding.getRoot();
    }

    @Override
    public void onResume() {
        ((MainActivity) getActivity()).setToolBarTitle(getString(R.string.personnal_informations));
        super.onResume();
    }

    private void sendRequestGetCustomer() {
        SharedPreferences sharedPreferences = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
        String pseudo = sharedPreferences.getString("pseudo", "vide");
        mInformationsViewModel.getClientFromWeb(pseudo);
        mPersonnalInformationsBinding.progressBar.setVisibility(View.VISIBLE);
        mPersonnalInformationsBinding.personnalInformationsLayout.setVisibility(View.GONE);
    }

    private void displayErrorScreen(NetworkError error) {
        mPersonnalInformationsBinding.progressBar.setVisibility(View.GONE);
        if (error == null) {
            mPersonnalInformationsBinding.personnalInformationsLayout.setVisibility(View.VISIBLE);
            //mPersonnalInformationsBinding.errorLayout.getRoot().setVisibility(View.GONE);
            setValues();
            return;
        }
        //mPersonnalInformationsBinding.errorLayout.getRoot().setVisibility(View.VISIBLE);
        mPersonnalInformationsBinding.personnalInformationsLayout.setVisibility(View.GONE);
       /* mPersonnalInformationsBinding.errorLayout.errorText.setText(error.getErrorMessage());
        mPersonnalInformationsBinding.errorLayout.errorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(),
                getActivity().getTheme()));
        mPersonnalInformationsBinding.errorLayout.floatingActionButton.setOnClickListener(view -> this.sendRequestGetCustomer());*/
    }

    private void setValues() {
        String pseudoText = Objects.requireNonNull(mInformationsViewModel.getAccount().getValue()).pseudo;
        mPersonnalInformationsBinding.pseudo.setText(pseudoText);

        String emailText = mInformationsViewModel.getAccount().getValue().email;
        mPersonnalInformationsBinding.pseudo.setText(emailText);
    }
}