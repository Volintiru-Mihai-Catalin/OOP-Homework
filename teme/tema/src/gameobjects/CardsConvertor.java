package gameobjects;

import fileio.CardInput;
import utils.Constants;

import java.util.ArrayList;

public final class CardsConvertor {

    private CardsConvertor() {

    }

    /**
     *
     * @param cardsInput the list of cards to be converted
     * @return an array list of a wrapper type Card that has more info about the card
     */
    public static ArrayList<Card> convertCards(final ArrayList<CardInput> cardsInput) {

        ArrayList<Card> cardsArrayList = new ArrayList<Card>();

        for (CardInput card : cardsInput) {
            switch (card.getName()) {
                case (Constants.SENTINEL), (Constants.BERSERKER), (Constants.GOLIATH),
                     (Constants.WARDEN), (Constants.MIRAJ), (Constants.THERIPPER),
                     (Constants.DISCIPLE), (Constants.THECURSEDONE) ->
                        cardsArrayList.add(new Minion(card));
                case (Constants.FIRESTORM), (Constants.WINTERFELL), (Constants.HEARTHOUND) ->
                        cardsArrayList.add(new Environment(card));
                default -> cardsArrayList.add(new Hero(card));
            }

        }

        return cardsArrayList;
    }
}
