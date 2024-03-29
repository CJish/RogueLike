package Main.Logic;

import Main.Models.Point;
import Main.Models.Tile;
import Main.Models.World;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class WorldBuilder {

    private int width;
    private int height;
    private int layers;
    private int regions[][][];
    private Tile[][][] tiles;

    public WorldBuilder(int width, int height, int layers) {
        this.width = width;
        this.height = height;
        this.layers = layers;
        this.tiles = new Tile[width][height][layers];
    }

    public World build() {
        return new World(tiles);
    }

    // create a region map where each region of contiguous space has a number;
    // if two location have the same region number you can walk from one to the
    // other without digging through walls

    private WorldBuilder createRegions() {
        regions = new int[width][height][layers];
        int nextRegion = 0;

        for (int z = 0; z < layers; z++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    if (tiles[x][y][z] != Tile.WALL && regions[x][y][z] == 0) {
                        int size = fillRegion(nextRegion++, x, y, z);
                        // if the region is too small it gets removed; a matter of preference
                        if (size < 25) {
                            removeRegion(nextRegion - 1, z);
                        }
                    }
                }
            }
        }
        return this;
    }

    private void removeRegion(int region, int z) {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (regions[x][y][z] == region) {
                    regions[x][y][z] = 0;
                    tiles[x][y][z] = Tile.WALL;
                }
            }
        }
    }

    private int fillRegion(int region, int x, int y, int z) {
        int size = 1;
        ArrayList<Point> open = new ArrayList<Point>();
        open.add(new Point(x, y, z));
        regions[x][y][z] = region;

        while (!open.isEmpty()) {
            Point p = open.remove(0);
            List<Point> neighbors = p.neighbors8();
            for (Point neighbor : neighbors) {
                if (regions[neighbor.x][neighbor.y][neighbor.z] > 0
                || tiles[neighbor.x][neighbor.y][neighbor.z] == Tile.WALL)
                    continue;
                size++;
                regions[neighbor.x][neighbor.y][neighbor.z] = region;
                open.add(neighbor);
            }
        }
        return size;
    }

    // Start at the top and connect down. Z = 0 = top
    public WorldBuilder connectRegions() {
        for (int z = 0; z < layers - 1; z++) {
            connectRegionsDown(z);
        }
        return this;
    }

    private void connectRegionsDown(int z) {
        List<String> connected = new ArrayList<String>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                String region = regions[x][y][z] + "," + regions[x][y][z+1];
                if (tiles[x][y][z] == Tile.FLOOR
                    && tiles[x][y][z+1] == Tile.FLOOR
                    && !connected.contains(region)) {
                    connected.add(region);
                    connectRegionsDown(z, regions[x][y][z], regions[x][y][z+1]);
                }
            }
        }
    }

    private void connectRegionsDown(int z, int r1, int r2) {
        List<Point> candidates = findRegionOverlaps(z, r1, r2);

        int stairs = 0;
        do {
            Point p = candidates.remove(0);
            tiles[p.x][p.y][z] = Tile.STAIRS_DOWN;
            tiles[p.x][p.y][z+1] = Tile.STAIRS_UP;
            stairs++;
        }
        while (candidates.size() / stairs > 250);
    }

    public List<Point> findRegionOverlaps(int z, int r1, int r2) {
        ArrayList<Point> candidates = new ArrayList<Point>();

        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (tiles[x][y][z] == Tile.FLOOR
                    && tiles[x][y][z+1] == Tile.FLOOR
                    && regions[x][y][z] == r1
                    && regions[x][y][z+1] == r2) {
                    candidates.add(new Point(x, y, z));
                }
            }
        }
        Collections.shuffle(candidates);
        return candidates;
    }

    public WorldBuilder makeCaves() {
        return randomizeTiles()
                .smooth(8)
                .createRegions()
                .connectRegions();
    }

    private WorldBuilder randomizeTiles() {
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < layers; z++) {
                    tiles[x][y][z] = Math.random() < 0.5 ? Tile.FLOOR : Tile.WALL;
                }
            }
        }
        return this;
    }

    // TODO: clean up arrow code
    private WorldBuilder smooth(int times) {
        Tile[][][] tiles2 = new Tile[width][height][layers];
        for (int time = 0; time < times; time++) {
            for (int z = 0; z < layers; z++) {
                for (int x = 0; x < width; x++) {
                    for (int y = 0; y < height; y++) {
                        int floors = 0;
                        int walls = 0;

                        for (int ox = -1; ox < 2; ox++) {
                            for (int oy = -1; oy < 2; oy++) {
                                if (x + ox < 0 || x + ox >= width || y + oy < 0 || y + oy >= height)
                                    continue;

                                if (tiles[x + ox][y + oy][z] == Tile.FLOOR) {
                                    floors++;
                                } else {
                                    walls++;
                                }
                            }
                        }
                        tiles2[x][y][z] = floors >= walls ? Tile.FLOOR : Tile.WALL;
                    }
                }
            }
            tiles = tiles2;
        }
        return this;
    }

}