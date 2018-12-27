import org.apache.commons.cli.ParseException;

import java.io.IOException;

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

            CommandLineOutput.printPreambule(ds);

            // main logic.
            ds.deriveResult();

            CommandLineOutput.printFinal(ds);

            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.JSON);
            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.MINECRAFT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}