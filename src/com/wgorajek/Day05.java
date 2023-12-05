package com.wgorajek;

import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.pow;

public class Day05 extends Solution {
    @Override
    public Object getPart1Solution() {
        final Almanac almanac = getInput();
        long lowestValue = Long.MAX_VALUE;
        for (long seed : almanac.seeds) {
            long transformedSeed = seed;
            for (var map : almanac.maps) {
                for (var mapElement : map) {
                    if ((mapElement.input <= transformedSeed) && (mapElement.input + mapElement.range - 1L >= transformedSeed)) {
                        transformedSeed += mapElement.output - mapElement.input;
                        break;
                    }
                }
            }
            lowestValue = Long.min(transformedSeed, lowestValue);
        }
        return lowestValue;

    }

    @Override
    public Object getPart2Solution() {
        final Almanac almanac = getInput();
        long lowestValue = Long.MAX_VALUE;
        LinkedList<SeedRange> seeds = new LinkedList<>();
        for (var i = 0; i <= almanac.seeds.size() - 1; i += 2) {
            seeds.push(new SeedRange(almanac.seeds.get(i), almanac.seeds.get(i) + almanac.seeds.get(i + 1) - 1L));
        }


        for (var map : almanac.maps) {
            LinkedList<SeedRange> newSeeds = new LinkedList<>();
            while (!seeds.isEmpty()) {
                var seed = seeds.pop();
                var matchFound = false;
                for (var mapElement : map) {
                    if ((mapElement.input <= seed.end) && (mapElement.inputEnd >= seed.start)) {
                        matchFound = true;
                        long change = mapElement.output - mapElement.input;
                        long newStart = seed.start;
                        long newEnd = seed.end;
                        if (seed.start < mapElement.input) {
                            seeds.push(new SeedRange(seed.start, mapElement.input-1L));
                            newStart = mapElement.input;
                        }
                        if (seed.end > mapElement.inputEnd) {
                            seeds.push(new SeedRange(mapElement.inputEnd + 1L, seed.end));
                            newEnd = mapElement.inputEnd;
                        }
                        newSeeds.push(new SeedRange(newStart + change, newEnd + change));
                    }
                }
                if (!matchFound) {
                    newSeeds.add(seed);
                }
            }
            for (var seed: newSeeds) {
                seeds.push(seed);
            }
            newSeeds.clear();
        }

        for (var seed: seeds) {
            lowestValue = Long.min(lowestValue, seed.start);
        }
        return lowestValue;

    }

    public static class AMap {
        long output;
        long input;
        long range;
        long inputEnd;
    }

    public static class SeedRange {
        long start;
        long end;

        public SeedRange(long start, long end) {
            this.start = start;
            this.end = end;
        }
    }

    public static class Almanac {
        ArrayList<ArrayList<AMap>> maps;
        ArrayList<Long> seeds;

        public Almanac() {
            maps = new ArrayList<>();
            seeds = new ArrayList<>();
        }
    }

    private Almanac getInput() {
        final List<String> input = getInputLines();
        var almanac = new Almanac();
        ArrayList<AMap> actualMap = new ArrayList<>();
        for (String line : input) {
            if (line.contains("seeds")) {
                Pattern pattern = Pattern.compile("\\d+");
                Matcher matcher = pattern.matcher(line);
                while (matcher.find()) {
                    almanac.seeds.add(Long.parseLong(matcher.group()));
                }
            } else if (!(line.isEmpty())) {
                if (line.contains("-")) {
                    actualMap = new ArrayList<>();
                    almanac.maps.add(actualMap);
                } else {
                    Pattern pattern = Pattern.compile("\\d+");
                    Matcher matcher = pattern.matcher(line);
                    var mapElement = new AMap();
                    actualMap.add(mapElement);

                    if (matcher.find()) {
                        mapElement.output = Long.parseLong(matcher.group());
                    }
                    if (matcher.find()) {
                        mapElement.input = Long.parseLong(matcher.group());
                    }
                    if (matcher.find()) {
                        mapElement.range = Long.parseLong(matcher.group());
                    }
                    mapElement.inputEnd = mapElement.input + mapElement.range - 1L;
                }
            }


        }
        return almanac;
    }
}
