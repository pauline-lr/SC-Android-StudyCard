package be.henallux.studycard.models;

import androidx.room.Entity;

@Entity
public class Login {
    public String pseudo;
    public String password;

    public Login(String pseudo, String password) {
        this.pseudo = pseudo;
        this.password = password;
    }
}
