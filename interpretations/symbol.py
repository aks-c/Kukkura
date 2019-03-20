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

    def get_second_position(self):
        second_pos = { 
            "x": int(self.position.get("x")) + int(self.size.get("x")),
            "y": int(self.position.get("y")) + int(self.size.get("y")),
            "z": int(self.position.get("z")) + int(self.size.get("z")),
        }
        return second_pos
        
        