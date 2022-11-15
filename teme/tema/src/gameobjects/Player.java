package gameobjects;

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

        if (mana >= card.getInstance().getMana()) {
            mana -= card.getInstance().getMana();
        } else {
            return null;
        }
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
}
