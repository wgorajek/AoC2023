package com.wgorajek;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day14 extends Solution {
    @Override
    public Object getPart1Solution() {
        Platform platform = getInput();
        platform.tilt(1);
        return platform.countWeight();
    }

    @Override
    public Object getPart2Solution() {
        Platform platform = getInput();
        var paterns = new HashMap<Integer, PlatformPeriod>();
        int period = 0;
        int todo = -1;
        for (var i = 1; todo != 0; i++) {
            platform.tilt(1);
            platform.tilt(2);
            platform.tilt(3);
            platform.tilt(4);
            var weight = platform.countWeight();
            if (paterns.containsKey(weight)) {
                if (platform.isRepeated(paterns.get(weight).balls)) {
                    period = i - paterns.get(weight).iteration;
                    todo = (1000000000 - i) % period;
                    System.out.println("jest " + period);
                    break;
                }
            }
            paterns.put(weight, new PlatformPeriod(i, new ArrayList<>(platform.balls)));
        }
        for (var i = 1; i <= todo ; i++) {
            platform.tilt(1);
            platform.tilt(2);
            platform.tilt(3);
            platform.tilt(4);
        }

        return platform.countWeight();
    }

    public class PlatformPeriod {
        ArrayList<Point> balls;
        int iteration;

        public PlatformPeriod(int iteration, ArrayList<Point> balls) {
            this.balls = balls;
            this.iteration = iteration;
        }
    }

    public class Platform {
        ArrayList<Point> rocks;
        ArrayList<Point> balls;
        int maxX;

        public Platform(int max) {
            rocks = new ArrayList<Point>();
            balls = new ArrayList<Point>();
            maxX = max;
        }

        public int countWeight() {
            int result = 0;
            for (var point : balls) {
                result += maxX - point.y;
            }
            return result;
        }

        public void tilt(int direction) {

            ArrayList<Point> newBalls = new ArrayList<Point>();

            if (direction % 4 == 1) {
                for (var i = 0; i <= maxX - 1; i++) {
                    Point rock = new Point(i, -1);
                    for (var j = 0; j <= maxX - 1; j++) {
                        Point point = new Point(i, j);
                        if (rocks.contains(point)) {
                            rock = point;
                        } else if (balls.contains(point)) {
                            rock = new Point(rock.x, rock.y + 1);
                            newBalls.add(rock);
                        }
                    }
                }
            } else if (direction % 4 == 2) {
                for (var i = 0; i <= maxX - 1; i++) {
                    Point rock = new Point(-1, i);
                    for (var j = 0; j <= maxX - 1; j++) {
                        Point point = new Point(j, i);
                        if (rocks.contains(point)) {
                            rock = point;
                        } else if (balls.contains(point)) {
                            rock = new Point(rock.x + 1, rock.y);
                            newBalls.add(rock);
                        }
                    }
                }
            } else if (direction % 4 == 3) {
                for (var i = 0; i <= maxX - 1; i++) {
                    Point rock = new Point(i, maxX);
                    for (var j = maxX - 1; j >= 0; j--) {
                        Point point = new Point(i, j);
                        if (rocks.contains(point)) {
                            rock = point;
                        } else if (balls.contains(point)) {
                            rock = new Point(rock.x, rock.y - 1);
                            newBalls.add(rock);
                        }
                    }
                }
            } else if (direction % 4 == 0) {
                for (var i = 0; i <= maxX - 1; i++) {
                    Point rock = new Point(maxX, i);
                    for (var j = maxX - 1; j >= 0; j--) {
                        Point point = new Point(j, i);
                        if (rocks.contains(point)) {
                            rock = point;
                        } else if (balls.contains(point)) {
                            rock = new Point(rock.x - 1, rock.y);
                            newBalls.add(rock);
                        }
                    }
                }
            }

            balls = newBalls;
        }

        public boolean isRepeated(ArrayList<Point> points) {
            for (var point : points) {
                if (!balls.contains(point)) {
                    return false;
                }
            }
            return true;
        }
    }


    private Platform getInput() {
        final List<String> input = getInputLines();
        Platform platform = new Platform(input.size());
        int i = 0;
        for (var line : input) {
            for (var j = 0; j <= line.length() - 1; j++) {
                if (line.charAt(j) == '#') {
                    platform.rocks.add(new Point(j, i));
                } else if (line.charAt(j) == 'O') {
                    platform.balls.add(new Point(j, i));
                }
            }
            i++;
        }
        return platform;
    }

}
