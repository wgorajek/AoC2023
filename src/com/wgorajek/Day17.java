package com.wgorajek;

import java.awt.*;
import java.util.*;

public class Day17 extends Solution {
    @Override
    public Object getPart1Solution() {
        var cave = getInput();
        return cave.findBestPath();
    }

    @Override
    public Object getPart2Solution() {
        var cave = getInput();
        return cave.findBestPathB();
    }

    private class CavePoint {
        Point point;
        Integer riskLevel;
        CavePoint[] neighbours;

        public CavePoint(Point point, Integer riskLevel) {
            this.point = point;
            this.riskLevel = riskLevel;
            neighbours = new CavePoint[4];
        }
    }

    private class Path {
        Integer totalRisk = 0;
        CavePoint lastPoint;
        int lastDirection = -100;
        int directionLength = 1;

        public Path(Integer totalRisk, CavePoint lastPoint) {
            this.totalRisk = totalRisk;
            this.lastPoint = lastPoint;
        }

        public void addNewPoint(CavePoint point) {
            lastPoint = point;
            totalRisk += point.riskLevel;
        }
    }

    private class Cave {
        CavePoint[][] cave;
        CavePoint finishPoint;

        public Cave(Integer[][] caveMap) {
            cave = new CavePoint[caveMap.length][caveMap[0].length];
            for (var i = 0; i < caveMap.length; i++) {
                for (var j = 0; j < caveMap[0].length; j++) {
                    cave[i][j] = new CavePoint(new Point(i, j), caveMap[i][j]);
                }
            }


            int[] vector = {-1, 0, 1};
            for (var i = 0; i < cave.length; i++) {
                for (var j = 0; j < cave[i].length; j++) {

                    if (validPoint(i - 1, j)) {
                        cave[i][j].neighbours[0] = cave[i - 1][j];
                    }
                    if (validPoint(i, j + 1)) {
                        cave[i][j].neighbours[1] = cave[i][j + 1];
                    }
                    if (validPoint(i + 1, j)) {
                        cave[i][j].neighbours[2] = cave[i + 1][j];
                    }
                    if (validPoint(i, j - 1)) {
                        cave[i][j].neighbours[3] = cave[i][j - 1];
                    }
                }
            }
            finishPoint = cave[cave.length - 1][cave[cave.length - 1].length - 1];
        }

        private boolean validPoint(int x, int y) {
            return (x >= 0 && y >= 0 && x < cave.length && y < cave[0].length);
        }

        public class BestPathsToPoint {
            ArrayList<Path> paths;
            Point point;

            public BestPathsToPoint(Point point) {
                paths = new ArrayList<Path>();
                this.point = new Point(point.x, point.y);
            }

            public boolean isPathBetter(Path newPath) {
                var pathsToRemove = new ArrayList<Path>();
                for (var path : paths) {
                    if (newPath.totalRisk > path.totalRisk + 36) {
                        return false;
                    }
                    if (path.totalRisk <= newPath.totalRisk && newPath.directionLength >= path.directionLength && newPath.lastDirection == path.lastDirection) {
                        return false;
                    } else if (path.totalRisk > newPath.totalRisk && newPath.directionLength <= path.directionLength && newPath.lastDirection == path.lastDirection) {
                        pathsToRemove.add(path);
                    }
                }
                paths.add(newPath);
                for (var pathToRemove : pathsToRemove) {
                    paths.remove(paths.indexOf(pathToRemove));
                }
                return true;
            }
            public boolean isPathBetterB(Path newPath) {
                var pathsToRemove = new ArrayList<Path>();
                for (var path : paths) {
                    if (path.totalRisk <= newPath.totalRisk && newPath.directionLength >= path.directionLength && (path.directionLength >= 4 || path.directionLength == newPath.directionLength)
                            && newPath.lastDirection == path.lastDirection) {
                        return false;
                    } else if (path.totalRisk > newPath.totalRisk && newPath.directionLength <= path.directionLength && newPath.lastDirection == path.lastDirection) {
                        pathsToRemove.add(path);
                    }
                }
                paths.add(newPath);
                for (var pathToRemove : pathsToRemove) {
                    paths.remove(paths.indexOf(pathToRemove));
                }
                return true;
            }

        }

