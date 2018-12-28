package com.github.aksc;

import com.github.aksc.Exceptions.BadInputException;
import com.github.aksc.Grammar.Symbol;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akselcakmak on 15/06/2018.
 *
 *
 */
public class Parser {
    private static final String ENCODING = "UTF-8";
    private static final String JSON_EXTENSION = ".json";
    private static final String MC_EXTENSION = ".mcfunction";

    private static final String DEFAULT_OUTPUT_FILE = "output";

    public enum FORMAT {
        JSON {
            @Override
            public void writeToOutput(ArrayList<Symbol> result, String outputFolder) throws BadInputException {
                String filename = outputFolder + DEFAULT_OUTPUT_FILE + JSON_EXTENSION;
                try(Writer writer = new FileWriter(filename);) {
                    Gson gson = new GsonBuilder().setPrettyPrinting().create();
                    gson.toJson(result, writer);
                    //writer.close();
                } catch(IOException e) {
                    throw new BadInputException("Error while writing output: " + e.getMessage());
                }
            }
        },

        MINECRAFT {
            @Override
            public void writeToOutput(ArrayList<Symbol> result, String outputFolder) throws BadInputException {
                String filename = outputFolder + DEFAULT_OUTPUT_FILE + MC_EXTENSION;
                try(PrintWriter writer = new PrintWriter(filename, ENCODING)) {
                    for (Symbol symbol: result) {
                        writer.println(symbol.getAsMinecraftCommand());
                    }
                    //writer.flush();
                    //writer.close();
                } catch(IOException e) {
                    throw new BadInputException("Error while writing output: " + e.getMessage());
                }
             }
        };

        public abstract void writeToOutput(ArrayList<Symbol> result, String outputFolder) throws BadInputException;
    }

    public static DerivationSystem getFinalDerivationSystem(String folderName, String subFolderName, String fileName) throws BadInputException {
        DerivationSystem finalDS = getDerivationSystem(folderName + fileName);
        DerivationSystem auxiliaryDS = getAllDerivationsInFolder(folderName + subFolderName);

        HashMap<String, ArrayList<Symbol>> newRules = new HashMap<>();
        newRules.putAll(finalDS.getRules());
        newRules.putAll(auxiliaryDS.getRules());
        ArrayList<String> newNonTerminals = new ArrayList<>();
        newNonTerminals.addAll(finalDS.getNonTerminals());
        newNonTerminals.addAll(auxiliaryDS.getNonTerminals());

        return new DerivationSystem(finalDS, newRules, newNonTerminals);
    }

    private static DerivationSystem getAllDerivationsInFolder(String folderName) throws BadInputException {
        final File folder = new File(folderName);

        HashMap<String, ArrayList<Symbol>> rules = new HashMap<>();
        ArrayList<String> nonTerminals = new ArrayList<>();

        for (final File fileEntry : folder.listFiles()) {
            if (fileEntry.isFile() && isJSON(fileEntry)) {
                DerivationSystem ds = Parser.getDerivationSystem(fileEntry);
                rules.putAll(ds.getRules());
                nonTerminals.addAll(ds.getNonTerminals());
            }
        }
        return new DerivationSystem(rules, nonTerminals);
    }

    private static String getFileExtension(final File file) {
        String extension = "";
        int index = file.getName().lastIndexOf('.');
        if (index > 0)
            extension = file.getName().substring(index);
        return extension;
    }

    private static boolean isJSON(final File file) {
        String extension = getFileExtension(file);
        return extension.equals(JSON_EXTENSION);
    }

    /**
     * De-serializes a structured JSON File into a usable com.github.aksc.DerivationSystem Object.
     */
    private static DerivationSystem getDerivationSystem(String filename) throws BadInputException {
        Gson gson = new Gson();

        JsonReader reader = null;
        DerivationSystem ds = null;
        try {
            reader = new JsonReader(new FileReader(filename));
            ds = gson.fromJson(reader, DerivationSystem.class);
        } catch (FileNotFoundException e) {
            throw new  BadInputException("The file: " + filename + " doesn't exist.");
        } catch (JsonSyntaxException e) {
            throw new BadInputException("Invalid JSON syntax: " + e.getMessage());
        }

        return ds;
    }
    private static DerivationSystem getDerivationSystem(File file) throws BadInputException {
        return getDerivationSystem(file.getPath());
    }
}
