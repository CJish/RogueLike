package Main.Models;

import asciiPanel.AsciiPanel;

import java.awt.*;

// TODO: add tile function for broken wall so that players take
// two turns to dig through a wall
public enum Tile {

    FLOOR((char)250, AsciiPanel.yellow),
    WALL((char)177, AsciiPanel.yellow),
    BOUNDS('x', AsciiPanel.brightBlack);

    // can't we just check if the tile is a wall over in World?
    public boolean isDiggable() {
        return this == Tile.WALL;
    }

    public boolean isGround() {
        return this == Tile.FLOOR;
    }

    private char glyph;
    public char glyph() {return glyph;}

    private Color color;
    public Color color() {return color;}

    Tile(char glyph, Color color) {
        this.glyph = glyph;
        this.color = color;
    }

}