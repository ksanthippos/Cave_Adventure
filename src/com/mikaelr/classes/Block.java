package com.mikaelr.classes;


import java.util.ArrayList;
import java.util.List;

public class Block {

    private int x;
    private int y;
    private boolean isPlayerStart;
    private boolean isCorridor;
    private boolean isWall;
    private boolean hasItems = false;
    private boolean isVisible = false;

    private List<Item> items;
    private List<Enemy> enemies;

    public Block(int x, int y) {
        this.x = x;
        this.y = y;
        this.items = new ArrayList<>();
        this.enemies = new ArrayList<>();

    }

    //*************************
    // block getters & setters

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public boolean isVisible() { return isVisible; }

    public void setPlayerStart() { isPlayerStart = true; }

    public void setWall() {
        isWall = true;
    }

    public void setCorridor() {
        isCorridor = true;
    }

    public void setVisible() { isVisible = true; }

    public boolean hasEnemies() {

        boolean enemyAlive = false;
        for (Enemy e: enemies) {
            if (e.isAlive()) {
                enemyAlive = true;
                break;
            }
        }

        return enemyAlive;
    }

    public boolean isPlayerStart() { return isPlayerStart; }

    public boolean isCorridor() { return isCorridor; }

    public boolean isWall() { return isWall; }


    // ***********************

    //*************************
    // enemies
    public void addEnemy(Enemy enemy, int x, int y) {
        enemy.setX(x);
        enemy.setY(y);
        enemies.add(enemy);
    }

    public List<Enemy> getEnemies() {
        return enemies;
    }


    // ***********************

    //*************************
    // items
    public void addItem(Item item) {
        items.add(item);
        this.hasItems = true;
    }

    public List<Item> getItems() {
        return items;
    }

    public boolean hasItems() { return hasItems; }

    public void clearItems() {
        items.clear();
        this.hasItems = false;
    }





}
