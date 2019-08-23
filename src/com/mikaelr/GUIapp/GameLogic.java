package com.mikaelr.GUIapp;

import com.mikaelr.textgameapp.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Random;

public class GameLogic {

    // players coordinates
    private int currentX;
    private int currentY;
    private int previousX;
    private int previousY;

    private int mapSizeX;
    private int mapSizeY;
    private Block[][] blocks;
    private char[][] map;
    private ArrayList<Item> roomItems;
    private ArrayList<Item> corridorItems;

    /*
    * maybe just:
    private ArrayList items
    *
    * in graphic environment there's no need for corridor/room-separation,
    * rats and snakes spawn everywhere (minus those locations already spawned by bigger enemies)
    *
    * */

    private ArrayList<Enemy> ratsnake;
    private ArrayList<Enemy> enemies;
    private Player player;
    private int turns;
    private boolean combatActivated;
    private boolean gameOver;


    public GameLogic() {

        this.mapSizeX = 12;
        this.mapSizeY = 12;
        this.blocks = new Block[mapSizeX][mapSizeY];
        this.roomItems = new ArrayList<>();
        this.corridorItems = new ArrayList<>();
        this.ratsnake = new ArrayList<>();
        this.enemies = new ArrayList<>();


        this.map = new char[][]{
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
    }

    // public getters

    public int getMapSizeX() {
        return mapSizeX;
    }

    public int getMapSizeY() {
        return mapSizeY;
    }

    public Block[][] getBlocks() {
        return blocks;
    }

    public char[][] getMap() {
        return map;
    }

    public int getCurrentX() {
        return currentX;
    }

    public int getCurrentY() {
        return currentY;
    }

    public int getPreviousX() {
        return previousX;
    }

    public int getPreviousY() {
        return previousY;
    }

    public boolean isCombatActivated() { return combatActivated; }

    public ArrayList<Item> getRoomItems() {
        return roomItems;
    }

    public ArrayList<Item> getCorridorItems() {
        return corridorItems;
    }

    public ArrayList<Enemy> getRatsnake() {
        return ratsnake;
    }

    public ArrayList<Enemy> getEnemies() {
        return enemies;
    }

    public Player getPlayer() {
        return player;
    }

    public int getTurns() {
        return turns;
    }

    public boolean isGameOver() {
        return gameOver;
    }

    public Enemy activatedEnemy() {

        for (Enemy e: enemies) {
            if (e.isActive()) {
                return e;
            }
        }

        return null;
    }

    // private getters

    private Block getCurrentBlock() {
        return blocks[currentX][currentY];
    }

    private Block getNextBlock(int dx, int dy) { return blocks[currentX + dx][currentY + dy]; }

    // *****************************

    // setters

    public void setCombatActivated(boolean value) {
        combatActivated = value;
        if (value == false) {
            enemies.stream().forEach(e -> e.setActive(false));
        }
    }

    public void setGameOver() {
        gameOver = true;
    }

    // *****************************

    // game init
    public void initGame() {

        this.currentX = 0;
        this.currentY = 0;
        this.previousX = 0;
        this.previousY = 0;
        this.turns = 0;
        this.combatActivated = false;
        this.gameOver = false;
        initItems();
        initEnemies();
        initBlocks();
        initPlayer();


        // TEST
        // kills all the rats and snakes
        ratsnake.stream().forEach(e -> e.takeHit(10));

        // MAYBE all the snakes and rats into same enemies list with the others?

    }


    private void initItems(){
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

    private void initEnemies() {
        // enemies generated in two lists: first for rats and snakes (corridors), the second for bigger enemies (rooms)
        // last one (dragon) --> spawns at dragon room

        for (int i = 0; i < 6; i++) {
            ratsnake.add(new Enemy(2, 0, 1, true, 0, 0, EnemyType.RAT, false,6));
        }
        for (int i = 0; i < 4; i++) {
            ratsnake.add(new Enemy(3, 0, 1, true, 0, 0, EnemyType.SNAKE, true, 6));
        }

        enemies.add(new Enemy(2, 0, 1, true, 0, 0, EnemyType.RAT, false,6));

        for (int i = 0; i < 4; i++) {
            enemies.add(new Enemy(4, 1, 2, true, 0, 0, EnemyType.GOBLIN, false,5));
        }
        for (int i = 0; i < 3; i++) {
            enemies.add(new Enemy(8, 2, 4, true, 0, 0, EnemyType.ORC, false,10));
        }
        for (int i = 0; i < 2; i++) {
            enemies.add(new Enemy(14, 4, 7, true, 0, 0, EnemyType.CAVE_TROLL, false,20));
        }

        enemies.add(new Enemy(20, 6, 10, true, 0, 0, EnemyType.DRAGON, false,50));

        Collections.shuffle(ratsnake);  // spawned with 60/40 - chance
    }

    private void initBlocks() {

        Random random = new Random();
        int rsCounter = 0;
        int ciCounter = 0;
        int roomID = 0;

        for (int i = 0; i < mapSizeX; i++) {
            for (int j = 0; j < mapSizeY; j++) {

                blocks[i][j] = new Block(i, j); // creates an empty block object
                char mapID = map[i][j];     // map array contains characters that describe the blocks type

                // player
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
                        blocks[i][j].addEnemy(enemies.get(10), i, j);
                    }
                }
            }
        }
    }

