# Interpretations

The workflow of someone using the tool is basically as follows:
- Develop rules and an L-system.
- Feed these rules to the Kukkura procedural generator.
- Run an interpreter script to interpret Kukkura's input.
  - The output is in `interpretations/output`.
  - For example, `minecraft.py` reads the symbols generated 
    by Kukkura and translates them to a series of commands Minecraft can understand.
  - If an interpreter doesn't exist, just create one in this directory.
  