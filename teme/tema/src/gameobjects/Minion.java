package gameobjects;


import fileio.CardInput;
import utils.Constants;

public final class Minion extends Card {

    private boolean frozen;
    private String power = null;
    private final CardInput card;
    private Integer row = null;

    public Minion(final CardInput card) {
        this.card = card;
        frozen = false;
        switch (card.getName()) {
            case (Constants.GOLIATH), (Constants.WARDEN) -> setPower("Tank");
            case (Constants.THERIPPER) -> setPower("Weak Knees");
            case (Constants.MIRAJ) -> setPower("Skyjack");
            case (Constants.THECURSEDONE) -> setPower("Shapeshift");
            case (Constants.DISCIPLE) -> setPower("God's Plan");
            default -> {
            }
        }
        switch (card.getName()) {
            case (Constants.GOLIATH), (Constants.WARDEN), (Constants.THERIPPER),
                 (Constants.MIRAJ) -> setRow(2);
            case (Constants.SENTINEL), (Constants.BERSERKER), (Constants.THECURSEDONE),
                 (Constants.DISCIPLE) -> setRow(1);
            default -> {
            }
        }
    }

    public void usePower() {

    }

    private void setPower(final String power) {
        this.power = power;
    }

    private void setRow(final Integer row) {
        this.row = row;
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
    public void freeze(final boolean freeze) {
        this.frozen = freeze;
    }

    public boolean isFrozen() {
        return this.frozen;
    }
    @Override
    public CardInput getInstance() {
        return card;
    }
}
