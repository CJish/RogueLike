package Main.Models;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;
import java.awt.Color;

public class World {

    private Tile[][][] tiles;
    private int width;
    private int height;
    private int depth;
    private List<Creature> creatures;

    public World(Tile[][][] tiles) {
        this.tiles = tiles;
        this.width = tiles.length;
        this.height = tiles[0].length;
        this.depth = tiles[0][0].length;
        this.creatures = new ArrayList();
    }

    public Tile tile(int x, int y, int z) {
        System.out.println("Launching World.tile");
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= 0) {
            return Tile.BOUNDS;
        } else {
            System.out.println("Finished World.Tile");
            return tiles[x][y][z];
        }
    }

    public Creature creature(int x, int y, int z) {
        System.out.println("Launching World.creature");
        for (Creature c : creatures) {
            if (c.x == x && c.y == y /**&& c.z == z*/) {return c;}
        }
        System.out.println("Finished World.creature");
        return null;
    }

    public void dig(int x, int y, int z) {
        System.out.println("Launching World.dig");
        if(tile(x,y,z).isDiggable()) {tiles[x][y][z] = Tile.FLOOR;}
        System.out.println("Finished World.dig");
    }

    public void addAtEmptyLocation(Creature creature, int z) {
        System.out.println("Launching World.addAtEmptyLocation");
        int x;
        int y;

        do {
            x = (int)(Math.random() * width);
            y = (int)(Math.random() * height);
        }
        while (!tile(x,y,z).isGround() || creature(x,y,z) != null);

        creature.x = x;
        creature.y = y;
        creature.z = z;
        creatures.add(creature);
        System.out.println("Ended World.addAtEmptyLocation");
    }

    public void remove(Creature other) {
        System.out.println("Launching World.remove");
        creatures.remove(other);
        System.out.println("Finished World.remove");
    }

    public void update() {
        System.out.println("Launching World.update");
        List<Creature> toUpdate = new ArrayList<Creature>(creatures);
        for(Creature creature: toUpdate) {
            creature.update();
        }
        System.out.println("Finished World.update");
    }

    public char glyph(int x, int y, int z) {
        return tile(x,y,z).glyph();
    }

    public Color color(int x, int y, int z) {
        return tile(x,y,z).color();
    }

    // interesting take on Getters
    public int width() {return width;}
    public int height() {return height;}
    public int depth() {return depth;}
}