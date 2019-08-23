package com.mikaelr.textgameapp;

public class Entity {

    private int HP;
    private int DP;
    private int AP;
    private boolean alive;

    private int x;
    private int y;

    public Entity(int HP, int DP, int AP, boolean alive, int x, int y) {
        this.HP = HP;   // health points
        this.DP = DP;   // defense --
        this.AP = AP;   // attack --
        this.alive = true;
        this.x = x;
        this.y = y;
    }

    public void takeHit(int amount) {

        HP = HP - amount;
        if (HP <= 0) {
            alive = false;
        }
    }

    public int getHP() {
        return HP;
    }

    public int getDP() {
        return DP;
    }

    public int getAP() {
        return AP;
    }

    public boolean isAlive() { return alive; }

    public void setHP(int amount) { this.HP = amount; }

    public void setDP(int amount) { this.DP = amount; }

    public void setAP(int amount) { this.AP = amount; }

    public void setX(int x) { this.x = x; }

    public void setY(int y) { this.y = y; }

    public int getX() { return x; }

    public int getY() { return y; }




}
