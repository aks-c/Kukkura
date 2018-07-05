package MetaData;

import com.google.gson.annotations.SerializedName;

/**
 * Created by akselcakmak on 05/07/2018.
 *
 */
public class Delta {

    @SerializedName("d")
    private String delta;

    @SerializedName("fac")
    private String factor;

    public String getDelta() {
        return delta;
    }

    public String getFactor() {
        return factor;
    }

    public void setDelta(String delta) {
        this.delta = delta;
    }
}
