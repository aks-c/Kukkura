package Grammar;

import MetaData.CoordinatesUtility;

import java.util.Random;

/**
 * Created by akselcakmak on 02/07/2018.
 *
 */
public class Resizing {
    public void applyRandomResize() {
        if (!canBeResized())
            return;
        getSize().setX(applyRandomResizeToField(CoordinatesUtility.AXIS.X));
        getSize().setY(applyRandomResizeToField(CoordinatesUtility.AXIS.Y));
        getSize().setZ(applyRandomResizeToField(CoordinatesUtility.AXIS.Z));
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
    private String applyRandomResizeToField(CoordinatesUtility.AXIS axis) {
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

}
