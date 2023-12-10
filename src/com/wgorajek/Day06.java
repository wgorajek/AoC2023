package com.wgorajek;

import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day06 extends Solution {
    @Override
    public Object getPart1Solution() {
        ArrayList<Race> races = getInput();
        int total = 1;
        for (var race : races) {
            total *= race.waysToWin();
        }
        return total;
    }

    @Override
    public Object getPart2Solution() {
        Race races = getInputPartB();
        return races.waysToWin();
    }

    public class Race {
        long time;
        long distance;

        public Race(long time, long distance) {
            this.time = time;
            this.distance = distance;
        }

        public long waysToWin() {
            long result = 0;
            for (var i = 1; i <= time - 1; i++) {
                if ((time - i) * i > distance) {
                    result++;
                }
            }
            return result;
        }
    }


    private ArrayList<Race> getInput() {
        final List<String> input = getInputLines();
        ArrayList<Race> races = new ArrayList<Race>();
        var firstLine = input.get(0);
        var secondLine = input.get(1);
        firstLine = firstLine.substring(firstLine.indexOf(":") + 1).trim().replaceAll("[ ]+", " ");
        secondLine = secondLine.substring(secondLine.indexOf(":") + 1).trim().replaceAll("[ ]+", " ");
        var times = firstLine.split(" ");
        var distances = secondLine.split(" ");
        for (var i = 0; i <= times.length - 1; i++) {
            races.add(new Race(Integer.parseInt(times[i]), Integer.parseInt(distances[i])));
        }
        return races;
    }

    private Race getInputPartB() {
        final List<String> input = getInputLines();
        var firstLine = input.get(0);
        var secondLine = input.get(1);
        firstLine = firstLine.substring(firstLine.indexOf(":") + 1).trim().replaceAll("[ ]+", "");
        secondLine = secondLine.substring(secondLine.indexOf(":") + 1).trim().replaceAll("[ ]+", "");
        return new Race(Long.parseLong(firstLine), Long.parseLong(secondLine));
    }
}
