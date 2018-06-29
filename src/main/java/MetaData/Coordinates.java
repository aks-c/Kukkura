package MetaData;

import Grammar.Symbol;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 21/06/2018.
 *
 * Wrapper around a triplet of Strings, representing coordinates in the Voxel world.
 *
 * In the input file, there are some derivation rules.
 * Some of these rules specify how some of the metadata should change, relative to the producing LHS symbol.
 * As a result, this Class supports references relative to some specific values of said LHS symbol.
 * This is why the fields are Strings, and not numbers, like say, ints.
 */
public class Coordinates {

    public Coordinates(String x, String y, String z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }

    @SerializedName("x")
    private String x;

    @SerializedName("y")
    private String y;

    @SerializedName("z")
    private String z;

    @SerializedName("delta_x")
    private ArrayList<String> delta_x;

    @SerializedName("delta_y")
    private ArrayList<String> delta_y;

    @SerializedName("delta_z")
    private ArrayList<String> delta_z;

    // Boolean to avoid setting deltas more than once.
    @SerializedName("deltaIsSet")
    boolean deltaIsSet = false;

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getZ() {
        return z;
    }

    public ArrayList<String> getDelta_x() {
        return delta_x;
    }

    public ArrayList<String> getDelta_y() {
        return delta_y;
    }

    public ArrayList<String> getDelta_z() {
        return delta_z;
    }


    public void setFinalCoordinates(Symbol symbol, Coordinates deltaCoordinates) {
        if (deltaIsSet)
            return;
        deltaIsSet = true;
        x = setFinalValue(x, deltaCoordinates.getDelta_x(), symbol);
        y = setFinalValue(y, deltaCoordinates.getDelta_y(), symbol);
        z = setFinalValue(z, deltaCoordinates.getDelta_z(), symbol);
    }

    private String setFinalValue(String field, ArrayList<String> deltas, Symbol symbol) {
        field = Coordinates.getDelta(field, symbol);
        for (String delta : deltas) {
            delta = Coordinates.getDelta(delta, symbol);
            field = Coordinates.applyDelta(field, delta);
        }
        return field;
    }

    /**
     * Applies a delta to a single field.
     */
    static public String applyDelta(String field, String delta) {
        int result = Integer.parseInt(field) + Integer.parseInt(delta);
        return String.valueOf(result);
    }

    static public String applyMinus(String field) {
        int result = Integer.parseInt(field) * -1;
        return String.valueOf(result);
    }

    /**
     * Deltas are applied relative to something: the value of a specific field inside the Coordinate of a symbol.
     * This function retrieves the value of the appropriate field, from the base symbol,
     * according to the String description in the delta field of this Coordinate.
     * e.g.: a value of "sx" for the current delta field means that
     * this field will add a value relative to the X size dimension of the base symbol.
     */
    static private String getDelta(String field, Symbol symbol){
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
