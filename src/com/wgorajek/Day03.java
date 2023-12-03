package com.wgorajek;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day03 extends Solution {
    @Override
    public Object getPart1Solution() {
        final Engine engine = getInput();
        int sumCalibration = 0;
        for (var part : engine.parts) {
            if (engine.isValidPart(part)) {
                sumCalibration += part.value;
            }
        }
        return sumCalibration;

    }

    @Override
    public Object getPart2Solution() {
        final Engine engine = getInput();
        int sumCalibration = 0;
        for (var cog : engine.cogs) {
            sumCalibration += engine.findCogValue(cog);
        }
//        for (var calibration : calibrations) {
//            sumCalibration += calibration;
//        }
        return sumCalibration;
    }

    public class Part {
        int engineLineNumber;
        int startPosition;
        int endPosition;
        int value;

        public Part(int engineLineNumber, int startPosition, int endPosition, int value) {
            this.engineLineNumber = engineLineNumber;
            this.startPosition = startPosition;
            this.endPosition = endPosition;
            this.value = value;
        }
    }

    public class Engine {
        ArrayList<String> engineList;
        ArrayList<Part> parts;
        ArrayList<Point> cogs;

        public Engine(ArrayList<String> engineList) {
            this.engineList = engineList;

            parts = new ArrayList<Part>();
            for (var i = 0; i <= engineList.size() - 1; i++) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(engineList.get(i));

                while (matcher.find()) {
                    String number = matcher.group();
                    int startPosition = matcher.start();
                    int endPosition = matcher.end() - 1;
                    parts.add(new Part(i, startPosition, endPosition, Integer.parseInt(number)));
                }
            }

            cogs = new ArrayList<Point>();
            for (var i = 0; i <= engineList.size() - 1; i++) {
                Pattern pattern = Pattern.compile("\\*");
                Matcher matcher = pattern.matcher(engineList.get(i));

                while (matcher.find()) {
                    String number = matcher.group();
                    cogs.add(new Point(matcher.start(), i));
                }
            }

        }

        public boolean isValidPart(Part part) {
            int partStart = Integer.max(part.startPosition - 1, 0);
            int partEnd = Integer.min(part.endPosition + 1, engineList.get(0).length() - 1);
            var stringToCheck = "";
            if (part.engineLineNumber > 0) {
                stringToCheck = stringToCheck + engineList.get(part.engineLineNumber - 1).substring(partStart, partEnd + 1);
            }
            stringToCheck = stringToCheck + engineList.get(part.engineLineNumber).substring(partStart, partStart + 1);
            stringToCheck = stringToCheck + engineList.get(part.engineLineNumber).substring(partEnd, partEnd + 1);
            if (part.engineLineNumber < engineList.size() - 1) {
                stringToCheck = stringToCheck + engineList.get(part.engineLineNumber + 1).substring(partStart, partEnd + 1);
            }
            stringToCheck = stringToCheck.replaceAll("[0-9.]+", "");
            return (stringToCheck.length() > 0);
        }

        public boolean partIsCogNeighbour(Part part, Point cog) {
            return ((cog.x >= part.startPosition - 1) && (cog.x <= part.endPosition + 1)
                    && (cog.y >= part.engineLineNumber - 1) && (cog.y <= part.engineLineNumber + 1));
        }

        public int findCogValue(Point cog) {
            int numberOfParts = 0;
            int cogValue = 1;
            for (var part : parts) {
                if (partIsCogNeighbour(part, cog)) {
                    numberOfParts++;
                    cogValue *= part.value;
                }
            }

            if (numberOfParts == 2) {
                return cogValue;
            } else
                return 0;
        }
    }

    private Engine getInput() {
        final List<String> input = getInputLines();
        ArrayList<String> engineLines = new ArrayList<String>();
        for (String line : input) {
            engineLines.add(line);
        }
        var engine = new Engine(engineLines);
        return engine;
    }
}
