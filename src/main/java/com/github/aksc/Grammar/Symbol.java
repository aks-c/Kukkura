package com.github.aksc.Grammar;

import com.github.aksc.DerivationSystem;
import com.github.aksc.ErrorHandling.BadLanguageException;
import com.github.aksc.MetaData.*;
import com.google.gson.annotations.SerializedName;

import java.util.HashMap;
import java.util.Random;

/**
 * Created by akselcakmak on 21/06/2018.
 *
 *
 * Handles the symbols we use along with their related meta-data, properties, etc..
 * This is one of the most essential building blocks of the procedural generator, along with the DerivationSystem class.
 */
public class Symbol {
    public Symbol(String symbolID, int probability, boolean exclusiveDerivation, boolean canBeResized, Coordinates resizeCoefficients, Coordinates size, Coordinates position, CoordinatesDelta deltaSize, CoordinatesDelta deltaPosition, Material material, String materialReference, String deltaSizeReference, String deltaPositionReference) {
        this.symbolID = symbolID;
        this.probability = probability;
        this.exclusiveDerivation = exclusiveDerivation;
        this.canBeResized = canBeResized;
        this.resizeCoefficients = resizeCoefficients;
        this.size = size;
        this.position = position;
        this.deltaSize = deltaSize;
        this.deltaPosition = deltaPosition;
        this.material = material;
        this.materialReference = materialReference;
        this.deltaSizeReference = deltaSizeReference;
        this.deltaPositionReference = deltaPositionReference;
    }

    public Symbol(Symbol other) {
        this(other.getSymbolID(), other.getProbability(), other.isExclusiveDerivation(), other.canBeResized(), new Coordinates(other.getResizeCoefficients()), new Coordinates(other.getSize()), new Coordinates(other.getPosition()), new CoordinatesDelta(other.getDeltaSize()), new CoordinatesDelta(other.getDeltaPosition()), other.getMaterial(), other.getMaterialReference(), other.getDeltaSizeReference(), other.getDeltaPositionReference());
    }

    public Symbol(Symbol other, Coordinates size, Coordinates position, Material material, CoordinatesDelta deltaSize, CoordinatesDelta deltaPosition) {
        this(other.getSymbolID(), other.getProbability(), other.isExclusiveDerivation(), other.canBeResized(), new Coordinates(other.getResizeCoefficients()), new Coordinates(size), new Coordinates(position), new CoordinatesDelta(deltaSize), new CoordinatesDelta(deltaPosition), material, other.getMaterialReference(), other.getDeltaSizeReference(), other.getDeltaPositionReference());
    }



    /** The actual symbol associated with this object. */
    @SerializedName("symbol")
    private final String symbolID;

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
    private final int probability;


    /**
     * This boolean is useful to differentiate between two possible systems of randomisation one might want to use.
     * An exclusive rule is one where: out of all the RHS symbols of the rule, only one, and exactly one is chosen.
     * A non-exclusive rule is one where: every RHS symbol is handled in a case by case fashion.
     * i.e.: it might be that all the symbols are chosen, or none of them, or only one, etc...
     */
    @SerializedName("exclusive_derivation")
    private final boolean exclusiveDerivation;

    /** Whether this symbol is allowed to be randomly resized when created by some rule. */
    @SerializedName("can_be_resized")
    private final boolean canBeResized;

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
    private final Coordinates resizeCoefficients;


    /**
     * The size and position of the symbol are not mandatory fields;
     * depending on whether the object comes from the axiom or a rule, size and position might or might not be needed.
     * Also, these are absolute values.
     * Important Note ! Minecraft counts from 0; a path from 0 to 10 is 11 blocks long, not 10,
     * and a structure of height "0" is 1 block high, not 0.
     */
    @SerializedName("size")
    private final Coordinates size;

    @SerializedName("position")
    private final Coordinates position;



    /**
     * The following delta_ fields are only relevant for objects instantiated from the grammar's rules.
     * They specify where/how this symbol should be placed with respect to the position/size of the symbol that produced it.
     * To get the final position and final size of the symbol, we first take into account its absolute size/pos values, then apply these deltas.
     */
    @SerializedName("delta_size")
    private final CoordinatesDelta deltaSize;

    @SerializedName("delta_position")
    private final CoordinatesDelta deltaPosition;

    /** Holds the information needed to ID a specific material. */
    @SerializedName("material")
    private final Material material;

    /**
     * To avoid duplication and avoid redefining the same materials again and again,
     * we define some key materials in the input file. Each has an ID String.
     * Then to use a pre-defined material, set this string to whatever the ID of said material is.
     */
    @SerializedName("material_ref")
    private final String materialReference;


    /**
     * To avoid duplication and avoid redefining the same delta positions again and again,
     * we define some key delta positions in the input file. Each has an ID String.
     * Then to use a pre-defined delta size, set this string to whatever the ID of said d_size is.
     */
    @SerializedName("delta_size_ref")
    private final String deltaSizeReference;

    @SerializedName("delta_position_ref")
    private final String deltaPositionReference;



    public String getSymbolID(){
        return symbolID;
    }

    public Coordinates getSize() {
        return size;
    }

    public Coordinates getPosition() {
        return position;
    }

    /**
     * Note: This should only be used for validation purposes, not the actual application logic.
     * If you want to get a valid usable deltaSize, use the getDeltaSizeFromRef() method.
     * Ditto for getDeltaSizeReference().
     */
    public CoordinatesDelta getDeltaSize() {
        return deltaSize;
    }

