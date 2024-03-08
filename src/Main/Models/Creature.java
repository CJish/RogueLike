package Main.Models;

import Main.Logic.CreatureAi;

import java.awt.*;

public class Creature {

    private World world;
    public int x; // might should use Getters/Setter instead
    public int y;
    private char glyph;
    private Color color;
    private CreatureAi ai;

    public Creature(World world, char glyph, Color color) {
        this.world = world;
        this.glyph = glyph;
        this.color = color;
    }

    public void moveBy(int mx, int my) {
        ai.onEnter(x + mx, y + my, world.tile(x + mx, y + my));
    }

    public void dig(int wx, int wy) {
        world.dig(wx, wy);
    }

    public char glyph() {return glyph;}
    public Color color() {return color;}
    // setter injection
    public void setCreatureAi(CreatureAi ai) {this.ai = ai;}
}