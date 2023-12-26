package com.wgorajek;

import java.util.*;

public class Day25 extends Solution {
    @Override
    public Object getPart1Solution() {
        HashMap<String, Wire> connections = getInput();
        int total = 0;
        Queue<Wire> toVisit = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        toVisit.add(connections.get("jqt"));

        for (var wire : connections.values()) {
            wire.connections2.putAll(connections);
        }

        ArrayList<Wire> wires = new ArrayList<Wire>();
        wires.addAll(connections.values());

        HashMap<Connection, Integer> map = new LinkedHashMap<Connection, Integer>();
        var counter = 1;
        for (var w1 : connections.values()) {
            if (counter % 5 == 0) {
                counter++;
                continue;
            }
            for (var j = counter % wires.size(); j <= wires.size() -1 ; j++) {
                var w2 = wires.get(j);
                if (w1 != w2) {
                    counter++;
                    if (counter > 5000) {
                        break;
                    }
                    var visitedWires = findConnection(connections, w1, w2);
                    if (visitedWires.size() > 10) {
                        for (var i = 1; i <= visitedWires.size() - 1; i++) {
                            var conn = new Connection(visitedWires.get(i - 1).name, visitedWires.get(i).name);
                            var tmp = map.getOrDefault(conn, 0);
                            map.put(conn, tmp + 1);
                        }
                    }
                }
            }
        }
        ArrayList<Integer> listInt = new ArrayList<Integer>();
        for (var wire : map.keySet()) {
            listInt.add(map.get(wire));
        }
        Collections.sort(listInt);

        var listConnections = new ArrayList<Connection>();
        for (var conn : map.keySet()) {
            if (map.get(conn) >= listInt.get(listInt.size()-20)) {
                listConnections.add(conn);
            }
        }

        for (var i = 0; i <= listConnections.size()-1; i++) {
            for (var j = i + 1; j <= listConnections.size()-1; j++) {
                for (var k = j + 1; k <= listConnections.size()-1; k++) {
                    var newConnections = getInput();
                    for (var wire: newConnections.values()) {
                        wire.connections.putAll(wire.connections2);
                    }
                    breakConnections(newConnections, listConnections.get(i).w1, listConnections.get(i).w2);
                    breakConnections(newConnections, listConnections.get(j).w1, listConnections.get(j).w2);
                    breakConnections(newConnections, listConnections.get(k).w1, listConnections.get(k).w2);
                    total = getResult(newConnections);
                    if (total > 0 ) {
                        return total;
                    }
                }
            }
        }
        return total;
    }


    public class Path {
        ArrayList<Wire> toVisit = new ArrayList<>();
        ArrayList<Wire> visited = new ArrayList<>();
        Wire actualPoint;

        public Path(ArrayList<Wire> toVisit, ArrayList<Wire> visited, Wire actualPoint) {
            this.toVisit.addAll(toVisit);
            this.visited.addAll(visited);
            this.actualPoint = actualPoint;
        }

        public Path() {

        }
    }

    public ArrayList<Wire> findConnection(HashMap<String, Wire> connections, Wire w1, Wire w2) {
        Queue<Path> paths = new LinkedList<Path>();
        connections.values();
        Path p = new Path();
        p.toVisit.addAll(connections.values());
        p.actualPoint = w1;
        p.visited.add(w1);
        p.toVisit.remove(w1);
        HashMap<Wire, Integer> foundPaths = new LinkedHashMap<>();

        paths.add(p);
        while (!paths.isEmpty()) {
            var path = paths.remove();
            if (path.actualPoint == w2) {
                return path.visited;
            } else {
                if (foundPaths.containsKey(path.actualPoint)) {
                    var value = foundPaths.get(path.actualPoint);
                    if (value < path.visited.size()) {
                        continue;
                    }
                } else {
                    foundPaths.put(path.actualPoint, path.visited.size());
                }
            }
            for (var wire : path.actualPoint.connections.values()) {
                if (path.toVisit.contains(wire)) {
                    var newPath = new Path(path.toVisit, path.visited, wire);
                    newPath.toVisit.remove(wire);
                    newPath.visited.add(wire);
                    paths.add(newPath);
                }
            }
        }
        return null;
    }

    public class Connection {
        String w1;
        String w2;

        public Connection(String w1, String w2) {
            this.w1 = w1;
            this.w2 = w2;
        }

        @Override
        public String toString() {
            return "(" + w1 + ", " + w2 + ")";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Connection c = (Connection) o;
            return w1.equals(c.w1) && w2.equals(c.w2);
        }

        @Override
        public int hashCode() {
            return Objects.hash(w1 + w2);
        }
    }

    public void breakConnections(HashMap<String, Wire> connections, String w1s, String w2s) {
        var w1 = connections.get(w1s);
        var w2 = connections.get(w2s);
        w1.connections.remove(w2.name);
        w2.connections.remove(w1.name);

    }

    public int getResult(HashMap<String, Wire> connections) {
        int total1 = 0;
        int total2 = 0;
        Queue<Wire> toVisit = new LinkedList<>();
        HashSet<String> visited = new HashSet<>();
        toVisit.add(connections.get("jqt"));
        while (!toVisit.isEmpty()) {
            var wire = toVisit.remove();
            for (var connection : wire.connections.values()) {
                if (!visited.contains(connection.name)) {
                    toVisit.add(connection);
                    visited.add(connection.name);
                }
            }
        }
        total1 = visited.size();
        Wire newWire = null;
        for (var wire : connections.values()) {
            if (!visited.contains(wire.name)) {
                newWire = wire;
                break;
            }
        }
        if (newWire != null) {
            toVisit.add(newWire);
            visited.clear();
            while (!toVisit.isEmpty()) {
                var wire = toVisit.remove();
                for (var connection : wire.connections.values()) {
                    if (!visited.contains(connection.name)) {
                        toVisit.add(connection);
                        visited.add(connection.name);
                    }
                }
            }
        }
        total2 = visited.size();

        if ((total1 + total2 == connections.size()) && total1 > 0 && total2 > 0) {
            return total1 * total2;
        } else {
            return 0;
        }
    }

    @Override
    public Object getPart2Solution() {
        int total = 0;

        return total;
    }


    public class Wire {
        HashMap<String, Wire> connections = new HashMap<String, Wire>();
        HashMap<String, Wire> connections2 = new HashMap<String, Wire>();
        String name;

        public Wire(String name) {
            this.name = name;
        }

        public Wire(HashMap<String, Wire> connections, String name) {
            this.connections.putAll(connections);
            this.name = name;
        }
    }

    private HashMap<String, Wire> getInput() {
        var input = getInputLines();
        HashMap<String, Wire> wires = new HashMap<String, Wire>();
        for (var line : input) {
            var split1 = line.split(":");
            if (!wires.containsKey(split1[0])) {
                wires.put(split1[0], new Wire(split1[0]));
            }
            var wire1 = wires.get(split1[0]);
            var split2 = split1[1].trim().split(" ");
            for (var split3 : split2) {
                if (!wires.containsKey(split3)) {
                    wires.put(split3, new Wire(split3));
                }
                var wire2 = wires.get(split3);
                wire1.connections.put(wire2.name, wire2);
                wire2.connections.put(wire1.name, wire1);
            }
        }


        return wires;
    }


}
