package com.github.aksc.MetaData;

import java.util.Random;

import static com.github.aksc.MetaData.CoordinatesUtility.*;

/**
 * Created by akselcakmak on 02/07/2018.
 *
 */
public class Resizing {
    /**
     * Get a resize of a Symbol.
     * This resize is chosen randomly within some defined interval.
     * Note that the resize doesn't modify the original size Coordinates; it returns a new updated Coordinates instance.
     */
    public static Coordinates getRandomSize(Coordinates sizeToChange, Coordinates resizeToApply, boolean symbolCanBeResized) {
        if (!symbolCanBeResized)
            return sizeToChange;
        String newX = getRandomResizeOfField(sizeToChange, resizeToApply, AXIS.X);
        String newY = getRandomResizeOfField(sizeToChange, resizeToApply, AXIS.Y);
        String newZ = getRandomResizeOfField(sizeToChange, resizeToApply, AXIS.Z);
        return new Coordinates(newX, newY, newZ);
    }

    /**
     * Get a randomized resize of a specific field, within some range.
     *
     * Call the resizing `delta`.
     * We define the range delta belongs to, using:
     * (1) The appropriate coefficient in the ResizeCoefficients triple
     * (2) The size of our symbol
     * We want the delta to belong to the following interval: [ - coeff * size * 1/2 ; + coeff * size * 1/2 ].
     * For example, say sx = 10, coeff = 1.
     * Then the delta will belong to the following range: [-5 ; 5].
     * Then, after we actually apply the resizing, x will be in the following range: [ 5 ; 15 ].
     */
    private static String getRandomResizeOfField(Coordinates sizeToChange, Coordinates resizeToApply, AXIS axis) {
        int coefficient = Integer.parseInt(resizeToApply.getField(axis));
        // By convention, a coefficient of 0 signifies that there should be no resize.
        // (bcs then the delta interval would be [- stuff * 0 ; stuff * 0] = [0 ; 0] => no resizing.
        if (coefficient == 0)
            return sizeToChange.getField(axis);

        int sizeField = Integer.parseInt(sizeToChange.getField(axis));
        int intervalBound = coefficient * sizeField;
        int delta = new Random().nextInt(intervalBound);
        delta -= sizeField >> 1;
        return getResizeToField(sizeToChange.getField(axis), delta);
    }

    /** Get deterministic resize of a given field. */
    private static String getResizeToField(String field, int delta) {
        return addDelta(field, delta);
    }

}
