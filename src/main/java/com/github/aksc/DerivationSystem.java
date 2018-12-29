package com.github.aksc;

import com.github.aksc.ErrorHandling.BadLanguageException;
import com.github.aksc.Grammar.Symbol;
import com.github.aksc.MetaData.*;
import com.google.gson.Gson;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

/**
 * Created by akselcakmak on 16/06/2018.
 *
 *
 * This Class handles the com.github.aksc.Main Logic of this program.
 * A com.github.aksc.DerivationSystem is composed of all the information needed to procedurally generate our intended stuff;
 * It has an axiom (ie the initial list of symbols), an alphabet of terminals and non-terminals,
 * and a set of rules to be followed to derive the sentence we have from the axiom
 * to some final result usable to generate content.
 */
public class DerivationSystem {
    /** Only used for getting the rules and the non-terminals out of the separate sub files. */
    public DerivationSystem(HashMap<String, ArrayList<Symbol>> newRules, ArrayList<String> newNonTerminals) {
        this.rules = newRules;
        this.nonTerminals = newNonTerminals;
    }

    /** Only used when building the final com.github.aksc.DerivationSystem that will actually be used. */
    public DerivationSystem(DerivationSystem finalDS, HashMap<String, ArrayList<Symbol>> newRules, ArrayList<String> newNonTerminals) {
        this.rules = newRules;
        this.nonTerminals = newNonTerminals;

        this.axiom = finalDS.getAxiom();
        this.terminals = finalDS.getTerminals();
        this.materials = finalDS.getMaterials();
        this.REF_TO_PREVIOUS_MATERIAL = finalDS.REF_TO_PREVIOUS_MATERIAL;
        this.ITERATION_LIMIT = finalDS.ITERATION_LIMIT;
        this.deltaSizes = finalDS.getDeltaSizes();
        this.deltaPositions = finalDS.getDeltaPositions();
    }

    @SerializedName("iteration_limit")
    private int ITERATION_LIMIT = 20;

    /** The axiom is the initial state of the system (i.e. it's the initial sentence we derive our result from). */
    @SerializedName("axiom")
    private ArrayList<Symbol> axiom = new ArrayList<>();

    /**
     * This specifies the production/derivation rules of the system;
     * For a given symbol in the current sentence, its rule specifies what it should be replaced by.
     * eg: A -> B means that in the sentence, A's will be replaced by B's.
     * If a symbol doesn't have an associated rule, we say it's a terminal.
     */
    @SerializedName("rules")
    private HashMap<String, ArrayList<Symbol>> rules = new HashMap<>();

    /**
     * Non-Terminals are symbols that have an associated production rule.
     * Note that the sets of Terminals and Non-Terminals are disjoint:
     * i.e. a symbol cannot be both a T and a NT.
     */
    @SerializedName("non-terminals")
    private ArrayList<String> nonTerminals = new ArrayList<>();

    /**
     * We explicitly separate our alphabet into lists of terminals and non-terminals because it makes processing easier.
     * Terminals are symbols that don't have an associated production rule.
     */
    @SerializedName("terminals")
    private ArrayList<String> terminals = new ArrayList<>();

    /**
     * This holds the current sentence produced by our system.
     * Note that from an external API point of view, there are only two states `result` seems to eventually go through:
     * Being an empty list (after initialization), and holding an actual final sentence (after calling deriveResult()).
     */
    private ArrayList<Symbol> result = new ArrayList<>();

    /**
     * A boolean used by deriveResult() so the function knows when it should stop trying to derive.
     * (i.e. so that our system knows when we got a final output, composed only of terminals).
     */
    private boolean resultContainsNT = true;

    /**
     * When using the Symbol::materialReference field, one can reference to some predefined materials.
     * But one can also reference to whatever material the parent Symbol had.
     * For that, we need to define a String that specifies when that is indeed the case.
     */
    @SerializedName("ref_to_previous_material")
    private String REF_TO_PREVIOUS_MATERIAL = new String();

    /**
     * A list of the most used material of this system.
     * Helps in making the rules much smaller and much easier to read/follow.
     */
    @SerializedName("materials")
    private HashMap<String, Material> materials = new HashMap<>();

    @SerializedName("delta_sizes")
    private HashMap<String, CoordinatesDelta> deltaSizes = new HashMap<>();

    @SerializedName("delta_positions")
    private HashMap<String, CoordinatesDelta> deltaPositions = new HashMap<>();


    public ArrayList<Symbol> getResult() {
        return result;
    }

    public ArrayList<String> getNonTerminals() {
        return nonTerminals;
    }

    public HashMap<String, ArrayList<Symbol>> getRules() {
        return rules;
    }

    public ArrayList<String> getTerminals() {
        return terminals;
    }

    public ArrayList<Symbol> getAxiom() {
        return axiom;
    }

    public HashMap<String, Material> getMaterials() {
        return materials;
    }

    public boolean getResultContainsNT() {
        return resultContainsNT;
    }

    public HashMap<String, CoordinatesDelta> getDeltaSizes() {
        return deltaSizes;
    }

    public HashMap<String, CoordinatesDelta> getDeltaPositions() {
        return deltaPositions;
    }

    public String getRefToPreviousMaterial() {
        return REF_TO_PREVIOUS_MATERIAL;
    }

    private boolean sentenceContainsNT(ArrayList<Symbol> sentence) {
        for (Symbol symbol: sentence) {
            if (nonTerminals.contains(symbol.getSymbolID()))
                return true;
        }
        return false;
    }

