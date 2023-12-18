package com.wgorajek;

import java.awt.*;
import java.util.*;

public class Day18 extends Solution {
    @Override
    public Object getPart1Solution() {
        var digPlans = getInput();
        int total = 0;
        HashSet<Point> map = new HashSet<Point>();
        var point = new Point(0, 0);
        map.add(point);
        int maxX = 0;
        int maxY = 0;
        int minX = 0;
        int minY = 0;
        for (var digPlan : digPlans) {
            var vX = 0;
            var vY = 0;
            if (digPlan.direction.equals("U")) {
                vY = 1;
            } else if (digPlan.direction.equals("R")) {
                vX = 1;
            } else if (digPlan.direction.equals("D")) {
                vY = -1;
            } else if (digPlan.direction.equals("L")) {
                vX = -1;
            }
            for (var i = 1; i <= digPlan.length; i++) {
                point = new Point(point.x + vX, point.y + vY);
                map.add(point);
                maxX = Math.max(maxX, point.x);
                minX = Math.min(minX, point.x);
                maxY = Math.max(maxY, point.y);
                minY = Math.min(minY, point.y);
            }
        }

        for (var x = minX; x <= maxX; x++) {
            var isOutside = true;
            var isRightBorder = false;
            var isLeftBorder = false;
            var isOnFence = false;


            for (var y = minY; y <= maxY; y++) {
                point = new Point(x, y);
                if (map.contains(point)) {
                    var rightPoint = new Point(x + 1, y);
                    var leftPoint = new Point(x - 1, y);
                    if (map.contains(rightPoint) && (map.contains(leftPoint))) {
                        isOutside = !isOutside;
                    } else if (map.contains(rightPoint)) {
                        if (isOnFence) {
                            isOnFence = false;
                            if (isRightBorder) {
                                isRightBorder = false;
                            } else {
                                isLeftBorder = false;
                                isOutside = !isOutside;
                            }
                        } else {
                            isRightBorder = true;
                            isOnFence = true;
                        }
                    } else if (map.contains(leftPoint)) {
                        if (isOnFence) {
                            isOnFence = false;
                            if (isLeftBorder) {
                                isLeftBorder = false;
                            } else {
                                isRightBorder = false;
                                isOutside = !isOutside;
                            }
                        } else {
                            isLeftBorder = true;
                            isOnFence = true;
                        }
                    }
                } else {
                    if (!isOutside) {
                        total++;
                    }
                }
            }
        }
        total += map.size();
        return total;
    }

    @Override
    public Object getPart2Solution() {
        var digPlans = getInput();
        long total = 0l;
        HashSet<Fence> map = new HashSet<Fence>();
        int maxX = 0;
        int maxY = 0;
        int minX = 0;
        int minY = 0;
        var beginPoint = new Point(0, 0);
        var endPoint = new Point(0, 0);
        for (var digPlan : digPlans) {
            var length = Integer.parseInt(digPlan.color.substring(2, 7), 16);
            var direction = digPlan.color.substring(7, 8);
            var vX = 0;
            var vY = 0;
            if (direction.equals("3")) {
                vY = 1;
            } else if (direction.equals("0")) {
                vX = 1;
            } else if (direction.equals("1")) {
                vY = -1;
            } else if (direction.equals("2")) {
                vX = -1;
            }
            endPoint = new Point(beginPoint.x + length * vX, beginPoint.y + length * vY);
            map.add(new Fence(beginPoint, endPoint));
            beginPoint = new Point(endPoint.x, endPoint.y);
            maxX = Math.max(maxX, beginPoint.x);
            minX = Math.min(minX, beginPoint.x);
            maxY = Math.max(maxY, beginPoint.y);
            minY = Math.min(minY, beginPoint.y);
        }

        var fenceMap = new FenceMap(map);
        int lastColumn = minX;
        for (var change : fenceMap.changesColumns) {
            total += fenceMap.count(change);
            if (change != minX) {
                total += (long) (Math.abs(change - lastColumn) - 1) * (long) fenceMap.count(change - 1);
                lastColumn = change;
            }
        }
        return total;
    }

