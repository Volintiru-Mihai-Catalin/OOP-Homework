package gameobjects;

import fileio.CardInput;
import utils.Constants;

public final class Hero extends Card {
    private final String power;
    private final CardInput card;

    public Hero(final CardInput card) {
        this.card = card;
        card.setHealth(Constants.HEROHP);
        switch (card.getName()) {
            case (Constants.LORDROYCE) -> power = "Sub-Zero";
            case (Constants.EMPRESSTHORINA) -> power = "Low Blow";
            case (Constants.KINGMUDFACE) -> power = "Earth Born";
            case (Constants.GENERALKOCIORAW) -> power = "Blood Thirst";
            default -> power = null;
        }
    }
    @Override
    public CardInput getInstance() {
        return card;
    }

    @Override
    public Integer getRow() {
        return 0;
    }

    @Override
    public String getPower() {
        return power;
    }

    @Override
    public String getAttribute() {
        return Constants.HERO;
    }
}
