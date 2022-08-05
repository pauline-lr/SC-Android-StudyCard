package be.henallux.studycard.services.mappers;

import be.henallux.studycard.models.RevisionCategory;
import be.henallux.studycard.repositories.web.dto.RevisionCategoryDto;

public class RevisionCategoryMapper {
    private static RevisionCategoryMapper instance = null;

    public static RevisionCategoryMapper getInstance() {
        if (instance == null) {
            instance = new RevisionCategoryMapper();
        }
        return instance;
    }

    public RevisionCategory mapToRevisionCategory(RevisionCategoryDto dto) {
        if (dto == null) {
            return null;
        }

        return new RevisionCategory(dto.getId(), dto.getCategoryName(),  dto.getDifficultyOrder(), dto.getDescription());
    }
}
