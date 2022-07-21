package be.henallux.studycard.models;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;


@Entity

public class RevisionCategory {
    @PrimaryKey(autoGenerate = true)
    public Integer id;
    public String categoryName;
    public Integer difficultyOrder;
    public String description;

    public RevisionCategory(Integer id, String categoryName, Integer difficultyOrder, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.difficultyOrder = difficultyOrder;
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "RevisionCategory{" +
                "id=" + id +
                ", category_name='" + categoryName + '\'' +
                ", difficulty_order=" + difficultyOrder +
                ", description='" + description + '\'' +
                '}';
    }
}

