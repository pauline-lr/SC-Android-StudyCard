package be.henallux.studycard.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import be.henallux.studycard.databinding.FragmentLoginBinding;
import be.henallux.studycard.ui.MainActivity;

public class LoginFragment extends Fragment {
    private FragmentLoginBinding binding;
    private LoginViewModel viewModel;

    public LoginFragment() {}

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = FragmentLoginBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        binding.loginButton.setOnClickListener(view -> this.sendRequest());

        binding.registerButton.setOnClickListener(view -> Toast.makeText(getActivity(), "Fonctionnalité supplémentaire à développer" , Toast.LENGTH_SHORT ).show());

        viewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        return binding.getRoot();
    }

    private void sendRequest() {
        String pseudo = binding.pseudo.getText().toString();
        String password = binding.password.getText().toString();
        viewModel.getTokenFromWeb(pseudo, password);
    }
    
    private void displayErrorScreen(be.henallux.studycard.models.NetworkError networkError) {
        if (networkError == null) {
            SharedPreferences sharedPref = requireContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", viewModel.getToken().getValue());
            editor.putString("pseudo", binding.pseudo.getText().toString());
            editor.commit();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            return;
        }
    }
}
