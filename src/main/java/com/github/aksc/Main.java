package com.github.aksc;

import com.github.aksc.CommandLineInput;
import org.apache.commons.cli.ParseException;


import com.github.aksc.Grammar.Symbol;
import com.github.aksc.Parser.FORMAT;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Main {
    public static void main (String args[]) throws ParseException {
        CommandLineInput cli = new CommandLineInput(args);
        cli.parseInput();

        if (cli.hasOption("help"))
            cli.printHelp();
        else
            runApp(cli);
    }

    public static void runApp(CommandLineInput cli) {
        try {
            String inputFolder = cli.getInputFolder();
            String subFolder = cli.getSubFolder();
            String mainInputFile = cli.getMainInputFile();
            String outputFolder = cli.getOutputFolder();

            DerivationSystem ds = Parser.getFinalDerivationSystem(inputFolder, subFolder, mainInputFile);

            CommandLineOutput.printPreambule(ds, cli.hasOption("terse"));

            // main logic.
            ds.deriveResult();

            CommandLineOutput.printFinal(ds, cli.hasOption("terse"));

            ArrayList<Symbol> results = ds.getResult();

            for (FORMAT format : FORMAT.values()) format.writeToOutput(results, outputFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}