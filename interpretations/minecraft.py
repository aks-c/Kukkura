import json
from symbol import Symbol
from file_io import get_symbol_list, write_output_to_file


def minecraft():
    ''' 
    The minecraft interpretation method.
    
    The Kukkura tool generates some set of symbols, and outputs this set to a json file.
    This interpreter (and the others) take this json file as input,
    and generate their own representation of this list of symbols,
    as appropriate for the platform/game/etc they target.

     '''
    print("Minecraft Interpretation")
    symbols = get_symbol_list()
    
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
    print(cmd_string)

    write_output_to_file(filename="minecraft.mcfunction", output_string=cmd_string)



if __name__ == "__main__":
    minecraft()