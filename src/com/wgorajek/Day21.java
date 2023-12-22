package com.wgorajek;

import java.awt.*;
import java.beans.PropertyEditorSupport;
import java.sql.Array;
import java.util.*;
import java.util.function.Predicate;

public class Day21 extends Solution {
    @Override
    public Object getPart1Solution() {
        var garden = getInput();
        return garden.getValueAfterSteps(garden.start, 64);
    }


    @Override
    public Object getPart2Solution() {
        var garden = getInput();

        long total = 0l;
        long evenFilledGarden = garden.getValueAfterSteps(garden.start, 260);
        long unEvenFilledGarden = garden.getValueAfterSteps(garden.start, 261);

        total += unEvenFilledGarden;
        total += garden.getValueAfterSteps(new Point(0, 65), 130);
        total += garden.getValueAfterSteps(new Point(130, 65), 130);
        total += garden.getValueAfterSteps(new Point(65, 0), 130);
        total += garden.getValueAfterSteps(new Point(65, 130), 130);

        long n = (26501365l - 65l) / 131l;
        long corners64 = 0l;
        corners64 += garden.getValueAfterSteps(new Point(0, 0), 64);
        corners64 += garden.getValueAfterSteps(new Point(0, 130), 64);
        corners64 += garden.getValueAfterSteps(new Point(130, 0), 64);
        corners64 += garden.getValueAfterSteps(new Point(130, 130), 64);
        total += corners64 * n;

        long corners195 = 0l;
        corners195 += garden.getValueAfterSteps(new Point(0, 0), 195);
        corners195 += garden.getValueAfterSteps(new Point(0, 130), 195);
        corners195 += garden.getValueAfterSteps(new Point(130, 0), 195);
        corners195 += garden.getValueAfterSteps(new Point(130, 130), 195);
        total += corners195 * (n - 1l);

        total += n*n * evenFilledGarden;
        total += (n*(n-2)) * unEvenFilledGarden;

        return total;
    }

    public class Garden {
        HashMap<Point, Character> map = new HashMap<Point, Character>();
        int maxX;
        Point start;

        public void addIfValid(Point point, HashSet<Point> set) {
            if (isValid(point)) {
                set.add(point);
            }
        }

        public boolean isValid(Point point) {
            return point.x >= 0 && point.x <= maxX && point.y >= 0 && point.y <= maxX && map.get(point) == '.';
        }

        public Garden() {

        }

        public int getValueAfterSteps(Point start, int steps) {
            HashSet<Point> visitedPoints = new HashSet<Point>();
            HashSet<Point> newVisitedPoints = new HashSet<Point>();
            visitedPoints.add(start);
            for (var i = 1; i <= steps; i++) {
                for (var point : visitedPoints) {
                    addIfValid(new Point(point.x - 1, point.y), newVisitedPoints);
                    addIfValid(new Point(point.x + 1, point.y), newVisitedPoints);
                    addIfValid(new Point(point.x, point.y - 1), newVisitedPoints);
                    addIfValid(new Point(point.x, point.y + 1), newVisitedPoints);

                }
                visitedPoints.clear();
                visitedPoints.addAll(newVisitedPoints);
                newVisitedPoints.clear();
            }
            return visitedPoints.size();
        }
    }

    private Garden getInput() {
        var input = getInputLines();
        Garden garden = new Garden();
        for (var i = 0; i <= input.size() - 1; i++) {
            for (var j = 0; j <= input.size() - 1; j++) {
                garden.map.put(new Point(j, i), input.get(i).charAt(j));
                if (input.get(i).charAt(j) == 'S') {
                    garden.start = new Point(j, i);
                    garden.map.put(new Point(j, i), '.');
                }
            }
        }
        garden.maxX = input.size() - 1;
        return garden;
    }

}