    // set up player
    private void initPlayer() {
        player = new Player(10, 0, 0, true, currentX, currentY, "Mikael", 1, 0, 0, false);
    }


    // move method
    public String movePlayer(int dx, int dy) {

        turns++;    // number of game turns is fixed on player movement
        currentX = player.getX();
        currentY = player.getY();
        // store the previous position before moving
        previousX = currentX;
        previousY = currentY;

        StringBuilder feedback = new StringBuilder();

        // boundary & wall check first
        if (currentX + dx > mapSizeX - 1 ||
                currentY + dy > mapSizeY - 1 ||
                    currentX + dx < 0 ||
                        currentY + dy < 0 ||
                            getNextBlock(dx, dy).isWall())
                            {
                                return "Can't go through walls.";
                            }

        else if (getNextBlock(dx, dy).hasEnemies()) {
                combatActivated = true;
                getNextBlock(dx, dy).getEnemies().get(0).setActive(true);
                return "*********\nCOMBAT MODE ACTIVATED\nYou see " + getNextBlock(dx, dy).getEnemies().get(0).toString();
        }

        // route clear: player is moved
        currentX += dx;
        currentY += dy;
        player.setX(currentX);
        player.setY(currentY);

        map[currentX][currentY] = 'S';
        map[previousX][previousY] = 'C';

        if (dx < 0) {
            feedback.append("You went west.");
        }
        else if (dx > 0) {
            feedback.append("You went east.");
        }
        else if (dy > 0) {
            feedback.append("You went south.");
        }
        else if (dy < 0) {
            feedback.append("You went north.");
        }

        if (blocks[currentX][currentY].hasItems()) {
            if (blocks[currentX][currentY].isCorridor()) {
                feedback.append("\nYou see " +
                        blocks[currentX][currentY].getItems().get(0).toString() +
                        " lying on the corridor floor.");
            }
            else if (blocks[currentX][currentY].isRoom()) {
                feedback.append("\nYou see " +
                        blocks[currentX][currentY].getItems().get(0).toString() +
                        " in the room corner.");
            }
        }

        return feedback.toString();

    }

    // describes current block's access directions, enemies and items
    public String describeBlock() {

        int x = getCurrentBlock().getX();
        int y = getCurrentBlock().getY();

        StringBuilder feedback = new StringBuilder();

        if (blocks[x][y].isPlayerStart() && !blocks[x][y].hasItems()) {
            feedback.append("This is your starting point. There is a corridor to the east.");
        }

        else if (blocks[x][y].isPlayerStart() && blocks[x][y].hasItems()) {
            feedback.append("This is your starting point. There is a corridor to the east.\nYou see " +
                    blocks[x][y].getItems().get(0).toString() +
                    " lying on the corridor floor.");
        }

        else if (blocks[x][y].isCorridor() && !blocks[x][y].hasItems()) {
            feedback.append("You see a corridor.");
        }

        else if (blocks[x][y].isRoom() && !blocks[x][y].hasItems()) {
            feedback.append("You are in a room.");
        }

        else if (blocks[x][y].isCorridor() && blocks[x][y].hasItems()) {
            feedback.append("You see " +
                    blocks[x][y].getItems().get(0).toString() +
                    " lying on the corridor floor.");
        }

        else if (blocks[x][y].isRoom() && blocks[x][y].hasItems()) {
            feedback.append("You see " +
                    blocks[x][y].getItems().get(0).toString() +
                    " in the room corner.");
        }

        return feedback.toString();
    }

    public boolean takeItems() {

        int x = currentX;
        int y = currentY;

        if (blocks[x][y].hasItems()) {
            player.addToInventory(blocks[x][y].getItems());
            blocks[x][y].clearItems();
            map[x][y] = 'S';
            return true;
        }

        return false;
    }

    public String gameOver() {

        gameOver = true;
        StringBuilder feedback = new StringBuilder();

        feedback.append("\n* * * * * * * * * *\n");
        feedback.append("Sorry " + player.getName() + ", but it seems you have lost all your HP which makes you.. dead.");
        feedback.append("");
        feedback.append("\nDuring the game, you managed to collect " + player.getXP() + " XP and reached level " + player.getLevel() + ".");
        if (player.getLevel() < 2) {
            feedback.append("\nMaybe a bit more practice..?");
        }
        else if (player.getLevel() > 1 && player.getLevel() < 4) {
            feedback.append("\nNot bad. Keep going!");
        }
        else if (player.getLevel() > 3 && player.getLevel() < 5) {
            feedback.append("\nQuite impressive!");
        }
        else if (player.getLevel() >= 5) {
            feedback.append("\nMaster, I salute thee!");
        }

        return feedback.toString();


    }





}
