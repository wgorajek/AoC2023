package com.wgorajek;

import java.util.*;
import java.util.function.Predicate;

public class Day19 extends Solution {
    @Override
    public Object getPart1Solution() {
        var system = getInput();
        int total = 0;
        for (var rating : system.ratings) {
            var rule = system.rules.get("in");
            var result = rule.getResult(rating);
            while (!result.equals("A") && !result.equals("R")) {
                rule = system.rules.get(result);
                result = rule.getResult(rating);
            }
            if (result.equals("A")) {
                total += rating.totalValue();
            }
        }

        return total;
    }


    @Override
    public Object getPart2Solution() {
        var system = getInput();
        long total = 0l;
        var firstRating = new IntervalRating();
        firstRating.x.add(new Interval(1, 4000));
        firstRating.m.add(new Interval(1, 4000));
        firstRating.a.add(new Interval(1, 4000));
        firstRating.s.add(new Interval(1, 4000));
        Queue<State> states = new LinkedList<State>();
        states.add(new State(firstRating, "in"));

        while (!states.isEmpty()) {
            var state = states.remove();
            var rule = system.rules.get(state.ruleName);
            var newStates = rule.checkCondition(state.rating);
            for (var newState : newStates) {
                if (newState.ruleName.equals("A")) {
                    total += newState.rating.value();
                } else if (!newState.ruleName.equals("R")) {
                    states.add(newState);
                }
            }


        }

        return total;
    }


    public class State {
        IntervalRating rating;
        String ruleName;

        public State(IntervalRating rating, String ruleName) {
            this.rating = rating;
            this.ruleName = ruleName;
        }
    }

    public class Condition {
        String argumentName;
        Predicate<Integer> fun;
        String operator;
        int operatorValue;
        String result;

        public Condition(String argumentName, Predicate<Integer> fun, String result) {
            this.argumentName = argumentName;
            this.fun = fun;
            this.result = result;
        }

        public Condition(String argumentName, Predicate<Integer> fun, String operator, int operatorValue, String result) {
            this.argumentName = argumentName;
            this.fun = fun;
            this.operator = operator;
            this.operatorValue = operatorValue;
            this.result = result;
        }
    }

    public class Rule {
        String name;
        String elseResult;
        ArrayList<Condition> conditions;

        public Rule(String name) {
            this.name = name;
            this.conditions = new ArrayList<Condition>();
        }

        public String getResult(Rating rating) {
            for (var condition : conditions) {
                var argument = rating.value(condition.argumentName);
                if (condition.fun.test((argument))) {
                    return condition.result;
                }
            }
            return elseResult;
        }

        public ArrayList<State> checkCondition(IntervalRating chekedRating) {
            var result = new ArrayList<State>();
            var unProcessedRatings = new ArrayList<IntervalRating>();
            unProcessedRatings.add(chekedRating);
            for (var condition : conditions) {
                var newUnProcessedRatings = new ArrayList<IntervalRating>();
                for (var rating : unProcessedRatings) {
                    var passedIntervals = new ArrayList<Interval>();
                    var notPassedIntervals = new ArrayList<Interval>();

                    var intervals = rating.getIntervals(condition.argumentName);
                    for (var interval : intervals) {
                        var operatorValue = condition.operatorValue;
                        if (condition.operator.equals("<")) {
                            if (operatorValue > interval.end) {
                                passedIntervals.add(interval);
                            } else {
                                if (operatorValue <= interval.begin) {
                                    notPassedIntervals.add(interval);
                                } else {
                                    passedIntervals.add(new Interval(interval.begin, operatorValue - 1));
                                    notPassedIntervals.add(new Interval(operatorValue, interval.end));
                                }
                            }
                        } else if (condition.operator.equals(">")) {
                            if (operatorValue < interval.begin) {
                                passedIntervals.add(interval);
                            } else {
                                if (operatorValue >= interval.end) {
                                    notPassedIntervals.add(interval);
                                } else {
                                    notPassedIntervals.add(new Interval(interval.begin, operatorValue));
                                    passedIntervals.add(new Interval(operatorValue + 1, interval.end));
                                }
                            }
                        }
                    }
                    if (!passedIntervals.isEmpty()) {
                        var newState = new State(new IntervalRating(rating), condition.result);
                        newState.rating.setIntervals(passedIntervals, condition.argumentName);
                        result.add(newState);
                    }
                    if (!notPassedIntervals.isEmpty()) {
                        var tmp = new IntervalRating(rating);
                        tmp.setIntervals(notPassedIntervals, condition.argumentName);
                        newUnProcessedRatings.add(tmp);
                    }

                }
                unProcessedRatings.clear();
                unProcessedRatings.addAll(newUnProcessedRatings);
                newUnProcessedRatings.clear();
            }
            for (var rating: unProcessedRatings) {
                result.add(new State(rating, elseResult));
            }

            return result;
        }

    }

