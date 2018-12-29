package com.github.aksc;

import com.github.aksc.ErrorHandling.BadInputException;
import com.github.aksc.ErrorHandling.BadLanguageException;
import com.github.aksc.Grammar.Symbol;
import com.github.aksc.InputOutput.CommandLineInput;
import com.github.aksc.InputOutput.CommandLineOutput;
import com.github.aksc.InputOutput.Parser;
import com.github.aksc.InputOutput.Parser.FORMAT;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Main {
    public static void main (String args[]) throws BadInputException {
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

            ds.validate();

            CommandLineOutput.printPreambule(ds, cli.hasOption("terse"));

            // main logic.
            ds.deriveResult();

            CommandLineOutput.printFinal(ds, cli.hasOption("terse"));

            ArrayList<Symbol> results = ds.getResult();

            for (FORMAT format : FORMAT.values()) format.writeToOutput(results, outputFolder);
        } catch (BadInputException | BadLanguageException e) {
            System.err.println(e.getMessage());
            System.exit(1);
        }
    }
}