package com.wgorajek;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class Day01 extends Solution {
    @Override
    public Object getPart1Solution() {
        final List<Integer> calibrations = getInput();
        int sumCalibration = 0;
        for (var calibration : calibrations) {
            sumCalibration += calibration;
        }
        return sumCalibration;
    }

    @Override
    public Object getPart2Solution() {
        final List<Integer> calibrations = getInput2();
        int sumCalibration = 0;
        for (var calibration : calibrations) {
            sumCalibration += calibration;
        }
        return sumCalibration;
    }


    private List<Integer> getInput() {
        final List<String> input = getInputLines();
        final List<Integer> calibrations = new ArrayList<Integer>();

        List<Integer> items = new ArrayList<Integer>();
        for (String i : input) {
            var digitsOnly = i.replaceAll("[^0-9]", "");
            ;
            calibrations.add((10 * Character.getNumericValue(digitsOnly.charAt(0)) + Character.getNumericValue(digitsOnly.charAt(digitsOnly.length() - 1))));
        }
        return calibrations;
    }

    private List<Integer> getInput2() {
        final List<String> input = getInputLines();
        final List<Integer> calibrations = new ArrayList<Integer>();

        List<Integer> items = new ArrayList<Integer>();
        for (String i : input) {

            var digitsOnly = i.replaceAll("one", "o1e");
            digitsOnly = digitsOnly.replaceAll("two", "t2o");
            digitsOnly = digitsOnly.replaceAll("three", "t3re");
            digitsOnly = digitsOnly.replaceAll("four", "f4r");
            digitsOnly = digitsOnly.replaceAll("five", "f5e");
            digitsOnly = digitsOnly.replaceAll("six", "s6x");
            digitsOnly = digitsOnly.replaceAll("seven", "s7n");
            digitsOnly = digitsOnly.replaceAll("eight", "e8t");
            digitsOnly = digitsOnly.replaceAll("nine", "n9e");
            digitsOnly = digitsOnly.replaceAll("[^0-9]", "");

            calibrations.add((10 * Character.getNumericValue(digitsOnly.charAt(0)) + Character.getNumericValue(digitsOnly.charAt(digitsOnly.length() - 1))));
        }

        return calibrations;
    }
}
