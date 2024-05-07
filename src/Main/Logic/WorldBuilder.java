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
    private Tile[][][] tiles;//TODO: difference here (regions)
    private int nextRegion;

    public WorldBuilder(int width, int height, int layers) {
        System.out.println("Launched WorldBuilder.WorldBuilder");//TODO: Debugging 7
        this.width = width;
        this.height = height;
        this.layers = layers;
        this.tiles = new Tile[width][height][layers];
        System.out.println("Finished WorldBuilder.WorldBuilder");//TODO: Debugging 7
    }

    public World build() {
        System.out.println("Launched WorldBuilder.build");//TODO: Debugging 7
        System.out.println("Finished WorldBuilder.build");//TODO: Debugging 7
        return new World(tiles);
    }

    // create a region map where each region of contiguous space has a number;
    // if two location have the same region number you can walk from one to the
    // other without digging through walls
    private WorldBuilder createRegions() {
        System.out.println("Launched WorldBuilder.createRegions");//TODO: Debugging 7
        regions = new int[width][height][layers];
        nextRegion = 1;

        for (int z = 0; z < layers; z++) {
            for (int x = 0; x < width; x++) {
                for (int y = 0; y < height; y++) {
                    // if the tile at x,y,z isn't a wall AND the region is zero
                    if (tiles[x][y][z] != Tile.WALL && regions[x][y][z] == 0) {
                        // we fill the region's Points with the same number and return the size of the region
                        int size = fillRegion(nextRegion++, x, y, z);
                        // if the region is too small it gets removed; a matter of preference
                        if (size < 25) {
                            removeRegion(nextRegion - 1, z);
                        }
                    }
                }
            }
        }
        System.out.println("Finished WorldBuilder.createRegions");//TODO: Debugging 7
        return this;
    }

    private void removeRegion(int region, int z) {
        System.out.println("Launched WorldBuilder.removeRegion");//TODO: Debugging 7
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (regions[x][y][z] == region) {
                    regions[x][y][z] = 0;
                    tiles[x][y][z] = Tile.WALL;
                }
            }
        }
        System.out.println("Finished WorldBuilder.removeRegion");//TODO: Debugging 7
    }

    private int fillRegion(int region, int x, int y, int z) {
        System.out.println("Launched WorldBuilder.fillRegion");//TODO: Debugging 7
        int size = 1;
        // create a list of Points
        ArrayList<Point> open = new ArrayList<Point>();
        // add the Point x,y,z to the list of Points
        open.add(new Point(x, y, z));
        // set region x,y,z to the nextRegion++ from createRegions (starts at zero)
        regions[x][y][z] = region;

        // as long as the list open has Points in it...
        while (!open.isEmpty()) {
            // remove Point at index zero
            Point p = open.remove(0);
            // get that Point's 8 neighbors and put them into a list
//            List<Point> neighbors = p.neighbors8();
            // take the list of neighbors and iterate through its points one by one
            for (Point neighbor : p.neighbors8()) {

                if (neighbor.x >= 0 && neighbor.y >=0 && neighbor.x < width && neighbor.y < height) {
                    // if the region at x,y,z already has a value or is a wall...
                    if (regions[neighbor.x][neighbor.y][neighbor.z] > 0
                            || tiles[neighbor.x][neighbor.y][neighbor.z] == Tile.WALL)
                        // move to the next Point in the neighbors list
                        continue;
                    // otherwise we increase size by one
                    size++;
                    // and assign the current Point (from neighbors) as belonging to the current region
                    regions[neighbor.x][neighbor.y][neighbor.z] = region;
                    // and add the current Point (from neighbors) to the list of points to check for neighbors?
                    open.add(neighbor);
                }
            }
        }
        System.out.println("Finished WorldBuilder.fillRegion");//TODO: Debugging 7
        return size;
    }

    // Start at the top and connect down. Z = 0 = top
    public WorldBuilder connectRegions() {
        System.out.println("Launching WorldBuilder.connectRegions");//TODO: Debugging 7
        for (int z = 0; z < layers - 1; z++) {
            connectRegionsDown(z);
        }
        System.out.println("Finished WorldBuilder.connectRegions");//TODO: Debugging 7
        return this;
    }

    private void connectRegionsDown(int z) {
        System.out.println("Launching WorldBuilder.connectRegionsDown (z)");//TODO: Debugging 7
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
        System.out.println("Finished WorldRegions.connectRegionsDown(z)");//TODO: Debugging 7
    }

    private void connectRegionsDown(int z, int r1, int r2) {
        System.out.println("Launched WorldBuilder.connectRegionsDown(z,r1,r2)");//TODO: Debugging 7
        List<Point> candidates = findRegionOverlaps(z, r1, r2);

        int stairs = 0;
        do {
            Point p = candidates.remove(0);
            tiles[p.x][p.y][z] = Tile.STAIRS_DOWN;
            tiles[p.x][p.y][z+1] = Tile.STAIRS_UP;
            stairs++;
        }
        while (candidates.size() / stairs > 250);
        System.out.println("Finished WorldBuilder.connectRegionsDown(z,r1,r2)");//TODO: Debugging 7
    }

    public List<Point> findRegionOverlaps(int z, int r1, int r2) {
        System.out.println("Launched WorldBuilder.findRegionOverlaps");//TODO: Debugging 7
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
        System.out.println("Finished WorldBuilder.findRegionOverlaps");//TODO: Debugging 7
        return candidates;
    }

    public WorldBuilder makeCaves() {
        System.out.println("Launched and finished WorldBuilder.makeCaves");//TODO: Debugging 7
        return randomizeTiles()
                .smooth(8)
                .createRegions()
                .connectRegions();
    }

    private WorldBuilder randomizeTiles() {
        System.out.println("Launched WorldBuilder.randomizeTiles");//TODO: Debugging 7
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < layers; z++) {
                    tiles[x][y][z] = Math.random() < 0.5 ? Tile.FLOOR : Tile.WALL;
                }
            }
        }
        System.out.println("Finished WorldBuilder.randomizeTiles");//TODO: Debugging 7
        return this;
    }

    // TODO: clean up arrow code
    private WorldBuilder smooth(int times) {
        System.out.println("Launched WorldBuilder.smooth");//TODO: Debugging 7
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
        System.out.println("Finished WorldBuilder.smooth");//TODO: Debugging 7
        return this;
    }

}