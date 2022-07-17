package be.henallux.studycard.repositories.web.dto;

import com.squareup.moshi.Json;

public class ClientDto {
    @Json(name = "id") // JSON key
    public Integer id;
    public String pseudo;
    public String password;
    public String email;
    public Boolean isAdmin;

    public ClientDto(Integer id, String pseudo, String password, String email, Boolean isAdmin) {
        this.id = id;
        this.pseudo = pseudo;
        this.password = password;
        this.email = email;
        this.isAdmin = isAdmin;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getPseudo() {
        return pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Boolean getAdmin() {
        return isAdmin;
    }

    public void setAdmin(Boolean admin) {
        isAdmin = admin;
    }
}
