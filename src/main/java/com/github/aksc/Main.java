package com.github.aksc;

import com.github.aksc.Grammar.Symbol;
import com.github.aksc.Parser.FORMAT;
import java.io.IOException;
import java.util.ArrayList;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Main {
    public static void main (String args[]) {
        try {
            String inputFolder = Parser.getFileName(args, Parser.FILE_TYPE.INPUT_FOLDER);
            String subFolder = Parser.getFileName(args, Parser.FILE_TYPE.INPUT_SUBFOLDER);
            String mainInputFile = Parser.getFileName(args, Parser.FILE_TYPE.MAIN_FILE);
            String outputFolder = Parser.getFileName(args, Parser.FILE_TYPE.OUTPUT_FOLDER);

            DerivationSystem ds = Parser.getFinalDerivationSystem(inputFolder, subFolder, mainInputFile);

            CommandLineOutput.printPreambule(ds);

            // main logic.
            ds.deriveResult();

            CommandLineOutput.printFinal(ds);

            ArrayList<Symbol> results = ds.getResult();

            for (FORMAT format : FORMAT.values()) format.writeToOutput(results, outputFolder);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}