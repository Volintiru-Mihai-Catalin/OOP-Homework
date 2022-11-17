package gameplay;

import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gameobjects.Card;
import gameobjects.Table;
import utils.Constants;
import utils.Functions;

import java.util.ArrayList;


public final class Commands {

    private Commands() {
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param playerIdx the ID of the player
     * @param cardList the card list
     */
    public static void printPlayerDeck(final ArrayNode output, final ObjectNode node,
                                final int playerIdx, final ArrayList<Card> cardList) {
        node.put("command", Constants.GETPLAYERDECK);
        node.put("playerIdx", playerIdx);
        node.set("output", Functions.createArrayNodeFromCards(cardList));
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param playerIdx the ID of the player
     * @param hero the hero card
     */
    public static void printPlayerHero(final ArrayNode output, final ObjectNode node,
                                       final int playerIdx, final Card hero) {
        node.put("command", Constants.GETPLAYERHERO);
        node.put("playerIdx", playerIdx);
        node.set("output", Functions.createNodeFromHeroCard(hero));
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param playerIdx the ID of the player
     * @param hand the hand of the player
     */
    public static void printCardsInHand(final ArrayNode output, final ObjectNode node,
                                        final int playerIdx, final ArrayList<Card> hand) {
        node.put("command", Constants.GETCARDSINHAND);
        node.put("playerIdx", playerIdx);
        node.set("output",
                Functions.createArrayNodeFromCards(hand));
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param handIdx the ID of the card in the player's hand
     * @param error the error generated
     */
    public static void printPlaceCardError(final ArrayNode output, final ObjectNode node,
                                           final int handIdx, final String error) {
        node.put("command", Constants.PLACECARD);
        node.put("handIdx", handIdx);
        node.put("error", error);
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param playerIdx the player ID
     * @param mana the amount of mana
     */
    public static void printPlayerMana(final ArrayNode output, final ObjectNode node,
                                       final int playerIdx, final int mana) {
        node.put("command", Constants.GETPLAYERMANA);
        node.put("playerIdx", playerIdx);
        node.put("output", mana);
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param table the table
     * @param rows the node array where we add the cards
     */
    public static void printTable(final ArrayNode output, final ObjectNode node,
                                  final Table table, final ArrayNode rows) {
        node.put("command", Constants.GETCARDSONTABLE);
        rows.add(Functions.createArrayNodeFromCards(table.getRowOnePlayerTwo()));
        rows.add(Functions.createArrayNodeFromCards(table.getRowTwoPlayerTwo()));
        rows.add(Functions.createArrayNodeFromCards(table.getRowTwoPlayerOne()));
        rows.add(Functions.createArrayNodeFromCards(table.getRowOnePlayerOne()));
        node.set("output", rows);
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param playerIdx the player ID
     * @param cardList the environment cards in hand
     */
    public static void printEnvInHand(final ArrayNode output, final ObjectNode node,
                                    final int playerIdx, final ArrayList<Card> cardList) {
        node.put("command", Constants.GETENVCARDSINHAND);
        node.put("playerIdx", playerIdx);
        node.set("output",
                Functions.createArrayNodeFromCards(cardList));
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param x coordinate on X axis
     * @param y coordinate on Y axis
     * @param card the card
     */
    public static void printCardAtPos(final ArrayNode output, final ObjectNode node,
                                      final int x, final int y, final Card card) {
        node.put("command", Constants.GETCARDATPOSITION);
        node.put("x", x);
        node.put("y", y);
        node.set("output",
                Functions.createNodeFromMinionOrEnvCard(card));
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param row the row where to use the env card
     * @param error the error
     * @param handIdx the index of the env card in hand
     */
    public static void printEnvUseErors(final ArrayNode output, final ObjectNode node,
                                        final int row, final String error, final int handIdx) {
        node.put("command", Constants.USEENVCARD);
        node.put("handIdx", handIdx);
        node.put("affectedRow", row);
        node.put("error", error);
        output.add(node);
    }

    /**
     *
     * @param output the output json object
     * @param node the node json object
     * @param table
     */
    public static void printFrozenCards(final ArrayNode output, final ObjectNode node,
                                        final Table table) {
        node.put("command", Constants.GETFROZENCARDS);
        node.set("output", Functions.createArrayNodeFromCards(table.getFrozenCards()));
        output.add(node);
    }

}
