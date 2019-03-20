import json

KUKKURA_OUTPUT_FOLDER = "../dev/output/"

INTERPRETERS_OUTPUT_FOLDER = "output/"

def get_symbol_list():
    with open(KUKKURA_OUTPUT_FOLDER + 'output.json') as input_file:
        symbols = json.load(input_file)
    return symbols

def write_output_to_file(filename, output_string):
    with open(INTERPRETERS_OUTPUT_FOLDER + filename, "w") as output_file:
        output_file.write(output_string)