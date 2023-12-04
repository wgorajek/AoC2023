package com.wgorajek;

import java.awt.*;
import java.sql.Array;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.lang.Math.pow;

public class Day04 extends Solution {
    @Override
    public Object getPart1Solution() {
        final ArrayList<Card> cards = getInput();
        int totalValue = 0;
        for (var card : cards) {
            int matchingCards = 0;
            for (Integer number : card.givenNumbers) {
                if (card.winningNumbers.contains(number)) {
                    matchingCards++;
                }
            }
            totalValue += pow(2, matchingCards - 1);
        }
        return totalValue;

    }

    @Override
    public Object getPart2Solution() {

        final ArrayList<Card> cards = getInput();
        int totalValue = 0;
        for (var card : cards) {
            int matchingCards = 0;
            for (Integer number : card.givenNumbers) {
                if (card.winningNumbers.contains(number)) {
                    matchingCards++;
                }
            }
            for (var i = 0; i < matchingCards; i++) {
                cards.get(i + card.cardNumber).numberOfCards += card.numberOfCards;
            }

        }

        for (var card : cards) {
            totalValue += card.numberOfCards;
        }
        return totalValue;

    }

    public static class Card {
        ArrayList<Integer> winningNumbers;
        ArrayList<Integer> givenNumbers;
        int cardNumber;
        int numberOfCards = 1;

        public Card(int cardNumber, ArrayList<Integer> winningNumbers, ArrayList<Integer> givenNumbers) {
            this.winningNumbers = winningNumbers;
            this.givenNumbers = givenNumbers;
            this.cardNumber = cardNumber;
        }
    }

    private ArrayList<Card> getInput() {
        final List<String> input = getInputLines();
        ArrayList<Card> cards = new ArrayList<Card>();
        for (String line : input) {
            var splitResult = line.split(":");
            var firstPart = splitResult[0];

            Pattern pattern = Pattern.compile("\\d+");
            Matcher matcher = pattern.matcher(splitResult[0]);
            var cardNumber = 0;
            ArrayList<Integer> winningNumbers = new ArrayList<Integer>();
            ArrayList<Integer> givenNumbers = new ArrayList<Integer>();
            if (matcher.find()) {
                cardNumber = Integer.parseInt(matcher.group(0));
            }
            var secondPart = splitResult[1];
            splitResult = secondPart.split("\\|");

            matcher = pattern.matcher(splitResult[0]);
            while (matcher.find()) {
                winningNumbers.add(Integer.parseInt(matcher.group()));
            }

            matcher = pattern.matcher(splitResult[1]);
            while (matcher.find()) {
                givenNumbers.add(Integer.parseInt(matcher.group()));
            }

            cards.add(new Card(cardNumber, winningNumbers, givenNumbers));
        }
        return cards;
    }
}
