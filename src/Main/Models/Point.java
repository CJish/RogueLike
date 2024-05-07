/* Class to represent a point in space within the world map */

package Main.Models;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Point {

    public int x;
    public int y;
    public int z;

    public Point(int x, int y, int z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    // overriding hashCode() to tell Java that two points that represent the same location should be treated as equal;
    // making these value objects instead of reference objects
    // TODO: Research: why is this needed?
    @Override
    public int hashCode() {
//        System.out.println("Launching Point.hashCode");// Debugging 7
        final int prime = 31;
        int result = 1;
        result = prime * result + x;
        result = prime * result + y;
        result = prime * result + z;
//        System.out.println("Finished Point.hashCode");// Debugging 7
        return result;
    }

    // overriding equals() to tell Java that two points that represent the same location should be treated as equal;
    // making these value objects instead of reference objects
    @Override
    public boolean equals(Object obj) {
//        System.out.println("Launching Point.equals");// Debugging 7
        if (this == obj) {return true;}
        if (obj == null) {return false;}
        if (!(obj instanceof Point)) {return false;}
        Point other = (Point) obj;
        if (x != other.x) {return false;}
        if (y != other.y) {return false;}
        if (z != other.z) {return false;}
//        System.out.println("Finished Point.equals");// Debugging 7
        return true;
    }

    // returns a list of the 8 neighbors of a given point
    // TODO: fix it so it doesn't add negative numbers
    public List<Point> neighbors8() {
//        System.out.println("Launched Point.neighbors8");// Debugging 7
        List<Point> points = new ArrayList<Point>();

        for (int ox = -1; ox < 2; ox++) {
            for (int oy = -1; oy < 2; oy++) {
                if (ox == 0 && oy == 0) {continue;}
                Point newPoint = new Point(x + ox, y + oy, z);
//                if (newPoint.x < 0 || newPoint.y < 0 || newPoint.z < 0 ||
//                newPoint.x > 90 || newPoint.y > 31 || newPoint.z > 5) {continue;}
                points.add(newPoint);
            }
        }
        Collections.shuffle(points);
//        System.out.println("Finished Point.neighbors8");// Debugging 7
        return points;
    }
}