package MetaData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 30/06/2018.
 *
 */
public class CoordinatesDelta {
    public CoordinatesDelta(ArrayList<Delta> deltaX, ArrayList<Delta> deltaY, ArrayList<Delta> deltaZ) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
    }

    public CoordinatesDelta(CoordinatesDelta other){
        this.deltaX = other.getDeltaX();
        this.deltaY = other.getDeltaX();
        this.deltaZ = other.getDeltaX();
    }

    /**
     * Each one of these is a list of deltas to be eventually applied to their respective fields.
     * The deltas hold some value relative to the parent Symbol,
     * so that these values can be passed down as appropriate to the current Symbol.
     */
    @SerializedName("d_x")
    private final ArrayList<Delta> deltaX;

    @SerializedName("d_y")
    private final ArrayList<Delta> deltaY;

    @SerializedName("d_z")
    private final ArrayList<Delta> deltaZ;


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
