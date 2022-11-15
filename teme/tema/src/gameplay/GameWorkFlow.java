package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gameobjects.Card;
import gameobjects.CardsConvertor;
import fileio.CardInput;
import fileio.Input;
import fileio.GameInput;
import fileio.ActionsInput;
import utils.Constants;
import utils.Functions;
import gameobjects.Table;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class GameWorkFlow {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Input inputData;
    private final ArrayList<GameInput> gameInput;

    private Table table;
    private ArrayList<Card> deckPlayerOne;
    private ArrayList<Card> deckPlayerTwo;
    private final ArrayList<Card> handPlayerOne = new ArrayList<Card>();
    private final ArrayList<Card> handPlayerTwo = new ArrayList<Card>();

    private ArrayList<Card> heroPlayerOne = new ArrayList<Card>();
    private ArrayList<Card> heroPlayerTwo = new ArrayList<Card>();

    private static int playerOneMana;
    private static int playerTwoMana;

    private static int count = 0;
    private static int turn = 0;
    private static int mana = 0;
    public GameWorkFlow(final Input inputData) {
        this.inputData = inputData;
        gameInput = inputData.getGames();
    }

    public void startAllGames(final ArrayNode output) {
        for (GameInput game : gameInput) {
            startGame(game);
            for (ActionsInput action : game.getActions()) {
                performAction(action, output);
            }
        }
    }

    private void startGame(final GameInput game) {
        turn = game.getStartGame().getStartingPlayer();
        int playerOneIdx = game.getStartGame().getPlayerOneDeckIdx();
        int playerTwoIdx = game.getStartGame().getPlayerTwoDeckIdx();

        ArrayList<CardInput> cardIOne = inputData.getPlayerOneDecks().getDecks().get(playerOneIdx);
        ArrayList<CardInput> cardITwo =  inputData.getPlayerTwoDecks().getDecks().get(playerTwoIdx);
        deckPlayerOne = CardsConvertor.convertCards(cardIOne);
        deckPlayerTwo = CardsConvertor.convertCards(cardITwo);

        ArrayList<CardInput> heroOne = new ArrayList<CardInput>();
        heroOne.add(game.getStartGame().getPlayerOneHero());
        heroPlayerOne = CardsConvertor.convertCards(heroOne);

        ArrayList<CardInput> heroTwo = new ArrayList<CardInput>();
        heroTwo.add(game.getStartGame().getPlayerTwoHero());
        heroPlayerTwo = CardsConvertor.convertCards(heroTwo);

        Collections.shuffle(deckPlayerOne, new Random(game.getStartGame().getShuffleSeed()));
        Collections.shuffle(deckPlayerTwo, new Random(game.getStartGame().getShuffleSeed()));

        addCardToHandFromDeck(1);
        addCardToHandFromDeck(2);

        playerOneMana = 1;
        playerTwoMana = 1;
        mana = 1;

        table = new Table();
    }

    private void performAction(final ActionsInput action, final ArrayNode output) {
        switch (action.getCommand()) {
            case (Constants.GETPLAYERDECK) -> {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", action.getCommand());
                node.put("playerIdx", action.getPlayerIdx());
                node.set("output",
                        Functions.createArrayNodeFromCards(getPlayerDeck(action.getPlayerIdx())));
                output.add(node);
            }
            case (Constants.GETPLAYERHERO) -> {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", action.getCommand());
                node.put("playerIdx", action.getPlayerIdx());
                node.set("output",
                    Functions.createNodeFromHeroCard(getPlayerHero(action.getPlayerIdx()).get(0)));
                output.add(node);
            }
            case (Constants.GETPLAYERTURN) -> {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", action.getCommand());
                node.put("output", turn);
                output.add(node);
            }
            case (Constants.ENDPLAYERTURN) -> {
                updatePlayerTurn();
                updateRound();
            }
            case (Constants.GETCARDSINHAND) -> {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", action.getCommand());
                node.put("playerIdx", action.getPlayerIdx());
                node.set("output",
                        Functions.createArrayNodeFromCards(getCardsInHand(action.getPlayerIdx())));
                output.add(node);
            }
            case (Constants.PLACECARD) -> {
                ObjectNode node = MAPPER.createObjectNode();
                Card card = takeCardFromHand(getCardsInHand(turn), action.getHandIdx());
                if (card != null) {
                    if (card.getAttribute().compareTo(Constants.MINION) == 0) {
                        boolean isOnTable = table.playCard(card, turn);
                        if (!isOnTable) {
                            remakeHandAndDeck(card, action.getHandIdx());
                            node.put("command", action.getCommand());
                            node.put("handIdx", action.getHandIdx());
                            node.put("error", Constants.ERRORROWFULL);
                            output.add(node);
                        }
                    } else {
                        remakeHandAndDeck(card, action.getHandIdx());
                        node.put("command", action.getCommand());
                        node.put("handIdx", action.getHandIdx());
                        node.put("error", Constants.ERRORENVONTABLE);
                        output.add(node);
                    }
                } else {
                    node.put("command", action.getCommand());
                    node.put("handIdx", action.getHandIdx());
                    node.put("error", Constants.ERRORNOTENOUGHMANA);
                    output.add(node);
                }
            }
            case (Constants.GETPLAYERMANA) -> {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", action.getCommand());
                node.put("playerIdx", action.getPlayerIdx());
                node.put("output", getPlayerMana(action.getPlayerIdx()));
                output.add(node);
            }
            case (Constants.GETCARDSONTABLE) -> {
                ObjectNode node = MAPPER.createObjectNode();
                ArrayNode rows = MAPPER.createArrayNode();
                node.put("command", action.getCommand());
                rows.add(Functions.createArrayNodeFromCards(table.getRowOnePlayerTwo()));
                rows.add(Functions.createArrayNodeFromCards(table.getRowTwoPlayerTwo()));
                rows.add(Functions.createArrayNodeFromCards(table.getRowTwoPlayerOne()));
                rows.add(Functions.createArrayNodeFromCards(table.getRowOnePlayerOne()));
                node.set("output", rows);

                output.add(node);
            }
            default -> {
            }
        }
    }

    private ArrayList<Card> getPlayerDeck(final int playerIdx) {
        if (playerIdx == 2) {
            return deckPlayerTwo;
        }
        return deckPlayerOne;
    }

    private ArrayList<Card> getPlayerHero(final int playerIdx) {
        if (playerIdx == 2) {
            return heroPlayerTwo;
        }
        return heroPlayerOne;
    }

    private ArrayList<Card> getCardsInHand(final int playerIdx) {
        if (playerIdx == 2) {
            return handPlayerTwo;
        }
        return handPlayerOne;
    }

    private Card takeCardFromHand(final ArrayList<Card> hand, final int cardIdx) {
        if (hand.size() <= cardIdx) {
            return null;
        }
        Card card = hand.get(cardIdx);

        if (turn == 1) {
            if (playerOneMana >= card.getInstance().getMana()) {
                playerOneMana -= card.getInstance().getMana();
            } else {
                return null;
            }
        } else {
            if (playerTwoMana >= card.getInstance().getMana()) {
                playerTwoMana -= card.getInstance().getMana();
            } else {
                return null;
            }
        }
        hand.remove(card);
        return card;
    }

    private void addCardToHandFromDeck(final int playerIdx) {
        Card card;
        if (playerIdx == 1) {
            if (deckPlayerOne.size() > 0) {
                card = deckPlayerOne.get(0);
                deckPlayerOne.remove(card);
                handPlayerOne.add(card);
            }
        }
        if (playerIdx == 2) {
            if (deckPlayerTwo.size() > 0) {
                card = deckPlayerTwo.get(0);
                deckPlayerTwo.remove(card);
                handPlayerTwo.add(card);
            }
        }
    }

    public void updatePlayerTurn() {
        count++;
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
        }
    }

    public void updateRound() {
        if (count % 2 == 0) {
            if (mana < Constants.MAXMANA) {
                mana++;
            }
            playerOneMana += mana;
            playerTwoMana += mana;
            addCardToHandFromDeck(1);
            addCardToHandFromDeck(2);
        }
    }

    private int getPlayerMana(final int playerIdx) {
        if (playerIdx == 1) {
            return playerOneMana;
        }
        return playerTwoMana;
    }

    private void remakeHandAndDeck(final Card card, final int handIdx) {
        getCardsInHand(turn).add(handIdx, card);
        if (turn == 1) {
            playerOneMana += card.getInstance().getMana();
        } else {
            playerTwoMana += card.getInstance().getMana();
        }
    }

    public ArrayList<Card> getHandPlayerOne() {
        return handPlayerOne;
    }

    public ArrayList<Card> getHandPlayerTwo() {
        return handPlayerTwo;
    }

    public ArrayList<Card> getDeckPlayerOne() {
        return deckPlayerOne;
    }

    public ArrayList<Card> getDeckPlayerTwo() {
        return deckPlayerTwo;
    }

    public ArrayList<GameInput> getGameInput() {
        return gameInput;
    }

    public Input getInputData() {
        return inputData;
    }
}
