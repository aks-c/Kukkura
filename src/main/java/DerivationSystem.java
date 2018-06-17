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

    ArrayList<String> result = new ArrayList<>();

    //boolean resultContainsNT = true;

    public boolean containsNT(ArrayList<String> sentence) {
        for (String symbol: sentence) {
            if (nonTerminals.contains(symbol))
                return true;
        }
        return false;
    }



    public void display() {
        System.out.println("axiom:    " + this.axiom);
        System.out.println("rules:    " + this.rules);
        System.out.println("alphabet: " + this.nonTerminals + this.terminals + "\n");
    }

    public void deriveSingleStep() {
        /*
        * create a temp;
        * for each element in the result:
        *    take off the element (get it then delete it)
        *    take its derivation, add it to a temp;
        * at this point, there s nothing left in the result
        * now add this temp to it
        * */
        ArrayList<String> nextSentence = new ArrayList<>();
        for (String symbol: result) {
            //System.out.println("LHS: " + symbol);
            ArrayList<String> derivation = rules.get(symbol);
            nextSentence.addAll(derivation);
        }
        result.clear();
        result.addAll(nextSentence);

        System.out.println("result: " + result);
    }

    public void deriveResult() {
        System.out.println("In deriveResult()");
        System.out.println("result: " + result);

        result.addAll(axiom);

        System.out.println("result: " + result);

        deriveSingleStep();


    }


//    public ArrayList<String> deriveResult() {
//        ArrayList<String> result = deriveSingleStep(axiom);
//        while(resultContainsNT) {
//            System.out.println("result contains NT");
//            System.out.println("result in loop: " + result);
//            ArrayList<String> temp =  deriveSingleStep(result);
//        }
//
//
//        return result;
//    }

}
