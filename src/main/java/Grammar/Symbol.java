package Grammar;

import MetaData.Coordinates;
import com.google.gson.annotations.SerializedName;

/**
 * Created by akselcakmak on 21/06/2018.
 *
 *
 * Handles the symbols we use along with their related meta-data, properties, etc..
 */
public class Symbol {
    // The actual symbol associated with this object.
    @SerializedName("symbol")
    String symbol;


    // The size and position of the symbol are not mandatory fields;
    // depending on whether the object comes from the axiom or a rule, size and position might or might not be needed.
    // Also, these are absolute values.
    @SerializedName("size")
    Coordinates size;

    @SerializedName("position")
    Coordinates position;

    // These fields are only relevant for objects instantiated from the grammar's rules.
    // They specify where/how this symbol should be placed with respect to the position/size of the symbol that produced it.
    // To get the final position and final size of the symbol, we first take into account its absolute size/pos values,
    // then apply these deltas.

    @SerializedName("delta_size")
    Coordinates delta_size;

    @SerializedName("delta_position")
    Coordinates delta_position;

    // This is only needed for MC.
    @SerializedName("material")
    String material;


    public String getSymbol(){
        return symbol;
    }
}
