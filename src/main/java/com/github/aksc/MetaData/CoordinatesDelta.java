package com.github.aksc.MetaData;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by akselcakmak on 30/06/2018.
 *
 */
public class CoordinatesDelta {
    public CoordinatesDelta(ArrayList<Delta> deltaX, ArrayList<Delta> deltaY, ArrayList<Delta> deltaZ, String deltaReference) {
        this.deltaX = deltaX;
        this.deltaY = deltaY;
        this.deltaZ = deltaZ;
        this.deltaReference = deltaReference;
    }

    public CoordinatesDelta(CoordinatesDelta other){
        this(other.getDeltaX(), other.getDeltaY(), other.getDeltaZ(), other.getDeltaReference());
    }

    /**
     * A deltaPosition either just contains a reference to some globally defined deltaPosition, or it is defined explicitly.
     * This method resolves the actual deltaPosition to be used.
     * (in other words, if this is a reference, the deltaPos it points to will be fetched and returned).
     */
    public CoordinatesDelta fromRef(HashMap<String, CoordinatesDelta> globalDeltas) {
        if (deltaReference == null || deltaReference.equals(""))
            return this;
        else
            return globalDeltas.get(deltaReference);
    }

    /**
     * A DeltaCoordinate is either explicitly defined (which is what we check here) or it is defined by the deltaReference string.
     */
    public boolean isDeltaDefined() { return deltaX != null && deltaY != null && deltaZ != null; }

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

    @SerializedName("ref")
    private final String deltaReference;

    public String getDeltaReference() { return deltaReference; }

    public ArrayList<Delta> getDeltaX() { return deltaX; }

    public ArrayList<Delta> getDeltaY() { return deltaY; }

    public ArrayList<Delta> getDeltaZ() { return deltaZ; }
}
