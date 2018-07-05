package MetaData;

import Grammar.Symbol;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 30/06/2018.
 *
 */
public class CoordinatesDelta {

    /**
     * Each one of these is a list of deltas to be eventually applied to their respective fields.
     * The deltas hold some value relative to the parent Symbol,
     * so that these values can be passed down as appropriate to the current Symbol.
     */
    @SerializedName("d_x")
    private ArrayList<Delta> deltaX;

    @SerializedName("d_y")
    private ArrayList<Delta> deltaY;

    @SerializedName("d_z")
    private ArrayList<Delta> deltaZ;


    public ArrayList<Delta> getDeltaX() {
        return deltaX;
    }

    public ArrayList<Delta> getDeltaY() {
        return deltaY;
    }

    public ArrayList<Delta> getDeltaZ() {
        return deltaZ;
    }
}
