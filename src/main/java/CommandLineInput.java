import org.apache.commons.cli.Option;
import org.apache.commons.cli.Options;

/**
 * Created by akselcakmak on 27/12/2018.
 *
 */
public class CommandLineInput {

    private Options options = new Options();

    public CommandLineInput() {
        addAllOptions();
    }

    private void addAllOptions() {
        Option help = new Option("help", "Print this message.");
        Option verbose = new Option("verbose", "Verbose output.");
        Option inputFolder   = Option.builder("input folder")
                .argName( "in" )
                .longOpt("inputFolder")
                .hasArg()
                .desc("The main input folder. Defaults to ./input/")
                .required(false)
                .build();
        Option subFolder = Option.builder("sub input folder")
                .argName( "sub" )
                .longOpt("subFolder")
                .hasArg()
                .desc("The sub input folder. Located inside the input folder. Defaults to /sub/")
                .required(false)
                .build();
        Option mainFile = Option.builder("main file")
                .argName( "main" )
                .longOpt("mainFile")
                .hasArg()
                .desc("The main input file. Located inside the input folder. Defaults to /playground.json")
                .required(false)
                .build();
        Option outputFolder = Option.builder("output folder")
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
}


