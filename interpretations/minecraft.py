import json
from symbol import Symbol


def minecraft():
    print("Minecraft Interpretation")
    with open('../dev/output/output.json') as f:
        symbols = json.load(f)
    
    cmd_list = []

    
    for s in symbols:
        symbol = Symbol(s)
        #print(symbol, "\n")
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

    



if __name__ == "__main__":
    minecraft()