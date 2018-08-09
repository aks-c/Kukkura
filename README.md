# Voxel_PG &middot; [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/aks-c/Voxel_PG-v2/blob/oss_ready/LICENSE)

This is a Procedural Generator based on [Context-Free](https://en.wikipedia.org/wiki/Context-free_grammar "CFG wiki page") rule systems.
The gist of the system is that you specify rules as input (in JSON format) that manipulate some set of symbols.
The system then outputs another set of symbols.
Every symbol has some meta-data associated with it (e.g.: a `size` field, a `position` field, etc…).

You can then interpret this data yourself to get the procedurally generated content for your purposes (i.e. you decide yourself what a size of 10 means, etc..).
The program already supports Minecraft-compatible output (i.e. Minecraft users don't need to interpret the data, it's handled for them in one of the output files).
No support for other platforms/programs/games/etc (yet!), but the output is comprehensible enough that it is not too hard for someone to interpret it for their own use. 

To get a taste of what you can do using Context-Free Grammars, see this https://www.contextfreeart.org/index.html (not mine) for example.

## Examples

Coming Soon...

## Getting Started

### Prerequisites

The following are needed:
- `Maven` (to build the executable `jar`) (how to install [here](https://maven.apache.org/install.html "how to install maven"))
- `Java SE Runtime Environment >1.8` (to run) (how to install [here](http://www.oracle.com/technetwork/java/javase/downloads/jre8-downloads-2133155.html "how to install the jre"))

### Installing

- `git clone` the repository.
- `cd` into the repo.
- `maven clean install` to create the executable `jar`.

The executable is inside the `/target` folder.

### Usage

Execute as follows:
```bash
java -jar nameOfExecutable.jar
```

- Devise your context-free rules in one (or several) JSON files. 
- Run the program through the terminal.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.