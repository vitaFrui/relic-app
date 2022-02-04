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
     * Executes the program. Accepts a variety of parameters to perform different operations. The following are valid
     * parameters:
     *
     * <ul>
     *     <li>A singular file (to process)</li>
     *     <li>A space delimited list of file names (to process)</li>
     * </ul>
     *
     * @param args User provided arguments
     */
    public static void main(final String[] args) {
        final boolean isResetEachInput = null != System.getProperty(RESET_EACH_INPUT);

        final TextReader textReader = new TextReader();

        System.out.println("Parsing the following file: " + Arrays.toString(args));

        if (null != args && args.length > 0) {
            for (final String arg : args) {
                textReader.processInput(arg);

                if (isResetEachInput) {
                    System.out.println(textReader.getMostCommonWords());
                    textReader.reset();
                }
            }

            if (!isResetEachInput) {
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
