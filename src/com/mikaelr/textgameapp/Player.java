package com.mikaelr.textgameapp;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class Player extends Entity {

    // Also a type/class? Like barbarian, wizard, ranger etd..
    private String name;
    private int level;
    private int XP;
    private int MP; // mana points
    private boolean poisoned;
    private boolean lightningArmed;
    private boolean newLevel;
    private List<Item> inventory;
    private Item equippedWeapon;
    private Item equippedArmor;


    public Player(int HP, int DP, int AP, boolean alive, int x, int y, String name, int level, int XP, int MP, boolean poisoned) {
        super(HP, DP, AP, alive, x, y);
        this.name = name;
        this.level = level;
        this.XP = XP;
        this.MP = MP;
        this.poisoned = false;
        this.newLevel = false;
        this.lightningArmed = false;
        this.inventory = new ArrayList<>();
    }

    public void disarmLightning() {
        lightningArmed = false;
    }

    public boolean isLightningArmed() {
        return lightningArmed;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    @Override
    public int getHP() { return super.getHP(); }

    @Override
    public int getAP() { return super.getAP(); }

    @Override
    public int getDP() { return super.getDP(); }

    @Override
    public int getX() {
        return super.getX();
    }

    @Override
    public int getY() {
        return super.getY();
    }

    public int getXP() {
        return XP;
    }

    public int getMP() { return MP; }

    public boolean isPoisoned() { return poisoned; }

    public void setX(int x) {
        super.setX(x);
    }

    public void setY(int y) {
        super.setY(y);
    }

    public void setPoisoned(boolean value) { this.poisoned = value; }

    public void setXP(int amount) {

        int previousXP = XP;
        XP += amount;

        if (XP >= 10 && XP < 20 && previousXP < 10) {
            level = 2;
            newLevel = true;
        }
        else if (XP >= 20 && XP < 30 && previousXP < 20) {
            level = 3;
            newLevel = true;
        }
        else if (XP >= 30 && XP < 50 && previousXP < 30) {
            level = 4;
            newLevel = true;
        }
        else if (XP >= 50 && previousXP < 50) {
            level = 5;
            newLevel = true;
        }
        else {
            newLevel = false;
        }
    }

    public boolean setMP(int amount) {

        if (MP + amount < 0) {
            return false;
        }
        else {
            MP += amount;
            return true;
        }
    }

    public boolean levelUp() {
        if (newLevel) {
            return true;
        }
        return false;
    }

    public void addToInventory(List<Item> items) { items.stream().forEach(e -> inventory.add(e)); }

    public void addOneItem(Item item) { inventory.add(item);}

    // search inventory for equipped item, set item as equipped, reset values and set a new value
    public void equipItem(ItemType type) {

        for (Item i: inventory) {
            if (i.getType().equals(type) && i.getItemClass().equals("weapon")) {
                equippedWeapon = i;
                setAP(0);
                setAP(i.getWeapon());
                return;
            }
            else if (i.getType().equals(type) && i.getItemClass().equals("armor")) {
                equippedArmor = i;
                setDP(0);
                setDP(i.getArmor());
                return;
            }
        }
    }

    // same as before but for usable item
    public void useItem(ItemType type) {
        if (type.equals(ItemType.HEALING_POTION)) {
            int index = 0;
            for (Item i: inventory) {
                if (i.getType().equals(type)) {
                    index = inventory.indexOf(i);   // found item marked, so it can be removed after use
                    if (getHP() + i.getPotion() > 10) {
                        setHP(10);
                    }
                    else {
                        setHP(getHP() + i.getPotion());
                    }
                    break;
                }
            }
            inventory.remove(index);
        }
        else if (type.equals(ItemType.MANA_POTION)) {
            int index = 0;
            for (Item i: inventory) {
                if (i.getType().equals(type)) {
                    index = inventory.indexOf(i);
                    setMP(MP + i.getPotion());
                    break;
                }
            }
            inventory.remove(index);
        }
        else if (type.equals(ItemType.HEALING_SPELL)) {
            int index = 0;
            for (Item i: inventory) {
                if (i.getType().equals(type)) {
                    index = inventory.indexOf(i);
                    setMP(getMP() - 4);
                    setHP(10);
                    break;
                }
            }
            inventory.remove(index);
        }
        else if (type.equals(ItemType.LIGHTNING_BOLT)) {
            int index = 0;
            for (Item i: inventory) {
                if (i.getType().equals(type)) {
                    index = inventory.indexOf(i);
                    setMP(getMP() - 6);
                    lightningArmed = true;
                    break;
                }
            }
            inventory.remove(index);
        }
    }


    //***********************************


    // return the whole inventory list
    public List<Item> getInventory() {
        return inventory;
    }

    // these are just for checking which of inventory items are equippable (weapons & armor) and usable (potions & spells)
    public List<Item> itemsToEquip() {

        List<Item> equippable = inventory
                                    .stream()
                                    .filter(e -> e.getItemClass().equals("weapon") || e.getItemClass().equals("armor"))
                                    .collect(Collectors.toList());

        return equippable;
    }

    public List<Item> itemsToUse() {

        List<Item> usable = inventory
                                .stream()
                                .filter(e -> e.getItemClass().equals("potion") || e.getItemClass().equals("spell"))
                                .collect(Collectors.toList());

        return usable;
    }

    public boolean findItemsByType(ItemType type) {

        boolean found = false;
        for (Item i: inventory) {
            if (i.getType().equals(type)) {
                found = true;
                break;
            }
        }
        return found;
    }

    // shows player's stats
    @Override
    public String toString() {

        String holdingWeapon = "";
        String holdingArmor = "";
        String poisonInfo = "";

        if (equippedWeapon == null)
            holdingWeapon = "none equipped";
        else
            holdingWeapon = equippedWeapon.toString();

        if (equippedArmor == null)
            holdingArmor = "none equipped";
        else holdingArmor = equippedArmor.toString();

        if (poisoned)
            poisonInfo = "yes";
        else
            poisonInfo = "no";


        return "\nPlayer info:\n" +
                "Name: " + name + "\n" +
                "Level: " + level + "\n" +
                "HP: " + getHP() + "\n" +
                "AP: " + getAP() + "\n" +
                "DP: " + getDP() + "\n" +
                "MP: " + MP + "\n" +
                "XP: " + XP + "\n" +
                "Weapon: " + holdingWeapon + "\n" +
                "Armor: " + holdingArmor + "\n" +
                "Poisoned: " + poisonInfo + "\n" +
                "X: " + getX() +
                "Y: " + getY();

    }
}
