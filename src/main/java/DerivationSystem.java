import Grammar.Symbol;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akselcakmak on 16/06/2018.
 *
 *
 * This Class handles the Main Logic of this program.
 * A DerivationSystem is composed of all the information needed to procedurally generate our intended stuff;
 * It is composed of an axiom (ie the initial list of symbols), an alphabet of terminals and non-terminals,
 * and a set of rules to be followed to derive the sentence we have from the axiom
 * to some final result usable to generate content.
 */
public class DerivationSystem {
    // The axiom is the initial state of the system (i.e. it's the initial sentence we derive our result from);
    // By changing it, you change the output of the derivation system.
    @SerializedName("axiom")
    ArrayList<Symbol> axiom;

    // This specifies the derivation rules of the system;
    // For a given symbol in the current sentence, its rule specifies what it should be replaced by.
    // If a symbol doesn't have an associated rule, it means it's a terminal.
    @SerializedName("rules")
    private HashMap<String, ArrayList<Symbol>> rules = new HashMap<>();

    // We explicitly separate our alphabet into lists of terminals and non-terminals because it makes processing easier.
    // Terminals are symbols that don't have an associated production rule.
    @SerializedName("terminals")
    private ArrayList<String> terminals = new ArrayList<>();

    // Non-Terminals are symbols that have an associated production rule.
    @SerializedName("non-terminals")
    private ArrayList<String> nonTerminals = new ArrayList<>();

    // This holds the current sentence derived by our system.
    // Note that from an external API point of view, there are only two states `result` eventually goes through:
    // Being an empty list (after initialization), and holding an actual derivation (after calling deriveResult()).
    private ArrayList<Symbol> result = new ArrayList<>();

    // A boolean used by deriveResult() so the function knows when it should stop trying to derive
    // (i.e. so that our system knows when we got a final output, composed only of terminals).
    private boolean resultContainsNT = true;

    ArrayList<Symbol> getResult() {
        return result;
    }


    private boolean sentenceContainsNT(ArrayList<Symbol> sentence) {
        for (Symbol symbol: sentence) {
            if (nonTerminals.contains(symbol.getSymbol()))
                return true;
        }
        return false;
    }



    /**
     * This function is used to create a single derivation of the current result.
     */
    private void deriveSingleStep() {
        // This is a temporary List, in which we add all the RHS productions of each symbol in the current result.
        ArrayList<Symbol> nextSentence = new ArrayList<>();
        for (Symbol symbol: result) {
            // Symbol is Non-Terminal: Add its RHS Derivation.
            // Symbol is Terminal: Add the Symbol itself.
            if (nonTerminals.contains(symbol.getSymbol())) {
                ArrayList<Symbol> derivation = rules.get(symbol.getSymbol());
                nextSentence.addAll(derivation);
            } else {
                nextSentence.add(symbol);
            }
        }
        // The symbols in `result` should be replaced by their derivation;
        // We clear it to avoid an infinite loop.
        result.clear();
        result.addAll(nextSentence);
    }

    /**
     * This function derives the whole result, from the axiom to one final list of Symbols.
     * It iteratively derives it, step by step, until the result contains only terminals.
     */
    void deriveResult() {
        result.addAll(axiom);

        while(resultContainsNT) {
            deriveSingleStep();
            resultContainsNT = sentenceContainsNT(result);
        }
    }


}