        public int findBestPath() {
            var bestPathMap = new HashMap<CavePoint, BestPathsToPoint>();

            Integer bestPathLength = 9999999;
            var pathStack = new Stack<Path>();
            var firstPath = new Path(0, cave[0][0]);
            pathStack.push(firstPath);
            bestPathMap.put(firstPath.lastPoint, new BestPathsToPoint(firstPath.lastPoint.point));
            for (var i = 0; i<= 3; i++) {
                firstPath = new Path(0, cave[0][0]);
                firstPath.lastDirection = i;
                firstPath.directionLength = 0;
                bestPathMap.get(cave[0][0]).isPathBetter(firstPath);
            }

            while (!pathStack.isEmpty()) {
                var path = pathStack.pop();
                for (var i = 0; i <= 3; i++) {
                    var neighbour = path.lastPoint.neighbours[i];
                    if (neighbour == null || ((path.lastDirection + 2) % 4 == i && path.lastDirection != -100)) {
                        continue;
                    }

                    var newPath = new Path(path.totalRisk, path.lastPoint);
                    newPath.lastDirection = i;
                    if (path.lastDirection == newPath.lastDirection) {
                        newPath.directionLength = path.directionLength + 1;
                    }
                    if (newPath.directionLength <= 3) {
                        newPath.addNewPoint(neighbour);
                        if (!bestPathMap.containsKey(neighbour)) {
                            bestPathMap.put(neighbour, new BestPathsToPoint(neighbour.point));
                        }
                        if (bestPathMap.get(neighbour).isPathBetter(newPath)) {
                            var bestToThisPoint = bestPathMap.get(neighbour);
                            if (newPath.lastPoint != finishPoint) {
                                if (newPath.totalRisk < bestPathLength) {
                                    pathStack.push(newPath);
                                }
                            } else {
                                bestPathLength = Integer.min(bestPathLength, newPath.totalRisk);
                            }
                        }
                    }
                }
            }
            return bestPathLength;
        }

        public int findBestPathB() {
            var bestPathMap = new HashMap<CavePoint, BestPathsToPoint>();

            Integer bestPathLength = 9999999;
            var pathStack = new Stack<Path>();
            var firstPath = new Path(0, cave[0][0]);
            firstPath.directionLength = 10;
            pathStack.push(firstPath);
            bestPathMap.put(firstPath.lastPoint, new BestPathsToPoint(firstPath.lastPoint.point));
            for (var i = 0; i <= 3; i++) {
                firstPath = new Path(0, cave[0][0]);
                firstPath.lastDirection = i;
                firstPath.directionLength = 0;
                bestPathMap.get(cave[0][0]).isPathBetterB(firstPath);
            }

            while (!pathStack.isEmpty()) {
                var path = pathStack.pop();
                for (var i = 0; i <= 3; i++) {
                    var neighbour = path.lastPoint.neighbours[i];
                    if (neighbour == null || ((path.lastDirection + 2) % 4 == i && path.lastDirection != -100)) {
                        continue;
                    }

                    var newPath = new Path(path.totalRisk, path.lastPoint);
                    newPath.lastDirection = i;
                    if (path.lastDirection == newPath.lastDirection) {
                        newPath.directionLength = path.directionLength + 1;
                    }
                    if (newPath.directionLength > 10) {
                        continue;
                    }
                    if (path.directionLength < 4 && path.lastDirection != newPath.lastDirection) {
                        continue;
                    }
                    newPath.addNewPoint(neighbour);
                    if (!bestPathMap.containsKey(neighbour)) {
                        bestPathMap.put(neighbour, new BestPathsToPoint(neighbour.point));
                    }
                    if (bestPathMap.get(neighbour).isPathBetterB(newPath)) {
                        if (newPath.lastPoint != finishPoint) {
                            if (newPath.totalRisk < bestPathLength) {
                                pathStack.push(newPath);
                            }
                        } else {
                            if (newPath.directionLength >= 4) {
                                bestPathLength = Integer.min(bestPathLength, newPath.totalRisk);
                            }
                        }
                    }

                }
            }
            return bestPathLength;
        }


    }

    private Cave getInput() {
        var input = getInputLines();
        var caveMap = new Integer[input.size()][input.get(0).length()];
        for (var i = 0; i < input.size(); i++) {
            var line = input.get(i);
            for (var j = 0; j < line.length(); j++) {
                caveMap[i][j] = Integer.parseInt(line.substring(j, j + 1));
            }
        }
        return new Cave(caveMap);
    }

}
