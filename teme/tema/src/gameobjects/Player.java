package gameobjects;

import utils.Constants;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class Player {

    private final ArrayList<Card> deck;
    private final ArrayList<Card> hand = new ArrayList<>();
    private final ArrayList<Card> hero;
    private int mana = 0;

    public Player(final ArrayList<Card> deck, final ArrayList<Card> hero) {
        this.deck = deck;
        this.hero = hero;
    }

    public void shuffleDeck(final int seed) {
        Collections.shuffle(deck, new Random(seed));
    }
    public Card takeCardFromHand(final int cardIdx) {
        if (hand.size() <= cardIdx) {
            return null;
        }
        Card card = hand.get(cardIdx);

        mana -= card.getInstance().getMana();
        hand.remove(card);
        return card;
    }

    public void addCardToHandFromDeck() {
        Card card;
        if (deck.size() > 0) {
            card = deck.get(0);
            deck.remove(card);
            hand.add(card);
        }
    }

    public void remakeHandAndDeck(final Card card, final int handIdx) {
        hand.add(handIdx, card);
        mana += card.getInstance().getMana();
    }

    public ArrayList<Card> getDeck() {
        return deck;
    }

    public ArrayList<Card> getEnvCardsInHand() {
        ArrayList<Card> env = new ArrayList<>();
        for (Card card : hand) {
            if (card.getAttribute().compareTo(Constants.ENVIRONMENT) == 0) {
                env.add(card);
            }
        }
        return env;
    }

    public ArrayList<Card> getHand() {
        return hand;
    }

    public ArrayList<Card> getHero() {
        return hero;
    }

    public int getMana() {
        return mana;
    }

    public void addMana(final int manaAmount) {
        mana += manaAmount;
    }

    public Card getCard(final int handIdx) {
        if (hand.size() <= handIdx) {
            return null;
        }
        return hand.get(handIdx);
    }
}
