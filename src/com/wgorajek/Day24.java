package com.wgorajek;

import java.awt.*;
import java.beans.PropertyEditorSupport;
import java.sql.Array;
import java.util.*;

import Jama.Matrix;

import java.lang.Math.*;
import java.util.function.Predicate;

public class Day24 extends Solution {
    @Override
    public Object getPart1Solution() {
        var hailstones = getInput();
        var total = 0;
        for (var i = 0; i <= hailstones.size() - 1; i++) {
            var hailstone1 = hailstones.get(i);
            for (var j = i + 1; j <= hailstones.size() - 1; j++) {
                var hailstone2 = hailstones.get(j);
                if (hailstone1 != hailstone2) {
                    Point3DDouble point = hailstone1.intersectPoint(hailstone2);
                    if (point != null && point.x >= 200000000000000L && point.x <= 400000000000000l && point.y >= 200000000000000L && point.y <= 400000000000000l) {
                        total++;
                    }
                }
            }
        }
        return total;
    }


    @Override
    public Object getPart2Solution() {
        var hailstones = getInput();
        var total = 0L;

        HashMap<Long, ArrayList<Hailstone>> mapX = new HashMap<Long, ArrayList<Hailstone>>();
        HashMap<Long, ArrayList<Hailstone>> mapY = new HashMap<Long, ArrayList<Hailstone>>();
        HashMap<Long, ArrayList<Hailstone>> mapZ = new HashMap<Long, ArrayList<Hailstone>>();
        for (var hailstone : hailstones) {
            if (!mapX.containsKey(hailstone.velocity.x)) {
                mapX.put(hailstone.velocity.x, new ArrayList<>());
            }
            if (!mapY.containsKey(hailstone.velocity.y)) {
                mapY.put(hailstone.velocity.y, new ArrayList<>());
            }
            if (!mapZ.containsKey(hailstone.velocity.z)) {
                mapZ.put(hailstone.velocity.z, new ArrayList<>());
            }
            mapX.get(hailstone.velocity.x).add(hailstone);
            mapY.get(hailstone.velocity.y).add(hailstone);
            mapZ.get(hailstone.velocity.z).add(hailstone);
        }

        var velocityX = 99999L;
        for (var tmp : mapX.keySet()) {
            if (mapX.get(tmp).size() >= 4) {
                var list = mapX.get(tmp);
                velocityX = Math.min(velocityX, gcd(list.get(0).point.x - list.get(1).point.x, list.get(3).point.x - list.get(2).point.x) + tmp);
            }
        }

        var velocityY = 99999L;
        for (var tmp : mapY.keySet()) {
            if (mapY.get(tmp).size() >= 4) {
                var list = mapY.get(tmp);
                velocityY = Math.min(velocityY, gcd(list.get(0).point.y - list.get(1).point.y, list.get(3).point.y - list.get(2).point.y) + tmp);
            }
        }

        var velocityZ = 99999L;
        for (var tmp : mapZ.keySet()) {
            if (mapZ.get(tmp).size() >= 3) {
                var list = mapZ.get(tmp);
                velocityZ = Math.min(velocityZ, gcd(Math.abs(list.get(0).point.z - list.get(1).point.z), Math.abs(list.get(1).point.z - list.get(2).point.z)) + tmp);
            }
        }

        var hailstone1 = hailstones.get(0);
        var hailstone2 = hailstones.get(1);
        Point3D v1 = new Point3D(hailstone1.velocity.z - velocityZ, hailstone1.velocity.y - velocityY, 0);
        Point3D v2 = new Point3D(hailstone2.velocity.z - velocityZ, hailstone2.velocity.y - velocityY, 0);
        Point3D v3 = new Point3D(hailstone1.velocity.x - velocityX, hailstone1.velocity.y - velocityY, 0);
        Point3D v4 = new Point3D(hailstone2.velocity.x - velocityX, hailstone2.velocity.y - velocityY, 0);
        var hail1 = new Hailstone(new Point3D(hailstone1.point.z, hailstone1.point.y, 0), v1);
        var hail2 = new Hailstone(new Point3D(hailstone2.point.z, hailstone2.point.y, 0), v2);
        var hail3 = new Hailstone(new Point3D(hailstone1.point.x, hailstone1.point.y, 0), v3);
        var hail4 = new Hailstone(new Point3D(hailstone2.point.x, hailstone2.point.y, 0), v4);

        var c = hail1.intersectPoint(hail2);
        total += c.x + c.y; //getting point.Y and point.Z
        c = hail3.intersectPoint(hail4);
        total += c.x; //getting point.X

        return total;
    }

    public static long gcd(long a, long b) {
        while (b != 0) {
            long temp = b;
            b = a % b;
            a = temp;
        }
        return a;
    }

    public class Point3DDouble {
        double x;
        double y;
        double z;

        public Point3DDouble(double x, double y, double z) {
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
            Day22.Point3D point3D = (Day22.Point3D) o;
            return x == point3D.x && y == point3D.y && z == point3D.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }
    }

    public class Point3D {
        long x;
        long y;
        long z;

        public Point3D(long x, long y, long z) {
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
            Day22.Point3D point3D = (Day22.Point3D) o;
            return x == point3D.x && y == point3D.y && z == point3D.z;
        }

        @Override
        public int hashCode() {
            return Objects.hash(x, y, z);
        }

    }

    public class Hailstone {
        Point3D point;
        Point3D velocity;


        public Hailstone(Point3D point, Point3D velocity) {
            this.point = point;
            this.velocity = velocity;
        }

        public Point3DDouble intersectPoint(Hailstone hailstone2) {
            double[][] lhsArray = {{velocity.x, -hailstone2.velocity.x}, {velocity.y, -hailstone2.velocity.y}};
            double[] rhsArray = {hailstone2.point.x - point.x, hailstone2.point.y - point.y};
            Matrix lhs = new Matrix(lhsArray);
            Matrix rhs = new Matrix(rhsArray, 2);
            if (lhs.det() != 0) {
                Matrix ans = lhs.solve(rhs);
                var tmp2 = ans.get(0, 0);
                var tmp = ans.get(1, 0);

                if (tmp >= 0 && tmp2 >= 0) {
                    return new Point3DDouble(tmp * hailstone2.velocity.x + hailstone2.point.x, tmp * hailstone2.velocity.y + hailstone2.point.y, 0);
                }
            }

            return null;
        }
    }

    private ArrayList<Hailstone> getInput() {
        var input = getInputLines();
        ArrayList<Hailstone> hailstones = new ArrayList<Hailstone>();
        for (var line : input) {
            var split = line.split("@");
            var pointSplit = split[0].split(",");
            var velocitySplit = split[1].split(",");
            var point = new Point3D(Long.parseLong(pointSplit[0].trim()), Long.parseLong(pointSplit[1].trim()), Long.parseLong(pointSplit[2].trim()));
            var velocity = new Point3D(Long.parseLong(velocitySplit[0].trim()), Long.parseLong(velocitySplit[1].trim()), Long.parseLong(velocitySplit[2].trim()));
            hailstones.add(new Hailstone(point, velocity));

        }
        return hailstones;
    }

}
