package be.henallux.studycard.models;

import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;

@Entity (foreignKeys = {
        @ForeignKey(entity = Deck.class, parentColumns = "id", childColumns = "deck_id" ),
        @ForeignKey(entity = RevisionCategory.class, parentColumns = "id", childColumns = "category_id")})
public class Card {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    @ColumnInfo(name = "deck_id")
    public Deck deck;
    @ColumnInfo(name = "category_id")
    public RevisionCategory category;
    public String frontCard;
    public String backCard;

    public Card(Integer id, Deck deck, RevisionCategory category, String frontCard, String backCard) {
        this.id = id;
        this.deck = deck;
        this.category = category;
        this.frontCard = frontCard;
        this.backCard = backCard;
    }

    @NonNull
    @Override
    public String toString() {
        return "Card{" +
                "id=" + id +
                ", decks=" + deck +
                ", category=" + category +
                ", frontCard='" + frontCard + '\'' +
                ", backCard='" + backCard + '\'' +
                '}';
    }
}

