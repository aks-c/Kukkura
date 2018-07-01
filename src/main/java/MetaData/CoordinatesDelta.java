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
    private ArrayList<String> delta_x;

    @SerializedName("delta_y")
    private ArrayList<String> delta_y;

    @SerializedName("delta_z")
    private ArrayList<String> delta_z;

    public ArrayList<String> getDelta_x() {
        return delta_x;
    }

    public ArrayList<String> getDelta_y() {
        return delta_y;
    }

    public ArrayList<String> getDelta_z() {
        return delta_z;
    }
}