    /**
     * Note: This should only be used for validation purposes, not the actual application logic.
     * If you want to get a valid usable deltaPosition, use the getDeltaPositionFromRef() method.
     * Ditto for getDeltaPositionReference().
     */
    public CoordinatesDelta getDeltaPosition() {
        return deltaPosition;
    }

    /**
     * Note: This should only be used for validation purposes (ie: in the Validation class), not the actual application logic.
     * If you want to get a valid usable material, use the getMaterialFromRef() method.
     * Ditto for getMaterialReference().
     */
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

    /** Should only be used in the Validation phase. See getMaterial() for more.*/
    public String getMaterialReference() {
        return materialReference;
    }

    /** Should only be used in the Validation phase. See getDeltaSize() for more. */
    public String getDeltaSizeReference() {
        return deltaSizeReference;
    }

    /** Should only be used in the Validation phase. See getDeltaPosition() for more. */
    public String getDeltaPositionReference() {
        return deltaPositionReference;
    }


    /**
     * The deltaPosition field cannot be accessed from outside through its regular getter:
     * It might not exist as a properly defined object, but as a String reference to some deltaPosition pre-defined in the input file.
     * This function handles the logic of either returning the deltaPosition of this symbol (if there is one)
     * or returning (a reference to) said pre-defined deltaPosition.
     */
    public CoordinatesDelta getDeltaPositionFromRef(HashMap<String, CoordinatesDelta> deltaSizes) {
        if (getDeltaPositionReference() == null)
            return this.getDeltaPosition();
        else
            return deltaSizes.get(getDeltaPositionReference());
    }

    public CoordinatesDelta getDeltaSizeFromRef(HashMap<String, CoordinatesDelta> deltaSizes) {
        if (getDeltaSizeReference() == null)
            return this.getDeltaSize();
        else
            return deltaSizes.get(getDeltaSizeReference());
    }

    /**
     * The MaterialReference field holds some String value.
     * This String represents the name of a commonly used material.
     * This sets the current material of the Symbol to whatever is referenced by the MaterialReference String.
     *
     * A materialReference can have values for one of three cases:
     * - The material is already explicitly defined, so there is no reference to anything (materialRef = null)
     * - The material is defined with respect to the material of the parent Symbol (materialRef = refToParentMat)
     * - The material is defined with respect to one of the globally available materials (materialRef = ID of the desired global material)
     */
    public Material getMaterialFromRef(HashMap<String, Material> materials, String referenceToParentMaterial, Symbol previousSymbol) {
        if (getMaterialReference() == null)
            return this.getMaterial();
        else if (getMaterialReference().equals(referenceToParentMaterial))
            return previousSymbol.getMaterial();
        else
            return materials.get(getMaterialReference());
    }

    /**
     * Serializes the Symbol Object into a String.
     * That String can be executed as a syntactically valid command by the Minecraft interpreter.
     * (in order to create the structure associated with our symbol in the game).
     */
    public String getAsMinecraftCommand() {
        Coordinates secondPosition = getSecondPosition(this.getPosition(), this.getSize());;
        return String.format("fill ~%s ~%s ~%s ~%s ~%s ~%s %s %s %s", getPosition().getX(), getPosition().getY(), getPosition().getZ(), secondPosition.getX(), secondPosition.getY(), secondPosition.getZ(), getMaterial().getMainID(), getMaterial().getSubID(), getMaterial().getState());
    }

    /**
     * In ℝ^3, a structure can be "fenced" using two Coordinates.
     * The first Coordinate to define those boundaries is our Position field.
     * The second position is calculated wrt said first Position, and the Size of the Symbol/Structure.
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

    /**
     *  This checks that the Symbol's state is valid, and throws a BadLanguageException otherwise.
     *  It is called for every **initial** Symbol, exactly once.
     *  It can be shown that if the validation holds for every initial symbol and every symbol in every rule,
     *  then it also holds for every symbol derived afterwards.
     *  We can take advantage of this: not every symbol needs to be validated. Only the initial ones need to.
     */
    public void validate(String parentSymbol, DerivationSystem ds) throws BadLanguageException {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("\n" + "Symbol " + symbolID + " in rule " + parentSymbol + ": \n");
        boolean isValid = true;


        isValid = Validation.checkMaterial(this, ds, errorMsg) && isValid;

        isValid = Validation.checkDeltaPos(this, ds, errorMsg) && isValid;

        isValid = Validation.checkDeltaSize(this, ds, errorMsg) && isValid;

        isValid = Validation.checkInitialSize(this, ds, errorMsg) && isValid;

        isValid = Validation.checkInitialPosition(this, ds, errorMsg) && isValid;

        isValid = Validation.checkProbabilityWeight(this, ds, errorMsg) && isValid;

        isValid = Validation.checkSymbolIDExistence(this, ds, errorMsg) && isValid;


        if (!isValid)
            throw new BadLanguageException(errorMsg.toString());
    }

    /**
     * Validates a Symbol's state.
     * This is only called for symbols part of the initial axiom,
     * because the validity rules for those are different.
     */
    public void validateInitial(DerivationSystem ds) throws BadLanguageException {
        StringBuilder errorMsg = new StringBuilder();
        errorMsg.append("\n" + "Symbol " + symbolID + " in the Axiom: \n");
        boolean isValid = true;


        isValid = Validation.checkInitialSize(this, ds, errorMsg) && isValid;

        isValid = Validation.checkInitialPosition(this, ds, errorMsg) && isValid;

        isValid = Validation.checkReferencesExist(this, ds, errorMsg) && isValid;

        isValid = Validation.checkSymbolIDExistence(this, ds, errorMsg) && isValid;


        if (!isValid)
            throw new BadLanguageException(errorMsg.toString());
    }
}