import org.apache.commons.cli.*;

/**
 * Created by akselcakmak on 27/12/2018.
 *
 */
public class CommandLineInput {
    private String args[];

    private String inputFolder;
    private String subFolder;
    private String mainInputFile;
    private String outputFolder;

    private static final String DEFAULT_INPUT_FOLDER = "./input";
    private static final String DEFAULT_INPUT_SUBFOLDER = "/sub/";
    private static final String DEFAULT_INPUT_MAINFILE = "/playground.json";
    private static final String DEFAULT_OUTPUT_FOLDER = "./output/";

    private Options options = new Options();

    private HelpFormatter formatter = new HelpFormatter();
    private CommandLine line;

    public CommandLineInput(String args[]) {
        this.args = args;
        addAllOptions();
    }

    public void parseInput() throws ParseException {
        CommandLineParser parser = new DefaultParser();

        line = parser.parse(options, args);
        inputFolder = line.getOptionValue("inputFolder", DEFAULT_INPUT_FOLDER);
        subFolder = line.getOptionValue("subFolder", DEFAULT_INPUT_SUBFOLDER);
        mainInputFile = line.getOptionValue("mainFile", DEFAULT_INPUT_MAINFILE);
        outputFolder = line.getOptionValue("outputFolder", DEFAULT_OUTPUT_FOLDER);
    }

    private void addAllOptions() {
        Option help = new Option("help", "Print this message.");
        Option verbose = new Option("terse", "Terse output.");
        Option inputFolder   = Option.builder("inputFolder")
                .argName( "in" )
                .longOpt("inputFolder")
                .hasArg()
                .desc("The main input folder. Defaults to ./input")
                .required(false)
                .build();
        Option subFolder = Option.builder("subFolder")
                .argName( "sub" )
                .longOpt("subFolder")
                .hasArg()
                .desc("The sub input folder. Located inside the input folder. Defaults to /sub/")
                .required(false)
                .build();
        Option mainFile = Option.builder("mainFile")
                .argName( "main" )
                .longOpt("mainFile")
                .hasArg()
                .desc("The main input file. Located inside the input folder. Defaults to /playground.json")
                .required(false)
                .build();
        Option outputFolder = Option.builder("outputFolder")
                .argName( "out" )
                .longOpt("outputFolder")
                .hasArg()
                .desc("The main output folder. Defaults to ./output/")
                .required(false)
                .build();

        options.addOption(help);
        options.addOption(verbose);
        options.addOption(inputFolder);
        options.addOption(subFolder);
        options.addOption(mainFile);
        options.addOption(outputFolder);
    }

    public void printHelp() { formatter.printHelp( "Kukkura", options ); }

    public boolean hasOption(String option) { return line.hasOption(option); }

    public String getInputFolder() { return inputFolder; }
    public String getSubFolder() { return subFolder; }
    public String getMainInputFile() { return mainInputFile; }
    public String getOutputFolder() { return outputFolder; }
}


