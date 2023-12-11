package com.wgorajek;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day11 extends Solution {
    @Override
    public Object getPart1Solution() {
        Universe universe = getInput();
        long total = 0;
        int galaxies = 0;
        for (var i = 0; i <= universe.galaxies.size() - 2; i++) {
            var galaxy1 = universe.galaxies.get(i);
            for (var j = i + 1; j <= universe.galaxies.size() - 1; j++) {
                galaxies++;
                var galaxy2 = universe.galaxies.get(j);
                total += universe.calculateDistance(galaxy1, galaxy2, 2l);
            }
        }
        return total;
    }

    @Override
    public Object getPart2Solution() {
        Universe universe = getInput();
        long total = 0;
        int galaxies = 0;
        for (var i = 0; i <= universe.galaxies.size() - 2; i++) {
            var galaxy1 = universe.galaxies.get(i);
            for (var j = i + 1; j <= universe.galaxies.size() - 1; j++) {
                galaxies++;
                var galaxy2 = universe.galaxies.get(j);
                total += universe.calculateDistance(galaxy1, galaxy2, 1000000l);
            }
        }
        return total;
    }


    public class Universe {
        ArrayList<String> map;
        ArrayList<Integer> emptyColumns;
        ArrayList<Integer> emptyLines;
        ArrayList<Point> galaxies;

        public Universe(ArrayList<String> map) {
            this.map = map;
            galaxies = new ArrayList<Point>();
            emptyLines = new ArrayList<Integer>();
            emptyColumns = new ArrayList<Integer>();

            for (var i = 0; i <= map.size() - 1; i++) {
                if (!map.get(i).contains("#")) {
                    emptyLines.add(i);
                }
                var isEmpty = true;
                for (var j = 0; j <= map.size() - 1; j++) {
                    var tmp = map.get(j).substring(i, i + 1);
                    if (tmp.equals("#")) {
                        isEmpty = false;
                        break;
                    }
                }
                if (isEmpty) {
                    emptyColumns.add(i);
                }
            }

            for (var x = 0; x <= map.size() - 1; x++) {
                for (var y = 0; y <= map.size() - 1; y++) {
                    var tmp = map.get(y).substring(x, x + 1);
                    if (tmp.equals("#")) {
                        galaxies.add(new Point(x, y));
                    }
                }
            }
        }

        public long calculateDistance(Point point1, Point point2, long multiplier) {
            multiplier -= 1;
            if (point1.x == 3 && point1.y == 0 && point2.x == 7 && point2.y == 8) {
                var tmp = '3';
            }
            var distance = Math.abs(point1.x - point2.x) + Math.abs(point1.y - point2.y);
            for (var x = Math.min(point1.x, point2.x) + 1; x < Math.max(point1.x, point2.x); x++) {
                if (emptyColumns.contains(x)) {
                    distance+= multiplier;
                }
            }
            for (var y = Math.min(point1.y, point2.y) + 1; y < Math.max(point1.y, point2.y); y++) {
                if (emptyLines.contains(y)) {
                    distance+= multiplier;
                }
            }
            return distance;
        }
    }

    private Universe getInput() {
        final List<String> input = getInputLines();
        ArrayList<String> map = new ArrayList<String>();
        for (var line : input) {
            map.add(line);
        }
        return new Universe(map);
    }

}
