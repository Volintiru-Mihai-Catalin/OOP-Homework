package utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import gameUtils.Card;

import java.util.ArrayList;

public class Functions {

    private static final ObjectMapper mapper = new ObjectMapper();


    public static ObjectNode createNodeFromHeroCard(Card card) {
        ObjectNode node = mapper.createObjectNode();
        node.put("mana", card.getInstance().getMana());
        node.put("description", card.getInstance().getDescription());
        ArrayNode colors = mapper.createArrayNode();
        for (String color : card.getInstance().getColors()) {
            colors.add(color);
        }
        node.set("colors", colors);
        node.put("name", card.getInstance().getName());
        node.put("health", card.getInstance().getHealth());

        return node;
    }
    public static ObjectNode createNodeFromMinionOrEnvCard(Card card){
        ObjectNode node = mapper.createObjectNode();
        node.put("mana", card.getInstance().getMana());
        if (card.getAttribute().compareTo(Constants.ENVIRONMENT) != 0) {
            node.put("attackDamage", card.getInstance().getAttackDamage());
            node.put("health", card.getInstance().getHealth());
        }
        node.put("description", card.getInstance().getDescription());
        ArrayNode colors = mapper.createArrayNode();
        for (String color : card.getInstance().getColors()) {
            colors.add(color);
        }
        node.set("colors", colors);
        node.put("name", card.getInstance().getName());
        return node;
    }

    public static ArrayNode createArrayNodeFromCards(ArrayList<Card> cards) {
        ArrayNode array = mapper.createArrayNode();
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
