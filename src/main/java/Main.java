import org.apache.commons.cli.ParseException;

import java.io.IOException;

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

            CommandLineInput input = new CommandLineInput(args);
            input.parseInput();

            DerivationSystem ds = Parser.getFinalDerivationSystem(inputFolder, subFolder, mainInputFile);

            CommandLineOutput.printPreambule(ds);

            // main logic.
            ds.deriveResult();

            CommandLineOutput.printFinal(ds);

            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.JSON);
            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.MINECRAFT);
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }
}