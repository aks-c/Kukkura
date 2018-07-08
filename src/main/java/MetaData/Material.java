package MetaData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akselcakmak on 06/07/2018.
 *
 * Holds the critical information needed to identify a given material, and functions to manipulate that.
 * Illustrative examples:
 * - The tuple (mainID="planks", subID="0") represents Oak Wood Planks.
 * - The tuple (mainID="planks", subID="3") represents Jungle Wood Planks.
 */
public class Material {

    /**
     * The mainID is the name of the material (i.e. "planks", "dirt", etc..).
     */
    @SerializedName("mainID")
    private String mainID;

    /**
     * The subID is used to differentiate between different planks, stones, etc..
     * It is typically an int.
     */
    @SerializedName("subID")
    private String subID;

    /**
     * Stores meta-data about the structure of the material, like whether it's hollow or not, etc..
     */
    @SerializedName("state")
    private String state;


    public String getMainID() {
        return mainID;
    }

    public String getSubID() {
        return subID;
    }

    public String getState() {
        return state;
    }
}
