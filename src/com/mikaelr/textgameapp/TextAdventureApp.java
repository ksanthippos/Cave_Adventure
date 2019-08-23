package com.mikaelr.textgameapp;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.*;


public class TextAdventureApp extends Application {

    // input & output
    private TextArea output = new TextArea();
    private TextField input = new TextField();

    // commands
    private Map<String, Command> commands = new LinkedHashMap<>();
    private Map<String, SubCommand> subcommands = new LinkedHashMap<>();
    private boolean subCommand = false;

    // map & blocks
    private int mapSizeX = 12;
    private int mapSizeY = 12;
    private Block[][] blocks = new Block[mapSizeX][mapSizeY];

    private char[][] map = {
                                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                                {'#', '1', 'C', 'C', '#', 'S', '#', 'C', '#', '#', '5', '#'},
                                {'#', 'C', '#', 'C', '#', 'C', '#', 'C', 'C', 'C', 'C', '#'},
                                {'#', 'C', 'C', '0', 'C', 'C', '#', 'C', '#', '#', 'C', '#'},
                                {'#', '#', 'C', '#', '#', 'C', 'C', '3', '#', '#', 'C', '#'},
                                {'#', 'C', '2', 'C', '#', '#', '#', '#', '#', 'C', 'C', '#'},
                                {'#', '#', 'C', '#', '#', 'C', 'C', 'C', '6', 'C', '#', '#'},
                                {'#', 'C', 'C', '#', 'C', 'C', '#', '#', '#', '#', '#', '#'},
                                {'#', 'C', '#', '#', 'C', '#', '7', '#', 'C', '8', 'C', '#'},
                                {'#', 'C', '4', '#', 'C', 'C', 'C', 'C', 'C', '#', 'C', '#'},
                                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '9', 'C', '#'},
                                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'}
                            };


    private int currentX;
    private int currentY;
    private int previousX;
    private int previousY;

    private List<Item> roomItems = new ArrayList<>();
    private List<Item> corridorItems = new ArrayList<>();

    private List<Enemy> ratsnake = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();

    private Rooms rooms = new Rooms();

    private Player player;

    private int turns;

    private Combat combat;
    private boolean combatActivated;
    private boolean gameIsOver;


    // **********************
    // UI & game content

    private Parent createContent() {
        output.setPrefHeight(600);
        output.setEditable(false);
        output.setFocusTraversable(false);
        output.setFont(Font.font(20));
        output.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-font-size: 12; -fx-text-fill: white; ");
        input.setStyle("-fx-control-inner-background:#000000; -fx-font-family: Consolas; -fx-font-size: 12; -fx-text-fill: white; ");

        input.setOnAction(e -> {
            String inputText = input.getText();
            input.clear();
            output.appendText(">" + inputText + "\n");  // player sees previous inputs
            onInput(inputText);

        });

        VBox root = new VBox(15, output, input);
        root.setPadding(new Insets(15));
        root.setPrefSize(800, 600);

        initGame();

        return root;
    }

    // game initialization
    private void initGame() {

        println("Welcome to Grand Text Adventure v.0.1!!");
        combatActivated = false;
        gameIsOver = false;
        turns = 1;
        initCommands();
        initItems();
        initEnemies();
        initBlocks();
        initPlayer();
        runCurrentBlock();


    }

    private void initCommands() {

        commands.put("w", new Command("w", "North", () -> runGo(0, -1)));
        commands.put("s", new Command("s", "South", () -> runGo(0, 1)));
        commands.put("d", new Command("d", "East", () -> runGo(1, 0)));
        commands.put("a", new Command("a", "West", () -> runGo(-1, 0)));
        commands.put("l", new Command("l", "Look around", this::runCurrentBlock));
        commands.put("t", new Command("t", "Take all items", this::runTakeItem));
        commands.put("e", new Command("e", "Equip item", this::runEquipItem));
        commands.put("u", new Command("u", "Use item", this::runUseItem));
        commands.put("i", new Command("i", "Inventory", this::runShowInventory));
        commands.put("p", new Command("p", "Player info", this::runPlayerInfo));
        commands.put("h", new Command("h", "Show all main commands", this::runHelp));
        commands.put("hsub", new Command("hs", "Show all subcommands", this::runSubHelp));
        commands.put("c", new Command("c", "Clear text screen", this::runClear));
        commands.put("quit", new Command("quit", "Quit game", Platform::exit));

        // subcommands
        subcommands.put("x", new SubCommand("x", "Fight", this::runSubFight));
        subcommands.put("f", new SubCommand("f", "Flee", this::runSubFlee));
        subcommands.put("ss", new SubCommand("ss", "Equip short sword", this::runSubSS));
        subcommands.put("ba", new SubCommand("ba", "Equip battle axe", this::runSubBA));
        subcommands.put("cm", new SubCommand("cm", "Equip chainmail", this::runSubCM));
        subcommands.put("ka", new SubCommand("ka", "Equip knight's armor", this::runSubKA));
        subcommands.put("hp", new SubCommand("hp", "Drink healing potion", this::runSubHP));
        subcommands.put("mp", new SubCommand("mp", "Drink mana potion", this::runSubMP));
        subcommands.put("hs", new SubCommand("hs", "Cast healing spell", this::runSubHeSpell));
        subcommands.put("lb", new SubCommand("lb", "Cast lightning bolt", this::runSubLiSpell));
        subcommands.put("hsub", new SubCommand("hsub", "Show all subcommands", this::runSubHelp));
        subcommands.put("y", new SubCommand("y", "Play again", this::runSubPlayAgain));
        subcommands.put("n", new SubCommand("n", "Quit", this::runSubQuit));


    }

    private void initItems() {

        // 8
        roomItems.add(new Item(ItemType.BATTLE_AXE));
        roomItems.add(new Item(ItemType.CHAINMAIL_ARMOR));
        roomItems.add(new Item(ItemType.KNIGHT_ARMOR));
        roomItems.add(new Item(ItemType.HEALING_SPELL));
        roomItems.add(new Item(ItemType.LIGHTNING_BOLT));
        roomItems.add(new Item(ItemType.HEALING_POTION));
        roomItems.add(new Item(ItemType.MANA_POTION));
        roomItems.add(new Item(ItemType.SILVER));
        roomItems.add(new Item(ItemType.GOLD));

        // 8
        corridorItems.add(new Item(ItemType.HEALING_POTION));
        corridorItems.add(new Item(ItemType.HEALING_POTION));
        corridorItems.add(new Item(ItemType.HEALING_POTION));
        corridorItems.add(new Item(ItemType.MANA_POTION));
        corridorItems.add(new Item(ItemType.MANA_POTION));
        corridorItems.add(new Item(ItemType.MANA_POTION));
        corridorItems.add(new Item(ItemType.HEALING_SPELL));
        corridorItems.add(new Item(ItemType.LIGHTNING_BOLT));

        Collections.shuffle(roomItems);
        Collections.shuffle(corridorItems);

    }

    // all the enemies are spawned first into location (0, 0), but will be moved to new locations at initBlocks()
    private void initEnemies() {

        // enemies generated in two lists: first for rats and snakes (corridors), the second for bigger enemies (rooms)
        // last one (dragon) --> spawns at dragon room

        for (int i = 0; i < 6; i++) {
            ratsnake.add(new Enemy(2, 0, 1, true, 0, 0, EnemyType.RAT, false,6));
        }
        for (int i = 0; i < 4; i++) {
            ratsnake.add(new Enemy(3, 0, 1, true, 0, 0, EnemyType.SNAKE, true, 6));
        }
        for (int i = 0; i < 4; i++) {
            enemies.add(new Enemy(4, 1, 2, true, 0, 0, EnemyType.GOBLIN, false,5));
        }
        for (int i = 0; i < 3; i++) {
            enemies.add(new Enemy(8, 3, 5, true, 0, 0, EnemyType.ORC, false,10));
        }
        for (int i = 0; i < 2; i++) {
            enemies.add(new Enemy(14, 4, 7, true, 0, 0, EnemyType.CAVE_TROLL, false,20));
        }

        enemies.add(new Enemy(20, 6, 10, true, 0, 0, EnemyType.DRAGON, false,50));

        Collections.shuffle(ratsnake);  // spawned with 60/40 - chance

    }

    // creates a playable map with 10 x 10 = 100 blocks, which each contains info of a blocks type, position and content
    private void initBlocks() {

        Random random = new Random();
        int rsCounter = 0;
        int ciCounter = 0;
        int roomID = 0;

        for (int i = 0; i < mapSizeX; i++) {
            for (int j = 0; j < mapSizeY; j++) {

                blocks[i][j] = new Block(i, j); // creates an empty block object
                char mapID = map[i][j];     // map array contains characters that describe the blocks type

                // player starting point
                if (mapID == 'S') {
                    currentX = i;
                    currentY = j;
                    blocks[i][j].setPlayerStart();
                    blocks[i][j].addItem(new Item(ItemType.SHORT_SWORD));   // starting item
                }
                // wall
                else if (mapID == '#') {
                    blocks[i][j].setWall();
                }
                // corridor
                else if (mapID == 'C') {
                    blocks[i][j].setCorridor();

                    if (rsCounter < 10) {
                        blocks[i][j].addEnemy(ratsnake.get(rsCounter), i, j);
                        rsCounter++;
                    }
                    if (ciCounter < 8 && random.nextInt(2) > 0) {
                        blocks[i][j].addItem(corridorItems.get(ciCounter));
                        ciCounter++;
                    }
                }
                // room
                else {
                    roomID = Character.getNumericValue(mapID);
                    blocks[i][j].setRoom();
                    blocks[i][j].setRoomID(roomID);

                    // rooms 0 - 9
                    if (roomID < 9) {
                        blocks[i][j].addItem(roomItems.get(roomID));
                        blocks[i][j].addEnemy(enemies.get(roomID), i , j);
                    }
                    // room 9 (dragon)
                    else if (roomID == 9) {
                        blocks[i][j].addItem(new Item(ItemType.GOLD));
                        blocks[i][j].addItem(new Item(ItemType.GOLD));
                        blocks[i][j].addItem(new Item(ItemType.GOLD));
                        blocks[i][j].addEnemy(enemies.get(9), i, j);
                    }
                }
            }
        }
    }

    private void initPlayer() {

        player = new Player(10, 0, 0, true, currentX, currentY, "Mikael", 1, 0, 0, false);
    }
    // ****************************


    // *****************************
    // main commands
    private void runHelp() {

        println("");
        println("Game commands:");
        for (String komento: commands.keySet()) {
            println(komento + " : " + commands.get(komento).getDescription());
        }
    }

    private void runSubHelp() {
        println("");
        println("Subcommands are activated with commands e (equip), u (use) and in the combat mode.");
        println("For example, if you want to drink a healing potion, type: u -> enter -> hp -> enter.");
        println("Also notice, that equipping a new item will automatically replace the old one.");
        println("If you choose to flee during the combat, the enemy is granted a chance to hit you.\n");
        println("List of subcommands:");
        for (String komento: subcommands.keySet()) {
            println(komento + " : " + subcommands.get(komento).getDescription());
        }
    }

    private void runGo(int dx, int dy) {

        // save these for later possible use (fleeing from combat)
        previousX = currentX;
        previousY = currentY;

        // boundary & wall check first
        if (currentX + dx > mapSizeX - 1 ||
                currentY + dy > mapSizeY - 1 ||
                    currentX + dx < 0 ||
                        currentY + dy < 0 ||
                            getNextBlock(dx, dy).isWall()) {
            println("That way there is a wall. You shall not pass.");
            return;
        }

        // player is moved to new location
        currentX += dx;
        currentY += dy;

        if (dx < 0)
            println("You went west.");
        else if (dx > 0)
            println("You went east.");
        else if (dy < 0)
            println("You went north.");
        else if (dy > 0)
            println("You went south.");

        runCurrentBlock();

    }

    // describes current block's access directions, enemies and items
    private void runCurrentBlock() {

        int x = getCurrentBlock().getX();
        int y = getCurrentBlock().getY();

        if (blocks[x][y].isPlayerStart()) {
            println("This is your starting point. There is only one way to go: a pitch black corridor to the east.");
        }

        else if (blocks[x][y].isCorridor()) {
            println("You are on a corridor. You can go to following directions: ");

            if (getNextBlock(-1, 0).isCorridor() || getNextBlock(-1, 0).isRoom()) {
                println("west");
            }
            if (getNextBlock(1, 0).isCorridor() || getNextBlock(1, 0).isRoom()) {
                println("east");
            }
            if (getNextBlock(0, -1).isCorridor() || getNextBlock(0, -1).isRoom()) {
                println("north");
            }
            if (getNextBlock(0, 1).isCorridor() || getNextBlock(0, 1).isRoom()) {
                println("south");
            }
        }

        else if (blocks[x][y].isRoom()) {
            println("*****");
            println("You are in a room. There is access to following directions:");

            if (getNextBlock(-1, 0).isCorridor()) {
                println("west");
            }
            if (getNextBlock(1, 0).isCorridor()) {
                println("east");
            }
            if (getNextBlock(0, -1).isCorridor()) {
                println("north");
            }
            if (getNextBlock(0, 1).isCorridor()) {
                println("south");
            }
        }

        // in enemy encounters, player is forced to fight (or flee)
        if (blocks[x][y].hasEnemies()) {
            subCommand = true;
            combatActivated = true;
            println("__________________");
            println("COMBAT MODE ACTIVE");
            blocks[x][y].getEnemies().stream().forEach(e -> println("You see " + e.toString()));
            println("Select action:");
            println("(x = fight, f = flee, hsub = more commands)");
        }
        if (blocks[x][y].hasItems() && !blocks[x][y].hasEnemies()) {
            println("You see following items on the floor: ");
            blocks[x][y].getItems().stream().forEach(e -> println(e.toString()));
        }
        else if (!blocks[x][y].hasItems() && !blocks[x][y].hasEnemies()) {
            println("There is nothing special to be found.");
        }
    }

    private void runTakeItem() {

        int x = currentX;
        int y = currentY;
        if (blocks[x][y].hasItems()) {
            println("All items collected to your inventory!");
            player.addToInventory(blocks[x][y].getItems());
            blocks[x][y].clearItems();
        }
    }

    private void runShowInventory() {

        println("Inventory:");
        player.getInventory().stream().forEach(e -> println(e.toString()));
    }

    private void runPlayerInfo() {
        println(player.toString());
    }

    private void runEquipItem() {

        if (player.itemsToEquip().isEmpty()) {
            println("You have nothing to equip!");
        }
        else {
            println("Select item:");
            subCommand = true;
        }
    }

    private void runUseItem() {

        if (player.itemsToUse().isEmpty()) {
            println("You have nothing to use!");
        }
        else {
            println("Select item:");
            subCommand = true;
        }
    }

    // *****************************

    // subcommands
    private void runSubSS() {
        if (player.findItemsByType(ItemType.SHORT_SWORD)) {
            println("You are now holding short, but a deadly sword. Your AP is now 2.");
            player.equipItem(ItemType.SHORT_SWORD);
        }
        else {
            println("You don't have a short sword.");
        }
    }
    private void runSubBA() {
        if (player.findItemsByType(ItemType.BATTLE_AXE)) {
            println("You are wielding a big, chunky battle axe! Your AP is now 5.");
            player.equipItem(ItemType.BATTLE_AXE);
        }
        else {
            println("You don't have a battle axe.");
        }
    }
    private void runSubCM() {
        if (player.findItemsByType(ItemType.CHAINMAIL_ARMOR)) {
            println("You are wearing a basic chainmail shirt. Your DP is now 2.");
            player.equipItem(ItemType.CHAINMAIL_ARMOR);
        }
        else {
            println("You don't own a chainmail.");
        }
    }
    private void runSubKA() {
        if (player.findItemsByType(ItemType.KNIGHT_ARMOR)) {
            println("You are wearing a full-body knight's armor! Your DP is now 4.");
            player.equipItem(ItemType.KNIGHT_ARMOR);
        }
        else {
            println("You don't have knight's armor. Peasant..");
        }
    }
    private void runSubHP() {
        if (player.findItemsByType(ItemType.HEALING_POTION)) {
            println("*GLUG GLUG* You drank from a potion of healing. You gain 5 HP!");
            player.useItem(ItemType.HEALING_POTION);
        }
        else {
            println("You wish you had a potion of health. Sigh..");
        }
    }
    private void runSubMP() {
        if (player.findItemsByType(ItemType.MANA_POTION)) {
            println("*GLUG GLUG* You drank from a potion of mana. You gain 10 MP!");
            player.useItem(ItemType.MANA_POTION);
        }
        else {
            println("No mana potion in your possession, sorry.");
        }
    }
    private void runSubHeSpell() {
        if (player.findItemsByType(ItemType.HEALING_SPELL)) {
            if (player.setMP(player.getMP() - 4)) {
                println("*You cast a healing spell! *BLING* You got back your full health.");
                player.useItem(ItemType.HEALING_SPELL);
                if (player.isPoisoned()) {
                    println("You are also healed from poisoning!");
                    player.setPoisoned(false);
                }
            }
            else {
                println("Not enough MP to cast the spell!");
            }
        }
        else {
            println("No healing spells up from your sleeve, sorry.");
        }
    }
    private void runSubLiSpell() {
        if (player.findItemsByType(ItemType.LIGHTNING_BOLT)) {
            if (player.setMP(player.getMP() - 6)) {
                println("You are armed with a lightning bolt! Next hit to enemy gets +5 points boost.");
                player.useItem(ItemType.LIGHTNING_BOLT);
            }
            else {
                println("Not enough MP to cast the spell!");
            }
        }
        else {
            println("No lighthing bolts up from your sleeve, sorry.");
        }
    }
    private void runSubFight() {

        combat = new Combat(player, blocks[currentX][currentY].getEnemies().get(0));    // fights the first enemy on the list
        combat.engageCombat();
        println(combat.combatReport());

        if (!blocks[currentX][currentY].hasEnemies()) {
            combatActivated = false;
            println("_______________");
            println("COMBAT MODE off");
            runCurrentBlock();
        }
        if (!player.isAlive()) {
            gameOver();
        }
    }
    private void runSubFlee() {

        // player has 50% chance of getting hit before escaping
        Random random = new Random();
        int hit = blocks[currentX][currentY].getEnemies().get(0).getAP();
        if (random.nextInt(2) > 0) {
            player.takeHit(player.getDP() - hit);
            println("Enemy strikes you in the back before you escape! You get " + hit + " damage.");
        }
        else {
            println("Enemy fails to hit and you manage to escape unharmed!");
        }
        if (!player.isAlive()) {
            gameOver();
        }
        subCommand = false;
        combatActivated = false;
        println("_______________");
        println("COMBAT MODE off");

        // move player to the previous location
        currentX = previousX;
        currentY = previousY;
        runCurrentBlock();

    }
    private void runSubPlayAgain() {
        if (gameIsOver) {
            output.clear();
            initGame();
        }
        else {
            println("Cannot use that command while alive.");
        }
    }
    private void runSubQuit() {
        if (gameIsOver) {
            Platform.exit();
        }
        else {
            println("Cannot use that command while alive.");
        }
    }


    // ******************************
    // assisting methods

    private void runClear() {
        output.clear();
    }

    private Block getCurrentBlock() {
        return blocks[currentX][currentY];
    }

    private Block getNextBlock(int dx, int dy) { return blocks[currentX + dx][currentY + dy]; }

    private void println(String text) {
        output.appendText(text + "\n");
    }

    // method that runs on every enter-press at the input
    private void onInput(String text) {

        turns++;
        if (turns % 10 == 0 && player.isPoisoned()) {
            println("There is venomous poison in your veins! You lose 1 HP.");
            player.setHP(player.getHP() - 1);
        }

        if (!subCommand) {
            if (!commands.containsKey(text)) {
                println("Command " + text + " not found.");
                return;
            }
            commands.get(text).execute();
        }
        else {
            if (!subcommands.containsKey(text) && !combatActivated) {
                println("No such item found.");
                subCommand = false;
                return;
            }
            else if (!subcommands.containsKey(text) && combatActivated) {
                println("Command not valid.");
                return;
            }

            subcommands.get(text).execute();

            if (!combatActivated) {
                subCommand = false;
            }
        }

    }

    private void gameOver() {
        gameIsOver = true;
        println("* * * * * * * * * *");
        println("Sorry " + player.getName() + ", but it seems you have lost all your HP which that makes you.. dead.");
        println("");
        println("During the game, you managed to collect " + player.getXP() + " XP and reached level " + player.getLevel() + ".");
        if (player.getLevel() < 2) {
            println("Maybe a bit more practice..");
        }
        else if (player.getLevel() > 1 && player.getLevel() < 4) {
            println("Not bad.");
        }
        else if (player.getLevel() > 3 && player.getLevel() < 5) {
            println("Quite impressive!");
        }
        else if (player.getLevel() >= 5) {
            println("True talent!!");
        }
        println("");
        println("Want to try again? (y/n)");
    }
    // ******************************


    // *****************************
    // main method
    @Override
    public void start(Stage stage) throws Exception {

        stage.setScene(new Scene(createContent()));
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

}
