import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by akselcakmak on 16/06/2018.
 *
 */
public class DerivationSystem {
    @SerializedName("axiom")
    ArrayList<String> axiom;

    @SerializedName("rules")
    private HashMap<String, ArrayList<String>> rules = new HashMap<>();

    @SerializedName("terminals")
    private ArrayList<String> terminals = new ArrayList<>();

    @SerializedName("non-terminals")
    private ArrayList<String> nonTerminals = new ArrayList<>();

    private ArrayList<String> result = new ArrayList<>();

    private boolean resultContainsNT = true;

    private boolean sentenceContainsNT(ArrayList<String> sentence) {
        for (String symbol: sentence) {
            if (nonTerminals.contains(symbol))
                return true;
        }
        return false;
    }



    void display() {
        System.out.println("axiom:    " + this.axiom);
        System.out.println("rules:    " + this.rules);
        System.out.println("alphabet: " + this.nonTerminals + this.terminals + "\n");
    }

    private void deriveSingleStep() {
        ArrayList<String> nextSentence = new ArrayList<>();
        for (String symbol: result) {
            // Symbol is Non-Terminal: Add its RHS Derivation.
            // Symbol is Terminal: Add the Symbol itself.
            if (nonTerminals.contains(symbol)) {
                ArrayList<String> derivation = rules.get(symbol);
                nextSentence.addAll(derivation);
            } else {
                nextSentence.add(symbol);
            }
        }
        result.clear();
        result.addAll(nextSentence);
    }

    void deriveResult() {
        result.addAll(axiom);

        while(resultContainsNT) {
            deriveSingleStep();
            resultContainsNT = sentenceContainsNT(result);
        }
    }


}
