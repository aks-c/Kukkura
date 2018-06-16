import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akselcakmak on 16/06/2018.
 *
 */
public class DerivationSystem {
    @SerializedName("axiom")
    ArrayList<String> axiom;

    @SerializedName("rules")
    HashMap<String, ArrayList<String>> rules = new HashMap<>();

    @SerializedName("alphabet")
    ArrayList<String> alphabet = new ArrayList<>();


    public void display() {
        System.out.println("axiom:    " + this.axiom);
        System.out.println("rules:    " + this.rules);
        System.out.println("alphabet: " + this.alphabet);
    }
}
