package com.wgorajek;


import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

public abstract class Solution {
    private Path mInputPath;

    public static Solution getSolution(int day) throws Exception {
        final Solution solution = (Solution)Class.forName(String.format("com.wgorajek.Day%02d", day)).getConstructor().newInstance();

        solution.mInputPath = Path.of(String.format("data/day%02d.txt", day));
//        solution.mInputPath = Path.of(String.format("data/day%02dTest.txt", day));

        return solution;
    }

    public abstract Object getPart1Solution();

    public abstract Object getPart2Solution();

    protected List<String> getInputLines() {
        try {
            return Files.readAllLines(mInputPath);
        } catch (IOException e) {
            return null;
        }
    }

    protected String getInputString() {
        try {
            return Files.readString(mInputPath);
        } catch (IOException e) {
            return null;
        }
    }
}
