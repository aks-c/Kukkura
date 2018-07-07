package MetaData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akselcakmak on 05/07/2018.
 *
 * A Delta Object holds 2 values:
 * - A "delta" String, which represents some value relative to the parent Symbol.
 * (i.e. the Symbol that was on the RHS of the rule that created the current Symbol).
 * - A factor (can support Floating pt values). It is used to multiply the value represented by the delta field.
 */
public class Delta {

    /**
     * Represents a value relative to the Symbol that holds this Object.
     * e.g. "sx" represents the x size of the preceding symbol, "z" represents it z position, etc..
     */
    @SerializedName("d")
    private String delta;

    /**
     * Holds the value by which to multiply the delta value.
     * Supports a float value.
     * For example, say the preceding Symbol is such that x=10.
     * Consider this Delta := {"d": "x", "fac": "1.5"}.
     * When applied to the current symbol, the final delta value will then be "15".
     */
    @SerializedName("fac")
    private String factor;

    public String getDelta() {
        return delta;
    }

    public String getFactor() {
        return factor;
    }
}
