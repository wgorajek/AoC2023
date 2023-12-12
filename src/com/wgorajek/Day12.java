package com.wgorajek;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day12 extends Solution {
    @Override
    public Object getPart1Solution() {
        HashMap<String , Long> results = new HashMap<String, Long>();
        ArrayList<Spring> springsList = getInput();
        int total = 0;
        var springs = new LinkedList<Spring>();
        for (var spring : springsList) {
            springs.push(spring);
        }
        while (!springs.isEmpty()) {
            var spring = springs.pop();
            total += getNumberOfCombinations(spring, results);
        }

        return total;
    }

    @Override
    public Object getPart2Solution() {
        HashMap<String , Long> results = new HashMap<String, Long>();
        ArrayList<Spring> springsList = getInput();
        long total = 0l;
        var springs = new LinkedList<Spring>();
        for (var spring : springsList) {
            springs.push(spring);
            spring.unfoldPartB();
        }
        while (!springs.isEmpty()) {
            var spring = springs.pop();
            total += getNumberOfCombinations(spring, results);
        }

        return total;
    }


    public long getNumberOfCombinations(Spring givenSpring, HashMap<String , Long> results) {
        var spring = new Spring(givenSpring.row, givenSpring.damages);
        var str = spring.toString();
        if ( results.containsKey(str)) {
            return results.get(str);
        }
        spring.cutUntilUnknown();
        if (spring.isValid) {
            if (spring.damages.size() == 0) {
                results.put(str, 1l);
                return 1l;
            } else {
                long result = getNumberOfCombinations(new Spring("." + spring.row.substring(1), spring.damages), results) +
                        getNumberOfCombinations(new Spring("#" + spring.row.substring(1), spring.damages), results);
                results.put(str, result);
                return result;
            }
        } else {
            return 0l;
        }
    }

    public class Spring {
        String row;
        LinkedList<Integer> damages;
        boolean isValid = true;

        public Spring(String row, LinkedList<Integer> damages) {
            this.row = row;
            this.damages = new LinkedList<Integer>();
            for (var damage : damages) {
                this.damages.add(damage);
            }
        }

        @Override
        public String toString() {
            var result = row;
            for (var damage : damages) {
                result += damage.toString() + ",";
            }
            return result;
        }

        public void cutUntilUnknown() {
            while (!(!isValid || row.length() == 0 || row.charAt(0) == '?' || damages.size() == 0)) {
                if (row.charAt(0) == '.') {
                    row = row.substring(1);
                } else {
                    var damage = damages.pop();
                    if ((row.length() < damage) || row.substring(0, damage).contains(".") || (row.length() > damage && row.charAt(damage) == '#')) {
                        isValid = false;
                    } else {
                        row = row.substring(Math.min(damage + 1, row.length()));
                    }
                }
            }
            if (row.length() == 0 && damages.size() > 0) {
                isValid = false;
            }
            if (damages.size() == 0 && row.contains("#")) {
                isValid = false;
            }
        }

        public void unfoldPartB() {
            var oldRow = row;
            var damagesSize = damages.size();
            for (var i = 1; i <= 4; i++) {
                row += "?" + oldRow;
                for (var j = 0; j <= damagesSize - 1; j++) {
                    damages.add(damages.get(j));
                }
            }
        }

    }

    private ArrayList<Spring> getInput() {
        final List<String> input = getInputLines();
        ArrayList<Spring> springs = new ArrayList<Spring>();
        for (var line : input) {
            var spacePosition = line.indexOf(" ");
            var row = line.substring(0, spacePosition);
            var damagesStr = line.substring(spacePosition + 1);
            var damages = new LinkedList<Integer>();
            for (var damage : damagesStr.split(",")) {
                damages.add(Integer.parseInt(damage));
            }
            springs.add(new Spring(row, damages));
        }
        return springs;
    }

}
