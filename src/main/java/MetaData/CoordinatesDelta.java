package MetaData;

import Grammar.Symbol;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 30/06/2018.
 *
 */
public class CoordinatesDelta {
    @SerializedName("delta_x")
    private ArrayList<String> delta_x;

    @SerializedName("delta_y")
    private ArrayList<String> delta_y;

    @SerializedName("delta_z")
    private ArrayList<String> delta_z;

    public ArrayList<String> getDelta_x() {
        return delta_x;
    }

    public ArrayList<String> getDelta_y() {
        return delta_y;
    }

    public ArrayList<String> getDelta_z() {
        return delta_z;
    }

    /**
     * Applies a delta to a single field.
     */
    static public String applyDelta(String field, String delta) {
        int result = Integer.parseInt(field) + Integer.parseInt(delta);
        return String.valueOf(result);
    }

    /**
     * Deltas are applied relative to something: the value of a specific field inside the Coordinate of a symbol.
     * This function retrieves the value of the appropriate field, from the base symbol,
     * according to the String description in the delta field of this Coordinate.
     * e.g.: a value of "sx" for the current delta field means that
     * this field will add a value relative to the X size dimension of the base symbol.
     */
    static public String getDelta(String field, Symbol symbol){
        int offset = 0;
        if (hasMinus(field))
            offset = 1;

        String delta;
        switch (field.substring(offset)) {
            case "x":
                delta = symbol.getPosition().getX();
                break;
            case "y":
                delta = symbol.getPosition().getY();
                break;
            case "z":
                delta = symbol.getPosition().getZ();
                break;

            case "sx":
                delta = symbol.getSize().getX();
                break;
            case "sy":
                delta = symbol.getSize().getY();
                break;
            case "sz":
                delta = symbol.getSize().getZ();
                break;
            case "":
                delta = "0";
                break;
            default:
                delta = field.substring(offset);
                break;
        }
        if (hasMinus(field))
            return "-" + delta;
        return delta;
    }

    // Small utility used only in getDelta()
    // Handles the case when the deltas possibly hold negative values
    private static boolean hasMinus(String field) {
        return (field.charAt(0) == '-');
    }
}
