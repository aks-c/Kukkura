package MetaData;

import Grammar.Symbol;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;

/**
 * Created by akselcakmak on 30/06/2018.
 *
 */
public class CoordinatesDelta {
    @SerializedName("delta_x")
    private ArrayList<Delta> delta_x;

    @SerializedName("delta_y")
    private ArrayList<Delta> delta_y;

    @SerializedName("delta_z")
    private ArrayList<Delta> delta_z;

    public ArrayList<Delta> getDelta_x() {
        return delta_x;
    }

    public ArrayList<Delta> getDelta_y() {
        return delta_y;
    }

    public ArrayList<Delta> getDelta_z() {
        return delta_z;
    }
}
