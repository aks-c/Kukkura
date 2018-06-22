package MetaData;

import Grammar.Symbol;
import com.google.gson.annotations.SerializedName;

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

    @SerializedName("x")
    public String x;

    @SerializedName("y")
    String y;

    @SerializedName("z")
    String z;

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getZ() {
        return z;
    }

    public void display() {
        System.out.println("coordinates:: x=" + x + " y=" + y + " z=" +z);
    }

    /**
     * This is for when the coordinates are relative.
     * It turns the deltas from relative String information (stuff like "x", "sz", etc..) into actual absolute delta values.
     * It does so with respect to the properties (i.e. size and position) of the symbol provided.
     */
    public void actualizeDelta(Symbol symbol) {
        x = getDelta(x, symbol);
        y = getDelta(y, symbol);
        z = getDelta(z, symbol);
    }

    /**
     * This is called only once the deltas are actualized, and not String descriptions.
     * It applies the delta to the current values of this coordinate.
     * Note that calling this function twice would add the deltas twice into the coordinates.
     */
    public void applyDelta(Coordinates deltaCoordinates) {
        x = applyDelta(x, deltaCoordinates.getX());
        y = applyDelta(y, deltaCoordinates.getY());
        z = applyDelta(z, deltaCoordinates.getZ());
    }

    /**
     * Applies a delta to a single field.
     */
    private String applyDelta(String field, String delta) {
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
    private String getDelta(String field, Symbol symbol){
        String delta;
        switch (field) {
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
            default:
                delta = "0";
                break;
        }
        return delta;
    }

    //TODO: fill this in.
    public Coordinates getSecondPosition() {
        return this;
    }
}
