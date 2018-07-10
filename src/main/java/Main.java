import MetaData.CoordinatesUtility;

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
            DerivationSystem ds = Parser.getDerivationSystem("src/main/resources/playground.json");
            ds.deriveResult();

            //Parser.writeResults(ds.getResult(), "src/main/resources/output.json", Parser.FORMAT.JSON);
            Parser.writeResults(ds.getResult(), "src/main/resources/commands.mcfunction", Parser.FORMAT.MINECRAFT);
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

MVP
- add support for several separate input files
- add support for referentials of more fields than just materials (d_coord, etc..)

MVP
- add support for individual iteration limit (Y/N ?)
- add support for orientation
- add support for n-repeaters

MVP
- add interface/GUI to easily modify the JSON input File to this system

MVP
- input and output from some external sources (ie not resource folder)
- accept command line arguments to do so
*/