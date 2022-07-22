package be.henallux.studycard.repositories.web.dto;


import com.squareup.moshi.Json;

public class CardDto {
    @Json(name = "id") // JSON key
    public Integer id;
    public DeckDto deck;
    public RevisionCategoryDto category;
    public String front_card;
    public String back_card;

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

    public String getFront_card() {
        return front_card;
    }

    public void setFront_card(String front_card) {
        this.front_card = front_card;
    }

    public String getBack_card() {
        return back_card;
    }

    public void setBack_card(String back_card) {
        this.back_card = back_card;
    }

    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", deck=" + deck.getId().toString() +
                ", category='" + category.getId().toString() +
                ", frontCard=" + front_card + '\'' +
                ", backCard=" + back_card + '\'' +
                '}';
    }
}
