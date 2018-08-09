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

            DerivationSystem ds = Parser.getFinalDerivationSystem(inputFolder, subFolder, mainInputFile);

            printPreambule(ds);

            // main logic.
            ds.deriveResult();

            printFinal(ds);

            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.JSON);
            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.MINECRAFT);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printPreambule(DerivationSystem ds) {
        System.out.println("*------------------------------------*");
        System.out.println("*        Procedural Generator        *");
        System.out.println("*------------------------------------*");
        System.out.println("* Rules in the Derivation System: " + ds.getRules().size());
        System.out.println("* Size of input:                  " + ds.getAxiom().size());
        System.out.println("* Deriving...                         ");
        System.out.println("* ... ");
        System.out.println("* ... ");
    }

    private static void printFinal(DerivationSystem ds) {
        System.out.println("* Successful Derivation.              ");
        System.out.println("* Size of output:                 " + ds.getResult().size());
        System.out.println("*------------------------------------*");
    }
}