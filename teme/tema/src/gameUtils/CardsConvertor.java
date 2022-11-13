package gameUtils;

import fileio.CardInput;
import utils.Constants;

import java.util.ArrayList;

public class CardsConvertor {
    public static ArrayList<Card> convertCards(ArrayList<CardInput> cardsInput) {

        ArrayList<Card> cardsArrayList = new ArrayList<Card>();

        for (CardInput card : cardsInput) {
            switch (card.getName()){
                case(Constants.SENTINEL):
                case(Constants.BERSERKER):
                case(Constants.GOLIATH):
                case(Constants.WARDEN):
                case(Constants.MIRAJ):
                case(Constants.THERIPPER):
                case(Constants.DISCIPLE):
                case(Constants.THECURSEDONE):
                    cardsArrayList.add(new Minion(card));
                    break;
                case(Constants.FIRESTORM):
                case(Constants.WINTERFELL):
                case(Constants.HEARTHOUND):
                    cardsArrayList.add(new Environment(card));
                    break;
                default:
                    cardsArrayList.add(new Hero(card));
                    break;
            }

        }

        return cardsArrayList;
    }
}
