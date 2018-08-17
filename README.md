# Kukkura &middot; [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/aks-c/Kukkura/blob/master/LICENSE) [![CircleCI](https://circleci.com/gh/aks-c/Kukkura.svg?style=shield&circle-token=81b7f70aaab28074269d37e9ea109ca9746df77a)](https://circleci.com/gh/aks-c/Kukkura)

This is a [Procedural Generator](https://en.wikipedia.org/wiki/Procedural_generation "PG wiki page") based on [Context-Free](https://en.wikipedia.org/wiki/Context-free_grammar "CFG wiki page") rule systems.

For more information on the project, and stuff like how to use it and tutorials, visit the project [Wiki](https://github.com/aks-c/Kukkura/wiki).

The gist of it: 
- You specify some rules as input (in a bunch of `JSON` files).
- Those rules describe how you want to manipulate some initial set of symbols.
- The key here is that every symbol has some meta-data associated with it (e.g.: a `size` field, a `position` field, whether this symbol supports randomization, etc…).
- Then, according to your rules and the meta-data of each symbol, the system outputs some other set of symbols.

You can then interpret this output yourself to get the procedurally generated content for your purposes 
(i.e. you decide yourself what a "size of 10" means, etc..).
The program already supports Minecraft-compatible output 
(i.e. Minecraft users don't need to interpret the data, it's handled for them in one of the output files).
No support for other platforms/programs/games/etc (yet!), 
but the output is comprehensible enough that it is not too hard for someone to do so.

## Examples

Coming Soon...

Though, to get a taste of what one can do using Context-Free Grammars, you can see [this project](https://www.contextfreeart.org/index.html "another project that uses CFGs to produce content") (not mine) for example.

## Getting Started

### Prerequisites

The following are needed:
- `Maven` to build the executable `jar` (how to install Maven [here](https://maven.apache.org/install.html "How to install Maven.")).
- `Java SE Runtime Environment >1.8` to run (how to install the JRE [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html "How to install the JRE.")).

### Installing

- `git clone` the repo.
- `cd` into the repo.
- `mvn clean install` to create an executable `jar`.

The executable is generated inside the `/target` folder.

### Usage

Execute as follows:
```bash
java -jar nameOfExecutable.jar
```

- Devise context-free rules in one (or several) JSON files however you like. 
By default, the program will try to get its input from a folder `/input` in the same directory.
This can be changed by passing the appropriate argument through the command line.
- Run the program through the terminal.
By default, the output will be written in a folder `/output` in the same directory.
This can also be changed by passing an argument. 
- If you're a Minecraft user, you can load the `output.mcfunction` file directly into the game.
See how [here](https://www.digminecraft.com/game_commands/function_command.php "A tutorial showing how to use mcfunction files in Minecraft.").
- If you used this program to create content for another platform, you can use the `output.json` file.
The file contains all the information needed to interpret the content for your usage. 
It describes all the symbols outputted by the system, along with all their relevant meta-data. 

For more detailed explanations, examples, and tutorials, visit the project [Wiki](https://github.com/aks-c/Kukkura/wiki).

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
