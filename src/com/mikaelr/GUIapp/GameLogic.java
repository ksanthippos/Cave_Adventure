package com.mikaelr.GUIapp;

import com.mikaelr.classes.*;

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

    private ArrayList<Item> items;
    private ArrayList<Enemy> enemies;

    private Player player;
    private int turns;
    private boolean combatActivated;
    private boolean gameOver;


    public GameLogic() {

        this.mapSizeX = 12;
        this.mapSizeY = 12;
        this.blocks = new Block[mapSizeX][mapSizeY];
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();

        this.map = new char[][]{
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#', '#'},
                {'#', '2', 'C', 'C', '#', 'S', '#', 'C', '#', '#', '2', '#'},
                {'#', 'C', '#', 'C', '#', 'C', '#', 'C', 'C', 'C', 'C', '#'},
                {'#', 'C', 'C', 'C', '1', 'C', '#', 'C', '#', '#', 'C', '#'},
                {'#', '#', 'C', '#', '#', 'C', 'C', '1', '#', '#', 'C', '#'},
                {'#', 'C', '2', 'C', '#', '#', '#', '#', '#', 'C', '1', '#'},
                {'#', '#', 'C', '#', '#', 'C', 'C', 'C', '3', 'C', '#', '#'},
                {'#', '1', 'C', '#', 'C', 'C', '#', '#', '#', '#', '#', '#'},
                {'#', 'C', '#', '#', '4', '#', 'C', '#', 'C', '4', 'C', '#'},
                {'#', 'C', '3', '#', 'C', 'C', 'C', 'C', 'C', '#', 'C', '#'},
                {'#', '#', '#', '#', '#', '#', '#', '#', '#', '5', 'C', '#'},
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

    public boolean isCombatActivated() { return combatActivated; }

    public Player getPlayer() {
        return player;
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
        initBlocks();
        initPlayer();

    }


    private void initItems(){

        items.add(new Item(ItemType.BATTLE_AXE));
        items.add(new Item(ItemType.CHAINMAIL_ARMOR));
        items.add(new Item(ItemType.KNIGHT_ARMOR));
        items.add(new Item(ItemType.HEALING_SPELL));
        items.add(new Item(ItemType.LIGHTNING_BOLT));
        items.add(new Item(ItemType.MANA_POTION));
        items.add(new Item(ItemType.HEALING_POTION));
        items.add(new Item(ItemType.HEALING_POTION));
        items.add(new Item(ItemType.HEALING_POTION));
        items.add(new Item(ItemType.MANA_POTION));
        items.add(new Item(ItemType.MANA_POTION));
        items.add(new Item(ItemType.MANA_POTION));
        items.add(new Item(ItemType.HEALING_SPELL));
        items.add(new Item(ItemType.LIGHTNING_BOLT));

        Collections.shuffle(items);

    }

    private void initBlocks() {

        Random random = new Random();
        int itemCounter = 0;
        int corridorID = 0;

        for (int i = 0; i < mapSizeX; i++) {
            for (int j = 0; j < mapSizeY; j++) {

                blocks[i][j] = new Block(i, j); // creates an empty block object
                char mapID = map[i][j];     // map array contains characters that describe the blocks type

                // player
                if (mapID == 'S') {
                    currentX = i;
                    currentY = j;
                    blocks[i][j].setPlayerStart();
                    // starting items
                    blocks[i][j].addItem(new Item(ItemType.HEALING_POTION));
                    blocks[i][j].addItem(new Item(ItemType.SHORT_SWORD));
                }
                // wall
                else if (mapID == '#') {
                    blocks[i][j].setWall();
                }
                // corridor
                else if (mapID == 'C') {
                    blocks[i][j].setCorridor();

                    // 20 % chance of finding items
                    if (itemCounter < 14 && random.nextInt(5) > 3) {
                        blocks[i][j].addItem(items.get(itemCounter));
                        itemCounter++;
                    }
                }

                // enemies
                else {
                    corridorID = Character.getNumericValue(mapID);  // convert char value from map to int
                    blocks[i][j].setCorridor();

                    // ID = 1 --> 60% chance: rat, 40% chance: snake
                    if (corridorID == 1) {
                        if (random.nextInt(5) > 2) {
                            Enemy rat = new Enemy(2, 0, 1, true, 0, 0, EnemyType.RAT, false, 6);
                            enemies.add(rat);
                            blocks[i][j].addEnemy(rat, i, j);
                        }
                        else {
                            Enemy snake = new Enemy(3, 0, 1, true, 0, 0, EnemyType.SNAKE, true, 8);
                            enemies.add(snake);
                            blocks[i][j].addEnemy(snake, i, j);
                        }

                        // 60% chance of finding items
                        if (itemCounter < 14 && random.nextInt(5) > 1) {
                            blocks[i][j].addItem(items.get(itemCounter));
                            itemCounter++;
                        }
                    }

                    // ID = 2 --> goblin (maybe another type too?)
                    else if (corridorID == 2) {
                        Enemy goblin = new Enemy(4, 1, 2, true, 0, 0, EnemyType.GOBLIN, false,10);
                        enemies.add(goblin);
                        blocks[i][j].addEnemy(goblin, i, j);

                        // 100% chance of items
                        if (itemCounter < 14) {
                            blocks[i][j].addItem(items.get(itemCounter));
                            itemCounter++;

                            // if did not have any items, add extra
                            if (!blocks[i][j].hasItems()) {
                                blocks[i][j].addItem(new Item(ItemType.HEALING_POTION));
                            }
                        }
                    }

                    // ID = 3 --> orc (maybe another type too?)
                    else if (corridorID == 3) {
                        Enemy orc = new Enemy(8, 2, 4, true, 0, 0, EnemyType.ORC, false,14);
                        enemies.add(orc);
                        blocks[i][j].addEnemy(orc, i, j);

                        // 100% chance of items
                        if (itemCounter < 14) {
                            blocks[i][j].addItem(items.get(itemCounter));
                            itemCounter++;

                            // if did not have any items, add extra
                            if (!blocks[i][j].hasItems()) {
                                blocks[i][j].addItem(new Item(ItemType.MANA_POTION));
                                blocks[i][j].addItem(new Item(ItemType.HEALING_SPELL));
                            }
                        }
                    }
                    // ID = 4 --> cave troll (maybe another type too?)
                    else if (corridorID == 4) {
                        Enemy troll = new Enemy(14, 4, 7, true, 0, 0, EnemyType.CAVE_TROLL, false,20);
                        enemies.add(troll);
                        blocks[i][j].addEnemy(troll, i, j);

                        // 100% chance of items
                        if (itemCounter < 14) {
                            blocks[i][j].addItem(items.get(itemCounter));
                            itemCounter++;

                            // if did not have any items, add extra
                            if (!blocks[i][j].hasItems()) {
                                blocks[i][j].addItem(new Item(ItemType.MANA_POTION));
                                blocks[i][j].addItem(new Item(ItemType.LIGHTNING_BOLT));
                            }
                        }
                    }
                    else if (corridorID == 5) {
                        Enemy dragon = new Enemy(20, 6, 10, true, 0, 0, EnemyType.DRAGON, false,50);
                        enemies.add(dragon);
                        blocks[i][j].addEnemy(dragon, i, j);

                        // A DOOR TO A NEXT LEVEL--?


                    }

                }
            }
        }
    }

    // set up player
    private void initPlayer() {

        player = new Player(10, 0, 0, true, currentX, currentY, "Mikael", 1, 0, 0, false);
        // players surroundings are set visible
        blocks[currentX + 1][currentY].setVisible();
        blocks[currentX - 1][currentY].setVisible();
        blocks[currentX][currentY + 1].setVisible();
        blocks[currentX][currentY - 1].setVisible();
        blocks[currentX + 1][currentY + 1].setVisible();
        blocks[currentX + 1][currentY - 1].setVisible();
        blocks[currentX - 1][currentY + 1].setVisible();
        blocks[currentX - 1][currentY - 1].setVisible();


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

        // set players char to new position
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
        }

        if (player.isPoisoned() && turns % 10 == 0) {
            feedback.append("\nPoison takes effect! You lose 1 HP. Better find a cure quickly!");
            player.setHP(player.getHP() - 1);
            if (!player.isAlive()) {
                setGameOver();
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
            feedback.append("This is your starting point. There is a corridor to the east.\n");
            feedback.append("You see following items on the floor:\n");
            for (Item e: blocks[x][y].getItems()) {
                feedback.append(e.toString() + "\n");
            }
        }

        else if (blocks[x][y].isCorridor() && !blocks[x][y].hasItems()) {
            feedback.append("You see a corridor.");
        }

        else if (blocks[x][y].isCorridor() && blocks[x][y].hasItems()) {
            feedback.append("You see following items on the floor:\n");
            for (Item e: blocks[x][y].getItems()) {
                feedback.append(e.toString() + "\n");
            }
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
