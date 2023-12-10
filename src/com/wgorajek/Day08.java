package com.wgorajek;

import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day08 extends Solution {
    @Override
    public Object getPart1Solution() {
        Wasteland wasteland = getInput();
        int total = 0;
        var networkNumber = 0;
        Node node = wasteland.map.get("AAA");
        while ((networkNumber != 0) || (!node.name.equals("ZZZ"))) {
            if (String.valueOf(wasteland.network.charAt(networkNumber)).equals("L")) {
                node = wasteland.map.get(node.left);
            } else {
                node = wasteland.map.get(node.right);
            }

            networkNumber = (networkNumber + 1) % wasteland.network.length();
            total++;
        }

        return total;
    }

    @Override
    public Object getPart2Solution() {
        Wasteland wasteland = getInput();
        long total = 0l;
        var networkNumber = 0;

        ArrayList<Node> newGhosts = new ArrayList<Node>();
        HashMap<Integer, Long> ghostPeriod = new HashMap<Integer, Long>();

        ArrayList<Node> ghosts = new ArrayList<Node>();
        for (var node : wasteland.map.values()) {
            if (node.name.endsWith("A")) {
                ghosts.add(node);
            }
        }


        var stop = false;
        while (!stop) {
            stop = true;
            for (var i = 0; i <= ghosts.size() - 1; i++) {

                if (String.valueOf(wasteland.network.charAt(networkNumber)).equals("L")) {
                    newGhosts.add(wasteland.map.get(ghosts.get(i).left));
                } else {
                    newGhosts.add(wasteland.map.get(ghosts.get(i).right));
                }
            }
            ghosts.clear();
            for (var ghost : newGhosts) {
                ghosts.add(ghost);
            }
            newGhosts.clear();

            networkNumber = (networkNumber + 1) % wasteland.network.length();
            total++;
            for (var i = 0; i <= ghosts.size() - 1; i++) {
                if (ghosts.get(i).name.endsWith("Z")) {
                    if (!ghostPeriod.containsKey(i)) {
                        ghostPeriod.put(i, total);
                    }
                }
            }
            stop = (ghostPeriod.size() == ghosts.size());
        }
        total = 1l;
        for (var ghostResult : ghostPeriod.values()) {
            total = calculateLCM(total, ghostResult);
        }
        return total;
    }

    public class Node {
        String name;
        String left;
        String right;

        public Node(String name, String left, String right) {
            this.name = name;
            this.left = left;
            this.right = right;
        }
    }

    private static long calculateGCD(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    private static long calculateLCM(long a, long b) {
        return Math.abs(a * b) / calculateGCD(a, b);
    }

    public class Wasteland {
        String network;
        HashMap<String, Node> map;

        public Wasteland(String network) {
            this.network = network;
            map = new HashMap<String, Node>();
        }
    }

    private Wasteland getInput() {
        final List<String> input = getInputLines();
        var wasteland = new Wasteland(input.get(0));
        for (var i = 2; i <= input.size() - 1; i++) {
            Pattern pattern = Pattern.compile("([A-Z]{3}).*([A-Z]{3}).*([A-Z]{3})");
            Matcher matcher = pattern.matcher(input.get(i));
            if (matcher.find()) {
                wasteland.map.put(matcher.group(1), new Node(matcher.group(1), matcher.group(2), matcher.group(3)));
            }
        }

        return wasteland;
    }
}
