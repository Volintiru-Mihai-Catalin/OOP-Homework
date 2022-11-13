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

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public final class GameWorkFlow {
    private static final ObjectMapper MAPPER = new ObjectMapper();
    private final Input inputData;
    private final ArrayList<GameInput> gameInput;
    private ArrayList<Card> deckPlayerOne;
    private ArrayList<Card> deckPlayerTwo;
    private final ArrayList<Card> handPlayerOne = new ArrayList<Card>();
    private final ArrayList<Card> handPlayerTwo = new ArrayList<Card>();

    private ArrayList<Card> heroPlayerOne = new ArrayList<Card>();
    private ArrayList<Card> heroPlayerTwo = new ArrayList<Card>();
    private static int turn = 0;
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
    }

    private void performAction(final ActionsInput action, final ArrayNode output) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("command", action.getCommand());

        switch (action.getCommand()) {
            case (Constants.GETPLAYERDECK) -> {
                node.put("playerIdx", action.getPlayerIdx());
                node.set("output",
                        Functions.createArrayNodeFromCards(getPlayerDeck(action.getPlayerIdx())));
            }
            case (Constants.GETPLAYERHERO) -> {
                node.put("playerIdx", action.getPlayerIdx());
                node.set("output",
                    Functions.createNodeFromHeroCard(getPlayerHero(action.getPlayerIdx()).get(0)));
            }
            case (Constants.GETPLAYERTURN) -> node.put("output", turn);
            default -> {
            }
        }

        output.add(node);
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
        if (turn == 1) {
            turn = 2;
        } else {
            turn = 1;
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
