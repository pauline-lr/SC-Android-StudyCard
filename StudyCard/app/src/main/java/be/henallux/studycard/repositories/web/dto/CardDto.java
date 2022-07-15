package be.henallux.studycard.repositories.web.dto;


import com.squareup.moshi.Json;

public class CardDto {
    @Json(name = "id") // JSON key
    public Integer id;
    public DeckDto deck;
    public RevisionCategoryDto category;
    public String frontCard;
    public String backCard;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public DeckDto getDeck() {
        return deck;
    }

    public void setDeck(DeckDto deck) {
        this.deck = deck;
    }

    public RevisionCategoryDto getCategory() {
        return category;
    }

    public void setCategory(RevisionCategoryDto category) {
        this.category = category;
    }

    public String getFrontCard() {
        return frontCard;
    }

    public void setFrontCard(String frontCard) {
        this.frontCard = frontCard;
    }

    public String getBackCard() {
        return backCard;
    }

    public void setBackCard(String backCard) {
        this.backCard = backCard;
    }
}
