import java.io.FileNotFoundException;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Main {
    public static void main (String args[]) {
        System.out.println("print");
        try {
            DerivationSystem test = Parser.getDerivationSystem("src/main/resources/test.json");
            System.out.println(test.axiom);

            Parser.writeResults(test.axiom, "src/main/resources/output.json");
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
