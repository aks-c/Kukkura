package Grammar;

import MetaData.Position;
import MetaData.Size;

/**
 * Created by akselcakmak on 21/06/2018.
 *
 *
 * Handles the symbols we use along with their related meta-data, properties, etc..
 */
public class Symbol {
    String symbol;

    Size size;
    Position position;

    Size delta_size;
    Position delta_position;

    String material;
}
