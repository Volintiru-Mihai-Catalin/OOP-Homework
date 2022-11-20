package gameplay;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import fileio.Coordinates;
import fileio.Input;
import fileio.GameInput;
import fileio.ActionsInput;
import fileio.CardInput;
import gameobjects.Table;
import gameobjects.Player;
import gameobjects.Card;
import gameobjects.CardsConvertor;
import gameobjects.Minion;
import utils.Constants;

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
        ObjectNode node = MAPPER.createObjectNode();
        switch (action.getCommand()) {
            case (Constants.GETPLAYERDECK) -> {
                Commands.printPlayerDeck(output, node, action.getPlayerIdx(),
                                        getPlayerDeck(action.getPlayerIdx()));
            }
            case (Constants.GETPLAYERHERO) -> {
                Commands.printPlayerHero(output, node, action.getPlayerIdx(),
                                    getPlayerHero(action.getPlayerIdx()).get(0));
            }
            case (Constants.GETPLAYERTURN) -> {
                node.put("command", action.getCommand());
                node.put("output", turn);
                output.add(node);
            }
            case (Constants.ENDPLAYERTURN) -> {
                updatePlayerTurn();
                updateRound();
            }
            case (Constants.GETCARDSINHAND) -> {
                Commands.printCardsInHand(output, node, action.getPlayerIdx(),
                                            getCardsInHand(action.getPlayerIdx()));
            }
            case (Constants.PLACECARD) -> {
                Card cardToBePlayed = getCard(action.getHandIdx());
                if (cardToBePlayed != null) {
                    if (cardToBePlayed.getAttribute().compareTo(Constants.MINION) == 0) {
                        if (cardToBePlayed.getInstance().getMana() <= getPlayerMana(turn)) {
                            Card card = takeCardFromHand(action.getHandIdx());
                            boolean isOnTable = table.playCard(card, turn);
                            if (!isOnTable) {
                                remakeHandAndDeck(card, action.getHandIdx());
                                Commands.printPlaceCardError(output, node,
                                        action.getHandIdx(), Constants.ERRORROWFULL);
                            }
                        } else {
                            Commands.printPlaceCardError(output, node,
                                    action.getHandIdx(), Constants.ERRORNOTENOUGHMANA);
                        }
                    } else {
                        Commands.printPlaceCardError(output, node,
                                action.getHandIdx(), Constants.ERRORENVONTABLE);
                    }
                }
            }
            case (Constants.GETPLAYERMANA) -> {
                Commands.printPlayerMana(output, node, action.getPlayerIdx(),
                        getPlayerMana(action.getPlayerIdx()));
            }
            case (Constants.GETCARDSONTABLE) -> {
                Commands.printTable(output, node, table, MAPPER.createArrayNode());
            }
            case (Constants.GETENVCARDSINHAND) -> {
                Commands.printEnvInHand(output, node, action.getPlayerIdx(),
                        getEnvCardsInHand(action.getPlayerIdx()));
            }
            case (Constants.GETCARDATPOSITION) -> {
                Card card = table.getCardAtPosition(action.getX(), action.getY());
                if (card != null) {
                    Commands.printCardAtPos(output, node, action.getX(), action.getY(), card);
                } else {
                    Commands.printCardAtPosErrors(output, node, action.getX(), action.getY(),
                                    Constants.ERRORNOTFOUND);
                }
            }
            case (Constants.USEENVCARD) -> {
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
                                        Commands.printEnvUseErrors(output, node,
                                               action.getAffectedRow(), Constants.ERROECANNOTSTEAL,
                                               action.getHandIdx());
                                    }
                                } else {
                                    Card card = takeCardFromHand(action.getHandIdx());
                                    table.useEnvCardOnRow(card, action.getAffectedRow());
                                }
                            } else {
                                Commands.printEnvUseErrors(output, node, action.getAffectedRow(),
                                        Constants.ERRORNOTENEMYROW, action.getHandIdx());
                            }
                        } else {
                            Commands.printEnvUseErrors(output, node, action.getAffectedRow(),
                                    Constants.ERRORNOTENOUGHMANAENV, action.getHandIdx());
                        }
                    } else {
                        Commands.printEnvUseErrors(output, node, action.getAffectedRow(),
                                Constants.ERRORNOTENVCARD, action.getHandIdx());
                    }
                }
            }
            case (Constants.GETFROZENCARDS) -> {
                Commands.printFrozenCards(output, node, table);
            }
            case (Constants.USEATTACK) -> {
                Coordinates attacked = action.getCardAttacked();
                Coordinates attacker = action.getCardAttacker();
                if (checkRow(attacked.getX())) {
                    Card cardAttacker = table.getCardAtPosition(attacker.getX(), attacker.getY());
                    Card cardAttacked = table.getCardAtPosition(attacked.getX(), attacked.getY());
                    if (cardAttacker != null) {
                        if (((Minion) cardAttacker).hasAttacked()) {
                            Commands.printUseAttackErrors(output, node, attacked, attacker,
                                    Constants.ERRORALREADYATTACKED);
                        } else {
                            if (cardAttacker.isFrozen()) {
                                Commands.printUseAttackErrors(output, node, attacked, attacker,
                                        Constants.ERRORATACKERFROZEN);
                            } else {
                                if (cardAttacked != null) {
                                    if (table.hasTank(enemy)) {
                                        if (cardAttacked.getPower().
                                                                compareTo(Constants.TANK) != 0) {
                                            Commands.printUseAttackErrors(output, node, attacked,
                                                    attacker, Constants.ERRORNOTTANK);
                                        } else {
                                            ((Minion) cardAttacker).
                                                    setAttacked(Constants.HASATACKED);
                                            cardAttacked.getInstance().setHealth(
                                                    cardAttacked.getInstance().getHealth()
                                                            - cardAttacker.getInstance()
                                                                    .getAttackDamage());
                                        }
                                    } else {
                                        ((Minion) cardAttacker).setAttacked(Constants.HASATACKED);
                                        cardAttacked.getInstance().setHealth(
                                                cardAttacked.getInstance().getHealth()
                                                        - cardAttacker.getInstance()
                                                        .getAttackDamage());
                                    }
                                }
                            }
                        }
                    }
                } else {
                    Commands.printUseAttackErrors(output, node, attacked, attacker,
                            Constants.ERRORCANNOTATACKFRIENDLY);
                }
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
        table.removeDeadMinions(table.getRowOnePlayerOne());
        table.removeDeadMinions(table.getRowOnePlayerTwo());
        table.removeDeadMinions(table.getRowTwoPlayerOne());
        table.removeDeadMinions(table.getRowTwoPlayerTwo());
        table.makeMinionsAbleToAttack(turn);
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
