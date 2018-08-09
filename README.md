# Voxel_PG &middot; [![GitHub license](https://img.shields.io/badge/license-MIT-blue.svg)](https://github.com/aks-c/Voxel_PG-v2/blob/oss_ready/LICENSE)

This is a Procedural Generator based on [Context-Free](https://en.wikipedia.org/wiki/Context-free_grammar) rule systems.
The gist of the system is that you specify rules as input (in JSON format) that manipulate some set of symbols.
The system then outputs another set of symbols.
Every symbol has some meta-data associated with it (e.g.: a `size` field, a `position` field, etc…).

You can then interpret this data to get content for what you’re doing (i.e. you decide yourself what a size of 10 means, etc..).
The program already supports Minecraft-compatible output.
No support for other platforms/games/etc yet, but the output is comprehensible enough to make this as easy as possible for someone to interpret it for their own use. 

To get a taste of what you can do using Context-Free Grammars, see this https://www.contextfreeart.org/index.html (not mine) for example.

## Examples

## Getting Started

### Prerequisites

For now the following are needed:
- maven
- java >1.8

Will work on an easier way to install (with an installer maybe ? Something else ?).

### Installing

Run the following in your terminal:

```bash
git clone https://github.com/aks-c/Voxel_PG-v2
cd path/to/repository
maven clean install
```

The `jar` inside the `/target` folder is the executable.

You execute it as follows:

```bash
java -jar nameOfExecutable.jar
```


### Usage

- Devise your context-free rules in one (or several) JSON files. 
- Run the program through the terminal.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.