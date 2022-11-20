package gameobjects;

import fileio.CardInput;
import utils.Constants;

import java.util.ArrayList;

public final class Hero extends Card {

    private boolean hasAttacked;
    private String power = null;
    private final CardInput card;

    public Hero(final CardInput card) {
        this.hasAttacked = false;
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

    @Override
    public void freeze(final boolean freeze) {
    }

    @Override
    public boolean isFrozen() {
        return false;
    }

    public void usePower(final ArrayList<Card> row, final Table table) {
        switch (getPower()) {
            case (Constants.SUBZERO) -> {
                int maxAttack = 0;
                Card targetCard = null;
                for (Card card : row) {
                    if (card.getInstance().getHealth() > maxAttack) {
                        maxAttack = card.getInstance().getHealth();
                        targetCard = card;
                    }
                }
                if (targetCard != null) {
                    targetCard.freeze(Constants.ISFROZEN);
                }
            }
            case (Constants.LOWBLOW) -> {
                int maxAttack = 0;
                Card targetCard = null;
                for (Card card : row) {
                    if (card.getInstance().getHealth() > maxAttack) {
                        maxAttack = card.getInstance().getHealth();
                        targetCard = card;
                    }
                }
                if (targetCard != null) {
                    targetCard.getInstance().setHealth(Constants.ZERO);
                    table.removeDeadMinions(row);
                }
            }
            case (Constants.EARTHBORN) -> {
                for (Card card : row) {
                    card.getInstance().setHealth(card.getInstance().getHealth() + Constants.ONE);
                }
            }
            case (Constants.BLOODTHIRST) -> {
                for (Card card : row) {
                    card.getInstance().setAttackDamage(card.getInstance().getAttackDamage() +
                                                                                    Constants.ONE);
                }
            }
            default -> {
            }
        }
    }

    public boolean getHasAttacked() {
        return this.hasAttacked;
    }

    public void setHasAttacked(final boolean hasAttacked) {
        this.hasAttacked = hasAttacked;
    }
}
