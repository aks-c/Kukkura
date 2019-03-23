import json
from symbol import Symbol

KUKKURA_OUTPUT_FOLDER = "../dev/output/"

INTERPRETERS_OUTPUT_FOLDER = "output/"

def get_symbol_list():
    ''' Returns a list of symbols deserialised from the procedural generator's output. '''
    print("Getting Symbol List..")
    with open(KUKKURA_OUTPUT_FOLDER + 'output.json') as input_file:
        data = json.load(input_file)
    symbols = []
    for s in data:
        symbols.append(Symbol(s))

    print("Got", len(symbols), "symbols !")
    return symbols

def write_output_to_file(filename, output_string):
    ''' 
    Write a specific's interpreter's output to file.
    
    Note that you need only specify the file name, not the full path,
    because all intepreters' outputs are just assumed to be held in the same folder (interpretations/output).
    
     '''
    with open(INTERPRETERS_OUTPUT_FOLDER + filename, "w") as output_file:
        output_file.write(output_string)