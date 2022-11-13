package gameUtils;

import fileio.CardInput;
import utils.Constants;

public class Hero extends Card {
    private final String power;
    private final CardInput card;

    public Hero(CardInput card) {
        this.card = card;
        card.setHealth(30);
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
        return -1;
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
