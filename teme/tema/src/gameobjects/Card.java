package gameobjects;

import fileio.CardInput;

public abstract class Card {
    public abstract CardInput getInstance();
    public abstract Integer getRow();
    public abstract String getPower();
    public abstract String getAttribute();

    public abstract void freeze(boolean freeze);

    public abstract boolean isFrozen();
}
