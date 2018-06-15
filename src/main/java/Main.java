import java.io.FileNotFoundException;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Main {
    public static void main (String args[]) {
        System.out.println("print");
        try {
            Parser.getDerivationSystem("src/main/resources/test.json");
        } catch (FileNotFoundException e) {
            System.out.println(e.getMessage());
        }
    }
}
