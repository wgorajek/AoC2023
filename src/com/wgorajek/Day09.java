package com.wgorajek;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day09 extends Solution {
    @Override
    public Object getPart1Solution() {
        ArrayList<ArrayList<Long>> valuesArray = getInput();
        long total = 0l;
        for (var values : valuesArray) {
            var newValues = new ArrayList<Long>();
            var stop = false;
            while (!stop) {
                total += values.get(values.size() - 1);
                newValues.clear();
                for (var i = 0; i <= values.size() - 2; i++) {
                    newValues.add(values.get(i + 1) - values.get(i));
                }
                stop = true;
                values.clear();
                for (var value : newValues) {
                    if (value != 0l) {
                        stop = false;
                    }
                    values.add(value);
                }
            }
        }
        return total;
    }

    @Override
    public Object getPart2Solution() {
        ArrayList<ArrayList<Long>> valuesArray = getInput();
        long total = 0l;
        for (var values : valuesArray) {
            var newValues = new ArrayList<Long>();
            var stop = false;
            var step = 0;
            while (!stop) {
                if (step % 2 == 0) {
                    total += values.get(0);
                } else {
                    total -= values.get(0);
                }
                step++;

                newValues.clear();
                for (var i = 0; i <= values.size() - 2; i++) {
                    newValues.add(values.get(i + 1) - values.get(i));
                }
                stop = true;
                values.clear();
                for (var value : newValues) {
                    if (value != 0l) {
                        stop = false;
                    }
                    values.add(value);
                }
            }
        }
        return total;
    }

    private ArrayList<ArrayList<Long>> getInput() {
        final List<String> input = getInputLines();
        ArrayList<ArrayList<Long>> valuesArray = new ArrayList<>();
        for (String line : input) {
            var values = new ArrayList<Long>();
            valuesArray.add(values);
            Pattern pattern = Pattern.compile("-?\\d+");
            Matcher matcher = pattern.matcher(line);
            while (matcher.find()) {
                values.add(Long.parseLong(matcher.group()));
            }
        }

        return valuesArray;
    }
}
