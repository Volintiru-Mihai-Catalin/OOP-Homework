package gameobjects;

import fileio.CardInput;

public abstract class Card {
    /**
     * Method to get the CardInput method
     */
    public abstract CardInput getInstance();

    /**
     * Method to get the row where the card should be played
     */
    public abstract Integer getRow();

    /**
     * Method to get the attribute (card type) -> hero, env or minion
     */
    public abstract String getAttribute();

    /**
     * Method that freezes a card
     */
    public abstract void freeze(boolean freeze);

    /**
     * Method to check if the card is frozen
     */
    public abstract boolean isFrozen();

    /**
     * Method to return the power of a card
     */
    public abstract String getPower();
}
