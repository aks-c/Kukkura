package Grammar;

import MetaData.Coordinates;
import MetaData.CoordinatesDelta;
import MetaData.CoordinatesUtility;
import MetaData.CoordinatesUtility.AXIS;
import MetaData.CoordinatesUtility.ROTATION;
import com.google.gson.annotations.SerializedName;

import java.util.Random;

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

    // Some rules might have this symbol in their RHS.
    // This denotes the probability that the symbol will actually end up being added to the final result.
    // For Inclusive Rules: The probability is encoded as a number in [0-100].
    // For Exclusive Rules: The field is interpreted as an integer weight:
    // The higher the weight, the higher the chance that this would be the symbol chosen.
    // It's not a float because:
    // (1) It doesn't need to be a float in our specific limited use case.
    // (2) I don't want to add unnecessary complexity.
    // (and I'd also surmise it'd be faster this way, although tbf at this stage of development idc yet).
    // (3) Turns out that in the context of exclusive rules, an int field just makes a lot more sense.
    @SerializedName("probability")
    private int probability;

    // This boolean is useful to differentiate between two possible systems of randomisation one might want to use.
    // It specifies whether the rule coming from this symbol is exclusive or not.
    // An exclusive rule is one where: out of all the RHS symbols of the rule, only one, and exactly one is chosen.
    // A non-exclusive rule is one where: every RHS symbol can be chosen or not, independently. (This is the current default btw).
    @SerializedName("exclusive_derivation")
    private boolean exclusiveDerivation;

    // Whether this symbol is allowed to be randomly resized when created by some rule.
    @SerializedName("can_be_resized")
    private boolean canBeResized;

    // This holds a triplet of coefficients for the resizing.
    // The coefficients are with respect to the size of the symbol (ie: wrt sx, sy, sz).
    // A resizing is a modification of one of these sx, sy, sz.
    // This Tuple denotes the possible range of said modification.
    // For a given size field, s, the modification will be as follows: s = s + rand in [ - coef * s * 1/2; + coef * s * 1/2 ]
    // eg: if the value for x is say 1, and x is say x=10,
    // then the new value of x will be in between: x=10 + rand=(-5) and x=10 rand=(5).
    // i.e. x will be in the range [5, 15].
    @SerializedName("resize_coefficients")
    private Coordinates resizeCoefficients;

    // Whether this symbol is allowed to be randomly rotated when created by some rule.
    @SerializedName("can_be_rotated")
    private boolean canBeRotated;

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
    private CoordinatesDelta deltaSize;

    @SerializedName("delta_position")
    private CoordinatesDelta deltaPosition;


    // This following are only needed for MC.
    // `material` represents the material name,
    // and `material_sub` represents the sub-ID (for a lack of a better name) of that material.
    // eg: the tuple (material="planks", sub="0") represents Oak Wood Planks.
    // eg: the tuple (material="planks", sub="3") represents Jungle Wood Planks.
    @SerializedName("material")
    private String material;

    @SerializedName("material_sub")
    private String materialSub;

    // Stores info about the structure represented by the symbol, like whether it's hollow or not, etc..
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

    public CoordinatesDelta getDeltaSize() {
        return deltaSize;
    }

    public CoordinatesDelta getDeltaPosition() {
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

    /**
     * Apply a random Rotation, with respect to a random Axis.
     */
    public void applyRandomRotation() {
        if (!canBeRotated)
            return;
        applyRotation(AXIS.randomAxis(), ROTATION.randomRotation());
    }

    /**
     * Apply a given Rotation, with respect to some random Axis.
     */
    public void applyRandomRotation(ROTATION rotation) {
        if (!canBeRotated)
            return;
        applyRotation(AXIS.randomAxis(), rotation);
    }

    /**
     * Apply a random Rotation, with respect to some given Axis.
     */
    public void applyRandomRotation(AXIS axis) {
        if (!canBeRotated)
            return;
        applyRotation(axis, ROTATION.randomRotation());
    }

    /**
     * Applies a given Rotation with respect to some given Axis.
     *
     * For now, only the X Axis is supported.
     * TBF, all the rotations can be expressed wrt a single Axis anyway (X just happens to be the most convenient);
     * The rest might be implemented later if some Rotations are more naturally expressed wrt other axes.
     */
    public void applyRotation(AXIS axis, ROTATION rotation) {
        if (!canBeRotated)
            return;
        switch (axis) {
            case X:
                applyRotationX(rotation);
                break;
            default:
                applyRotationX(rotation);
                break;
        }
    }

    /**
     *  Handles every defined Rotation, for the X axis only.
     *
     *  RIGHT and DOWN apply the same absolute rotations as LEFT and UP respectively,
     *  (as in, the Size fields change by the same absolute values),
     *  but they change the orientation of said rotation.
     *  For example, a RIGHT rotation orients the symbol towards the viewer; i.e. it sets a negative value for sz.
     *  We want to avoid negative values of Sizes;
     *  They can and are being handled, but it just doesn't make to think about them this way IMO.
     *  So then instead of setting negative Sizes,
     *  we apply the "normal" (non-negative) rotation (i.e. LEFT or UP), then apply a negative offset to the Position,
     *  which brings about the same net effect as an actual RIGHT/DOWN rotation, without the drawback of having to deal with weird shit.
     */
    private void applyRotationX(ROTATION rotation) {
        switch (rotation) {
            case LEFT:  // swap sx and sz; sy unchanged;
                getSize().swap(AXIS.X, AXIS.Z);
                break;
            case UP:    // swap sx and sy; sz unchanged;
                getSize().swap(AXIS.X, AXIS.Y);
                break;
            case RIGHT: // same as LEFT; also decrease z
                applyRotationX(ROTATION.LEFT);
                CoordinatesUtility.applyDelta(getPosition().getField(AXIS.Y), "-" + getSize().getField(AXIS.Y));
                break;
            case DOWN:  // same as UP;   also decrease y
                applyRotationX(ROTATION.UP);
                CoordinatesUtility.applyDelta(getPosition().getField(AXIS.Z), "-" + getSize().getField(AXIS.Z));
                break;
            case NONE:
                break;
        }
    }

    public void applyRandomResize() {
        if (!canBeResized())
            return;
        getSize().setX(applyRandomResizeToField(AXIS.X));
        getSize().setY(applyRandomResizeToField(AXIS.Y));
        getSize().setZ(applyRandomResizeToField(AXIS.Z));
    }

    /**
     * Apply a randomized Resize on a specific field.
     *
     * We want to modify some field.
     * We apply some random resizing to it, but this resizing needs to be within some defined range.
     * Call that resizing `delta`.
     * We define the range delta belongs to, using:
     * (1) The appropriate coefficient in the ResizeCoefficients triple
     * (2) The size of our symbol
     * We want the delta to belong in the following range: [ - coeff * size * 1/2 ; + coeff * size * 1/2 ].
     * For example, say sx= 10, coeff = 1.
     * Then the delta will belong to the following range: [-5 ; 5].
     * Then, after we actually apply the resizing, x will be in the following range: [ 5 ; 15 ].
     */
    private String applyRandomResizeToField(AXIS axis) {
        int coefficient = Integer.parseInt(getResizeCoefficients().getField(axis));
        // By convention, if the coefficient is 0, it signifies that there should be no resize.
        if (coefficient == 0)
            return getSize().getField(axis);

        int sizeField = Integer.parseInt(getSize().getField(axis));
        int intervalBound = coefficient * sizeField;
        int delta = new Random().nextInt(intervalBound);
        delta -= sizeField >> 1;
        return applyResizeToField(getSize().getField(axis), delta);
    }

    /**
     * Apply deterministic Resize to a specific field.
     */
    private String applyResizeToField(String field, int delta) {
        return CoordinatesUtility.applyDelta(field, delta);
    }

    /**
     * Serializes the Symbol Object into a String.
     * That String can be executed as a syntactically valid command by the Minecraft interpreter.
     * (in order to create the structure associated with our symbol in the game).
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
        return CoordinatesUtility.applyDelta(field, size);
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