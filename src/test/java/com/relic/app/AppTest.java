package com.relic.app;

import static org.junit.Assert.assertEquals;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Unit test for {@link App}. Lot of overlap with {@link com.relic.app.reader.TextReaderTest}, but this is necessary due
 * to the nature of this program.
 */
public class AppTest {
    private static final String MOBY_DICK_FILE = "src/test/resources/files/moby-dick.txt";
    private static final String BROTHERS_KARAMAZOV_FILE = "src/test/resources/files/brothers-karamazov.txt";

    private static final Path MOBY_DICK_RESULTS_FILE = new File("src/test/resources/results/moby-dick-results.txt").toPath();
    private static final Path BROTHERS_KARAMAZOV_RESULTS_FILE = new File("src/test/resources/results/brothers-karamazov-results.txt").toPath();
    private static final Path AGGREGATE_RESULTS_FILE = new File("src/test/resources/results/aggregate-results.txt").toPath();
    private static final String NEWLINE_SEPARATOR = System.getProperty("line.separator");

    private final InputStream stdIn = System.in;
    private final PrintStream stdOut = System.out;
    private final PrintStream errOut = System.err;

    private ByteArrayInputStream mockInput;
    private ByteArrayOutputStream mockOutput;
    private ByteArrayOutputStream mockErrOutput;

    @Before
    public void init() {
        // prepare mock output stream
        mockOutput = new ByteArrayOutputStream();
        System.setOut(new PrintStream(mockOutput));

        // prepare mock err stream
        mockErrOutput = new ByteArrayOutputStream();
        System.setErr(new PrintStream(mockErrOutput));
    }

    @After
    public void reset() {
        System.setIn(stdIn);
        System.setOut(stdOut);
        System.setErr(errOut);
    }

    private void setInput(final String data) {
        mockInput = new ByteArrayInputStream(data.getBytes());
        System.setIn(mockInput);
    }

    @Test
    public void should_expect_stdin_if_no_args() {
        setInput("");
        App.main(null);

        assertEquals("Parsing StdIn" + NEWLINE_SEPARATOR + NEWLINE_SEPARATOR +
                "Here are the most common word sequences for StdIn" + NEWLINE_SEPARATOR + NEWLINE_SEPARATOR +
                "Sequence                            | Count" + NEWLINE_SEPARATOR +
                "-------------------------------------------" + NEWLINE_SEPARATOR +
                "===========================================" + NEWLINE_SEPARATOR, mockOutput.toString());

        System.setOut(stdOut);
        App.main(new String[]{});

        assertEquals("Parsing StdIn" + NEWLINE_SEPARATOR + NEWLINE_SEPARATOR +
                "Here are the most common word sequences for StdIn" + NEWLINE_SEPARATOR + NEWLINE_SEPARATOR +
                "Sequence                            | Count" + NEWLINE_SEPARATOR +
                "-------------------------------------------" + NEWLINE_SEPARATOR +
                "===========================================" + NEWLINE_SEPARATOR, mockOutput.toString());
    }

    @Test
    public void should_handle_bad_files() {
        App.main(new String[]{"badfile.txt"});

        assertEquals("Parsing the following file(s): [badfile.txt]" + NEWLINE_SEPARATOR + NEWLINE_SEPARATOR +
                "No sources were provided, and thus no output is available! =^-^=" + NEWLINE_SEPARATOR +
                "===========================================" + NEWLINE_SEPARATOR, mockOutput.toString());
    }

    @Test
    public void should_handle_file() throws IOException {
        App.main(new String[]{MOBY_DICK_FILE});

        assertEquals("Parsing the following file(s): [src/test/resources/files/moby-dick.txt]" +
                NEWLINE_SEPARATOR +
                toString(MOBY_DICK_RESULTS_FILE) +
                NEWLINE_SEPARATOR, mockOutput.toString());
    }

    @Test
    public void should_handle_files() throws IOException {
        App.main(new String[]{MOBY_DICK_FILE, BROTHERS_KARAMAZOV_FILE});

        assertEquals("Parsing the following file(s): [src/test/resources/files/moby-dick.txt, src/test/resources/files/brothers-karamazov.txt]" +
                NEWLINE_SEPARATOR +
                toString(AGGREGATE_RESULTS_FILE) +
                NEWLINE_SEPARATOR, mockOutput.toString());
    }

    @Test
    public void should_handle_files_with_reset_each_enabled() throws IOException {
        // set the reset property
        System.setProperty("resetEach", "");
        App.main(new String[]{MOBY_DICK_FILE, BROTHERS_KARAMAZOV_FILE});

        assertEquals("Parsing the following file(s): [src/test/resources/files/moby-dick.txt, src/test/resources/files/brothers-karamazov.txt]" +
                NEWLINE_SEPARATOR +
                toString(MOBY_DICK_RESULTS_FILE) +
                NEWLINE_SEPARATOR +
                toString(BROTHERS_KARAMAZOV_RESULTS_FILE) +
                NEWLINE_SEPARATOR, mockOutput.toString());

        System.clearProperty("resetEach");
    }

    @Test
    public void should_handle_stdin() throws IOException {
        setInput(toString(new File(MOBY_DICK_FILE).toPath()));
        App.main(null);

        assertEquals("Parsing StdIn" +
                NEWLINE_SEPARATOR +
                toString(MOBY_DICK_RESULTS_FILE).replace("src/test/resources/files/moby-dick.txt", "StdIn") +
                NEWLINE_SEPARATOR, mockOutput.toString());
    }

    private static String toString(final Path path) throws IOException {
        return new String(Files.readAllBytes(path));
    }
}
