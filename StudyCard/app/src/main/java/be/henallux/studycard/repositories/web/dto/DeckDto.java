package be.henallux.studycard.repositories.web.dto;


import com.squareup.moshi.Json;

public class DeckDto {
    @Json(name = "id") // JSON key
    private Integer id;
    public ClientDto client;
    public String deck_name;

    public DeckDto(Integer id, ClientDto client, String deck_name) {
        this.id = id;
        this.client = client;
        this.deck_name = deck_name;
    }

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

    public String getDeck_name() {
        return deck_name;
    }

    public void setDeck_name(String deck_name) {
        this.deck_name = deck_name;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "id=" + id +
                ", client=" + client.getId().toString() +
                ", deckName='" + deck_name + '\'' +
                '}';
    }
}
