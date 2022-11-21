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

    /**
     *
     * @param row the row where to use the power
     * @param table the table instance
     */
    public void usePower(final ArrayList<Card> row, final Table table) {
        switch (getPower()) {
            case (Constants.SUBZERO) -> {
                int maxAttack = 0;
                Card targetCard = null;
                for (Card cardInRow : row) {
                    if (cardInRow.getInstance().getHealth() > maxAttack) {
                        maxAttack = cardInRow.getInstance().getHealth();
                        targetCard = cardInRow;
                    }
                }
                if (targetCard != null) {
                    targetCard.freeze(Constants.ISFROZEN);
                }
            }
            case (Constants.LOWBLOW) -> {
                int maxAttack = 0;
                Card targetCard = null;
                for (Card cardInRow : row) {
                    if (cardInRow.getInstance().getHealth() > maxAttack) {
                        maxAttack = cardInRow.getInstance().getHealth();
                        targetCard = cardInRow;
                    }
                }
                if (targetCard != null) {
                    targetCard.getInstance().setHealth(Constants.ZERO);
                    table.removeDeadMinions(row);
                }
            }
            case (Constants.EARTHBORN) -> {
                for (Card cardInRow : row) {
                    cardInRow.getInstance().setHealth(cardInRow.getInstance().getHealth()
                                                                                + Constants.ONE);
                }
            }
            case (Constants.BLOODTHIRST) -> {
                for (Card cardInRow : row) {
                    cardInRow.getInstance().setAttackDamage(cardInRow.getInstance()
                            .getAttackDamage() + Constants.ONE);
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
