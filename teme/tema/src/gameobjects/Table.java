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
}
