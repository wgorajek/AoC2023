package com.wgorajek;

import java.awt.*;
import java.beans.PropertyEditorSupport;
import java.sql.Array;
import java.util.*;
import java.util.function.Predicate;

public class Day23 extends Solution {
    @Override
    public Object getPart1Solution() {
        var garden = getInput();
        var total = 0;
        var start = new Point(1, 0);
        var end = new Point(garden.maxX - 1, garden.maxX);
        Queue<Path> paths = new LinkedList<Path>();

        var firstPath = new Path();
        firstPath.map.putAll(garden.map);
        firstPath.lastPoint = start;
        firstPath.lastPointCharacter = firstPath.map.remove(start);
        paths.add(firstPath);
        while (!paths.isEmpty()) {
            var path = paths.remove();
            if (path.lastPoint.equals(end)) {
                total = Math.max(total, path.pathlength);
            } else {
                paths.addAll(path.getNextPaths());
            }
        }
        return total;
    }


    @Override
    public Object getPart2Solution() {
        var garden = getInput();
        var total = 0;
        var start = new Point(1, 0);
        var end = new Point(garden.maxX - 1, garden.maxX);
        var crossroads = new HashMap<Point, Crossroad>();

        var firstCrossroad = new Crossroad(start);

        Queue<Crossroad> crossroadsToFill = new LinkedList<Crossroad>();

        firstCrossroad.exits.addAll(garden.getAllNeighbours(start));
        crossroadsToFill.add(firstCrossroad);
        crossroads.put(firstCrossroad.point, firstCrossroad);
        while (!crossroadsToFill.isEmpty()) {
            var crossroad = crossroadsToFill.remove();
            for (var point : crossroad.exits) {
                var path = new Path();
                path.pathlength = 1;
                path.map.putAll(garden.map);
                path.lastPoint = point;
                path.lastPointCharacter = path.map.remove(point);
                path.map.remove(crossroad.point);
                path.getToTheNextCrossroad();
                Crossroad secondCrossroad = null;
                if (crossroads.containsKey(path.lastPoint)) {
                    secondCrossroad = crossroads.get(path.lastPoint);
                } else {
                    secondCrossroad = new Crossroad(path.lastPoint);
                    secondCrossroad.exits.addAll(garden.getAllNeighbours(path.lastPoint));
                    crossroadsToFill.add(secondCrossroad);
                    crossroads.put(secondCrossroad.point, secondCrossroad);
                }
                crossroad.neighbours.put(point, secondCrossroad);
                crossroad.neighbourPathLength.put(point, path.pathlength);
            }
        }

        var paths = new LinkedList<ComplexPath>();
        var firstPath = new ComplexPath(crossroads.get(start));
        firstPath.crossroadsToVisit.addAll(crossroads.values());
        firstPath.crossroadsToVisit.remove(firstPath.actualCrossroad);
        paths.add(firstPath);
        while (!paths.isEmpty()) {
            var path = paths.remove();
            for (var neighbourPoint : path.actualCrossroad.exits) {
                var neighbour = path.actualCrossroad.neighbours.get(neighbourPoint);
                if (path.crossroadsToVisit.contains(neighbour)) {
                    var newPath = new ComplexPath(neighbour);
                    newPath.crossroadsToVisit.addAll(path.crossroadsToVisit);
                    newPath.crossroadsToVisit.remove(neighbour);
                    newPath.pathLength = path.pathLength + path.actualCrossroad.neighbourPathLength.get(neighbourPoint);
                    if (newPath.actualCrossroad.point.equals(end)) {
                        total = Math.max(total, newPath.pathLength);
                    } else {
                        paths.add(newPath);
                    }
                }
            }
        }
        return total;
    }

    public class ComplexPath {
        HashSet<Crossroad> crossroadsToVisit = new HashSet<>();
        Crossroad actualCrossroad;
        int pathLength = 0;

        public ComplexPath(Crossroad actualCrossroad) {
            this.actualCrossroad = actualCrossroad;
        }
    }

    public class Crossroad {
        Point point;
        ArrayList<Point> exits = new ArrayList<Point>();
        HashMap<Point, Crossroad> neighbours = new LinkedHashMap<Point, Crossroad>();
        HashMap<Point, Integer> neighbourPathLength = new LinkedHashMap<Point, Integer>();

