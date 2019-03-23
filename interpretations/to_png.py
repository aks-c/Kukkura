from PIL import Image
import numpy as np
from symbol import Symbol, BadSymbolException
from file_io import get_symbol_list


def get_bounds(symbols):
    s = max(symbols, key=lambda x: x.get_maxmin_coordinates("x", max))
    x_max = s.get_maxmin_coordinates("x", max)

    s = min(symbols, key=lambda x: x.get_maxmin_coordinates("x", min))
    x_min = s.get_maxmin_coordinates("x", min)

    s = max(symbols, key=lambda x: x.get_maxmin_coordinates("y", max))
    y_max = s.get_maxmin_coordinates("y", max)

    s = min(symbols, key=lambda x: x.get_maxmin_coordinates("y", min))
    y_min = s.get_maxmin_coordinates("y", min)

    return x_max, x_min, y_max, y_min

def render_symbol(symbol, data, x_min, y_min):
    # print("\n")
    # print("render symbol:", symbol.symbol_name)

    pos = symbol.position
    second_pos = symbol.get_second_position()
    color = [symbol.meta_data.get("R"), symbol.meta_data.get("G"), symbol.meta_data.get("B")]

    # A specific symbol (in 3D) in our initial space
    # is always bounded by two points: a1 = (x1, y2), a2 = (x2, y2).
    # We translate these coordinates to fit into the space of a png matrix:
    # a1' = (first_x, first_y), and a2' = (second_x, second_y).
    first_x = int(pos.get("x")) - x_min
    first_y = int(pos.get("y")) - y_min
    second_x = second_pos.get("x") - x_min
    second_y = second_pos.get("y") - y_min

    # do this to handle the special case when the size of a symbol
    # is specified as a negative offset.
    first_x, second_x = min(first_x, second_x), max(first_x, second_x)
    first_y, second_y = min(first_y, second_y), max(first_y, second_y)

    for x in range(first_x, second_x):
        for y in range(first_y, second_y):
            data[x][y] = color


    


def to_png():
    print("\nPNG Interpretation..")

    symbols = []
    for s in get_symbol_list():
        symbols.append(Symbol(s))

    # Sort the symbols according to the highest z coordinates of a symbol's bounds.
    # When we want to paint the symbols on the image, we start in order with the lowest ones.
    symbols.sort(key=lambda s: s.get_maxmin_coordinates("z", max))

    # We get the bounds of the symbols.
    # That is bcs we want to be able to create an image of the right size.
    # Those are also used to translate the current coordinates of the symbols to this new space.
    print("Calculating Bounds..")
    x_max, x_min, y_max, y_min = get_bounds(symbols)

    row = x_max - x_min
    col = y_max - y_min

    print("Size of PNG (row, col):", row, col)


    data = np.zeros((row, col, 3), dtype=np.uint8)

    print("Rendering Symbols..")
    for s in symbols:
        render_symbol(s, data, x_min, y_min)

    filename = "to_png.png"
    img = Image.fromarray(data, 'RGB')
    img.save('output/' + filename)

    print("Interpretation done. File:", filename)

if __name__ == "__main__":
    to_png()



    
    