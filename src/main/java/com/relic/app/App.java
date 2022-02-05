package com.relic.app;


import com.relic.app.reader.TextReader;

import java.util.Arrays;

/**
 * Main class - handles instantiation of the application.
 */
public class App {
    /**
     * Argument that denotes that the application should report and reset its word sequence tracking after each input is
     * processed, rather than processing and reporting it as a singular aggregate input.
     */
    private static final String RESET_EACH_INPUT = "resetEach";

    /**
     * Executes the program, processing provided input. The following are valid parameters:
     *
     * <ul>
     *     <li>A singular file name</li>
     *     <li>OR a space delimited list of file names</li>
     *     <li>OR console input via StdIn</li>
     * </ul>
     *
     * @param args User provided arguments
     */
    public static void main(final String[] args) {
        final boolean isResetEachInput = null != System.getProperty(RESET_EACH_INPUT);
        final TextReader textReader = new TextReader();

        if (null != args && args.length > 0) {
            System.out.println("Parsing the following file(s): " + Arrays.toString(args));

            for (final String arg : args) {
                textReader.processInput(arg);

                if (isResetEachInput) {
                    System.out.println(textReader.getMostCommonWords());
                    textReader.reset();
                }
            }

            if (!isResetEachInput) {
                // if not reporting and resetting after each input make sure we do it after all file inputs are processed
                System.out.println(textReader.getMostCommonWords());
            }
        } else {
            System.out.println("Parsing StdIn");
            // Attempt to process StdIn
            textReader.processInput();

            System.out.println(textReader.getMostCommonWords());
        }
    }
}
