package gameobjects;


import fileio.CardInput;
import utils.Constants;

public final class Minion extends Card {

    private boolean attacked;
    private boolean frozen;
    private String power;
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
                setPower(Constants.NOPOWER);
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

    /**
     *
     * @param attacker the card that will attack
     * @param attacked the card that will be attacked
     */
    public static void usePower(final Card attacker, final Card attacked) {
        switch (attacker.getPower()) {
            case (Constants.WEAKKNEES) -> {
                int newAttack = attacked.getInstance().getAttackDamage() - Constants.TWO;
                if (newAttack < Constants.ZERO) {
                    newAttack = 0;
                }
                attacked.getInstance().setAttackDamage(newAttack);
            }
            case (Constants.SKYJACK) -> {
                int aux = attacker.getInstance().getHealth();
                attacker.getInstance().setHealth(attacked.getInstance().getHealth());
                attacked.getInstance().setHealth(aux);
            }
            case (Constants.SHAPESHIFT) -> {
                int aux = attacked.getInstance().getHealth();
                attacked.getInstance().setHealth(attacked.getInstance().getAttackDamage());
                attacked.getInstance().setAttackDamage(aux);
            }
            case (Constants.GODSPLAN) -> {
                attacked.getInstance().setHealth(attacked.getInstance().getHealth()
                                                                                + Constants.TWO);
            }
            default -> {
            }
        }
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
        return this.power;
    }

    public String getAttribute() {
        return Constants.MINION;
    }

    @Override
    public void freeze(final boolean freeze) {
        this.frozen = freeze;
    }

    public void setAttacked(final boolean attacked) {
        this.attacked = attacked;
    }

    /**
     *
     * @return boolean value -> true if the card has attacked this turn
     */
    public boolean hasAttacked() {
        return this.attacked;
    }
    public boolean isFrozen() {
        return this.frozen;
    }
    @Override
    public CardInput getInstance() {
        return card;
    }
}
