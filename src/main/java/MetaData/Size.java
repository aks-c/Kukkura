package MetaData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akselcakmak on 21/06/2018.
 *
 *
 * Handles the Size meta-data of our symbols.
 *
 * (Might have some duplicate logic with Position,
 * but it's better to go from duplicated => refactored,
 * than to go from single unwieldy class => multiple single-purpose classes.)
 */
public class Size {
    @SerializedName("sx")
    String sx;

    @SerializedName("sy")
    String sy;

    @SerializedName("sz")
    String sz;
}