        public Crossroad(Point point) {
            this.point = point;
        }
    }

    public class Path {
        HashMap<Point, Character> map = new HashMap<Point, Character>();
        Point lastPoint;
        Character lastPointCharacter;
        int pathlength = 0;

        public boolean isValid(Point point) {
            return map.containsKey(point) && map.get(point) != '#';
        }

        public Path(Path path) {
            this.map.putAll(path.map);
            this.pathlength = path.pathlength;
        }

        public Path() {

        }

        public void getToTheNextCrossroad() {
            while (true) {
                var validPoints = 0;
                Point validPoint = null;
                if (isValid(new Point(lastPoint.x + 1, lastPoint.y))) {
                    validPoint = new Point(lastPoint.x + 1, lastPoint.y);
                    validPoints++;
                }
                if (isValid(new Point(lastPoint.x - 1, lastPoint.y))) {
                    validPoint = new Point(lastPoint.x - 1, lastPoint.y);
                    validPoints++;
                }
                if (isValid(new Point(lastPoint.x, lastPoint.y + 1))) {
                    validPoint = new Point(lastPoint.x, lastPoint.y + 1);
                    validPoints++;
                }
                if (isValid(new Point(lastPoint.x, lastPoint.y - 1))) {
                    validPoint = new Point(lastPoint.x, lastPoint.y - 1);
                    validPoints++;
                }
                if (validPoints != 1) {
                    return;
                } else {
                    this.lastPoint = validPoint;
                    this.lastPointCharacter = map.remove(lastPoint);
                    pathlength++;
                }
            }
        }

        public void addNewPathIfValid(Point point, ArrayList<Path> paths) {
            if (isValid(point)) {
                var newPath = new Path(this);
                newPath.lastPoint = point;
                newPath.lastPointCharacter = newPath.map.remove(point);
                newPath.pathlength++;
                paths.add(newPath);
            }
        }

        public ArrayList<Path> getNextPaths() {
            var result = new ArrayList<Path>();
            if (lastPointCharacter == '>') {
                addNewPathIfValid(new Point(lastPoint.x + 1, lastPoint.y), result);
            } else if (lastPointCharacter == 'v') {
                addNewPathIfValid(new Point(lastPoint.x, lastPoint.y + 1), result);
            } else if (lastPointCharacter == '<') {
                addNewPathIfValid(new Point(lastPoint.x - 1, lastPoint.y), result);
            } else if (lastPointCharacter == '^') {
                addNewPathIfValid(new Point(lastPoint.x, lastPoint.y - 1), result);
            } else {
                addNewPathIfValid(new Point(lastPoint.x + 1, lastPoint.y), result);
                addNewPathIfValid(new Point(lastPoint.x, lastPoint.y - 1), result);
                addNewPathIfValid(new Point(lastPoint.x - 1, lastPoint.y), result);
                addNewPathIfValid(new Point(lastPoint.x, lastPoint.y + 1), result);
            }
            return result;
        }
    }

    public class Garden {
        HashMap<Point, Character> map = new HashMap<Point, Character>();
        int maxX;

        public boolean isValid(Point point) {
            return map.containsKey(point) && map.get(point) != '#';
        }

        public Garden() {

        }

        public ArrayList<Point> getAllNeighbours(Point lastPoint) {
            var result = new ArrayList<Point>();
            addPointIfValid(new Point(lastPoint.x + 1, lastPoint.y), result);
            addPointIfValid(new Point(lastPoint.x - 1, lastPoint.y), result);
            addPointIfValid(new Point(lastPoint.x, lastPoint.y + 1), result);
            addPointIfValid(new Point(lastPoint.x, lastPoint.y - 1), result);

            return result;
        }

        public void addPointIfValid(Point point, ArrayList<Point> points) {
            if (isValid(point)) {
                points.add(point);
            }
        }
    }

    private Garden getInput() {
        var input = getInputLines();
        Garden garden = new Garden();
        for (var i = 0; i <= input.size() - 1; i++) {
            for (var j = 0; j <= input.size() - 1; j++) {
                garden.map.put(new Point(j, i), input.get(i).charAt(j));
            }
        }
        garden.maxX = input.size() - 1;
        return garden;
    }

}
