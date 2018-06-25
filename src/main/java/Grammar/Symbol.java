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
    // Important Note ! Minecraft counts from 0; a path from 0 to 10 is 11 blocks long, not 10,
    // and a structure of height "0" is 1 block high, not 0.
    @SerializedName("size")
    private Coordinates size;

    @SerializedName("position")
    private Coordinates position;


    // The following delta_ fields are only relevant for objects instantiated from the grammar's rules.
    // They specify where/how this symbol should be placed with respect to the position/size of the symbol that produced it.
    // To get the final position and final size of the symbol, we first take into account its absolute size/pos values,
    // then apply these deltas.
    @SerializedName("delta_size")
    private Coordinates deltaSize;

    @SerializedName("delta_position")
    private Coordinates deltaPosition;


    // This following are only needed for MC.
    // `material` represents the material name,
    // and `material_sub` represents the sub-ID (for a lack of a better name) of that material.
    // eg: the tuple (material="planks", sub="0") represents Oak Wood Planks.
    // eg: the tuple (material="planks", sub="3") represents Jungle Wood Planks.
    @SerializedName("material")
    private String material;
    @SerializedName("material_sub")
    private String materialSub;
    // `material_state` stores info about the structure represented by the symbol, like whether it's hollow or not, etc..
    @SerializedName("symbol_state")
    private String symbolState;


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
        return deltaSize;
    }

    public Coordinates getDeltaPosition() {
        return deltaPosition;
    }

    public String getMaterial() {
        return material;
    }

    public String getMaterialSub() {
        return materialSub;
    }

    public String getSymbolState() {
        return symbolState;
    }


    /**
     * serializes the Symbol Object into a String.
     * That String can be executed as a syntactically valid command by the Minecraft interpreter
     * (to create the structure associated with our symbol in the game).
     */
    public String getAsMinecraftCommand() {
        Coordinates secondPosition = getSecondPosition(this.getPosition(), this.getSize());
        return String.format("fill ~%s ~%s ~%s ~%s ~%s ~%s %s %s %s", getPosition().getX(), getPosition().getY(), getPosition().getZ(), secondPosition.getX(), secondPosition.getY(), secondPosition.getZ(), getMaterial(), getMaterialSub(), getSymbolState());
    }

    /**
     * A structure can be ringed by two 3D Coordinates.
     * The first Coordinate to define those boundaries is just the Position field.
     * The second position depends on: the first position and the Size of the Symbol/Structure.
     * This function calculates said second position, bcs it's needed by MC's /fill command.
     */
    public Coordinates getSecondPosition(Coordinates position, Coordinates size) {
        String x = getSecondPosition(position.getX(), size.getX());
        String y = getSecondPosition(position.getY(), size.getY());
        String z = getSecondPosition(position.getZ(), size.getZ());
        return new Coordinates(x, y, z);
    }

    private String getSecondPosition(String field, String size){
        return Coordinates.applyDelta(field, size);
    }
}