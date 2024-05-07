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
//        System.out.println("Launching World.tile");//TODO: Debugging 7
        // if within (x, y, z) bounds
        if (x < 0 || x >= width || y < 0 || y >= height || z < 0 || z >= depth) { //TODO: difference here THIS IS WHERE THE ERROR WAS. had 0 but should have been depth
            return Tile.BOUNDS;
        } else {
            System.out.println("Finished World.Tile");//TODO: Debugging 7
            return tiles[x][y][z];
        }
    }

    public Creature creature(int x, int y, int z) {
        System.out.println("Launching World.creature");//TODO: Debugging 7
        for (Creature c : creatures) {
            if (c.x == x && c.y == y /**&& c.z == z*/) {return c;}
        }
        System.out.println("Finished World.creature");//TODO: Debugging 7
        return null;
    }

    public void dig(int x, int y, int z) {
        System.out.println("Launching World.dig");//TODO: Debugging 7
        if(tile(x,y,z).isDiggable()) {tiles[x][y][z] = Tile.FLOOR;}
        System.out.println("Finished World.dig");//TODO: Debugging 7
    }

    public void addAtEmptyLocation(Creature creature, int z) {
        System.out.println("Launching World.addAtEmptyLocation");//TODO: Debugging 7
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
        System.out.println("Ended World.addAtEmptyLocation");//TODO: Debugging 7
    }

    public void remove(Creature other) {
        System.out.println("Launching World.remove");//TODO: Debugging 7
        creatures.remove(other);
        System.out.println("Finished World.remove");//TODO: Debugging 7
    }

    public void update() {
        System.out.println("Launching World.update");//TODO: Debugging 7
        List<Creature> toUpdate = new ArrayList<Creature>(creatures);
        for(Creature creature: toUpdate) {
            creature.update();
        }
        System.out.println("Finished World.update");//TODO: Debugging 7
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