    /** Note that the Symbol added into the nextSentence is a Deep Copy of the Symbol intended. */
    private void addSymbol(ArrayList<Symbol> nextSentence, Symbol parentSymbol, Symbol symbolToAdd) {
        CoordinatesDelta newDeltaSize = symbolToAdd.getDeltaSizeFromRef(deltaSizes);
        CoordinatesDelta newDeltaPosition = symbolToAdd.getDeltaPositionFromRef(deltaPositions);
        Coordinates newSize = symbolToAdd.getSize().getFinalCoordinates(parentSymbol, newDeltaSize);
        newSize = Resizing.getRandomSize(newSize, symbolToAdd.getResizeCoefficients(), symbolToAdd.canBeResized());
        Coordinates newPosition = symbolToAdd.getPosition().getFinalCoordinates(parentSymbol, newDeltaPosition);
        Material newMaterial = symbolToAdd.getMaterialFromRef(materials, REF_TO_PREVIOUS_MATERIAL, parentSymbol);

        Symbol newSymbol = new Symbol(symbolToAdd, newSize, newPosition, newMaterial, newDeltaSize, newDeltaPosition);

        Gson gson = new Gson();
        Symbol copy = gson.fromJson(gson.toJson(newSymbol), Symbol.class);
        nextSentence.add(copy);
    }

    /**
     * A non-exclusive rule is one where:
     * Every RHS symbol can be chosen or not, independently.
     * This does not follow a global probability distribution: each symbol is handled one by one.
     */
    private void deriveNonExclusiveRule(ArrayList<Symbol> nextSentence, Symbol parentSymbol, ArrayList<Symbol> derivation) {
        for (Symbol symbolDerived: derivation) {
            // only process this symbol if its probability of appearing is high enough.
            if (symbolDerived.shouldBeAdded()) {
                addSymbol(nextSentence, parentSymbol, symbolDerived);
            }
        }
    }

    /**
     * An exclusive rule is one where:
     * Out of all the RHS symbols of the rule, only one, and exactly one is chosen.
     * The symbol derived are chosen following a probability distribution.
     * Note that this PD is not normalised.
     *
     * Each Symbol has a certain weight (Higher Weight => Higher Probability of being chosen).
     * All the weights are summed up, then a random number in that range is generated.
     * The weights represent the size of the area "owned" by each Symbol.
     * The generated rand then falls in the area of one of the symbols.
     * The Symbol we end up adding is the one that owns the area the rand fell into.
     */
    private void deriveExclusiveRule(ArrayList<Symbol> nextSentence, Symbol parentSymbol, ArrayList<Symbol> derivation) {
        // The thresholds represent the upper limits of each Symbols' domains.
        // Then, we can represent the ownership of the entire range with a precomputed ArrayList of all the thresholds.
        int sum = 0;
        ArrayList<Integer> thresholds = new ArrayList<>();
        for (Symbol symbolDerived: derivation) {
            sum += symbolDerived.getProbability();
            thresholds.add(sum);
        }
        int indexToDerive = 0;
        int r = new Random().nextInt(sum);
        while (thresholds.get(indexToDerive) < r) {
            indexToDerive++;
        }
        addSymbol(nextSentence, parentSymbol, derivation.get(indexToDerive));
    }

    private void deriveSingleSymbol(ArrayList<Symbol> nextSentence, Symbol parentSymbol) {
        // Symbol is Non-Terminal: Add its RHS Derivation.
        // Symbol is Terminal: Add the Symbol itself.
        if (nonTerminals.contains(parentSymbol.getSymbolID())) {
            final ArrayList<Symbol> derivation = rules.get(parentSymbol.getSymbolID());
            // we make a deep copy of each symbol we got back from the rules map.
            // for each derived symbol, we figure out what the absolute values of the deltas are,
            // then we apply them to the actual Position/Size.
            if (parentSymbol.isExclusiveDerivation()) {
                deriveExclusiveRule(nextSentence, parentSymbol, derivation);
            } else {
                deriveNonExclusiveRule(nextSentence, parentSymbol, derivation);
            }
        } else {
            nextSentence.add(parentSymbol);
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

    /** Computes the whole result, from the initial Axiom to one final list of Symbols. */
    void deriveResult() {
        int iterations = 0;
        result.addAll(axiom);
        resultContainsNT = sentenceContainsNT(result);

        while(resultContainsNT & iterations < ITERATION_LIMIT) {
            deriveSingleStep();
            resultContainsNT = sentenceContainsNT(result);
            iterations++;
        }
    }

    /**
     * Validates the DerivationSystem and its state.
     * Also checks the state of every symbol in the axiom and on the RHS of each rule.
     * Called once and exactly once, once the DerivationSystem is fully formed.
     */
    void validate() throws BadLanguageException {
        StringBuilder errorMsg = new StringBuilder();
        boolean isValid = true;

        isValid = Validation.checkAxiomSize(this, errorMsg) && isValid;

        isValid = Validation.checkDeltaSizesExistence(this, errorMsg) && isValid;

        isValid = Validation.checkDeltaPositionsExistence(this, errorMsg) && isValid;

        isValid = Validation.checkNTs(this, errorMsg) && isValid;

        // Note that all the checks above were about the DS itself,
        // and that the checks below recursively validate the symbols in the DS (and their properties).

        isValid = Validation.checkSymbolsInAxiom(this, errorMsg) && isValid;

        isValid = Validation.checkSymbolsInRules(this, errorMsg) && isValid;


        if (!isValid)
            throw new BadLanguageException(errorMsg.toString());
    }
}
