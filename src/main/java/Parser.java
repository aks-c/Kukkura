import Grammar.Symbol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 *
 * For now, mainly a wrapper around serializing/de-serializing between JSON Files, DerivationSystem objects, and our output
 * Later on though, this will probably be a bit more developed.
 */
public class Parser {
    private static final String ENCODING = "UTF-8";
    private static final String JSON_EXTENSION = "json";

    public enum FORMAT {
        JSON,
        MINECRAFT
    }

    public static void getAllDerivationSystems(String folderName) throws FileNotFoundException {
        final File folder = new File(folderName);
        DerivationSystem mainSystem = Parser.getDerivationSystem("src/main/resources/input/playground.json");

        HashMap<String, ArrayList<Symbol>> rules = new HashMap<>();
        ArrayList<String> nonTerminals = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile() && isJSON(fileEntry)) {
                DerivationSystem ds = Parser.getDerivationSystem(fileEntry);
                rules.putAll(ds.getRules());
                nonTerminals.addAll(ds.getNonTerminals());
            }
        }


    }

    public static String getFileExtension(final File file) {
        String extension = "";
        int index = file.getName().lastIndexOf('.');
        if (index > 0)
            extension = file.getName().substring(index+1);
        return extension;
    }

    public static boolean isJSON(final File file) {
        String extension = getFileExtension(file);
        return extension.equals(JSON_EXTENSION);
    }

    /**
     * De-serializes a specifically formatted JSON File into a DerivationSystem Object usable by us.
     */
    public static DerivationSystem getDerivationSystem(String filename) throws FileNotFoundException {
        Gson gson = new Gson();
        JsonReader reader = new JsonReader(new FileReader(filename));

        return gson.fromJson(reader, DerivationSystem.class);
    }
    public static DerivationSystem getDerivationSystem(File file) throws FileNotFoundException {
        return getDerivationSystem(file.getPath());
    }

    /**
     * Serializes the result we got from the DerivationSystem into a JSON File.
     */
    private static void writeToJSON(ArrayList<Symbol> result, String filename) throws IOException {
        Writer writer = new FileWriter(filename);
        Gson gson = new GsonBuilder().setPrettyPrinting().create();
        gson.toJson(result, writer);
        writer.close();
    }

    /**
     * Serializes the result in a .mcfunction file, as a list of Minecraft-compatible commands.
     */
    private static void writeToMinecraft(ArrayList<Symbol> result, String filename) throws FileNotFoundException, UnsupportedEncodingException {
        PrintWriter writer = new PrintWriter(filename, ENCODING);
        for (Symbol symbol: result) {
            writer.println(symbol.getAsMinecraftCommand());
        }
        writer.flush();
        writer.close();
    }

    /**
     * The API of the Parser class only exposes writeResults(), and an Enum to choose the target format.
     * Then results are only written wrt said FORMAT enum.
     */
    public static void writeResults(ArrayList<Symbol> result, String filename, Parser.FORMAT format) throws IOException {
        switch (format) {
            case JSON:
                writeToJSON(result, filename);
                break;
            case MINECRAFT:
                writeToMinecraft(result, filename);
                break;
        }
    }
}
