import json
import sys

from symbol import Symbol, BadSymbolException
from file_io import get_symbol_list, write_output_to_file

def validate_symbols(symbols):
    required_fields = [ "material_ID", "material_state" ]
    for s in symbols:
        symbol = Symbol(s)
        try:
            symbol.meta_data_validation(required_fields)
        except BadSymbolException as err:
            print("Exception:", err.args[0])
            sys.exit(1)

def get_symbols_interpretation(symbols):
    cmd_list = []
    for s in symbols:
        symbol = Symbol(s)
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
    print("\n")
    print("Minecraft Interpretation..")

    print("Getting Symbol List..")
    symbols = get_symbol_list()

    print("Validating Symbols..")
    validate_symbols(symbols)

    print("Getting Interpretation..")
    out_string = get_symbols_interpretation(symbols)


    filename = "minecraft.mcfunction"
    write_output_to_file(filename=filename, output_string=out_string)
    print("Interpretation done. File:", filename)



if __name__ == "__main__":
    minecraft()