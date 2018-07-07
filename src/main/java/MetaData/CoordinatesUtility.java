package MetaData;

import Grammar.Symbol;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * Created by akselcakmak on 01/07/2018.
 *
 * Some functions are useful to several Coordinate classes, and to some other classes, like Symbol.
 * They're all used to modify in one way or the other the Coordinates of a certain Symbol, but none of them truly belongs to any of those classes.
 * For now, they're all in this Utility kind of class, until a better refactor is found.
 */
// TODO: Refactor the common behaviour of the two enums and the random field selection.
// I tried a bunch of things that didn't work; well (1) I don't want to stop the progress rn,
// and (2) I know this specific behaviour won't be changed.
// This is why I've decided to keep this ugly duplication, for now, which will be refactored along with other stuff,
// in the planned CleanUp PRs following this mvp5.
public class CoordinatesUtility {
    /**
     * Helper for some functions that need to know what axis the field they work on belongs to.
     */
    public enum AXIS {
        X,
        Y,
        Z;
        // Hold the values() of this enum in a final immutable list that can then be used to return a random field from the enum.
        private static final List<AXIS> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();
        // Sometimes when applying a random Rotation, we want to apply wrt a random axis too.
        // That's when this function is used.
        public static AXIS randomAxis() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }

    /**
     * Specifies what kind of rotation should be applied.
     * How these are defined, in an intuitive way:
     * Take the considered axis vector; position yourself on this axis, then look at its direction;
     * Then Left/Right/etc... are defined wrt that point of view.
     */
    public enum ROTATION {
        LEFT,
        RIGHT,
        UP,
        DOWN,
        NONE;
        // Hold the values() of this enum in a final immutable list that can then be used to return a random field from the enum.
        private static final List<ROTATION> VALUES = Collections.unmodifiableList(Arrays.asList(values()));
        private static final int SIZE = VALUES.size();
        private static final Random RANDOM = new Random();
        // Sometimes when applying a random Rotation, we want to apply wrt a random axis too.
        // That's when this function is used.
        public static ROTATION randomRotation() {
            return VALUES.get(RANDOM.nextInt(SIZE));
        }
    }



    /**
     * Adds the value of a delta to a field.
     */
    static public String addDelta(String field, String delta) {
        int result = Integer.parseInt(field.trim()) + Integer.parseInt(delta.trim());
        return String.valueOf(result);
    }

    static public String addDelta(String field, int delta) {
        return addDelta(field, String.valueOf(delta));
    }

    /**
     * Multiplies the value of a delta with some factor.
     * Because our system only works with discrete blocks/units, the return value is trimmed.
     */
    static public String multiplyDelta(String delta, String factor) {
        float result = (Integer.parseInt(delta.trim()) * Float.parseFloat(factor.trim()));
        return String.format("%.0f", result);
    }

    /**
     * Gets the value represented by the delta ("sx", "y", etc..) and multiplies it by its associated factor.
     */
    static public String getDeltaValueWithFactor(Delta delta, Symbol parentSymbol) {
        String withoutFactor = getDeltaValue(delta, parentSymbol);
        return multiplyDelta(withoutFactor, delta.getFactor());
    }

    /**
     * Only gets the value represented by the given delta ("sx", "y", etc..).
     */
    static public String getDeltaValue(Delta delta, Symbol parentSymbol) {
        return getDeltaValue(delta.getDelta(), parentSymbol);
    }

    /**
     * Returns whatever value the given delta string represents.
     *
     * Deltas are applied relative to something: the value of a specific field inside the Coordinate of a symbol.
     * This function retrieves the value of the appropriate field, from the base symbol,
     * according to the String description in the delta field of this Coordinate.
     * e.g.: a value of "sx" for the current delta field means that
     * this field will add a value relative to the X size dimension of the base symbol.
     */
    static public String getDeltaValue(String delta, Symbol parentSymbol){
        String result;

        switch (delta) {
            case "x":
                result = parentSymbol.getPosition().getX();
                break;
            case "y":
                result = parentSymbol.getPosition().getY();
                break;
            case "z":
                result = parentSymbol.getPosition().getZ();
                break;

            case "sx":
                result = parentSymbol.getSize().getX();
                break;
            case "sy":
                result = parentSymbol.getSize().getY();
                break;
            case "sz":
                result = parentSymbol.getSize().getZ();
                break;
            case "":
                result = "0";
                break;
            default:
                result = delta;
                break;
        }
        return result;
    }
}
