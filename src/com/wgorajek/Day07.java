package com.wgorajek;

import java.awt.*;
import java.sql.Array;
import java.util.*;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Day07 extends Solution {
    @Override
    public Object getPart1Solution() {
        ArrayList<Hand> hands = getInput();
        int total = 0;
        for (var i = 0; i <= hands.size() - 2; i++) {
            var hand1 = hands.get(i);
            for (var j = i + 1; j <= hands.size() - 1; j++) {
                var hand2 = hands.get(j);
                if (hand1.cards.equals(hand2.cards)) {
                    total++;
                }
                if (hand1.isGreaterThan(hand2)) {
                    hand1.rank++;
                } else {
                    hand2.rank++;
                }
            }
        }
        for (var hand : hands) {
            total += hand.rank * hand.bid;
        }

        return total;
    }

    @Override
    public Object getPart2Solution() {
        ArrayList<Hand> hands = getInput();
        int total = 0;
        for (var i = 0; i <= hands.size() - 2; i++) {
            var hand1 = hands.get(i);
            for (var j = i + 1; j <= hands.size() - 1; j++) {
                var hand2 = hands.get(j);
                if (hand1.cards.equals(hand2.cards)) {
                    total++;
                }
                if (hand1.isGreaterThanPartB(hand2)) {
                    hand1.rank++;
                } else {
                    hand2.rank++;
                }
            }
        }
        for (var hand : hands) {
            total += hand.rank * hand.bid;
        }

        return total;
    }

    public class Hand {
        String cards;
        int bid;
        int rank = 1;

        public Hand(String cards, int bid) {
            this.cards = cards;
            this.bid = bid;
        }

        public boolean isGreaterThan(Hand otherHand) {
            if (trickStrength() > otherHand.trickStrength()) {
                return true;
            } else if (trickStrength() == otherHand.trickStrength()) {
                for (var i = 0; i <= cards.length() - 1; i++) {
                    var card1Value = cardValue(cards.substring(i, i + 1), false);
                    var card2Value = cardValue(otherHand.cards.substring(i, i + 1), false);
                    if (card1Value > card2Value) {
                        return true;
                    } else if (card1Value < card2Value) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return false;
        }

        public boolean isGreaterThanPartB(Hand otherHand) {
            if (trickStrengthPartB() > otherHand.trickStrengthPartB()) {
                return true;
            } else if (trickStrengthPartB() == otherHand.trickStrengthPartB()) {
                for (var i = 0; i <= cards.length() - 1; i++) {
                    var card1Value = cardValue(cards.substring(i, i + 1), true);
                    var card2Value = cardValue(otherHand.cards.substring(i, i + 1), true);
                    if (card1Value > card2Value) {
                        return true;
                    } else if (card1Value < card2Value) {
                        return false;
                    }
                }
            } else {
                return false;
            }
            return false;
        }

        public int cardValue(String card, boolean useJokers) {
            if (card.equals("A")) {
                return 14;
            } else if (card.equals("K")) {
                return 13;
            } else if (card.equals("Q")) {
                return 12;
            } else if (card.equals("J")) {
                if (useJokers) {
                    return 1;
                } else {
                    return 11;
                }
            } else if (card.equals("T")) {
                return 10;
            } else {
                return Integer.parseInt(card);
            }
        }

        public int trickStrengthPartB() {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            int numberOfJokers = 0;
            for (var i = 0; i <= 4; i++) {
                if (String.valueOf(cards.charAt(i)).equals("J")) {
                    numberOfJokers++;
                } else {
                    map.put(String.valueOf(cards.charAt(i)), 1 + map.getOrDefault(String.valueOf(cards.charAt(i)), 0));
                }
            }

            if (map.values().contains(5 - numberOfJokers) || numberOfJokers == 5) {
                return 100;
            } else if (map.values().contains(4 - numberOfJokers)) {
                return 80;
            } else if ((map.values().contains(3) && map.values().contains(2))
                    || (map.size() == 2 && numberOfJokers == 1)) {
                return 60;
            } else if (map.values().contains(3 - numberOfJokers)) {
                return 40;
            } else if (map.values().contains(2) && map.size() == 3) {
                return 20;
            } else if (map.values().contains(2 - numberOfJokers)) {
                return 10;
            }
            return 5;
        }

        public int trickStrength() {
            HashMap<String, Integer> map = new HashMap<String, Integer>();
            for (var i = 0; i <= 4; i++) {
                map.put(String.valueOf(cards.charAt(i)), 1 + map.getOrDefault(String.valueOf(cards.charAt(i)), 0));
            }

            if (map.values().contains(5)) {
                return 100;
            } else if (map.values().contains(4)) {
                return 80;
            } else if (map.values().contains(3) && map.values().contains(2)) {
                return 60;
            } else if (map.values().contains(3)) {
                return 40;
            } else if (map.values().contains(2) && map.size() == 3) {
                return 20;
            } else if (map.values().contains(2)) {
                return 10;
            }
            return 5;
        }
    }


    private ArrayList<Hand> getInput() {
        final List<String> input = getInputLines();
        ArrayList<Hand> hands = new ArrayList<Hand>();
        for (var line : input) {
            hands.add(new Hand(line.substring(0, 5), Integer.parseInt(line.substring(6))));
        }
        return hands;
    }
}
