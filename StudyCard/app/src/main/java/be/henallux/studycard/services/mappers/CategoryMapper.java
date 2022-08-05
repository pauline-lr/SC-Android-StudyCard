package be.henallux.studycard.services.mappers;

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


    public RevisionCategoryDto mapToCategoryDto(RevisionCategory category) {
        if (category == null) {
            return null;
        }

        return new RevisionCategoryDto(category.id, category.categoryName, category.difficultyOrder, category.description);
    }
}
