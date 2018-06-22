import Grammar.Symbol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 *
 * For now, mainly a wrapper around serializing/de-serializing between JSON Files, DerivationSystem objects, and our output
 * Later on though, this will probably be a bit more developed.
 */
public class Parser {
    private static final String ENCODING = "UTF-8";
    /**
     * This function de-serializes a specifically formatted JSON File into a DerivationSystem Object usable by us.
     */
    public static DerivationSystem getDerivationSystem(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));

        DerivationSystem input = gson.fromJson(reader, DerivationSystem.class);

        return input;
    }

    /**
     * This function serializes the result we got from the DerivationSystem into a JSON File.
     */
    public static void writeToJSON(ArrayList<Symbol> result, String filename) throws IOException {
        Writer writer = new FileWriter(filename);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(result, writer);
        writer.close();
    }

    /**
     * Serializes the result in a .mcfunction file, as a list of Minecraft-compatible commands.
     */
    public static void writeToMinecraft(ArrayList<Symbol> result, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, ENCODING);
        for (Symbol symbol: result) {
            writer.println(symbol.getAsMinecraftCommand());
        }
        writer.close();
    }
}
