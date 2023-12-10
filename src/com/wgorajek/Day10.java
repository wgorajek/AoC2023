package com.wgorajek;

import java.awt.*;
import java.nio.channels.Pipe;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day10 extends Solution {
    @Override
    public Object getPart1Solution() {
        final Maze maze = getInput();
        return maze.calculatePart1();
    }

    @Override
    public Object getPart2Solution() {
        final Maze maze = getInput();
        int total = 0;
        int x = maze.mazeStart.x;
        int y = maze.mazeStart.y;
        PipePoint startPipePoint = maze.pipeMap.get(maze.mazeStart);

        maze.calculatePart1(); // to set isPartOfFence
        var pointsToRemove = new ArrayList<>();
        for (var pipePoint : maze.pipeMap.values()) {
            if (!pipePoint.isPartOfFence) {
                pointsToRemove.add(pipePoint.location);
            }
        }

        for (var point : pointsToRemove) {
            maze.pipeMap.remove(point);
        }

        if (y > 0) {
            var actualPoint = new Point(x, y - 1);
            var pipePoint = maze.pipeMap.get(actualPoint);
            if (pipePoint != null) {
                startPipePoint.isNorthConnected = pipePoint.isSouthConnected;
            }
        }
        if (y < maze.map.size() - 1) {
            var actualPoint = new Point(x, y + 1);
            var pipePoint = maze.pipeMap.get(actualPoint);
            if (pipePoint != null) {
                startPipePoint.isSouthConnected = pipePoint.isNorthConnected;
            }
        }
        if (x > 0) {
            var actualPoint = new Point(x - 1, y);
            var pipePoint = maze.pipeMap.get(actualPoint);
            if (pipePoint != null) {
                startPipePoint.isWestConnected = pipePoint.isEastConnected;
            }
        }
        if (x < maze.map.get(0).length() - 1) {
            var actualPoint = new Point(x + 1, y);
            var pipePoint = maze.pipeMap.get(actualPoint);
            if (pipePoint != null) {
                startPipePoint.isEastConnected = pipePoint.isWestConnected;
            }
        }
        if (startPipePoint.isNorthConnected && startPipePoint.isSouthConnected) {
            startPipePoint.connectionType = "|";
        }
        if (startPipePoint.isEastConnected && startPipePoint.isWestConnected) {
            startPipePoint.connectionType = "-";
        }
        if (startPipePoint.isNorthConnected && startPipePoint.isEastConnected) {
            startPipePoint.connectionType = "L";
        }
        if (startPipePoint.isNorthConnected && startPipePoint.isWestConnected) {
            startPipePoint.connectionType = "J";
        }
        if (startPipePoint.isSouthConnected && startPipePoint.isWestConnected) {
            startPipePoint.connectionType = "7";
        }
        if (startPipePoint.isSouthConnected && startPipePoint.isEastConnected) {
            startPipePoint.connectionType = "F";
        }

        for (y = 0; y <= maze.map.size() - 1; y++) {
            var isInside = false;
            var onFenceNorth = false;
            var onFenceSouth = false;

            for (x = 0; x <= maze.map.get(y).length() - 1; x++) {
                var actualPoint = new Point(x, y);
                if (!maze.pipeMap.containsKey(actualPoint)) {
                    if (isInside) {
                        total++;
                    }
                } else {
                    var pipePoint = maze.pipeMap.get(actualPoint);
                    if (pipePoint.connectionType.equals("|")) {
                        isInside = !isInside;
                    } else if (pipePoint.connectionType.equals("L")) {
                        onFenceNorth = true;
                    } else if (pipePoint.connectionType.equals("F")) {
                        onFenceSouth = true;
                    } else if (pipePoint.connectionType.equals("J")) {
                        if (onFenceSouth) {
                            isInside = !isInside;
                        }
                        onFenceNorth = false;
                        onFenceSouth = false;
                    } else if (pipePoint.connectionType.equals("7")) {
                        if (onFenceNorth) {
                            isInside = !isInside;
                        }
                        onFenceNorth = false;
                        onFenceSouth = false;
                    }
                }

            }
        }

        return total;
    }


    public class PipePoint {
        Point location;
        String connectionType;
        boolean isSouthConnected;
        boolean isNorthConnected;
        boolean isEastConnected;
        boolean isWestConnected;
        boolean isPartOfFence = false;

        public PipePoint(Point location, String connectionType) {
            this.location = location;
            this.connectionType = connectionType;
            fillAllConnections();
        }

        void fillAllConnections() {
            isSouthConnected = connectionType.equals("|") || connectionType.equals("7") || connectionType.equals("F");
            isNorthConnected = connectionType.equals("|") || connectionType.equals("L") || connectionType.equals("J");
            isEastConnected = connectionType.equals("-") || connectionType.equals("L") || connectionType.equals("F");
            isWestConnected = connectionType.equals("-") || connectionType.equals("7") || connectionType.equals("J");
        }

    }

    public class Maze {
        ArrayList<String> map;
        HashMap<Point, PipePoint> pipeMap;
        Point mazeStart;

        public Maze(ArrayList<String> map) {
            this.map = map;
            pipeMap = new HashMap<Point, PipePoint>();

            for (var y = 0; y <= map.size() - 1; y++) {
                for (var x = 0; x <= map.get(y).length() - 1; x++) {
                    var connectionType = String.valueOf(map.get(y).charAt(x));
                    if (!connectionType.equals("S")) {
                        var point = new Point(x, y);
                        if (!connectionType.equals(".")) {
                            var pipePoint = new PipePoint(point, connectionType);
                            pipeMap.put(point, pipePoint);
                        }
                    }
                }
            }
        }

        public int calculatePart1() {
            int length = 0;
            int x = mazeStart.x;
            int y = mazeStart.y;
            var entry = "";
            PipePoint firstPoint = null;
            boolean firstPointFound = false;
            if (y > 0) {
                var actualPoint = new Point(x, y - 1);
                var pipePoint = pipeMap.get(actualPoint);
                if (pipePoint != null && pipePoint.isSouthConnected) {
                    firstPointFound = true;
                    firstPoint = pipePoint;
                    entry = "south";
                }
            }
            if (!firstPointFound && (y < map.size() - 1)) {
                var actualPoint = new Point(x, y + 1);
                var pipePoint = pipeMap.get(actualPoint);
                if (pipePoint != null && pipePoint.isNorthConnected) {
                    firstPointFound = true;
                    firstPoint = pipePoint;
                    entry = "north";
                }
            }
            if (!firstPointFound && (x > 0)) {
                var actualPoint = new Point(x - 1, y);
                var pipePoint = pipeMap.get(actualPoint);
                if (pipePoint != null && pipePoint.isEastConnected) {
                    firstPoint = pipePoint;
                    entry = "east";
                }
            }
            length++;
            var actualPoint = firstPoint;
            actualPoint.isPartOfFence = true;
            PipePoint exitPoint = null;
            while (!actualPoint.connectionType.equals("S")) {
                if (actualPoint.isNorthConnected && !entry.equals("north")) {
                    exitPoint = pipeMap.get(new Point(actualPoint.location.x, actualPoint.location.y - 1));
                    entry = "south";
                } else if (actualPoint.isSouthConnected && !entry.equals("south")) {
                    exitPoint = pipeMap.get(new Point(actualPoint.location.x, actualPoint.location.y + 1));
                    entry = "north";
                } else if (actualPoint.isEastConnected && !entry.equals("east")) {
                    exitPoint = pipeMap.get(new Point(actualPoint.location.x + 1, actualPoint.location.y));
                    entry = "west";
                } else if (actualPoint.isWestConnected && !entry.equals("west")) {
                    exitPoint = pipeMap.get(new Point(actualPoint.location.x - 1, actualPoint.location.y));
                    entry = "east";
                }
                length++;
                actualPoint = exitPoint;
                actualPoint.isPartOfFence = true;
            }

            return length / 2;
        }
    }

    private Maze getInput() {
        final List<String> input = getInputLines();
        ArrayList<String> map = new ArrayList<String>();
        Point mazeStart = null;
        for (var i = 0; i <= input.size() - 1; i++) {
            map.add(input.get(i));
            if (input.get(i).contains("S")) {
                mazeStart = new Point(input.get(i).indexOf("S"), i);
            }
        }
        var maze = new Maze(map);
        maze.mazeStart = mazeStart;

        var startPoint = new PipePoint(mazeStart, "S");
        startPoint.isPartOfFence = true;
        maze.pipeMap.put(mazeStart, startPoint);
        return maze;
    }
}
