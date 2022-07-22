package be.henallux.studycard.database.mappers;

import be.henallux.studycard.models.RevisionCategory;
import be.henallux.studycard.repositories.web.dto.RevisionCategoryDto;

public class CategoryMapper {
    private static CategoryMapper instance = null;

    public static CategoryMapper getInstance() {
        if (instance == null) {
            instance = new CategoryMapper();
        }
        return instance;
    }

    public RevisionCategory mapToCategory(RevisionCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        return new RevisionCategory(dto.getId(), dto.getCategoryName(), dto.getDifficultyOrder(), dto.getDescription());
    }
}
