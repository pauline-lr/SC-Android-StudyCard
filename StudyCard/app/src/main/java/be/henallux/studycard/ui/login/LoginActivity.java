package be.henallux.studycard.ui.login;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import be.henallux.studycard.R;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "Login";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);
    }

}
