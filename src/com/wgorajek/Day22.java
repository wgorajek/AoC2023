package com.wgorajek;

import java.util.*;

public class Day22 extends Solution {
    @Override
    public Object getPart1Solution() {
        int total = 0;
        ArrayList<Brick> bricks = getInput();
        var fallenBricks = new ArrayList<Brick>();
        bricks.sort(Comparator.comparingInt(brick -> brick.end.z));
        for (var brick : bricks) {
            brick.fall(fallenBricks);
            fallenBricks.add(brick);
            fallenBricks.sort(Comparator.comparingInt(brickT -> brickT.end.z));
        }
        for (var brick1 : fallenBricks) {
            Brick supporter = null;
            for (var brick2 : fallenBricks) {
                if (brick1 != brick2) {
                    if (brick1.isSupportedBy(brick2)) {
                        if (supporter == null) {
                            supporter = brick2;
                        } else { //there are more than one supporter
                            supporter = null;
                            break;
                        }
                    }
                }
            }
            if (supporter != null) {
                supporter.canBeDesintegrated = false;
            }
        }

        for (var brick : bricks) {
            if (brick.canBeDesintegrated) {
                total++;
            }
        }
        return total;

    }


    @Override
    public Object getPart2Solution() {
        int total = 0;
        ArrayList<Brick> bricks = getInput();
        var fallenBricks = new ArrayList<Brick>();
        bricks.sort(Comparator.comparingInt(brick -> brick.end.z));
        for (var brick : bricks) {
            brick.fall(fallenBricks);
            fallenBricks.add(brick);
            fallenBricks.sort(Comparator.comparingInt(brickT -> brickT.end.z));
        }
        for (var brick1 : fallenBricks) {
            for (var brick2 : fallenBricks) {
                if (brick1 != brick2) {
                    if (brick1.isSupportedBy(brick2)) {
                        brick2.supporting.add(brick1);
                        brick1.isSupportedBy.add(brick2);
                    }
                }
            }
        }


        for (var brickToRemove : bricks) {
            Queue<Brick> bricksToCheck = new LinkedList<Brick>();
            bricksToCheck.addAll(brickToRemove.supporting);

            while (!bricksToCheck.isEmpty()) {
                var brick = bricksToCheck.remove();
                if (brick.isFalling) {
                    continue;
                }
                var isSupported = false;
                for (var supportedByBrick : brick.isSupportedBy) {
                    if (!supportedByBrick.isFalling && supportedByBrick != brickToRemove) {
                        isSupported = true;
                    }
                }
                brick.isFalling = !isSupported;
                if (brick.isFalling) {
                    bricksToCheck.addAll(brick.supporting);
                }


            }
            var fallingCount = 0;
            for (var brick : bricks) {
                if (brick.isFalling) {
                    fallingCount++;
                }
                brick.isFalling = false;

            }
            total += fallingCount;
        }
        return total;
    }


    public class Point3D {
        int x;
        int y;
        int z;

        public Point3D(int x, int y, int z) {
            this.x = x;
            this.y = y;
            this.z = z;
        }

        @Override
        public String toString() {
            return "(" + x + ", " + y + ", " + z + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Point3D point3D = (Point3D) o;
            return x == point3D.x && y == point3D.y && z == point3D.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

    }

    public class Brick {
        Point3D start;
        Point3D end;
        boolean canBeDesintegrated = true;
        boolean isFalling = false;

        ArrayList<Brick> supporting = new ArrayList<Brick>();
        ArrayList<Brick> isSupportedBy = new ArrayList<Brick>();

        public Brick(Point3D start, Point3D end) {
            this.start = start;
            this.end = end;
        }

        public boolean isOver(Brick brick) {
            if (this.start.x <= brick.end.x && this.end.x >= brick.start.x && this.start.y <= brick.end.y && this.end.y >= brick.start.y) {
                //&& this.start.z > brick.end.z
                return true;
            }
            return false;
        }

        public void fallUntil(Brick brick) {
            if (brick == null) {
                this.end.z -= this.start.z - 1;
                this.start.z = 1;
            } else {
                this.end.z -= this.start.z - brick.end.z - 1;
                this.start.z -= this.start.z - brick.end.z - 1;
            }
        }

        public void fall(ArrayList<Brick> bricks) {
            for (var i = bricks.size() - 1; i >= 0; i--) {
                var brick = bricks.get(i);
                if (isOver(brick)) {
                    fallUntil(brick);
                    return;
                }
            }
            fallUntil(null);
        }

        public boolean isSupportedBy(Brick brick) {
            if (this.start.x <= brick.end.x && this.end.x >= brick.start.x && this.start.y <= brick.end.y && this.end.y >= brick.start.y && this.start.z == brick.end.z + 1) {
                return true;
            }
            return false;
        }

        public boolean isOverlapped(Brick brick) {
            return this.isOver(brick) && this.start.z <= brick.end.z && this.end.z >= brick.start.z;

        }
    }


    private ArrayList<Brick> getInput() {
        var input = getInputLines();
        ArrayList<Brick> bricks = new ArrayList<Brick>();
        for (var line : input) {
            var split = line.split("~");
            var point1Split = split[0].split(",");
            var point2Split = split[1].split(",");
            var point1 = new Point3D(Integer.parseInt(point1Split[0]), Integer.parseInt(point1Split[1]), Integer.parseInt(point1Split[2]));
            var point2 = new Point3D(Integer.parseInt(point2Split[0]), Integer.parseInt(point2Split[1]), Integer.parseInt(point2Split[2]));

            bricks.add(new Brick(point1, point2));
        }


        return bricks;
    }

}
