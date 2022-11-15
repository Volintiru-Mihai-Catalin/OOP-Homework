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
import gameobjects.Player;
import utils.Constants;
import utils.Functions;
import gameobjects.Table;

import java.util.ArrayList;

public final class GameWorkFlow {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Input inputData;
    private final ArrayList<GameInput> gameInput;

    private Table table;
    private Player playerOne;
    private Player playerTwo;

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

        ArrayList<CardInput> heroOne = new ArrayList<CardInput>();
        heroOne.add(game.getStartGame().getPlayerOneHero());

        ArrayList<CardInput> heroTwo = new ArrayList<CardInput>();
        heroTwo.add(game.getStartGame().getPlayerTwoHero());

        playerOne = new Player(CardsConvertor.convertCards(cardIOne),
                                CardsConvertor.convertCards(heroOne));
        playerTwo = new Player(CardsConvertor.convertCards(cardITwo),
                                CardsConvertor.convertCards(heroTwo));

        playerOne.shuffleDeck(game.getStartGame().getShuffleSeed());
        playerTwo.shuffleDeck(game.getStartGame().getShuffleSeed());

        playerOne.addCardToHandFromDeck();
        playerTwo.addCardToHandFromDeck();

        mana = 1;
        playerOne.addMana(mana);
        playerTwo.addMana(mana);

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
                Card card = takeCardFromHand(action.getHandIdx());
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
        if (playerIdx == 1) {
            return playerOne.getDeck();
        }
        return playerTwo.getDeck();
    }

    private ArrayList<Card> getPlayerHero(final int playerIdx) {
        if (playerIdx == 1) {
            return playerOne.getHero();
        }
        return playerTwo.getHero();
    }

    private ArrayList<Card> getCardsInHand(final int playerIdx) {
        if (playerIdx == 1) {
            return playerOne.getHand();
        }
        return playerTwo.getHand();
    }

    private Card takeCardFromHand(final int cardIdx) {
        if (turn == 1) {
            return playerOne.takeCardFromHand(cardIdx);
        }
        return playerTwo.takeCardFromHand(cardIdx);
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

            playerOne.addMana(mana);
            playerTwo.addMana(mana);
            playerOne.addCardToHandFromDeck();
            playerTwo.addCardToHandFromDeck();
        }
    }

    private int getPlayerMana(final int playerIdx) {
        if (playerIdx == 1) {
            return playerOne.getMana();
        }
        return playerTwo.getMana();
    }

    private void remakeHandAndDeck(final Card card, final int handIdx) {
        if (turn == 1) {
            playerOne.remakeHandAndDeck(card, handIdx);
        } else {
            playerTwo.remakeHandAndDeck(card, handIdx);
        }
    }
}
