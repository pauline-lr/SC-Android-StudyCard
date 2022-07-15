package be.henallux.studycard.repositories.web.dto;

import com.squareup.moshi.Json;

public class SessionDto {
    @Json(name = "id") // JSON key
    public Integer id;
    public Boolean completed;
    public DeckDto deck;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Boolean getCompleted() {
        return completed;
    }

    public void setCompleted(Boolean completed) {
        this.completed = completed;
    }

    public DeckDto getDeck() {
        return deck;
    }

    public void setDeck(DeckDto deck) {
        this.deck = deck;
    }
}
