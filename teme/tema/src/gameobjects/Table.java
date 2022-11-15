package gameobjects;

import utils.Constants;

import java.util.ArrayList;

public final class Table {
    private final ArrayList<Card> rowOnePlayerOne = new ArrayList<>();
    private final ArrayList<Card> rowTwoPlayerOne = new ArrayList<>();
    private final ArrayList<Card> rowOnePlayerTwo = new ArrayList<>();
    private final ArrayList<Card> rowTwoPlayerTwo = new ArrayList<>();

    public Table() {

    }

    public ArrayList<Card> getRowOnePlayerOne() {
        return rowOnePlayerOne;
    }

    public ArrayList<Card> getRowOnePlayerTwo() {
        return rowOnePlayerTwo;
    }

    public ArrayList<Card> getRowTwoPlayerOne() {
        return rowTwoPlayerOne;
    }

    public ArrayList<Card> getRowTwoPlayerTwo() {
        return rowTwoPlayerTwo;
    }

    public boolean playCard(final Card card, final int playerIdx) {
        if (card.getAttribute().compareTo(Constants.MINION) == 0) {
            if (playerIdx == 1) {
                if (card.getRow() == 1) {
                    if (rowOnePlayerOne.size() < Constants.MAXCARDS) {
                        rowOnePlayerOne.add(card);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (rowTwoPlayerOne.size() < Constants.MAXCARDS) {
                        rowTwoPlayerOne.add(card);
                        return true;
                    } else {
                        return false;
                    }
                }
            } else {
                if (card.getRow() == 1) {
                    if (rowOnePlayerTwo.size() < Constants.MAXCARDS) {
                        rowOnePlayerTwo.add(card);
                        return true;
                    } else {
                        return false;
                    }
                } else {
                    if (rowTwoPlayerTwo.size() < Constants.MAXCARDS) {
                        rowTwoPlayerTwo.add(card);
                        return true;
                    } else {
                        return false;
                    }
                }
            }
        }

        return false;
    }

    public Card getCardAtPosition(final int x, final int y) {

        switch (x) {
            case (Constants.ZERO) -> {
                if (rowOnePlayerTwo.size() > y) {
                    return rowOnePlayerTwo.get(y);
                }
            }
            case (Constants.ONE) -> {
                if (rowTwoPlayerTwo.size() > y) {
                    return rowTwoPlayerTwo.get(y);
                }
            }
            case (Constants.TWO) -> {
                if (rowTwoPlayerOne.size() > y) {
                    return rowTwoPlayerOne.get(y);
                }
            }

            case (Constants.THREE) -> {
                if (rowOnePlayerOne.size() > y) {
                    return rowOnePlayerOne.get(y);
                }
            }
            default -> {
            }
        }

        return null;
    }

    public void useEnvCardOnRow(final Card card, final int rowIdx) {
        switch (rowIdx) {
            case (Constants.ZERO) -> ((Environment) card).usePower(rowOnePlayerTwo, this);
            case (Constants.ONE) -> ((Environment) card).usePower(rowTwoPlayerTwo, this);
            case (Constants.TWO) -> ((Environment) card).usePower(rowTwoPlayerOne, this);
            case (Constants.THREE) -> ((Environment) card).usePower(rowOnePlayerOne, this);
            default -> {
            }
        }
    }

    public void stealCardFromRow(final ArrayList<Card> row, final Card card) {
        if (card != null) {
            ArrayList<Card> mirrorRow = null;
            if (row.equals(rowOnePlayerOne)) {
                mirrorRow = rowOnePlayerTwo;
            } else if (row.equals(rowOnePlayerTwo)) {
                mirrorRow = rowOnePlayerOne;
            } else if (row.equals(rowTwoPlayerOne)) {
                mirrorRow = rowTwoPlayerTwo;
            } else if (row.equals(rowTwoPlayerTwo)) {
                mirrorRow = rowTwoPlayerOne;
            }

            if (mirrorRow != null) {
                if (mirrorRow.size() < Constants.MAXCARDS) {
                    row.remove(card);
                    mirrorRow.add(card);
                }
            }
        }
    }

    public void removeDeadMinions(final ArrayList<Card> row) {
        row.removeIf(card -> card.getInstance().getHealth() < 1);
    }

    public void unfreezeMinions() {
        for (Card card : rowOnePlayerOne) {
            card.freeze(Constants.ISNOTFROZEN);
        }
        for (Card card : rowOnePlayerTwo) {
            card.freeze(Constants.ISNOTFROZEN);
        }
        for (Card card : rowTwoPlayerOne) {
            card.freeze(Constants.ISNOTFROZEN);
        }
        for (Card card : rowTwoPlayerTwo) {
            card.freeze(Constants.ISNOTFROZEN);
        }
    }
}
