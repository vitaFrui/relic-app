package com.relic.app.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Handles reading the provided input and parsing out the most common word sequences from them. Consequential inputs are
 * considered in aggregate unless {@link #reset()} is called.
 */
public class TextReader {
    private static final Pattern WORD_PATTERN = Pattern.compile("([\\w]+[-']?[\\w]*)");
    private static final Pattern UNICODE_DASH_PATTERN = Pattern.compile("\\p{Pd}");
    private static final Pattern UNICODE_SINGLE_QUOTE_PATTERN = Pattern.compile("’");
    private static final Integer DEFAULT_RESULT_LIMIT = 100;
    private static final Integer SEQUENCE_OUTPUT_PADDING = 35;
    private static final String OUTPUT_SEPARATOR = "===========================================";
    private static final String HEADER_SEPARATOR = "-------------------------------------------";
    private static final String NEWLINE_SEPARATOR = System.getProperty("line.separator");

    private final Map<String, Integer> wordSequenceMap = new HashMap<>();
    private final List<String> sources = new ArrayList<>();
    private final Sequence<String> sequence;
    private final Integer resultCount;

    /**
     * Creates an instance of {@link TextReader} with default values.
     */
    public TextReader() {
        this(DEFAULT_RESULT_LIMIT, null);
    }

    /**
     * Creates an instance of {@link TextReader} with the values specified.
     *
     * @param resultCount The number of word sequences to report
     * @param sequenceSize The size of the word sequences to track
     */
    public TextReader(final Integer resultCount, final Integer sequenceSize) {
        this.resultCount = resultCount;
        sequence = (null == sequenceSize) ?  new Sequence<>() : new Sequence<>(sequenceSize);
    }

    /**
     * Processes StdIn for the most common word sequences.
     */
    public void processInput() {
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in, StandardCharsets.UTF_8))) {
            processInput("StdIn", reader);
        } catch (IOException e) {
            System.err.println("Unable to read input from StdIn: " + e);
        }
    }

    /**
     * Processes the provided file for the most common word sequences.
     *
     * @param file Name of file to process
     */
    public void processInput(final String file) {
        try (final BufferedReader reader = Files.newBufferedReader(Paths.get(file), StandardCharsets.UTF_8)) {
            processInput(file, reader);
        } catch (final IOException e) {
            System.err.println("There was an issue processing the file: " + file + "... " + e);
        }
    }

    /**
     * Processes the provided input for the most common word sequences.
     *
     * @param source Source of the input being processed (e.g. "StdIn" or a file name)
     * @param reader BufferedReader from which input is read
     * @throws IOException if an error is encountered trying to read the provided input
     */
    private void processInput(final String source, final BufferedReader reader) throws IOException {
        // keep track of currently processed sources - useful for providing context to formatted output
        sources.add(source);
        String line;

        while ((line = reader.readLine()) != null) {
            handleLine(line);
        }
    }

    /**
     * Contains logic to process an input line.
     *
     * @param line Line to process
     */
    private void handleLine(final String line) {
        if (line.isEmpty()) {
            // skip empty lines
            return;
        }

        // prepare the line and capture its words
        final Matcher matcher = WORD_PATTERN.matcher(prepareLine(line));

        // as long as we have a word, process it
        while (matcher.find()) {
            sequence.addToSequence(matcher.group());

            // if we have a valid sequence join it into a singular space delimited string and add it to our sequence map
            sequence.getSequence().ifPresent(strings -> {
                final StringJoiner joiner = new StringJoiner(" ");
                strings.forEach(joiner::add);
                wordSequenceMap.compute(joiner.toString(), (s, i) -> i == null ? 1 : i + 1);
            });
        }
    }

    /**
     * Returns the String formatted listing of the most common word sequences of the size specified up the result count
     * requested.
     *
     * @return formatted String containing top 100 most common word sequences
     */
    public String getMostCommonWords() {
        final StringBuilder sb = new StringBuilder().append(NEWLINE_SEPARATOR);

        if (sources.isEmpty()) {
            sb.append("No sources were provided, and thus no output is available! =^-^=").append(NEWLINE_SEPARATOR);
        } else {
            final StringJoiner sj = new StringJoiner(" | ");
            sources.forEach(sj::add);
            sb.append("Here are the most common word sequences for")
                    .append(sources.size() == 1 ? " " : " the following combined sources: ")
                    .append(sj)
                    .append(NEWLINE_SEPARATOR)
                    .append(NEWLINE_SEPARATOR)
                    .append("Sequence                            | Count")
                    .append(NEWLINE_SEPARATOR)
                    .append(HEADER_SEPARATOR)
                    .append(NEWLINE_SEPARATOR);
        }

        wordSequenceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(resultCount)
                .forEach(e -> {
                    sb.append(String.format("%-" + SEQUENCE_OUTPUT_PADDING + "s", e.getKey()))
                            .append(" | ")
                            .append(e.getValue())
                            .append(NEWLINE_SEPARATOR);
                });

        return sb.append(OUTPUT_SEPARATOR).toString();
    }

    /**
     * Helper method that prepares the input line for parsing by:
     *
     * <ul>
     *     <li>Replacing unicode punctuation dashes with an ascii dash (-)</li>
     *     <li>Replacing unicode single quotes with an ascii single quote</li>
     *     <li>Setting the entire line to lowercase to facilitate case in-sensitive matching</li>
     * </ul>
     *
     * @param line Line to clean up
     * @return the line with unicode cleaned up and replaced with ascii alternatives
     */
    private String prepareLine(final String line) {
        String toReturn = line.toLowerCase(Locale.ROOT);

        toReturn = UNICODE_DASH_PATTERN.matcher(toReturn).replaceAll("-");
        toReturn = UNICODE_SINGLE_QUOTE_PATTERN.matcher(toReturn).replaceAll("'");

        return toReturn;
    }

    /**
     * Method that resets the tracked data held by this class instance. Useful for resetting data between processed inputs.
     */
    public void reset() {
        wordSequenceMap.clear();
        sequence.clear();
        sources.clear();
    }
}
