package gameobjects;

import fileio.CardInput;
import gameobjects.Card;
import utils.Constants;

public class Environment extends Card {

    private final CardInput card;
    public Environment(CardInput card) {
        this.card = card;
    }

    public CardInput getCard() {
        return card;
    }

    @Override
    public CardInput getInstance() {
        return card;
    }

    @Override
    public Integer getRow() {
        return null;
    }

    @Override
    public String getPower() {
        return null;
    }

    @Override
    public String getAttribute() {
        return Constants.ENVIRONMENT;
    }
}
