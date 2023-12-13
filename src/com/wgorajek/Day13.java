package com.wgorajek;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day13 extends Solution {
    @Override
    public Object getPart1Solution() {
        ArrayList<Mirror> mirrors = getInput();
        int total = 0;
        for (var mirror : mirrors) {
            total += mirror.findReflection(mirror.mirror1);
            total += 100 * mirror.findReflection(mirror.mirror2);
        }
        return total;
    }

    @Override
    public Object getPart2Solution() {
        ArrayList<Mirror> mirrors = getInput();
        int total = 0;
        for (var mirror : mirrors) {
            var previousReflection = mirror.findReflection(mirror.mirror1);
            previousReflection += 100 * mirror.findReflection(mirror.mirror2);
            var recentReflectionFound = false;
            for (var i = 0; i <= mirror.mirror1.size() - 1; i++) {
                if (recentReflectionFound) {
                    break;
                }
                for (var j = 0; j <= mirror.mirror1.get(0).length() - 1; j++) {
                    if (previousReflection == j) {
                        continue;
                    }
                    var tmp = mirror.findReflectionB(mirror.mirror1, i, j, previousReflection);
                    if (tmp != previousReflection && tmp > 0) {
                        total += tmp;
                        recentReflectionFound = true;
                        break;
                    }
                    tmp = 100 * mirror.findReflectionB(mirror.mirror2, j, i, previousReflection / 100);
                    if (tmp != previousReflection && tmp > 0) {
                        total += tmp;
                        recentReflectionFound = true;
                        break;
                    }
                }
            }
        }
        return total;
    }

    public class Mirror {
        ArrayList<String> mirror1;
        ArrayList<String> mirror2;

        public Mirror() {
            mirror1 = new ArrayList<String>();
            mirror2 = new ArrayList<String>();
        }

        public void setMirror2() {
            for (var i = 0; i <= mirror1.get(0).length() - 1; i++) {
                var str = "";
                for (var j = 0; j <= mirror1.size() - 1; j++) {
                    str += mirror1.get(j).substring(i, i + 1);
                }
                mirror2.add(str);
            }
        }

        public String reverse(String str) {
            var result = "";
            for (var i = str.length() - 1; i >= 0; i--) {
                result += str.substring(i, i + 1);
            }
            return result;
        }

        public int findReflection(ArrayList<String> mirror) {
            int result = 0;
            for (var i = 1; i <= mirror.get(0).length() - 1; i++) {
                boolean isMatch = true;
                for (var j = 0; j <= mirror.size() - 1; j++) {
                    if (!isMatch) {
                        break;
                    }
                    var str1 = reverse(mirror.get(j).substring(0, i));
                    var str2 = mirror.get(j).substring(i);
                    if (str1.length() < str2.length()) {
                        str2 = str2.substring(0, str1.length());
                    } else {
                        str1 = str1.substring(0, str2.length());
                    }
                    if (!str1.equals(str2)) {
                        isMatch = false;
                    }
                }
                if (isMatch) {
                    result = i;
                    break;
                }
            }
            return result;
        }

        public void fixSmudge(ArrayList<String> mirror, int x, int y) {
            var tmp = mirror.get(x);
            var tmp2 = "";
            var iii = 0;
            if (y > 0) {
                tmp2 = tmp.substring(0, y);
            }
            if (tmp.charAt(y) == '#') {
                tmp2 += '.';
            } else {
                tmp2 += '#';
            }
            if (y < tmp.length()) {
                tmp2 += tmp.substring(y + 1);
            }
            mirror.remove(x);
            mirror.add(x, tmp2);
        }

        public int findReflectionB(ArrayList<String> mirror, int x, int y, int previousReflection) {
            int result = 0;
            fixSmudge(mirror, x, y);
            for (var i = 1; i <= mirror.get(0).length() - 1; i++) {
                if (i == previousReflection) {
                    continue;
                }
                boolean isMatch = true;
                for (var j = 0; j <= mirror.size() - 1; j++) {
                    if (!isMatch) {
                        break;
                    }
                    var str1 = reverse(mirror.get(j).substring(0, i));
                    var str2 = mirror.get(j).substring(i);
                    if (str1.length() < str2.length()) {
                        str2 = str2.substring(0, str1.length());
                    } else {
                        str1 = str1.substring(0, str2.length());
                    }
                    if (!str1.equals(str2)) {
                        isMatch = false;
                    }
                }
                if (isMatch) {
                    result = i;
                    break;
                }
            }
            fixSmudge(mirror, x, y);
            return result;
        }
    }

    private ArrayList<Mirror> getInput() {
        final List<String> input = getInputLines();
        ArrayList<Mirror> mirrors = new ArrayList<Mirror>();
        Mirror mirror = new Mirror();
        mirrors.add(mirror);
        for (var line : input) {
            if (line.isEmpty()) {
                mirror.setMirror2();
                mirror = new Mirror();
                mirrors.add(mirror);
            } else {
                mirror.mirror1.add(line);
            }
        }
        mirror.setMirror2();
        return mirrors;
    }

}
