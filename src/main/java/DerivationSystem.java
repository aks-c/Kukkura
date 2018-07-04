import Grammar.Symbol;
import MetaData.CoordinatesUtility;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

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
    private ArrayList<Symbol> axiom;

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

    private int ITERATION_LIMIT = 20;

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
     * Note that the Symbol added into the nextSentence is a Deep Copy of the Symbol intended.
     */
    private void addSymbol(ArrayList<Symbol> nextSentence, Symbol symbol, Symbol result) {
        Gson gson = new Gson();
        Symbol copy = gson.fromJson(gson.toJson(result), Symbol.class);
        copy.getSize().setFinalCoordinates(symbol, result.getDeltaSize());
        copy.getPosition().setFinalCoordinates(symbol, result.getDeltaPosition());
        copy.applyRandomResize();
        copy.applyRotationX(symbol, CoordinatesUtility.ROTATION.LEFT);
        nextSentence.add(copy);
    }

    /**
     * A non-exclusive rule is one where:
     * Every RHS symbol can be chosen or not, independently.
     * This does not follow any probability distribution: each symbol is handled, one by one.
     */
    private void deriveNonExclusiveRule(ArrayList<Symbol> nextSentence, Symbol symbol, ArrayList<Symbol> derivation) {
        for (Symbol result: derivation) {
            // only process this result if its probability of appearing is high enough.
            if (result.shouldBeAdded()) {
                addSymbol(nextSentence, symbol, result);
            }
        }
    }

    /**
     * An exclusive rule is one where:
     * Out of all the RHS symbols of the rule, only one, and exactly one is chosen.
     * The symbol derived are chosen following a probability distribution.
     * Note that this PD is not normalised.
     */
    private void deriveExclusiveRule(ArrayList<Symbol> nextSentence, Symbol symbol, ArrayList<Symbol> derivation) {
        // Each Symbol has a certain weight (Higher Weight => Higher Probability of being chosen).
        // All the weights are summed up, then a random in that range is generated.
        // The rand falls in the area of one of the symbols.
        // That symbol is the one chosen for the derivation.
        int sum = 0;
        ArrayList<Integer> thresholds = new ArrayList<>();
        for (Symbol result: derivation) {
            sum += result.getProbability();
            thresholds.add(sum);
        }
        int indexToDerive = 0;
        int r = new Random().nextInt(sum);
        while (thresholds.get(indexToDerive) < r) {
            indexToDerive++;
        }
        addSymbol(nextSentence, symbol, derivation.get(indexToDerive));
    }

    private void deriveSingleSymbol(ArrayList<Symbol> nextSentence, Symbol symbol) {
        // Symbol is Non-Terminal: Add its RHS Derivation.
        // Symbol is Terminal: Add the Symbol itself.
        if (nonTerminals.contains(symbol.getSymbol())) {
            final ArrayList<Symbol> derivation = rules.get(symbol.getSymbol());
            // we make a deep copy of each symbol we got back from the rules map.
            // for each derived symbol, we figure out what the absolute values of the deltas are,
            // then we apply them to the actual Position/Size.
            if (symbol.isExclusiveDerivation()) {
                deriveExclusiveRule(nextSentence, symbol, derivation);
            } else {
                deriveNonExclusiveRule(nextSentence, symbol, derivation);
            }
        } else {
            nextSentence.add(symbol);
        }
    }

    /**
     * Single iteration of the Derivation System.
     * Note an important point: an single iteration is not synonymous with a single derivation;
     * An iteration is intuitively defined as: a single step where all current non-terminals in the system derive once.
     * Then, most of the time, several derivations happen in a single iteration.
     */
    private void deriveSingleStep() {
        // This is a temporary List, in which we add all the RHS productions of each symbol in the current result.
        ArrayList<Symbol> nextSentence = new ArrayList<>();
        for (Symbol symbol: result) {
            deriveSingleSymbol(nextSentence, symbol);
        }
        // When a Symbol's rule is applied (and appropriate symbols are derived), the original Symbol should be deleted.
        // That is so as to avoid infinite loops (i.e. we avoid deriving the same symbol over and over again).
        // Only when the result is clean do we add the next sentence with the new symbols.
        result.clear();
        result.addAll(nextSentence);
    }

    /**
     * Computes the whole result, from the initial Axiom to one final list of Symbols.
     */
    void deriveResult() {
        int iterations = 0;
        result.addAll(axiom);

        while(resultContainsNT & iterations < ITERATION_LIMIT) {
            deriveSingleStep();
            resultContainsNT = sentenceContainsNT(result);
            iterations++;
        }
    }
}
