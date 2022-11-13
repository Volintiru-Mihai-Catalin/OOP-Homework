package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gameobjects.Card;

import java.util.ArrayList;

public final class Functions {

    private Functions() {

    }
    private static final ObjectMapper MAPPER = new ObjectMapper();


    public static ObjectNode createNodeFromHeroCard(final Card card) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("mana", card.getInstance().getMana());
        node.put("description", card.getInstance().getDescription());
        ArrayNode colors = MAPPER.createArrayNode();
        for (String color : card.getInstance().getColors()) {
            colors.add(color);
        }
        node.set("colors", colors);
        node.put("name", card.getInstance().getName());
        node.put("health", card.getInstance().getHealth());

        return node;
    }
    public static ObjectNode createNodeFromMinionOrEnvCard(final Card card) {
        ObjectNode node = MAPPER.createObjectNode();
        node.put("mana", card.getInstance().getMana());
        if (card.getAttribute().compareTo(Constants.ENVIRONMENT) != 0) {
            node.put("attackDamage", card.getInstance().getAttackDamage());
            node.put("health", card.getInstance().getHealth());
        }
        node.put("description", card.getInstance().getDescription());
        ArrayNode colors = MAPPER.createArrayNode();
        for (String color : card.getInstance().getColors()) {
            colors.add(color);
        }
        node.set("colors", colors);
        node.put("name", card.getInstance().getName());
        return node;
    }

    public static ArrayNode createArrayNodeFromCards(final ArrayList<Card> cards) {
        ArrayNode array = MAPPER.createArrayNode();
        for (Card card : cards) {
            if (card.getAttribute().compareTo(Constants.HERO) == 0) {
                array.add(createNodeFromHeroCard(card));
            } else {
                array.add(createNodeFromMinionOrEnvCard(card));
            }
        }

        return array;
    }
}
