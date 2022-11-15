package utils;

public final class Constants {
    private Constants() {
    }

    // Actions
    public static final String GETPLAYERDECK = "getPlayerDeck";
    public static final String GETPLAYERHERO = "getPlayerHero";
    public static final String GETPLAYERTURN = "getPlayerTurn";
    public static final String ENDPLAYERTURN = "endPlayerTurn";
    public static final String GETCARDSINHAND = "getCardsInHand";
    public static final String GETPLAYERMANA = "getPlayerMana";
    public static final String GETCARDSONTABLE = "getCardsOnTable";
    public static final String PLACECARD = "placeCard";
    public static final String GETENVCARDSINHAND = "getEnvironmentCardsInHand";
    public static final String GETCARDATPOSITION = "getCardAtPosition";
    public static final String USEENVCARD = "useEnvironmentCard";

    // Card types
    public static final String MINION = "Minion";
    public static final String ENVIRONMENT = "Environment";
    public static final String HERO = "Hero";

    // Minions
    public static final String SENTINEL = "Sentinel";
    public static final String BERSERKER = "Berserker";
    public static final String GOLIATH = "Goliath";
    public static final String WARDEN = "Warden";
    public static final String THERIPPER = "The Ripper";
    public static final String MIRAJ = "Miraj";
    public static final String THECURSEDONE = "The Cursed One";
    public static final String DISCIPLE = "Disciple";

    // Environment cards
    public static final String FIRESTORM = "Firestorm";
    public static final String WINTERFELL = "Winterfell";
    public static final String HEARTHOUND = "Heart Hound";

    // Minions abilities
    public static final String TANK = "Tank";
    public static final String GODSPLAN = "God's Plan";
    public static final String WEAKKNEES = "Weak Knees";
    public static final String SKYJACK = "Skyjack";
    public static final String SHAPESHIFT = "Shapeshift";

    // Hero cards
    public static final String LORDROYCE = "Lord Royce";
    public static final String EMPRESSTHORINA = "Empress Thorina";
    public static final String KINGMUDFACE = "King Mudface";
    public static final String GENERALKOCIORAW = "General Kocioraw";

    // Abilities
    public static final String SUBZERO = "Sub-Zero";
    public static final String LOWBLOW = "Low Blow";
    public static final String EARTHBORN = "Earth Born";
    public static final String BLOODTHIRST = "Blood Thirst";

    // Errors
    public static final String ERRORENVONTABLE = "Cannot place environment card on table.";
    public static final String ERRORNOTENOUGHMANA = "Not enough mana to place card on table.";
    public static final String ERRORROWFULL = "Cannot place card on table since row is full.";
    public static final String ERRORNOTENVCARD = "Chosen card is not of type environment.";
    public static final String NOTENEMYROW = "Chosen row does not belong to the enemy.";
    public static final int STATUSOK = 0;
    public static final int ERRORSTATUSONE = 1;

    // Gameplay constants
    public static final Integer HEROHP = 30;
    public static final Integer MAXMANA = 10;
    public static final Integer MAXCARDS = 5;
    public static final boolean ISFROZEN = true;
    public static final boolean ISNOTFROZEN = false;
    public static final int ZERO = 0;
    public static final int ONE = 1;
    public static final int TWO = 2;
    public static final int THREE = 3;
}
