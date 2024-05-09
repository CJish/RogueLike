package Main.Models;

import Main.Logic.CreatureAi;

import java.awt.*;

public class Creature {

    private World world;
    public int x; // might should use Getters/Setter instead
    public int y;
    public int z;
    private char glyph;
    private Color color;
    private CreatureAi ai;
    private int maxHP;
    private int hp;
    private int attackValue;
    private int defenseValue;

    public Creature(World world, char glyph, Color color, int maxHP, int attack, int defense) {
//        System.out.println("Created Creature.Creature");// Debugging 7
        this.world = world;
        this.glyph = glyph;
        this.color = color;
        this.maxHP = maxHP;
        this.hp = maxHP;
        this.attackValue = attack;
        this.defenseValue = defense;
//        System.out.println("Finished Creature.Creature");// Debugging 7
    }

    public void notify(String message, Object ... params) {

//        System.out.println("Launched Creature.notify");// Debugging 7
        ai.onNotify(String.format(message, params));
//        System.out.println("Finished Creature.notify");// Debugging 7
    }

    // TODO: This is the next part of STEP 7 that needs addressing
    /* All characters (currently only have stationary fungus and mobile player) need to be able to
    change floors.

    My idea is that chars will change floors when they try to enter a tile that has a stair on it
    // TODO: check and see if stairs are one way and reciprocal; ie the up tile on one floor is directly below the down tile on another floor
    This way when NPCs wander onto stairs they will change levels
    Should make the game more interesting
     */
    public void moveBy(int mx, int my, int mz) {
//        System.out.println("Launched Creature.moveBy");// Debugging 7
        Tile tile = world.tile(x+mx, y+my, z+mz);
        if (mz == -1) {
            if (tile ==Tile.STAIRS_DOWN) {
                doAction("walk down the stairs to level %d", z + mz);
            } else {
                doAction("no stairs here");
                return;
            }
        } else if (mz == 1) {
            if (tile == Tile.STAIRS_UP) {
                doAction("walk up the stairs to level %d", z + mz);
            } else {
                doAction("no stairs here");
                return;
            }
        }

        Creature other = world.creature(x + mx, y + my, z + mz);
        if (other == null) {
            ai.onEnter(x + mx, y + my, z + mz, tile);
        } else attack(other);
//        System.out.println("Finished Creature.moveBy");// Debugging 7
    }

    public void attack(Creature other) {
//        System.out.println("Launched Creature.attack");// Debugging 7
        int amount = Math.max(0, attackValue() - other.defenseValue());

        amount = (int)(Math.random() * amount + 1);
        other.modifyHP(-amount);
        // TODO: either notify or doAction, no both
//        notify("You attack the '%s' for %d damage.", other.glyph, amount);
//        other.notify("The '%s' attacks you for %d damage.", glyph, amount);
        doAction("attack the '%s' for %d damage", other.glyph, amount);
//        System.out.println("Finished Creature.attack");// Debugging 7
    }

    public void doAction(String message, Object ... params) {
//        System.out.println("Launched Creature.doAction");// Debugging 7
        int r = 9;
        for (int ox = -r; ox < r + 1; ox++) {
            for (int oy = -r; oy < r + 1; oy++) {
                if (ox*ox + oy*oy > r*r) {continue;}

                Creature other = world.creature(x + ox, y + oy, z);

                if (other == null) {continue;}

                if (other == this) {
                    other.notify("You " + message + ".", params);
                } else {
                    other.notify(String.format("The '%s' %s.", glyph, makeSecondPerson(message)), params);
                }
            }
        }
//        System.out.println("Finished Creature.doAction");// Debugging 7
    }

    // TODO: move this outta here
    private String makeSecondPerson(String text) {
//        System.out.println("Launched Creature.makeSecondPerson");// Debugging 7
        String[] words = text.split(" ");
        words[0] = words[0] + "s";

        StringBuilder builder = new StringBuilder();
        for (String word : words) {
            builder.append(" ");
            builder.append(word);
        }
//        System.out.println("Finished Creature.makeSecondPerson");// Debugging 7
        return builder.toString().trim();
    }

    public void modifyHP(int amount) {
//        System.out.println("Launched Creature.modifyHP");// Debugging 7
        hp += amount;
        if (hp < 1) {
            doAction("die");
            world.remove(this);
        }
//        System.out.println("Finished Creature.modifyHP");// Debugging 7
    }

    public void update() {
//        System.out.println("Launched Creature.update");// Debugging 7
        ai.onUpdate();
//        System.out.println("Finished Creature.update");// Debugging 7
    }

    public void dig(int wx, int wy, int wz) {
//        System.out.println("Launched Creature.dig");// Debugging 7
        world.dig(wx, wy, wz);
//        System.out.println("Finished Creature.dig");// Debugging 7
    }

    public char glyph() {return glyph;}
    public Color color() {return color;}
    // setter injection
    public void setCreatureAi(CreatureAi ai) {this.ai = ai;}
    public int maxHP() {return maxHP;}
    public int hp() {return hp;}
    public int attackValue() {return attackValue;}
    public int defenseValue() {return defenseValue;}

    public boolean canEnter(int wx, int wy, int wz) {
        return world.tile(wx, wy, wz).isGround() && world.creature(wx, wy, wz) == null;
    }
}