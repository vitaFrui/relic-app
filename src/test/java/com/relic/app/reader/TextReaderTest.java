package com.relic.app.reader;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Unit tests for {@link TextReader}.
 */
public class TextReaderTest {
    private static final String MOBY_DICK_FILE = "src/test/resources/files/moby-dick.txt";
    private static final String BROTHERS_KARAMAZOV_FILE = "src/test/resources/files/brothers-karamazov.txt";

    private static final Path MOBY_DICK_RESULTS_FILE = new File("src/test/resources/results/moby-dick-results.txt").toPath();
    private static final Path BROTHERS_KARAMAZOV_RESULTS_FILE = new File("src/test/resources/results/brothers-karamazov-results.txt").toPath();
    private static final Path AGGREGATE_RESULTS_FILE = new File("src/test/resources/results/aggregate-results.txt").toPath();
    private static final Path GENERIC_TEST_RESULTS_FILE = new File("src/test/resources/results/generic-test-results.txt").toPath();

    private static final String NEWLINE_SEPARATOR = System.getProperty("line.separator");
    private static final String EMPTY_READER_RESULT = NEWLINE_SEPARATOR +
            "No sources were provided, and thus no output is available! =^-^=" +
            NEWLINE_SEPARATOR +
            "===========================================";

    private final InputStream stdIn = System.in;
    private final PrintStream stdOut = System.out;
    private final PrintStream errOut = System.err;

    private ByteArrayInputStream mockInput;
    private ByteArrayOutputStream mockOutput;
    private ByteArrayOutputStream mockErrOutput;

    private TextReader textReader;

    @Before
    public void init() {
        // prepare mock output stream
        mockOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(mockOutput));

        // prepare mock err stream
        mockErrOutput = new ByteArrayOutputStream();
        System.setErr(new PrintStream(mockErrOutput));

        // initialize default TextReader instance
        textReader = new TextReader();
    }

    @After
    public void reset() {
        System.setIn(stdIn);
        System.setOut(stdOut);
        System.setErr(errOut);
    }

    private void setInput(final String data) {
        mockInput = new ByteArrayInputStream(data.getBytes(StandardCharsets.UTF_8));
        System.setIn(mockInput);
    }

    @Test
    public void should_handle_if_no_file() {
        textReader.processInput("badfileName.txt");
        assertTrue(mockErrOutput.toString().contains("There was an issue processing the file: badfileName.txt"));
    }

    @Test
    public void should_handle_if_no_stdin() {
        setInput("");

        textReader.processInput();
    }

    @Test
    public void should_parse_file() throws IOException {
        textReader.processInput(MOBY_DICK_FILE);
        assertEquals(toString(MOBY_DICK_RESULTS_FILE), textReader.getMostCommonWords());

    }

    @Test
    public void should_parse_multiple_files_in_aggregate() throws IOException {
        textReader.processInput(MOBY_DICK_FILE);
        textReader.processInput(BROTHERS_KARAMAZOV_FILE);

        assertEquals(toString(AGGREGATE_RESULTS_FILE), textReader.getMostCommonWords());
    }

    @Test
    public void should_reset_when_asked() throws IOException {
        textReader.processInput(MOBY_DICK_FILE);
        assertEquals(toString(MOBY_DICK_RESULTS_FILE), textReader.getMostCommonWords());

        textReader.reset();
        assertEquals(EMPTY_READER_RESULT, textReader.getMostCommonWords());

        textReader.processInput(BROTHERS_KARAMAZOV_FILE);
        assertEquals(toString(BROTHERS_KARAMAZOV_RESULTS_FILE), textReader.getMostCommonWords());
    }

    @Test
    public void should_handle_unicode_chars() throws IOException {
        setInput("The beef was fine—tough. The beef was fine—tough. The beef was fine—tough. " +
                "The bread—but that couldn’t be helped. The bread—but that couldn’t be helped.\n");

        textReader.processInput();
        assertEquals(toString(GENERIC_TEST_RESULTS_FILE), textReader.getMostCommonWords());
    }

    @Test
    public void should_handle_dashes_and_contractions() throws IOException {
        // while this appears similar to the above test the difference is this string uses ascii single quotes and dashes
        setInput("The beef was fine-tough. The beef was fine-tough. The beef was fine-tough. " +
                "The bread-but that couldn't be helped. The bread-but that couldn't be helped.\n");

        textReader.processInput();
        assertEquals(toString(GENERIC_TEST_RESULTS_FILE), textReader.getMostCommonWords());
    }

    @Test
    public void should_handle_sequences_across_line_breaks() throws IOException {
        setInput("The beef\nwas fine-tough. The beef was\r\nfine-tough. The beef was fine-tough. " +
                "The bread-but that couldn't be helped.\r\nThe bread-but that couldn't be helped.\n");

        textReader.processInput();
        assertEquals(toString(GENERIC_TEST_RESULTS_FILE), textReader.getMostCommonWords());
    }

    private static String toString(final Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }
}
