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

    /**
     * These Coordinates are Strings that actually represent some int values.
     * What happens is that these values are modified according to the system's rules,
     * and to some lists of deltas to apply.
     */
    @SerializedName("x")
    private String x;

    @SerializedName("y")
    private String y;

    @SerializedName("z")
    private String z;


    // Boolean to avoid setting deltas more than once.
    @SerializedName("deltaIsSet")
    private boolean deltaIsSet = false;

    public String getX() {
        return x;
    }

    public String getY() {
        return y;
    }

    public String getZ() {
        return z;
    }

    /**
     * Some methods need to get a specific field dynamically;
     * This returns x, y, z, depending on what the AXIS passed describes.
     */
    public String getField(CoordinatesUtility.AXIS axis) {
        switch(axis) {
            case X:
                return getX();
            case Y:
                return getY();
            case Z:
                return getZ();
        }
        return "";
    }

    /**
     * This is only called once.
     * It takes all the delta fields, actualises them all,
     * and applies all of them to this Object to get some finalised Coordinate values.
     * Note that those values are finalised, and not really "final";
     * We still modify these fields with one last touch as we apply some *possible* rotations and resizing.
     * But for all intent and purposes, the result of this is pretty much final (or very close to it).
     */
    public void setFinalCoordinates(Symbol parentSymbol, CoordinatesDelta deltaCoordinates) {
        if (deltaIsSet)
            return;
        deltaIsSet = true;
        x = setFinalValue(parentSymbol, deltaCoordinates.getDeltaX(), x);
        y = setFinalValue(parentSymbol, deltaCoordinates.getDeltaY(), y);
        z = setFinalValue(parentSymbol, deltaCoordinates.getDeltaZ(), z);
    }

    /**
     * For a given field,
     * get all the values the deltas represent (i.e. map the "x" and "sy" and all to actual values),
     * multiply them by their associated factors,
     * and apply them all to the given field, one by one.
     */
    private String setFinalValue(Symbol parentSymbol, ArrayList<Delta> deltas, String field) {
        field = CoordinatesUtility.getDeltaValue(field, parentSymbol);
        for (Delta delta : deltas) {
            String deltaValue = CoordinatesUtility.getDeltaValueWithFactor(delta, parentSymbol);
            field = CoordinatesUtility.addDelta(field, deltaValue);
        }
        return field;
    }

}
