package be.henallux.studycard.repositories.web.dto;

import com.squareup.moshi.Json;

public class RevisionCategoryDto {
    @Json(name = "id") // JSON key
    public Integer id;
    public String categoryName;
    public Integer difficultyOrder;
    public String description;

    public RevisionCategoryDto(Integer id, String categoryName, Integer difficultyOrder, String description) {
        this.id = id;
        this.categoryName = categoryName;
        this.difficultyOrder = difficultyOrder;
        this.description = description;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public Integer getDifficultyOrder() {
        return difficultyOrder;
    }

    public void setDifficultyOrder(Integer difficultyOrder) {
        this.difficultyOrder = difficultyOrder;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
