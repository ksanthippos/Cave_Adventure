package com.mikaelr.classes;


public class Enemy extends Entity {

    private EnemyType type;
    private boolean poisonous;
    private boolean active;
    private int rewardXP;

    public Enemy(int HP, int DP, int AP, boolean alive, int x, int y, EnemyType type, boolean poisonous, int rewardXP) {
        super(HP, DP, AP, alive, x, y);
        this.type = type;
        this.poisonous = poisonous;
        this.active = false;
        this.rewardXP = rewardXP;
    }

    public EnemyType getType() {
        return type;
    }

    public String toString() {
        if (type.equals(EnemyType.RAT)) {
            return "a filthy rat with long tail and a pair of glowing, red eyes. It is glaring at you.";
        }
        else if (type.equals(EnemyType.SNAKE)) {
            return "a green-black-snake, hissing at you. Wouldn't be a surprise, if it was a poisonous one.";
        }
        else if (type.equals(EnemyType.GOBLIN)) {
            return "a little green goblin, but not a particulary merry-looking-one.\nIt has small but sharp teeth, a leather " +
                    "armor and it's pointing a short\nspear in your direction.\n";
        }
        else if (type.equals(EnemyType.ORC)) {
            return "an ugly, muscular orc warrior.\nYou can hear its growl under a crude helmet and it is armed with a shield\n" +
                    "and angular-shaped, big sword.\n";
        }
        else if (type.equals(EnemyType.CAVE_TROLL)) {
            return "a huge cave troll flailing its gigantic limbs like crazy! It carries no weapons but\n" +
                    "you bet those meaty fists and big, yellow teeth can hold on their own against even the\n" +
                    "bravest knights.";
        }
        else if (type.equals(EnemyType.DRAGON)) {
            return "a glow.. then feel the heat.. and finally air is pierced with a terrifying screech! You have found a dragon's den,\n" +
                    "and you can tell its owner doesn't look like one of the kindest ones. It must be 20 meters long, has smoke hissing through\n" +
                    "its nostrils and it stares at you, with sharp, intelligent green eyes. One of you won't leave this room alive!";
        }
        return "";
    }

    public boolean isPoisonous() {
        if (poisonous) {
            return true;
        }
        return false;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean value) {
        active = value;
    }

    public int collectXP() {
        return rewardXP;
    }

}
