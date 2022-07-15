package be.henallux.studycard.models;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity (foreignKeys = @ForeignKey(entity = Client.class, parentColumns = "id", childColumns = "client_id"))
public class Session {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public Boolean completed;
    @ColumnInfo(name = "deck_id")
    public Deck deck;

    public Session(Integer id, Boolean completed, Deck deck) {
        this.id = id;
        this.completed = completed;
        this.deck = deck;
    }

    @Override
    public String toString() {
        return "Session{" +
                "id=" + id +
                ", completed=" + completed +
                ", deck=" + deck +
                '}';
    }
}

