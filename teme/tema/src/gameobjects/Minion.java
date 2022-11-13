package gameobjects;


import fileio.CardInput;
import utils.Constants;

public final class Minion extends Card {

    private final String power;
    private final CardInput card;
    private final Integer row;

    public Minion(final CardInput card) {
        this.card = card;
        switch (card.getName()) {
            case (Constants.GOLIATH), (Constants.WARDEN) -> power = "Tank";
            case (Constants.THERIPPER) -> power = "Weak Knees";
            case (Constants.MIRAJ) -> power = "Skyjack";
            case (Constants.THECURSEDONE) -> power = "Shapeshift";
            case (Constants.DISCIPLE) -> power = "God's Plan";
            default -> power = null;
        }
        switch (card.getName()) {
            case (Constants.GOLIATH), (Constants.WARDEN), (Constants.THERIPPER),
                 (Constants.MIRAJ) -> row = 1;
            case (Constants.SENTINEL), (Constants.BERSERKER), (Constants.THECURSEDONE),
                 (Constants.DISCIPLE) -> row = 0;
            default -> row = null;
        }
    }

    public Integer getRow() {
        return row;
    }

    public String getPower() {
        return power;
    }

    public String getAttribute() {
        return Constants.MINION;
    }
    @Override
    public CardInput getInstance() {
        return card;
    }
}
