package be.henallux.studycard.database.mappers;

import be.henallux.studycard.models.Deck;
import be.henallux.studycard.repositories.web.dto.DeckDto;

public class DeckMapper {
    private static DeckMapper instance = null;

    public static DeckMapper getInstance() {
        if (instance == null) {
            instance = new DeckMapper();
        }
        return instance;
    }

    public Deck mapToDeck(DeckDto dto) {
        if (dto == null) {
            return null;
        }

        return new Deck(dto.getId(), ClientMapper.getInstance().mapToClient(dto.getClient()), dto.getDeckName());
    }
}