    public class Interval {
        int begin;
        int end;

        public Interval(int begin, int end) {
            this.begin = begin;
            this.end = end;
        }

        public long getValue() {
            return end - begin + 1;
        }
    }

    public class IntervalRating {
        ArrayList<Interval> x;
        ArrayList<Interval> m;
        ArrayList<Interval> a;
        ArrayList<Interval> s;

        public IntervalRating(ArrayList<Interval> x, ArrayList<Interval> m, ArrayList<Interval> a, ArrayList<Interval> s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;
        }

        public IntervalRating() {
            x = new ArrayList<Interval>();
            m = new ArrayList<Interval>();
            a = new ArrayList<Interval>();
            s = new ArrayList<Interval>();
        }

        public IntervalRating(IntervalRating intervalRating) {
            x = new ArrayList<Interval>();
            m = new ArrayList<Interval>();
            a = new ArrayList<Interval>();
            s = new ArrayList<Interval>();
            x.addAll(intervalRating.x);
            m.addAll(intervalRating.m);
            a.addAll(intervalRating.a);
            s.addAll(intervalRating.s);
        }

        private ArrayList<Interval> getIntervals(String operator) {
            if (operator.equals("x")) {
                return x;
            } else if (operator.equals("m")) {
                return m;
            } else if (operator.equals("a")) {
                return a;
            } else {
                return s;
            }
        }

        public long value() {
            long result = 1l;
            for (var interval : x) {
                result *= interval.getValue();
            }
            for (var interval : m) {
                result *= interval.getValue();
            }
            for (var interval : a) {
                result *= interval.getValue();
            }
            for (var interval : s) {
                result *= interval.getValue();
            }
            return result;
        }

        public void setIntervals(ArrayList<Interval> passedIntervals, String argumentName) {
            ArrayList<Interval> result;
            if (argumentName.equals("x")) {
                result = x;
            } else if (argumentName.equals("m")) {
                result = m;
            } else if (argumentName.equals("a")) {
                result = a;
            } else {
                result = s;
            }
            result.clear();
            result.addAll(passedIntervals);
        }
    }

    public class Rating {
        int x;
        int m;
        int a;
        int s;

        public Rating(int x, int m, int a, int s) {
            this.x = x;
            this.m = m;
            this.a = a;
            this.s = s;

        }

        public int value(String argument) {
            if (argument.equals("x")) {
                return x;
            } else if (argument.equals("m")) {
                return m;
            } else if (argument.equals("a")) {
                return a;
            } else if (argument.equals("s")) {
                return s;
            }
            return -1;
        }

        public int totalValue() {
            return x + m + a + s;
        }
    }


    public class PartSystem {
        HashMap<String, Rule> rules;
        ArrayList<Rating> ratings;

        public PartSystem() {
            rules = new HashMap<String, Rule>();
            ratings = new ArrayList<Rating>();
        }
    }

    public Condition parseCondition(String conditionString) {
        String[] parts = conditionString.split(":");
        String[] condition = parts[0].split("[<>]");

        String argumentName = condition[0].trim();
        String operator = parts[0].replaceAll("[^<>]", "").trim();
        int value = Integer.parseInt(condition[1].trim());

        Predicate<Integer> predicate = null;
        switch (operator) {
            case "<":
                predicate = x -> x < value;
                break;
            case ">":
                predicate = x -> x > value;
                break;
        }

        String result = parts[1].trim();

        return new Condition(argumentName, predicate, operator, value, result);
    }

    private PartSystem getInput() {
        var input = getInputLines();
        PartSystem system = new PartSystem();
        var ratings = false;
        for (var line : input) {


            if (line.isEmpty()) {
                ratings = true;
            } else if (ratings) {
                String[] keyValuePairs = line.replaceAll("[{}]", "").split(",");
                int x = Integer.parseInt(keyValuePairs[0].split("=")[1].trim());
                int m = Integer.parseInt(keyValuePairs[1].split("=")[1].trim());
                int a = Integer.parseInt(keyValuePairs[2].split("=")[1].trim());
                int s = Integer.parseInt(keyValuePairs[3].split("=")[1].trim());
                system.ratings.add(new Rating(x, m, a, s));

            } else {
                String[] parts = line.split("\\{");
                String name = parts[0].trim();
                var rule = new Rule(name);
                for (var conditionStr : parts[1].split(",")) {
                    if (conditionStr.contains(":")) {
                        rule.conditions.add(parseCondition(conditionStr.trim()));
                    } else {
                        rule.elseResult = conditionStr.replaceAll("}", "");
                    }
                }
                system.rules.put(rule.name, rule);
            }
        }
        return system;
    }

}