    public class FenceMap {
        HashSet<Fence> fences;
        ArrayList<Fence> horizontalFences = new ArrayList<Fence>();
        ArrayList<Fence> verticalFences = new ArrayList<Fence>();
        ArrayList<Integer> changesColumns = new ArrayList<Integer>();

        public FenceMap(HashSet<Fence> fences) {
            this.fences = fences;

            for (var fence : fences) {
                if (fence.isHorizontal) {
                    horizontalFences.add(fence);
                } else {
                    verticalFences.add(fence);
                    if (!changesColumns.contains(fence.begin.x)) {
                        changesColumns.add(fence.begin.x);
                    }
                }
            }
            Collections.sort(horizontalFences, Comparator.comparingInt(fence -> fence.begin.y));
            Collections.sort(verticalFences, Comparator.comparingInt(fence -> fence.begin.x));
            Collections.sort(changesColumns);
        }

        public int count(int column) {
            int result = 0;
            if (changesColumns.contains(column)) {
                int beginInside = 0;
                var isInside = false;
                var isFenceRight = false;
                var isFenceLeft = false;
                for (var fence : horizontalFences) {
                    if (fence.isCrossed(column)) {
                        isInside = !isInside;
                        if (isInside) {
                            beginInside = fence.begin.y;
                        } else {
                            result += Math.abs(fence.begin.y - beginInside) + 1;
                        }
                    } else if (fence.isRightTouched(column) || fence.isLeftTouched(column)) {
                        if (isFenceRight || isFenceLeft) {
                            if (!((fence.isRightTouched(column) && isFenceRight) || (fence.isLeftTouched(column) && isFenceLeft))) {
                                isInside = !isInside;
                            }
                            isFenceRight = false;
                            isFenceLeft = false;
                            result += Math.abs(fence.begin.y - beginInside) + 1;
                            if (isInside) {
                                result--;
                            }
                            beginInside = fence.begin.y;

                        } else {
                            isFenceRight = fence.isRightTouched(column);
                            isFenceLeft = fence.isLeftTouched(column);
                            if (isInside) {
                                result += Math.abs(fence.begin.y - beginInside) ;
                            }
                            beginInside = fence.begin.y;
                        }

                    }


                }

            } else {
                int beginInside = 0;
                var isInside = false;
                for (var fence : horizontalFences) {
                    if (fence.isCrossed(column)) {
                        isInside = !isInside;
                        if (isInside) {
                            beginInside = fence.begin.y;
                        } else {
                            result += fence.begin.y - beginInside + 1;
                        }
                    }

                }
            }
            return result;

        }
    }

    public class Fence {
        Point begin;
        Point end;
        boolean isHorizontal;

        public Fence(Point begin, Point end) {
            this.begin = begin;
            this.end = end;
            isHorizontal = begin.x != end.x;
        }

        public boolean isCrossed(Integer changeLine) {
            return ((begin.x < changeLine && end.x > changeLine) || (begin.x > changeLine && end.x < changeLine));
        }

        public boolean isLeftTouched(Integer changeLine) {
            return ((begin.x == changeLine && end.x < begin.x) || (end.x == changeLine && end.x > begin.x));
        }

        public boolean isRightTouched(Integer changeLine) {
            return ((begin.x == changeLine && end.x > begin.x) || (end.x == changeLine && end.x < begin.x));
        }
    }

    public class DigPlan {
        String direction;
        int length;
        String color;
    }


    private ArrayList<DigPlan> getInput() {
        var input = getInputLines();
        ArrayList<DigPlan> digPlans = new ArrayList<DigPlan>();
        for (var line : input) {
            var digPlan = new DigPlan();
            digPlans.add(digPlan);
            digPlan.direction = line.substring(0, 1);
            line = line.substring(2);
            digPlan.length = Integer.parseInt(line.substring(0, line.indexOf(" ")));
            digPlan.color = line.substring(line.indexOf(" ")).trim();
        }
        return digPlans;
    }

}
