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

    public void playCard(final Card card, final int playerIdx) {
        if (card.getAttribute().compareTo(Constants.MINION) == 0) {
            if (playerIdx == 1) {
                if (card.getRow() == 1) {
                    rowOnePlayerOne.add(card);
                } else {
                    rowTwoPlayerOne.add(card);
                }
            } else {
                if (card.getRow() == 1) {
                    rowOnePlayerTwo.add(card);
                } else {
                    rowTwoPlayerTwo.add(card);
                }
            }
        }
    }
}
