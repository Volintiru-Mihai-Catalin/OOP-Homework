package gameobjects;

import fileio.CardInput;
import utils.Constants;

public final class Environment extends Card {

    private final CardInput card;
    public Environment(final CardInput card) {
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
        return -1;
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
