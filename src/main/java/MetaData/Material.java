package MetaData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akselcakmak on 06/07/2018.
 *
 * Holds the critical information needed to identify a given material, and functions to manipulate that.
 */
public class Material {
    // Example representations:
    // eg: the tuple (mainID="planks", subID="0") represents Oak Wood Planks.
    // eg: the tuple (mainID="planks", subID="3") represents Jungle Wood Planks.

    // The mainID is the name of the material (i.e. "planks", "dirt", etc..).
    @SerializedName("mainID")
    String mainID;

    // The subID is used to differentiate between different planks, stones, etc..
    // It is typically an int.
    @SerializedName("subID")
    String subID;
}
