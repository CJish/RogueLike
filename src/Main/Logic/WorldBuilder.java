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
//        System.out.println("Launched WorldBuilder.WorldBuilder");// Debugging 7
        this.width = width;
        this.height = height;
        this.layers = layers;
        this.tiles = new Tile[width][height][layers];
//        System.out.println("Finished WorldBuilder.WorldBuilder");// Debugging 7
    }

    public World build() {
//        System.out.println("Launched WorldBuilder.build");// Debugging 7
//        System.out.println("Finished WorldBuilder.build");// Debugging 7
        return new World(tiles);
    }

    // create a region map where each region of contiguous space has a number;
    // if two location have the same region number you can walk from one to the
    // other without digging through walls
    private WorldBuilder createRegions() {
//        System.out.println("Launched WorldBuilder.createRegions");// Debugging 7
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
//        System.out.println("Finished WorldBuilder.createRegions");// Debugging 7
        return this;
    }

    // This isn't strictly necessary; apparently only fills in regions that are less than
    // 25 tiles square via WorldBuilder.fillRegion()
    private void removeRegion(int region, int z) {
//        System.out.println("Launched WorldBuilder.removeRegion");// Debugging 7
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                if (regions[x][y][z] == region) {
                    regions[x][y][z] = 0;
                    tiles[x][y][z] = Tile.WALL;
                }
            }
        }
//        System.out.println("Finished WorldBuilder.removeRegion");// Debugging 7
    }

    // Labels each region (contiguous; don't need to go through walls)
    // "we can use the regions array to see if two tiles are part of the same open space.
    // TODO: why do we need this?
    // TODO: how are we going to use this?
    // TODO: will this be updated as characters dig tunnels?
    private int fillRegion(int region, int x, int y, int z) {
//        System.out.println("Launched WorldBuilder.fillRegion");// Debugging 7
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
//        System.out.println("Finished WorldBuilder.fillRegion");// Debugging 7
        return size;
    }

    // Start at the top and connect down. Z = 0 = top
    // connectRegions() is the first of three overloaded functions
    public WorldBuilder connectRegions() {
//        System.out.println("Launching WorldBuilder.connectRegions");// Debugging 7
        for (int z = 0; z < layers - 1; z++) { // iterate through each layer of the map
            connectRegionsDown(z); // call the next of the overloaded functions
        }
//        System.out.println("Finished WorldBuilder.connectRegions");// Debugging 7
        return this;
    }

    private void connectRegionsDown(int z) {
//        System.out.println("Launching WorldBuilder.connectRegionsDown (z)");// Debugging 7
        List<String> connected = new ArrayList<String>();

        for (int x = 0; x < width; x++) { // go through each horizontal tile on the floor
            for (int y = 0; y < height; y++) { // go through each vertical tile on the floor
                String region = regions[x][y][z] + "," + regions[x][y][z+1]; // just connecting two points into one
                if (tiles[x][y][z] == Tile.FLOOR // if the tile at (x,y,z) is floor
                    && tiles[x][y][z+1] == Tile.FLOOR // and the tile below it is a floor
                    && !connected.contains(region)) { // and they are not connected // TODO: how does 'connected' tie into this?
                    connected.add(region); // then connect them
                    connectRegionsDown(z, regions[x][y][z], regions[x][y][z+1]); // Overloading example. call the function that actually connects them - connectRegionsDown(z, r1, r2).
                }
            }
        }
//        System.out.println("Finished WorldRegions.connectRegionsDown(z)");// Debugging 7
    }

    // Last of the overloaded connectRegionsDown functions
    // takes the region from connectRegionsDown(int z), compares and //TODO: I don't understand this yet and need to look at it further


    private void connectRegionsDown(int z, int r1, int r2) {
//        System.out.println("Launched WorldBuilder.connectRegionsDown(z,r1,r2)");// Debugging 7
        List<Point> candidates = findRegionOverlaps(z, r1, r2); // We're using the Point class which is a simple (x,y,z) coord and calling findRegionOverlaps() to fill the List<Point>

        int stairs = 0;
        do {
            Point p = candidates.remove(0);
            tiles[p.x][p.y][z] = Tile.STAIRS_DOWN;
            tiles[p.x][p.y][z+1] = Tile.STAIRS_UP;
            stairs++;
        }
        while (candidates.size() / stairs > 250);
//        System.out.println("Finished WorldBuilder.connectRegionsDown(z,r1,r2)");// Debugging 7
    }

    // Super clever how we r1 and r2 passed into this function via several other functions
    public List<Point> findRegionOverlaps(int z, int r1, int r2) {
//        System.out.println("Launched WorldBuilder.findRegionOverlaps");// Debugging 7
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
//        System.out.println("Finished WorldBuilder.findRegionOverlaps");// Debugging 7
        return candidates;
    }

    public WorldBuilder makeCaves() {
//        System.out.println("Launched and finished WorldBuilder.makeCaves");// Debugging 7
        return randomizeTiles()
                .smooth(8)
                .createRegions()
                .connectRegions();
    }

    private WorldBuilder randomizeTiles() {
//        System.out.println("Launched WorldBuilder.randomizeTiles");// Debugging 7
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                for (int z = 0; z < layers; z++) {
                    tiles[x][y][z] = Math.random() < 0.5 ? Tile.FLOOR : Tile.WALL;
                }
            }
        }
//        System.out.println("Finished WorldBuilder.randomizeTiles");// Debugging 7
        return this;
    }

    // TODO: clean up arrow code
    private WorldBuilder smooth(int times) {
//        System.out.println("Launched WorldBuilder.smooth");// Debugging 7
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
//        System.out.println("Finished WorldBuilder.smooth");// Debugging 7
        return this;
    }

}