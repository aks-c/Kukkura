import json
import sys

from symbol import Symbol, BadSymbolException, validate_symbols
from file_io import get_symbol_list, write_output_to_file


def get_symbols_interpretation(symbols):
    '''
    For now, this simply does a one-to-one mapping from symbol to Minecraft commands.

    The cmds follow a syntax the MC command-line understands.
    
    Later on, this can be made much more interesting,
    by embedding additional behaviour via the meta-data.
    For example, one could add a "roof" field so that this symbol 
    is considered as a roof and not a cube. Or say a "room" field, so that 
    the inside of the cube is populated by various furnitures. etc...
    '''
    cmd_list = []
    for symbol in symbols:
        # print(symbol, "\n")
        cmd = "fill ~{} ~{} ~{} ~{} ~{} ~{} {} {}" # follow the MC functions syntax
        second_pos = symbol.get_second_position()
        cmd_list.append(cmd.format(
            symbol.position.get("x"),
            symbol.position.get("y"),
            symbol.position.get("z"),
            second_pos.get("x"),
            second_pos.get("y"),
            second_pos.get("z"),
            symbol.meta_data.get("material_ID"),
            symbol.meta_data.get("material_state")
        ))

    cmd_string = "\n".join(cmd_list)
    return cmd_string
    

def minecraft():
    ''' 
    The minecraft interpretation method.
    
    The Kukkura tool generates some set of symbols, and outputs this set to a json file.
    This interpreter (and the others) take this json file as input,
    and generate their own representation of this list of symbols,
    as appropriate for the platform/game/etc they target.

    '''
    print("\nMinecraft Interpretation..")

    symbols = get_symbol_list()

    required_fields = [ "material_ID", "material_state" ]
    validate_symbols(symbols, required_fields)

    print("Getting Interpretation..")
    out_string = get_symbols_interpretation(symbols)


    filename = "minecraft.mcfunction"
    write_output_to_file(filename=filename, output_string=out_string)
    print("Interpretation done. File:", filename)



if __name__ == "__main__":
    minecraft()