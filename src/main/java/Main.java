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

            Parser.writeResults(ds.getResult(), "src/main/resources/output.json");
        } catch (Exception e) {
            System.out.println(e.getMessage());
            System.out.println(e.toString());
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
- Add support for priority levels of derivations (eg: Muller et al, or that other one)
- Add support for higher levels of details (ie: until now, we could only safely generate valid Mass Models, but with this we'd also have valid façades and maybe valid details on top of that..)
*/
