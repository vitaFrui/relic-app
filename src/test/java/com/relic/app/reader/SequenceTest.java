package com.relic.app.reader;

import org.junit.Before;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;

import static org.junit.Assert.*;

/**
 * Unit tests for {@link Sequence}.
 */
public class SequenceTest {
    private static final Integer SEQ_SIZE = 3;
    private Sequence<String> sequence;

    @Before
    public void init() {
        // most tests will use default sequence constructor so consolidate that here and let unit tests override as needed
        sequence = new Sequence<>();
    }

    @Test
    public void should_init_sequence_with_default_size() {
        int i = 0;

        while (i < SEQ_SIZE) {
            i++;
            sequence.addToSequence("string");
            assertEquals(i == SEQ_SIZE, sequence.getSequence().isPresent());
        }
    }

    @Test
    public void should_init_sequence_with_custom_size() {
        final int customSeqSize = 5;
        sequence = new Sequence<>(customSeqSize);

        int i = 0;

        while (i < customSeqSize) {
            i++;
            sequence.addToSequence("string");
            assertEquals(i == customSeqSize, sequence.getSequence().isPresent());
        }
    }

    @Test
    public void should_maintain_specified_sequence_size() {
        int i = 0;

        while (i < 10) {
            i++;
            sequence.addToSequence("string");
            assertEquals(i >= SEQ_SIZE, sequence.getSequence().map(List::size).orElse(0).equals(SEQ_SIZE));
        }
    }

    @Test
    public void should_return_sequence_correctly() {
        sequence.addToSequence("stringOne");
        sequence.addToSequence("stringTwo");
        sequence.addToSequence("stringThree");
        assertEquals(Arrays.asList("stringOne", "stringTwo", "stringThree"), sequence.getSequence().orElse(null));
        sequence.addToSequence("stringFour");
        assertEquals(Arrays.asList("stringTwo", "stringThree", "stringFour"), sequence.getSequence().orElse(null));
    }

    @Test
    public void should_reset_sequence_when_asked() {
        sequence.addToSequence("stringOne");
        sequence.addToSequence("stringTwo");
        sequence.addToSequence("stringThree");
        assertEquals(Arrays.asList("stringOne", "stringTwo", "stringThree"), sequence.getSequence().orElse(null));
        sequence.clear();
        assertFalse(sequence.getSequence().isPresent());
    }
}
