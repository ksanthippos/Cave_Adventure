package com.mikaelr.textgameapp;


public class Item {

    private ItemType type;
    private String itemClass;
    private String name;

    private int armor;
    private int weapon;
    private int potion;
    private int spell;
    private int treasure;

    public Item(ItemType type) {
        this.type = type;

        if (type.equals(ItemType.SHORT_SWORD)) {
            this.name = "short sword";
            this.itemClass = "weapon";
            this.weapon = 2;
        }
        else if (type.equals(ItemType.BATTLE_AXE)) {
            this.name = "battle axe";
            this.itemClass = "weapon";
            this.weapon = 5;
        }
        else if (type.equals(ItemType.CHAINMAIL_ARMOR)) {
            this.name = "chainmail";
            this.itemClass = "armor";
            this.armor = 2;
        }
        else if (type.equals(ItemType.KNIGHT_ARMOR)) {
            this.name = "knight armor";
            this.itemClass = "armor";
            this.armor = 4;
        }
        else if (type.equals(ItemType.HEALING_SPELL)) {
            this.name = "healing spell";
            this.itemClass = "spell";
            this.spell = 10;
        }
        else if (type.equals(ItemType.LIGHTNING_BOLT)) {
            this.name = "lightning bolt";
            this.itemClass = "spell";
            this.spell = 7;
        }
        else if (type.equals(ItemType.HEALING_POTION)) {
            this.name = "healing potion";
            this.itemClass = "potion";
            this.potion = 5;
        }
        else if (type.equals(ItemType.MANA_POTION)) {
            this.name = "mana potion";
            this.itemClass = "potion";
            this.potion = 10;
        }
        else if (type.equals(ItemType.SILVER)) {
            this.name = "silver coins";
            this.itemClass = "treasure";
            this.treasure = 3;
        }
        else if (type.equals(ItemType.GOLD)) {
            this.name = "gold coins";
            this.itemClass = "treasure";
            this.treasure = 10;
        }
        else if (type.equals(ItemType.NOTHING)) {
            this.name = "nothing";
        }


    }

    public ItemType getType() {
        return type;
    }

    public String getItemClass() { return itemClass; }

    public int getArmor() { return armor; }

    public int getWeapon() {
        return weapon;
    }

    public int getPotion() {
        return potion;
    }

    public int getSpell() {
        return spell;
    }

    public int getTreasure() {
        return treasure;
    }

    @Override
    public String toString() {
        return this.name;
    }
}
