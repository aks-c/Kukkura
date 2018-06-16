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
    HashMap<String, ArrayList<String>> rules = new HashMap<>();

    @SerializedName("terminals")
    ArrayList<String> terminals = new ArrayList<>();

    @SerializedName("non-terminals")
    ArrayList<String> nonTerminals = new ArrayList<>();



    public void display() {
        System.out.println("axiom:    " + this.axiom);
        System.out.println("rules:    " + this.rules);
        System.out.println("alphabet: " + this.nonTerminals + this.terminals);
    }

    public ArrayList<String> deriveSingleStep(ArrayList<String> currentSentence) {
        ArrayList<String> nextSentence = new ArrayList<>();
        for (String symbol: currentSentence) {
            System.out.println("the LHS is: " + symbol);
            ArrayList<String> derivation = rules.get(symbol);
            System.out.println("the RHS is: " + derivation);
            nextSentence.addAll(derivation);
        }
        System.out.println(nextSentence);
        return nextSentence;
    }

    public ArrayList<String> deriveResult() {
        ArrayList<String> result = deriveSingleStep(axiom);
        return result;
    }

}
