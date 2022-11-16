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

    private static int enemy = 0;
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
        if (turn == 1) {
            enemy = 2;
        } else {
            enemy = 1;
        }
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
                Card cardToBePlayed = getCard(action.getHandIdx());
                if (cardToBePlayed != null) {
                    if (cardToBePlayed.getAttribute().compareTo(Constants.MINION) == 0) {
                        if (cardToBePlayed.getInstance().getMana() <= getPlayerMana(turn)) {
                            Card card = takeCardFromHand(action.getHandIdx());
                            boolean isOnTable = table.playCard(card, turn);
                            if (!isOnTable) {
                                remakeHandAndDeck(card, action.getHandIdx());
                                node.put("command", action.getCommand());
                                node.put("handIdx", action.getHandIdx());
                                node.put("error", Constants.ERRORROWFULL);
                                output.add(node);
                            }
                        } else {
                            node.put("command", action.getCommand());
                            node.put("handIdx", action.getHandIdx());
                            node.put("error", Constants.ERRORNOTENOUGHMANA);
                            output.add(node);
                        }
                    } else {
                        node.put("command", action.getCommand());
                        node.put("handIdx", action.getHandIdx());
                        node.put("error", Constants.ERRORENVONTABLE);
                        output.add(node);
                    }
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
            case (Constants.GETENVCARDSINHAND) -> {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", action.getCommand());
                node.put("playerIdx", action.getPlayerIdx());
                node.set("output",
                    Functions.createArrayNodeFromCards(getEnvCardsInHand(action.getPlayerIdx())));
                output.add(node);
            }
            case (Constants.GETCARDATPOSITION) -> {
                Card card = table.getCardAtPosition(action.getX(), action.getY());
                if (card != null) {
                    ObjectNode node = MAPPER.createObjectNode();
                    node.put("command", action.getCommand());
                    node.put("x", action.getX());
                    node.put("y", action.getY());
                    node.set("output",
                            Functions.createNodeFromMinionOrEnvCard(card));
                    output.add(node);
                }
            }
            case (Constants.USEENVCARD) -> {
                ObjectNode node = MAPPER.createObjectNode();
                Card cardToBePlayed = getCard(action.getHandIdx());
                if (cardToBePlayed != null) {
                    if (cardToBePlayed.getAttribute().compareTo(Constants.ENVIRONMENT) == 0) {
                        if (cardToBePlayed.getInstance().getMana() <= getPlayerMana(turn)) {
                            if (checkRow(action.getAffectedRow())) {
                                if (cardToBePlayed.getInstance().getName()
                                        .compareTo(Constants.HEARTHOUND) == 0) {
                                    if (table.isRowNotFull(
                                            table.getMirrorRow(action.getAffectedRow()))) {
                                        Card card = takeCardFromHand(action.getHandIdx());
                                        table.useEnvCardOnRow(card, action.getAffectedRow());
                                    } else {
                                        node.put("command", action.getCommand());
                                        node.put("handIdx", action.getHandIdx());
                                        node.put("affectedRow", action.getAffectedRow());
                                        node.put("error", Constants.ERROECANNOTSTEAL);
                                        output.add(node);
                                    }
                                } else {
                                    Card card = takeCardFromHand(action.getHandIdx());
                                    table.useEnvCardOnRow(card, action.getAffectedRow());
                                }
                            } else {
                                node.put("command", action.getCommand());
                                node.put("handIdx", action.getHandIdx());
                                node.put("affectedRow", action.getAffectedRow());
                                node.put("affectedRow", action.getAffectedRow());
                                node.put("error", Constants.ERRORNOTENEMYROW);
                                output.add(node);
                            }
                        } else {
                            node.put("command", action.getCommand());
                            node.put("handIdx", action.getHandIdx());
                            node.put("affectedRow", action.getAffectedRow());
                            node.put("error", Constants.ERRORNOTENOUGHMANAENV);
                            output.add(node);
                        }
                    } else {
                        node.put("command", action.getCommand());
                        node.put("handIdx", action.getHandIdx());
                        node.put("affectedRow", action.getAffectedRow());
                        node.put("error", Constants.ERRORNOTENVCARD);
                        output.add(node);
                    }
                }
            }
            case (Constants.GETFROZENCARDS) -> {
                ObjectNode node = MAPPER.createObjectNode();
                node.put("command", action.getCommand());
                node.set("output", Functions.createArrayNodeFromCards(table.getFrozenCards()));
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

    private ArrayList<Card> getEnvCardsInHand(final int playerIdx) {
        if (playerIdx == 1) {
            return playerOne.getEnvCardsInHand();
        }
        return playerTwo.getEnvCardsInHand();
    }

    private Card takeCardFromHand(final int cardIdx) {
        if (turn == 1) {
            return playerOne.takeCardFromHand(cardIdx);
        }
        return playerTwo.takeCardFromHand(cardIdx);
    }

    private Card getCard(final int cardIdx) {
        if (turn == 1) {
            return playerOne.getCard(cardIdx);
        }
        return playerTwo.getCard(cardIdx);
    }

    public void updatePlayerTurn() {
        count++;
        table.unfreezeMinions(turn);
        if (turn == 1) {
            turn = 2;
            enemy = 1;
        } else {
            turn = 1;
            enemy = 2;
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

    private boolean checkRow(final int row) {
        if (turn == Constants.ONE && (row == Constants.TWO || row == Constants.THREE)) {
            return false;
        }
        if (turn == Constants.TWO && (row == Constants.ZERO || row == Constants.ONE)) {
            return false;
        }
        return true;
    }
}
