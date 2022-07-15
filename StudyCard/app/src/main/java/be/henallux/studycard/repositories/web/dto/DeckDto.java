package be.henallux.studycard.repositories.web.dto;


import com.squareup.moshi.Json;

public class DeckDto {
    @Json(name = "id") // JSON key
    private Integer id;
    public ClientDto client;
    public String deckName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public ClientDto getClient() {
        return client;
    }

    public void setClient(ClientDto client) {
        this.client = client;
    }

    public String getDeckName() {
        return deckName;
    }

    public void setDeckName(String deckName) {
        this.deckName = deckName;
    }
}
