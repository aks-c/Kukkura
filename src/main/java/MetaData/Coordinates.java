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

    // Some methods need to get a specific field dynamically;
    // Returns x, y, z, depending on what the AXIS passed describes.
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

    public void setField(CoordinatesUtility.AXIS axis, String value) {
        switch(axis) {
            case X:
                setX(value);
                break;
            case Y:
                setY(value);
                break;
            case Z:
                setZ(value);
                break;
        }
    }

    public void setX(String x) {
        this.x = x;
    }

    public void setY(String y) {
        this.y = y;
    }

    public void setZ(String z) {
        this.z = z;
    }


    // Utility that swaps the values of two given axes.
    // For example, it is heavily used by the Rotation functions.
    public void swap(CoordinatesUtility.AXIS firstAxis, CoordinatesUtility.AXIS secondAxis) {
        String temp = getField(firstAxis);
        setField(firstAxis, getField(secondAxis));
        setField(secondAxis, temp);
    }

    public void setFinalCoordinates(Symbol symbol, CoordinatesDelta deltaCoordinates) {
        if (deltaIsSet)
            return;
        deltaIsSet = true;
        x = setFinalValue(x, deltaCoordinates.getDelta_x(), symbol);
        y = setFinalValue(y, deltaCoordinates.getDelta_y(), symbol);
        z = setFinalValue(z, deltaCoordinates.getDelta_z(), symbol);
    }

    private String setFinalValue(String field, ArrayList<String> deltas, Symbol symbol) {
        field = CoordinatesUtility.getDelta(field, symbol);
        for (String delta : deltas) {
            delta = CoordinatesUtility.getDelta(delta, symbol);
            field = CoordinatesUtility.applyDelta(field, delta);
        }
        return field;
    }

}
