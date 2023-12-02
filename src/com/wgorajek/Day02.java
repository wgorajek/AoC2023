package com.wgorajek;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


public class Day02 extends Solution {
    @Override
    public Object getPart1Solution() {
        final List<Game> games = getInput();
        int totalScore = 0;
        for (var game : games) {
            var isPossible = true;
            for (var cubeSet : game.cubesSets) {
                if (isPossible && (cubeSet.blue > 14 || cubeSet.green > 13 || cubeSet.red > 12)) {
                    isPossible = false;
                }
            }
            if (isPossible) {
                totalScore += game.gameNumber;
            }
        }
        return totalScore;
    }

    @Override
    public Object getPart2Solution() {
        final List<Game> games = getInput();
        int totalScore = 0;
        for (var game : games) {
            var maxBlue = 0;
            var maxGreen = 0;
            var maxRed = 0;
            for (var cubeSet : game.cubesSets) {
                maxBlue = Integer.max(maxBlue, cubeSet.blue);
                maxGreen = Integer.max(maxGreen, cubeSet.green);
                maxRed = Integer.max(maxRed, cubeSet.red);
            }
            totalScore += maxBlue * maxGreen * maxRed;
        }
        return totalScore;
    }


    private static class CubesSet {
        int blue;
        int red;
        int green;
    }

    private static class Game {
        List<CubesSet> cubesSets;
        int gameNumber;

        public Game(int gameNumber) {
            this.gameNumber = gameNumber;
            cubesSets = new ArrayList<CubesSet>();
        }
    }

    private List<Game> getInput() {
        final List<String> input = getInputLines();
        final List<Game> games = new ArrayList<Game>();
        for (String line : input) {
            Pattern pattern = Pattern.compile("Game (\\d+):");
            Matcher matcher = pattern.matcher(line);
            matcher.find();
            var gameNumber = Integer.parseInt(matcher.group(1));
            var game = new Game(gameNumber);
            games.add(game);

            line = line.substring(line.indexOf(":") + 1);

            var splitResult = line.split(";");
            for (var i : splitResult) {
                var cubeSet = new CubesSet();
                var finalSplitResult = i.split(",");
                for (var j : finalSplitResult) {
                    pattern = Pattern.compile("(\\d+)\\s+([a-zA-Z]+)");
                    matcher = pattern.matcher(j);
                    matcher.find();
                    var color = matcher.group(2).trim();
                    if (color.equals("blue")) {
                        cubeSet.blue = Integer.parseInt(matcher.group(1).trim());
                    } else if (color.equals("red")) {
                        cubeSet.red = Integer.parseInt(matcher.group(1).trim());
                    } else if (color.equals("green")) {
                        cubeSet.green = Integer.parseInt(matcher.group(1).trim());
                    }
                }
                game.cubesSets.add(cubeSet);
            }
        }
        return games;
    }
}
