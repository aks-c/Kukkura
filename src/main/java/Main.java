import java.io.FileNotFoundException;
import java.util.ArrayList;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Main {
    public static void main (String args[]) {
        System.out.println("print");
        try {
            DerivationSystem ds = Parser.getDerivationSystem("src/main/resources/input.json");
            ds.display();
            ds.deriveResult();

            Parser.writeResults(ds.getResult(), "src/main/resources/output.json");
        } catch (java.io.IOException e) {
            System.out.println(e.getMessage());
        }
    }
}
