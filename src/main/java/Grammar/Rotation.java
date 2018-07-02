package Grammar;

import MetaData.CoordinatesUtility;
import MetaData.CoordinatesUtility.AXIS;

/**
 * Created by akselcakmak on 02/07/2018.
 *
 */
public class Rotation {
    /**
     * Apply a random Rotation, with respect to a random Axis.
     */
    public void applyRandomRotation() {
        if (!canBeRotated)
            return;
        applyRotation(AXIS.randomAxis(), CoordinatesUtility.ROTATION.randomRotation());
    }

    /**
     * Apply a given Rotation, with respect to some random Axis.
     */
    public void applyRandomRotation(CoordinatesUtility.ROTATION rotation) {
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
        applyRotation(axis, CoordinatesUtility.ROTATION.randomRotation());
    }

    /**
     * Applies a given Rotation with respect to some given Axis.
     *
     * For now, only the X Axis is supported.
     * TBF, all the rotations can be expressed wrt a single Axis anyway (X just happens to be the most convenient);
     * The rest might be implemented later if some Rotations are more naturally expressed wrt other axes.
     */
    public void applyRotation(AXIS axis, CoordinatesUtility.ROTATION rotation) {
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
    private void applyRotationX(CoordinatesUtility.ROTATION rotation) {
        switch (rotation) {
            case LEFT:  // swap sx and sz; sy unchanged;
                getSize().swap(AXIS.X, AXIS.Z);
                break;
            case UP:    // swap sx and sy; sz unchanged;
                getSize().swap(AXIS.X, AXIS.Y);
                break;
            case RIGHT: // same as LEFT; also decrease z
                applyRotationX(CoordinatesUtility.ROTATION.LEFT);
                CoordinatesUtility.applyDelta(getPosition().getField(AXIS.Y), "-" + getSize().getField(AXIS.Y));
                break;
            case DOWN:  // same as UP;   also decrease y
                applyRotationX(CoordinatesUtility.ROTATION.UP);
                CoordinatesUtility.applyDelta(getPosition().getField(AXIS.Z), "-" + getSize().getField(AXIS.Z));
                break;
            case NONE:
                break;
        }
    }
}
