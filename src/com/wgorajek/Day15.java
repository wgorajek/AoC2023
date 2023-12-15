package com.wgorajek;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day15 extends Solution {
    @Override
    public Object getPart1Solution() {
        ArrayList<String> steps = getInput();
        int total = 0;
        for (var step : steps) {
            total += getHashValue(step);
        }
        return total;
    }

    @Override
    public Object getPart2Solution() {
        ArrayList<String> steps = getInput();
        var hashMap = new HashMap<Integer, ArrayList<Lens>>();
        for (var i = 0; i <= 255; i++) {
            hashMap.put(i, new ArrayList<Lens>());
        }

        for (var step : steps) {
            var lens = new Lens(step);
            var hashValue = getHashValue(lens.name);
            if (lens.sign == '-') {
                var hashArray = hashMap.get(hashValue);
                for (var i = 0; i <= hashArray.size() - 1; i++) {
                    if (lens.name.equals(hashArray.get(i).name)) {
                        hashArray.remove(i);
                    }
                }
            } else {
                var hashArray = hashMap.get(hashValue);
                var replaced = false;
                for (var i = 0; i <= hashArray.size() - 1; i++) {
                    if (lens.name.equals(hashArray.get(i).name)) {
                        hashArray.get(i).focalLength = lens.focalLength;
                        replaced = true;
                        break;
                    }
                }
                if (!replaced) {
                    hashArray.add(lens);
                }
            }
        }
        int total = 0;
        for (var i = 0; i <= 255; i++) {
            var hashArray = hashMap.get(i);
            for (var j = 0; j <= hashArray.size() - 1; j++) {
                total += (i + 1) * (j + 1) * hashArray.get(j).focalLength;
            }
        }
        return total;
    }

    public int getHashValue(String string) {
        var result = 0;
        for (var character : string.toCharArray()) {
            result += (int) character;
            result *= 17;
            result = result % 256;
        }

        return result;
    }

    public class Lens {
        int focalLength = 0;
        String name;
        char sign;

        public Lens(String string) {
            if (string.contains("-")) {
                sign = '-';
                name = string.substring(0, string.length() - 1);
            } else {
                sign = '=';
                name = string.substring(0, string.length() - 2);
                focalLength = Integer.parseInt(string.substring(string.length() - 1));
            }
            var i = 0;
        }
    }

    private ArrayList<String> getInput() {
        final List<String> input = getInputLines();
        ArrayList<String> steps = new ArrayList<String>();
        for (var step : getInputLines().get(0).split(",")) {
            steps.add(step);
        }
        return steps;
    }

}
