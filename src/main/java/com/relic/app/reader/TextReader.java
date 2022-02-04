package com.relic.app.reader;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
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
    private static final Pattern WORD_PATTERN = Pattern.compile("([\\w]+[’']?[\\w]*)");
    private static final char NEWLINE_CHAR = '\n';
    private static final Integer DEFAULT_RESULT_LIMIT = 100;

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
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(System.in))) {
            processInput("StdIn", reader);
        } catch (IOException e) {
            System.out.println("Unable to read input from StdIn: " + e);
        }
    }

    /**
     * Processes the provided file for the most common word sequences.
     *
     * @param file Name of file to process
     */
    public void processInput(final String file) {
        try (final BufferedReader reader = Files.newBufferedReader(Paths.get(file))) {
            processInput(file, reader);
        } catch (final IOException e) {
            System.out.println("There was an issue processing the file: " + file + "... " + e);
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

        final Matcher matcher = WORD_PATTERN.matcher(line);

        while (matcher.find()) {
            sequence.addToSequence(matcher.group().toLowerCase(Locale.ROOT).replace('’', '\''));
            sequence.getSequence().ifPresent(strings -> {
                final StringJoiner joiner = new StringJoiner(" ");
                strings.forEach(joiner::add);
                wordSequenceMap.compute(joiner.toString(), (s, i) -> i == null ? 1 : i + 1);
            });
        }
    }

    /**
     * Returns the String formatted listing of the 100 most common word sequences of the size specified.
     *
     * @return formatted String containing top 100 most common word sequences
     */
    public String getMostCommonWords() {
        final StringBuilder sb = new StringBuilder();

        if (sources.isEmpty()) {
            sb.append("No sources were provided, and thus no output is available! =^-^=");
        } else {
            final StringJoiner sj = new StringJoiner(" | ");
            sources.forEach(sj::add);
            sb.append("Here are the most common word sequences for")
                    .append(sources.size() == 1 ? " " : " the following combined sources: ")
                    .append(sj)
                    .append(NEWLINE_CHAR);
        }

        wordSequenceMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .limit(resultCount)
                .forEach(e -> sb.append(e.getKey()).append(" - ").append(e.getValue()).append(NEWLINE_CHAR));

        return sb.toString();
    }

    /**
     * Method that resets the tracked map of word sequences to occurrences. Useful for resetting counts between files.
     */
    public void reset() {
        wordSequenceMap.clear();
        sequence.clear();
        sources.clear();
    }
}
