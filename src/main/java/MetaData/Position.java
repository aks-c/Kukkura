package MetaData;

/**
 * Created by akselcakmak on 21/06/2018.
 *
 *
 * Handles the Position meta-data of our symbols.
 */
public class Position {
    // We use both absolute and relative values with respect to previous symbols to determine the Position of new derived symbols.
    // This is why we use Strings and not ints for the coordinates.
    String x;

    String y;

    String z;
}
