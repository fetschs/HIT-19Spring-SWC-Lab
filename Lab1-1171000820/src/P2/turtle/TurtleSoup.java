/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package P2.turtle;

import java.util.List;
import java.util.Set;

import java.util.ArrayList;
import java.util.HashSet;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle     the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        // throw new RuntimeException("implement me!");
        turtle.forward(sideLength);
        turtle.turn(90);
        turtle.forward(sideLength);
        turtle.turn(90);
        turtle.forward(sideLength);
        turtle.turn(90);
        turtle.forward(sideLength);
        turtle.turn(90);

    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon; you
     * should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        // throw new RuntimeException("implement me!");
        return (double) (sides - 2) * (double) (180) / (double) (sides);
    }

    /**
     * Determine number of sides given the size of interior angles of a regular
     * polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see
     * java.lang.Math). HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        throw new RuntimeException("implement me!");
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to
     * draw.
     * 
     * @param turtle     the turtle context
     * @param sides      number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        // throw new RuntimeException("implement me!");
        double degrees = TurtleSoup.calculateRegularPolygonAngle(sides);
        for (int i = 1; i <= sides; i++) {
            turtle.turn(degrees);
            turtle.forward(sideLength);
        }
    }

    /**
     * Given the current direction, current location, and a target location,
     * calculate the Bearing towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in
     * the direction of the target point (targetX,targetY), given that the turtle is
     * already at the point (currentX,currentY) and is facing at angle
     * currentBearing. The angle must be expressed in degrees, where 0 <= angle <
     * 360.
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX       current location x-coordinate
     * @param currentY       current location y-coordinate
     * @param targetX        target point x-coordinate
     * @param targetY        target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY, int targetX,
            int targetY) {
        // throw new RuntimeException("implement me!");
        double disx = (double) (targetX - currentX);
        double disy = (double) (targetY - currentY);
        double hypotenuse = Math.sqrt(Math.pow(disx, 2.0) + Math.pow(disy, 2.0));
        double cos = Math.abs(disy) / hypotenuse;
        double radian = Math.acos(cos);
        double angle = 180 / (Math.PI / radian);
        if (currentX <= targetX && currentY <= targetY) {
            angle = angle + 0;

        }

        else if (currentX <= targetX && currentY > targetY) {
            angle = 180 - angle;
        } else if (currentX > targetX && currentY > targetY) {
            angle = 180 + angle;

        } else if (currentX > targetX && currentY <= targetY) {
            angle = 360 - angle;

        }
        angle = angle - currentBearing;
        if (angle < 0)
            angle = angle + 360;
        return angle;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get
     * from each point to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0
     * degrees). For each subsequent point, assumes that the turtle is still facing
     * in the direction it was facing when it moved to the previous point. You
     * should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of
     *         points) == 0, otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
        // throw new RuntimeException("implement me!");
        int size = xCoords.size();
        int currentX = xCoords.get(0);
        int currentY = yCoords.get(0);
        double currentBearing = 0;
        List<Double> angleList = new ArrayList<>();
        for (int i = 1; i < size; i++) {
            int targetX = xCoords.get(i);
            int targetY = yCoords.get(i);
            double changeBearing = TurtleSoup.calculateBearingToPoint(currentBearing, currentX, currentY, targetX,
                    targetY);
            angleList.add(changeBearing);
            currentBearing = currentBearing + changeBearing;
            if (currentBearing >= 360) {
                currentBearing -= 360;
            }
            currentX = targetX;
            currentY = targetY;
        }
        return angleList;
    }

    /**
     * 
     * @param c point c
     * @param a point a
     * @param b point b
     * @return cross product
     */
    public static double cross(Point c, Point a, Point b) {
        return (c.x() - a.x()) * (a.y() - b.y()) - (c.y() - a.y()) * (a.x() - b.x());
    }

    /**
     * calculate the distance of these points
     * 
     * @param p1 point1
     * @param p2 point2
     * @return distance between p1 and p2
     */
    public static double distance(Point p1, Point p2) {
        return (Math.hypot((p1.x() - p2.x()), (p1.y() - p2.y())));
    }

    /*
     * Given a set of points, compute the convex hull, the smallest convex set that
     * contains all the points in a set of input points. The gift-wrapping algorithm
     * is one simple approach to this problem, and there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty,
     * contain only 1 point, two points or more.
     * 
     * @return minimal subset of the input points that form the vertices of the
     * perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        // throw new RuntimeException("implement me!");
        if (points.size() <= 3) {
            return points;
        }
        Set<Point> retpoints = new HashSet<Point>();
        Point startPoint = null;
        for (Point temp : points) {
            if (startPoint == null) {
                startPoint = temp;
                continue;
            }
            if (temp.x() < startPoint.x()) {
                startPoint = temp;
            } else if (temp.x() == startPoint.x()) {
                if (temp.y() < startPoint.y()) {
                    startPoint = temp;
                }
            }
        }
        retpoints.add(startPoint);// find the first point
        while (true) {
            Point nextPoint = null;
            for (Point temp : points) {
                if (retpoints.contains(temp) == false) {
                    nextPoint = temp;
                    break;
                }
            }
            if (nextPoint == null)
                break;
            // check if finish work
            for (Point temp : points) {
                if ((cross(startPoint, temp, nextPoint) > 0) || ((cross(startPoint, temp, nextPoint) == 0)
                        && (distance(startPoint, temp) > distance(startPoint, nextPoint)))) {
                    nextPoint = temp;
                }
            } // user cross to find next point
            if (retpoints.contains(nextPoint)) {
                break;
            }
            retpoints.add(nextPoint);
            startPoint = nextPoint;
        }
        return retpoints;
    }

    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a
     * turtle. For this function, draw something interesting; the complexity can be
     * as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        // throw new RuntimeException("implement me!");
        turtle.color(PenColor.RED);
        int count = 0;
        while (true) {
            turtle.forward(200);
            turtle.turn(164);
            count++;
            if (count > 100)
                break;
        }

    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();
        // drawSquare(turtle, 40);
        // drawRegularPolygon(turtle, 4, 40);
        drawPersonalArt(turtle);
        // draw the window
        turtle.draw();
    }

}
