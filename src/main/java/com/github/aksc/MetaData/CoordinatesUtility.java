package com.github.aksc.MetaData;

import com.github.aksc.Grammar.Symbol;

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
public class CoordinatesUtility {
    /**
     * Helper for some functions that need to know what axis the field they work on belongs to.
     */
    public enum AXIS {
        X,
        Y,
        Z
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
