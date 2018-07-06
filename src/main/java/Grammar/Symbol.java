package Grammar;

import MetaData.Coordinates;
import MetaData.CoordinatesDelta;
import MetaData.CoordinatesUtility;
import MetaData.CoordinatesUtility.AXIS;
import MetaData.CoordinatesUtility.ROTATION;
import MetaData.Material;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

/**
 * Created by akselcakmak on 21/06/2018.
 *
 *
 * Handles the symbols we use along with their related meta-data, properties, etc..
 */
public class Symbol {
    /**
     * The actual symbol associated with this object.
     */
    @SerializedName("symbol")
    private String symbol;

    /**
     * Some rules might have this symbol in their RHS.
     * This denotes the probability that the symbol will actually end up being added to the final result.
     * For Inclusive Rules: The probability is encoded as a number in [0-100].
     * For Exclusive Rules: The field is interpreted as an integer weight:
     * The higher the weight, the higher the chance that this would be the symbol chosen.
     * It's not a float because:
     * (1) It doesn't need to be a float in our specific limited use case.
     * (2) I don't want to add unnecessary complexity.
     * (and I'd also surmise it'd be faster this way, although tbf at this stage of development idc yet).
     * (3) Turns out that in the context of exclusive rules, an int field just makes a lot more sense.
     */
    @SerializedName("probability")
    private int probability;


    /**
     * This boolean is useful to differentiate between two possible systems of randomisation one might want to use.
     * It specifies whether the rule coming from this symbol is exclusive or not.
     * An exclusive rule is one where: out of all the RHS symbols of the rule, only one, and exactly one is chosen.
     * A non-exclusive rule is one where: every RHS symbol can be chosen or not, independently. (This is the current default btw).
     */
    @SerializedName("exclusive_derivation")
    private boolean exclusiveDerivation;

    /**
     * Whether this symbol is allowed to be randomly resized when created by some rule.
     */
    @SerializedName("can_be_resized")
    private boolean canBeResized;

    /**
     * This holds a triplet of coefficients for the resizing.
     * The coefficients are with respect to the size of the symbol (ie: wrt sx, sy, sz).
     * A resizing is a modification of one of these sx, sy, sz.
     * This Tuple denotes the possible range of said modification.
     * For a given size field, s, the modification will be as follows: s = s + rand in [ - coef * s * 1/2; + coef * s * 1/2 ]
     * eg: if the value for x is say 1, and x is say x=10,
     * then the new value of x will be in between: x=10 + rand=(-5) and x=10 rand=(5).
     * i.e. x will be in the range [5, 15].
     */
    @SerializedName("resize_coefficients")
    private Coordinates resizeCoefficients;

    /**
     * Whether this symbol is allowed to be randomly rotated when created by some rule.
     */
    @SerializedName("can_be_rotated")
    private boolean canBeRotated;

    /**
     * The size and position of the symbol are not mandatory fields;
     * depending on whether the object comes from the axiom or a rule, size and position might or might not be needed.
     * Also, these are absolute values.
     * Important Note ! Minecraft counts from 0; a path from 0 to 10 is 11 blocks long, not 10,
     * and a structure of height "0" is 1 block high, not 0.
     */
    @SerializedName("size")
    private Coordinates size;

    @SerializedName("position")
    private Coordinates position;



    /**
     * The following delta_ fields are only relevant for objects instantiated from the grammar's rules.
     * They specify where/how this symbol should be placed with respect to the position/size of the symbol that produced it.
     * To get the final position and final size of the symbol, we first take into account its absolute size/pos values, then apply these deltas.
     */
    @SerializedName("delta_size")
    private CoordinatesDelta deltaSize;

    @SerializedName("delta_position")
    private CoordinatesDelta deltaPosition;

    /**
     * Holds the information needed to ID a specific material.
     */
    @SerializedName("material")
    private Material material;

    /**
     * To avoid duplication and avoid redefining the same materials again and again,
     * one can reference the material of this symbol with this field.
     */
    @SerializedName("material_ref")
    private Material materialReference;



    public String getSymbol(){
        return symbol;
    }

    public Coordinates getSize() {
        return size;
    }

    public Coordinates getPosition() {
        return position;
    }

    public CoordinatesDelta getDeltaSize() {
        return deltaSize;
    }

    public CoordinatesDelta getDeltaPosition() {
        return deltaPosition;
    }

    public Material getMaterial() {
        return material;
    }

    public int getProbability() {
        return probability;
    }

    public Coordinates getResizeCoefficients() {
        return resizeCoefficients;
    }

    public boolean isExclusiveDerivation() {
        return exclusiveDerivation;
    }

    public boolean canBeResized() {
        return canBeResized;
    }

    public boolean canBeRotated() {
        return canBeRotated;
    }



//    private Material setMaterialFromRef() {
//        return
//    }

    /**
     * Apply a random Rotation, with respect to a random Axis.
     * Entirely random.
     */
    public void applyRandomRotation() {
        Rotation.applyRandomRotation(this);
    }

    /**
     * Applies a given Rotation with respect to some given Axis.
     * Entirely deterministic (i.e. no randomization).
     */
    public void applyRotation(AXIS axis, CoordinatesUtility.ROTATION rotation) {
        Rotation.applyRotation(this, axis, rotation);
    }

    public void applyRotationX(Symbol symbol, ROTATION rotation) {
        Rotation.applyRotationX(symbol, rotation);
    }

    /**
     * Apply a random Resize on a Symbol.
     * This resize is chosen randomly within some defined interval.
     */
    public void applyRandomResize() {
        Resizing.applyRandomResize(this);
    }

    /**
     * Serializes the Symbol Object into a String.
     * That String can be executed as a syntactically valid command by the Minecraft interpreter.
     * (in order to create the structure associated with our symbol in the game).
     */
    public String getAsMinecraftCommand() {
        Coordinates secondPosition = getSecondPosition(this.getPosition(), this.getSize());
        return String.format("fill ~%s ~%s ~%s ~%s ~%s ~%s %s %s %s", getPosition().getX(), getPosition().getY(), getPosition().getZ(), secondPosition.getX(), secondPosition.getY(), secondPosition.getZ(), getMaterial().getMainID(), getMaterial().getSubID(), getMaterial().getState());
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
        return CoordinatesUtility.addDelta(field, size);
    }

    /**
     * Used only when the derivation rule is Inclusive.
     * In that situation, every symbol is processed independently, one by one.
     * Then, a particular symbol should be added iff its weight/probability is high enough.
     */
    public boolean shouldBeAdded() {
        // the higher the probability, the higher the chance that (r < probability).
        int r = new Random().nextInt(100);
        return (r <= probability);
    }
}