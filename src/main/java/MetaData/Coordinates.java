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



    public void setFinalCoordinates(Symbol symbol, CoordinatesDelta deltaCoordinates) {
        if (deltaIsSet)
            return;
        deltaIsSet = true;
        x = setFinalValue(x, deltaCoordinates.getDelta_x(), symbol);
        y = setFinalValue(y, deltaCoordinates.getDelta_y(), symbol);
        z = setFinalValue(z, deltaCoordinates.getDelta_z(), symbol);
    }

    private String setFinalValue(String field, ArrayList<String> deltas, Symbol symbol) {
        field = CoordinatesDelta.getDelta(field, symbol);
        for (String delta : deltas) {
            delta = CoordinatesDelta.getDelta(delta, symbol);
            field = CoordinatesDelta.applyDelta(field, delta);
        }
        return field;
    }

}
