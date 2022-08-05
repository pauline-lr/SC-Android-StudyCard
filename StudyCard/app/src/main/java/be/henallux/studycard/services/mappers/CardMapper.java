package be.henallux.studycard.services.mappers;

import be.henallux.studycard.models.Card;
import be.henallux.studycard.repositories.web.dto.CardDto;

public class CardMapper {
    private static CardMapper instance = null;

    public static CardMapper getInstance() {
        if (instance == null) {
            instance = new CardMapper();
        }
        return instance;
    }

    public Card mapToCard(CardDto dto) {
        if (dto == null) {
            return null;
        }

       return new Card(dto.getId(), DeckMapper.getInstance().mapToDeck(dto.getDeck()), CategoryMapper.getInstance().mapToCategory(dto.getCategory()), dto.getFront_card(), dto.getBack_card());
    }

    public CardDto mapToCardDto(Card card) {
        return card == null ? null : new CardDto(card.id, DeckMapper.getInstance().mapToDeckDto(card.deck), CategoryMapper.getInstance().mapToCategoryDto(card.category), card.frontCard, card.backCard);
    }
}
