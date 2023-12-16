package com.wgorajek;

import java.awt.*;
import java.util.*;
import java.util.List;

public class Day16 extends Solution {
    @Override
    public Object getPart1Solution() {
        Contraption contraption = getInput();
        return contraption.getEnergy(new Beam(new Point(-1, 0), new Point(1, 0)));
    }

    @Override
    public Object getPart2Solution() {
        Contraption contraption = getInput();
        int highestEnergy = 0;
        for (var i = 0; i <= contraption.maxX; i++) {
            highestEnergy = Math.max(highestEnergy, contraption.getEnergy(new Beam(new Point(-1, i), new Point(1, 0))));
            highestEnergy = Math.max(highestEnergy, contraption.getEnergy(new Beam(new Point(contraption.maxX + 1, i), new Point(-1, 0))));
            highestEnergy = Math.max(highestEnergy, contraption.getEnergy(new Beam(new Point(i, -1), new Point(0, 1))));
            highestEnergy = Math.max(highestEnergy, contraption.getEnergy(new Beam(new Point(i, contraption.maxX + 1), new Point(0, -1))));
        }


        return highestEnergy;
    }

    public class Beam {
        Point point;
        Point direction;

        public Beam(Point point, Point direction) {
            this.point = point;
            this.direction = direction;
        }

        public boolean move(int maxX) {
            point.y += direction.y;
            point.x += direction.x;
            if (point.x > maxX || point.x < 0 || point.y > maxX || point.y < 0) {
                return false;
            }
            return true;
        }

        public int beamNumber() {
            if (direction.x == 0 && direction.y == -1) {
                return 0;
            } else if (direction.x == -1 && direction.y == 0) {
                return 2;
            } else if (direction.x == 0 && direction.y == 1) {
                return 4;
            } else if (direction.x == 1 && direction.y == 0) {
                return 6;
            }
            return -1;
        }

        public ArrayList<Beam> getReflections(Mirror mirror) {
            var result = new ArrayList<Beam>();
            if (mirror.number == beamNumber() % 4) {
                result.add(this);
            } else if (mirror.number == (beamNumber() + 1) % 4) {
                result.add(new Beam(new Point(point.x, point.y), rotateLeft90(this.direction)));
            } else if (mirror.number == (beamNumber() + 2) % 4) {
                result.add(new Beam(new Point(point.x, point.y), rotateLeft90(this.direction)));
                result.add(new Beam(new Point(point.x, point.y), rotateRight90(this.direction)));
            } else if (mirror.number == (beamNumber() + 3) % 4) {
                result.add(new Beam(new Point(point.x, point.y), rotateRight90(this.direction)));
            }

            return result;
        }

        public Point rotateRight90(Point point) {
            if (point.x == 0 && point.y == -1) {
                return new Point(-1, 0);
            } else if (point.x == -1 && point.y == 0) {
                return new Point(0, 1);
            } else if (point.x == 0 && point.y == 1) {
                return new Point(1, 0);
            } else if (point.x == 1 && point.y == 0) {
                return new Point(0, -1);
            }
            return null;
        }

        public Point rotateLeft90(Point point) {
            if (point.x == 0 && point.y == -1) {
                return new Point(1, 0);
            } else if (point.x == -1 && point.y == 0) {
                return new Point(0, -1);
            } else if (point.x == 0 && point.y == 1) {
                return new Point(-1, 0);
            } else if (point.x == 1 && point.y == 0) {
                return new Point(0, 1);
            }
            return null;
        }
    }

    public class Mirror {
        Point point;
        char mirrorChar;
        int energized = 0;
        int number;

        public Mirror(Point point, char mirrorChar) {
            this.point = point;
            this.mirrorChar = mirrorChar;
            if (mirrorChar == '|') {
                number = 0;
            } else if (mirrorChar == '/') {
                number = 1;
            } else if (mirrorChar == '-') {
                number = 2;
            } else if (mirrorChar == '\\') {
                number = 3;
            }
        }
    }

    public class Contraption {
        HashMap<Point, Mirror> mirrors;
        int maxX;

        public Contraption(int maxX) {
            this.mirrors = new HashMap<Point, Mirror>();
            this.maxX = maxX;
        }

        public int getEnergy(Beam startBeam) {
            HashMap<Point, HashSet<Point>> energized = new HashMap<Point, HashSet<Point>>();
            Queue<Beam> beams = new LinkedList<Beam>();
            beams.add(startBeam);
            while (!beams.isEmpty()) {
                var beam = beams.remove();
                var repeated = false;
                if (beam.move(maxX)) {
                    if (!energized.containsKey(beam.point)) {
                        var directions = new HashSet<Point>();
                        directions.add(new Point(beam.direction.x, beam.direction.y));
                        energized.put(new Point(beam.point.x, beam.point.y), directions);
                    } else {
                        if (energized.get(beam.point).contains(beam.direction)) {
                            repeated = true;
                        } else {
                            energized.get(beam.point).add(new Point(beam.direction.x, beam.direction.y));
                        }
                    }
                    if (!repeated) {
                        var mirror = mirrors.get(beam.point);
                        if (mirror != null) {
                            beams.addAll(beam.getReflections(mirror));
                        } else {
                            beams.add(beam);
                        }
                    }
                }
            }

            return energized.size();
        }

    }

    private Contraption getInput() {
        final List<String> input = getInputLines();
        ArrayList<String> steps = new ArrayList<String>();
        int i = 0;
        Contraption contraption = new Contraption(getInputLines().size() - 1);
        for (var line : input) {
            for (var j = 0; j <= line.length() - 1; j++) {
                if (line.charAt(j) != '.') {
                    contraption.mirrors.put(new Point(j, i), new Mirror(new Point(j, i), line.charAt(j)));
                }
            }
            i++;
        }
        return contraption;
    }

}
