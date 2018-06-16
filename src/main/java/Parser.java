import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.util.Arrays;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Parser {
    public static DerivationSystem getDerivationSystem(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));

        DerivationSystem input = gson.fromJson(reader, DerivationSystem.class);

        System.out.println("alphabet: " + input.alphabet);
        System.out.println("axiom: " + input.axiom);
        System.out.println("rules: " + input.rules);

        return input;
    }

    public static void writeResults() {

    }
}
