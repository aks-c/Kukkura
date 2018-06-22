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
    private String symbol;


    // The size and position of the symbol are not mandatory fields;
    // depending on whether the object comes from the axiom or a rule, size and position might or might not be needed.
    // Also, these are absolute values.
    @SerializedName("size")
    private Coordinates size;

    @SerializedName("position")
    private Coordinates position;



    // The following delta_ fields are only relevant for objects instantiated from the grammar's rules.
    // They specify where/how this symbol should be placed with respect to the position/size of the symbol that produced it.
    // To get the final position and final size of the symbol, we first take into account its absolute size/pos values,
    // then apply these deltas.
    @SerializedName("delta_size")
    private Coordinates delta_size;

    @SerializedName("delta_position")
    private Coordinates delta_position;


    // This following are only needed for MC.
    // `material` represents the material name,
    // and `material_sub` represents the sub-ID (for a lack of a better name) of that material.
    // eg: the tuple (material="planks", sub="0") represents Oak Wood Planks.
    // eg: the tuple (material="planks", sub="3") represents Jungle Wood Planks.
    @SerializedName("material")
    private String material;
    @SerializedName("material_sub")
    private String material_sub;
    // `material_state` stores info about the structure represented by the symbol, like whether it's hollow or not, etc..
    @SerializedName("symbol_state")
    private String symbol_state;

    public String getSymbol(){
        return symbol;
    }

    public Coordinates getSize() {
        return size;
    }

    public Coordinates getPosition() {
        return position;
    }

    public Coordinates getDeltaSize() {
        return delta_size;
    }

    public Coordinates getDeltaPosition() {
        return delta_position;
    }
}
