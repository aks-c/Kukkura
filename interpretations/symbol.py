class Symbol():
    '''
    Helper class to hold information and basic logic pertaining to Symbols.
    '''
    def __init__(self, s):
        self.symbol_name = s.get('symbol')
        self.meta_data = s.get('meta-data')
        self.size = s.get('size')
        self.position = s.get('position')
    
    def __str__(self):
        string = []
        string.append("symbol:")
        string.append(self.symbol_name)
        string.append("position (x, y, z):")
        string.append(self.position.get("x") + " " + self.position.get("y") + " " + self.position.get("z"))
        string.append("size (x, y, z):")
        string.append(self.size.get("x") + " " + self.size.get("y") + " " + self.size.get("z"))
        string.append("meta-data:")
        for k, v in self.meta_data.items():
            string.append(k + ": " + v)
        return "\n".join(string)

    def get_maxmin_coordinates(self, dimension, max_min):
        first_pos = int(self.position.get(dimension))
        second_pos = self.get_second_position().get(dimension)
        return max_min(first_pos, second_pos)

    def get_second_position(self):
        second_pos = { 
            "x": int(self.position.get("x")) + int(self.size.get("x")),
            "y": int(self.position.get("y")) + int(self.size.get("y")),
            "z": int(self.position.get("z")) + int(self.size.get("z")),
        }
        return second_pos

    def meta_data_validation(self, required_fields):
        is_valid = True
        errors = list()
        errors.append("Error on Symbol " + self.symbol_name)
        for k in required_fields:
            if k not in self.meta_data:
                errors.append("Key " + k + " is needed, but not in the meta-data dictionary.")
                errors.append("Add the required field, by either manually editing output.json, or changing the rules of the procedural generator.")
                is_valid = False
        if not is_valid:
            raise BadSymbolException("\n".join(errors))


class BadSymbolException(Exception):
    ''' 
    Exception raised when a Symbol doesn't contain the right meta-data required by an interpreter. 
    
    This is thrown by validation checks in the Symbol class.
    eg: an interpreter requires the "foo" key for a Symbol's meta-data;
    The dict of some symbol doesn't have a "foo" key => This is raised.
    '''
    pass
        
        