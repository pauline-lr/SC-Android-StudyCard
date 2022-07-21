package be.henallux.studycard.models;
import androidx.annotation.NonNull;
import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.ForeignKey;
import androidx.room.PrimaryKey;


@Entity (foreignKeys = @ForeignKey(entity = Client.class, parentColumns = "id", childColumns = "client_id"))
public class Deck {
    @PrimaryKey(autoGenerate = true)
    public int id;
    @ColumnInfo(name = "client_id")
    public Client client;
    public String deckName;

    public Deck(int id, Client client, String deckName) {
        this.id = id;
        this.client = client;
        this.deckName = deckName;
    }

    @Override
    public String toString() {
        return "Deck{" +
                "id=" + id +
                ", client_id=" + client +
                ", deck_name='" + deckName + '\'' +
                '}';
    }
}

