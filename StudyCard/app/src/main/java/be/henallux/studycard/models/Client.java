package be.henallux.studycard.models;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Client {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public String pseudo;
    public String password;
    public String email;
    public Boolean isAdmin;

    public Client(Integer idClient, String pseudo, String password, String email, Boolean isAdmin) {
        this.id = idClient;
        this.pseudo = pseudo;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    @Override
    public String toString() {
        return "Client{" +
                "id=" + id +
                ", pseudo='" + pseudo + '\'' +
                ", password='" + password + '\'' +
                ", email='" + email + '\'' +
                ", is_admin=" + isAdmin +
                '}';
    }
}

