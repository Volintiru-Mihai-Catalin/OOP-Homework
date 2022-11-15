package gameobjects;

import fileio.CardInput;
import utils.Constants;

public final class Hero extends Card {
    private String power = null;
    private final CardInput card;

    public Hero(final CardInput card) {
        this.card = card;
        card.setHealth(Constants.HEROHP);
        switch (card.getName()) {
            case (Constants.LORDROYCE) -> setPower("Sub-Zero");
            case (Constants.EMPRESSTHORINA) -> setPower("Low Blow");
            case (Constants.KINGMUDFACE) -> setPower("Earth Born");
            case (Constants.GENERALKOCIORAW) -> setPower("Blood Thirst");
            default -> {
            }
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

    private void setPower(final String power) {
        this.power = power;
    }

    @Override
    public String getAttribute() {
        return Constants.HERO;
    }
}
