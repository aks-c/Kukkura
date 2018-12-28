package com.github.aksc.InputOutput;

import com.github.aksc.DerivationSystem;

/**
 * Created by akselcakmak on 17/08/2018.
 *
 * Mostly for aesthetic purposes.
 * Just a pretty print of what the app is doing, for the user.
 * TBH, you could get a similar result using .format() stuff, but this was rly for fun :p
 */
public class CommandLineOutput {
    private static final int LINE_LENGTH = 40;
    private static final String HORIZONTAL_SEPARATOR = "-";
    private static final String VERTICAL_SEPARATOR = "*";
    private static final String PADDING = " ";
    private static final int PADDING_LENGTH = 1;


    public static void printPreambule(DerivationSystem ds, boolean isTerse) {
        if (!isTerse) {
            printFullHorizontalSeparation();
            printLine("Procedural Generator");
            printFullHorizontalSeparation();
            printLine("Rules in the Derivation System: " + ds.getRules().size());
            printLine("Size of initial axiom: " + ds.getAxiom().size());
            printLine("Deriving...");
            printLine("...");
            printLine("...");
        } else {
            System.out.println(ds.getRules().size() + " rule(s).");
        }
    }

    public static void printFinal(DerivationSystem ds, boolean isTerse) {
        if (!isTerse) {
            printLine("Successful Derivation!");
            printLine("Size of output: " + +ds.getResult().size());
            printFullHorizontalSeparation();
        } else {
            System.out.println("Created " + ds.getResult().size() + " symbol(s).");
        }
    }

    private static void printMultipleCharacters(String charToPrint, int numberOfChars) {
        for (int i = 0; i < numberOfChars; i++) {
            System.out.print(charToPrint);
        }
    }

    private static void printHorizontalSeparation() {
        printMultipleCharacters(HORIZONTAL_SEPARATOR, LINE_LENGTH - 2);
    }

    private static void printVerticalSeparation() {
        System.out.print(VERTICAL_SEPARATOR);
    }

    private static void printPadding() {
        printMultipleCharacters(PADDING, PADDING_LENGTH);
    }

    private static void printFullHorizontalSeparation() {
        printVerticalSeparation();
        printHorizontalSeparation();
        printVerticalSeparation();
        System.out.println();
    }

    private static void printLine(String content) {
        int contentLength = content.length();

        printVerticalSeparation();
        printPadding();
        System.out.print(content);
        printMultipleCharacters(PADDING, LINE_LENGTH - contentLength - 2 - PADDING_LENGTH);
        printVerticalSeparation();
        System.out.println();
    }

}
