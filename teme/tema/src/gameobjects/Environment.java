package gameobjects;

import fileio.CardInput;
import utils.Constants;

import java.util.ArrayList;

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
        switch (card.getName()) {
            case (Constants.WINTERFELL) -> {
                for (Card cardInRow : row) {
                    cardInRow.freeze(Constants.ISFROZEN);
                }
            }
            case (Constants.FIRESTORM) -> {
                for (Card cardInRow : row) {
                    cardInRow.getInstance().setHealth(cardInRow.getInstance().getHealth() - 1);
                }
                table.removeDeadMinions(row);
            }
            case (Constants.HEARTHOUND) -> {
                int hp = 0;
                Card cardToSteal = null;
                for (Card cardInRow : row) {
                    if (cardInRow.getInstance().getHealth() > hp) {
                        hp = cardInRow.getInstance().getHealth();
                        cardToSteal = cardInRow;
                    }
                }
                table.stealCardFromRow(row, cardToSteal);
            }
            default -> {
            }
        }
    }
}
