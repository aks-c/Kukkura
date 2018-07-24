import java.io.FileNotFoundException;
import java.io.IOException;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Main {
    public static void main (String args[]) {
        System.out.println("print");
        try {
            String inputFolder = Parser.getFileName(args, Parser.FILE_TYPE.INPUT_FOLDER);
            String subFolder = Parser.getFileName(args, Parser.FILE_TYPE.INPUT_SUBFOLDER);
            String mainInputFile = Parser.getFileName(args, Parser.FILE_TYPE.MAIN_FILE);
            String outputFolder = Parser.getFileName(args, Parser.FILE_TYPE.OUTPUT_FOLDER);

            DerivationSystem ds = Parser.getFinalDerivationSystem(inputFolder, subFolder, mainInputFile);
            ds.deriveResult();

            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.JSON);
            Parser.writeResults(ds.getResult(), outputFolder, Parser.FORMAT.MINECRAFT);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

/*
MVP 1
- parse a json file with: a grammar, a set of valid symbols, an axiom
- make the derivations
- output a valid string of derivations

MVP 2
- add support for metadata wrt to the blocks (material, etc..) and the shapes (coordinates, sizes, types (cuboids, etc..), etc..)
- the output is not a string, but some structured data (ie: a JSON file)

MVP 3
- add utilities/library that converts JSON data into valid sequences of Vanilla Minecraft commands (`/fill`, etc..)

MVP 4
- add randomization/non-determinism
- add possible limit on the number of iterations

MVP 5
- add support for random rotations
- add support for random resizing/transformations, within some range of acceptable values

MVP 6
- huge refactor
- immutable objects
- support for material referentials
- better delta expressions

MVP 7
- add support for several separate input files
- add support for referentials of more fields than just materials (d_coord, etc..)

MVP 8
- support for executable jar
- input and output from some external sources (ie not resource folder)
- accept command line arguments to do so

MVP 9
- add error handling

MVP
- add support for individual iteration limit (Y/N ?)
- add support for orientation
- add support for n-repeaters

MVP
- add interface/GUI to easily modify the JSON input File to this system
*/