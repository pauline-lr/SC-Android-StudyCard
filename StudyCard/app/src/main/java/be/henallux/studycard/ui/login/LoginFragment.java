package be.henallux.studycard.ui.login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.LinearInterpolator;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;

import be.henallux.studycard.R;
import be.henallux.studycard.databinding.LoginFragmentBinding;
import be.henallux.studycard.models.NetworkError;
import be.henallux.studycard.ui.MainActivity;

public class LoginFragment extends Fragment {

    private static final String TAG = "LoginFrag";
    private LoginFragmentBinding binding;
    private LoginViewModel viewModel;


    public LoginFragment() {
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        viewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        binding = LoginFragmentBinding.inflate(inflater, container, false);
        binding.setViewModel(viewModel);
        binding.setLifecycleOwner(this);

        addAnimationLogo();


        binding.register.setOnClickListener(view -> Navigation.findNavController(view)
                .navigate(R.id.action_loginFragment_to_registerFragment));
        binding.loginButton.setOnClickListener(view -> this.sendRequest());


        viewModel.getError().observe(getViewLifecycleOwner(), this::displayErrorScreen);

        return binding.getRoot();
    }


    private void addAnimationLogo() {

        RotateAnimation rotate1 = new RotateAnimation(0, 10, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate1.setInterpolator(new LinearInterpolator());
        rotate1.setDuration(1500);

        RotateAnimation rotate2 = new RotateAnimation(10, 0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        rotate2.setInterpolator(new LinearInterpolator());
        rotate2.setDuration(1500);

        ImageView logo = binding.loginLogo;

        rotate1.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(rotate2);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        rotate2.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                logo.startAnimation(rotate1);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }
        });

        logo.startAnimation(rotate1);
    }

    private void sendRequest() {
        String email = binding.username.getText().toString();
        String password = binding.password.getText().toString();
        viewModel.getTokenFromWeb(email, password);
        binding.courseErrorImage.setVisibility(View.VISIBLE);
        binding.courseErrorLayout.setVisibility(View.GONE);
    }

    private void displayErrorScreen(NetworkError error) {
        if (error == null) {
            SharedPreferences sharedPref = getContext().getSharedPreferences("login", Context.MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("token", viewModel.getToken().getValue());
            editor.putString("email", binding.username.getText().toString());
            editor.commit();
            binding.courseErrorLayout.setVisibility(View.GONE);
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);
            return;

        }
        binding.courseErrorLayout.setVisibility(View.VISIBLE);
        binding.courseErrorImage.setImageDrawable(getResources().getDrawable(error.getErrorDrawable(),
                getActivity().getTheme()));
        binding.courseErrorText.setText(error.getErrorMessage());
    }


}
