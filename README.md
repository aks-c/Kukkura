# Kukkura &middot; [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/aks-c/Kukkura/blob/master/LICENSE) [![CircleCI](https://circleci.com/gh/aks-c/Kukkura.svg?style=shield&circle-token=81b7f70aaab28074269d37e9ea109ca9746df77a)](https://circleci.com/gh/aks-c/Kukkura)

This is a [Procedural Generator](https://en.wikipedia.org/wiki/Procedural_generation "PG wiki page") based on [Context-Free Grammars](https://en.wikipedia.org/wiki/Context-free_grammar "CFG wiki page") and [L-Systems](https://en.wikipedia.org/wiki/L-system).

For more information on the project, and stuff like how to use it and tutorials, 
visit the project [Wiki](https://github.com/aks-c/Kukkura/wiki).

The gist of it: 
- You specify some rules as input (in a bunch of `JSON` files).
- Those rules describe how you want to manipulate some initial set of symbols.
- The key here is that every symbol has some meta-data associated with it (_e.g.:_ a `size` field, a `position` field, whether this symbol supports randomization, etc...).
- Then, according to your rules and the meta-data of each symbol, the system outputs some other set of symbols.

You can then express extremely complex and recursive structures.

You can then interpret/serialize this output yourself for your target platform/program/thingy.  
(_i.e.:_ you decide yourself what this "size" thingy means, or what it means for that symbol here to be at this position there, etc..).
The program already supports Minecraft-compatible output 
(_i.e.:_ Minecraft users don't need to serialize the data, it's handled for them in one of the output files).
No support for other platforms/programs/games/etc (yet!), 
but the output is comprehensible enough that it is not too hard for someone to do so.

## Examples

Coming Soon...


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

This is a very rough overview of how to use this program.
You can find actual detailed explanations, tutorials, examples, and other cool stuff on the project [Wiki](https://github.com/aks-c/Kukkura/wiki).

But anyway.
You execute the program as follows:
```bash
java -jar Kukkura.jar
```

Roughly, what you do is the following:
- Devise context-free rules in one (or several) JSON files. 
- Run the program through the terminal.
- If you're a Minecraft user, you can load the generated `output.mcfunction` file directly into your game and enjoy your creation without any need for serialization (the reason is that I did this initially with Minecraft in mind, then changed mid-way when I realized it could be used for pretty much any platform, provided one would do the necessary serialization).
See how [here](https://www.digminecraft.com/game_commands/function_command.php "A tutorial showing how to use mcfunction files in Minecraft.").
- If you use this program to create content for another platform, you have to serialize the `output.json` file. 

## Contributing

Any contribution or help is welcome !
Please don't hesitate to generate issues/PRs if you think a feature should be added, some code could be cleaned up, some typos caught your eye, you want to add examples/tutorials, or anything else really !

A `CONTRIBUTING.MD` guide is coming soon, to help with that process.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
