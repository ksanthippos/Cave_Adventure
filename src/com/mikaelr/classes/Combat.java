package com.mikaelr.classes;

import java.util.Random;

public class Combat {

    private Player player;
    private Enemy enemy;
    private boolean playerTurn;
    private boolean criticalHit;
    private StringBuilder log;

    public Combat(Player player, Enemy enemy) {
        this.player = player;
        this.enemy = enemy;
        this.playerTurn = true;
        this.criticalHit = false;
        this.log = new StringBuilder();
    }

    public void engageCombat() {

        // decide who starts the attack (50-50) and are there any critical hits (10% chance)
        Random random = new Random();

        if (random.nextInt(2) > 0) {
            playerTurn = true;
        }
        else {
            playerTurn = false;
        }
        if (random.nextInt(8) < 2) {
            criticalHit = true;
        }

        // hit point calculations (no critical)
        int hitToPlayer = player.getDP() - enemy.getAP();
        int hitToEnemy = enemy.getDP() - player.getAP();

        if (hitToPlayer > 0) {
            hitToPlayer = 0;
        }
        else {
            hitToPlayer = Math.abs(hitToPlayer);
        }
        if (hitToEnemy > 0) {
            hitToEnemy = 0;
        }
        else {
            hitToEnemy = Math.abs(hitToEnemy);
        }

        if (playerTurn) {
            if (!criticalHit && !player.isLightningArmed()) {
                enemy.takeHit(hitToEnemy);
                log.append("You deal " + (hitToEnemy) + " damage;");
                log.append("\nEnemy current HP: " + enemy.getHP() + ";");
            }
            else if (!criticalHit && player.isLightningArmed()) {
                enemy.takeHit(hitToEnemy + 5);
                log.append("Boosted with lightning bolt, you deal " + (hitToEnemy + 5) + " damage;");
                log.append("\nEnemy current HP: " + enemy.getHP() + ";");
                player.disarmLightning();
            }
            else {
                log.append("You hit enemy critically, dealing double the damage!");
                enemy.takeHit(player.getAP() * 2);  // critical hit ignores armor and deals 2 x damage
                log.append("\nEnemy current HP: " + enemy.getHP() + ";");
                criticalHit = false;
            }

            playerTurn = false;
        }
        else {
            if (!criticalHit) {
                player.takeHit(hitToPlayer);
                log.append("Enemy deals " + (hitToPlayer) + " damage;");
                log.append("\nYour current HP: " + player.getHP() + ";");
                if (enemy.isPoisonous()&& !player.isPoisoned() && random.nextInt(2) > 0) {
                    player.setPoisoned(true);
                    log.append("\nYou have been poisoned!");
                }
            }
            else {
                log.append("Enemy hits you critically, dealing double the damage!");
                player.takeHit(enemy.getAP() * 2);
                log.append("\nYour current HP: " + player.getHP() + ";");
                if (enemy.isPoisonous() && !player.isPoisoned() && random.nextInt(2) > 0) {
                    player.setPoisoned(true);
                    log.append("\nYou have been poisoned!");
                }
                criticalHit = false;
            }

            playerTurn = true;
        }


        if (player.isAlive() && !enemy.isAlive()) {
            log.append("\nYou survive the battle and gain " + enemy.collectXP() + " XP!");
            player.setXP(enemy.collectXP());
            if (player.levelUp()) {
                log.append("\nYou also reached level " + player.getLevel() + "!");
            }
            if (player.isPoisoned()) {
                log.append("\nYou are also poisoned. Remember that the venom affects you at every 10 turns,");
                log.append("\nso you better find a cure quickly!");
            }
        }

    }

    public String combatReport() {
        return log.toString();
    }






}
