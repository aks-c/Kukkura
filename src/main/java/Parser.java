import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;

import java.io.FileNotFoundException;
import java.io.FileReader;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 */
public class Parser {
    public static void getDerivationSystem(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));

        DerivationSystem input = gson.fromJson(reader, DerivationSystem.class);

        System.out.println(input.alphabet);
        System.out.println(input.axiom);
        System.out.println(input.rules);
    }

    public static void writeResults() {

    }
}
