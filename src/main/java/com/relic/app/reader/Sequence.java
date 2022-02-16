package com.relic.app.reader;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

/**
 * Data object that holds a specified number of elements T in sequence.
 */
public class Sequence<T> {
    private final static Integer DEFAULT_SEQUENCE_SIZE = 3;
    private final Integer sequenceSize;
    private final List<T> sequenceList;

    /**
     * Creates an instance of {@link Sequence} with the default sequence size of {@value DEFAULT_SEQUENCE_SIZE}.
     */
    public Sequence() {
        this(DEFAULT_SEQUENCE_SIZE);
    }

    /**
     * Creates an instance of {@link Sequence} with the specified sequence size.
     *
     * @param sequenceSize Size of sequence
     */
    public Sequence(final Integer sequenceSize) {
        this.sequenceSize = sequenceSize;
        this.sequenceList = new ArrayList<>(sequenceSize);
    }

    /**
     * Adds the provided element to the sequence. If adding the element causes the sequence to surpass the size
     * specified then removes an element from the start of the sequence.
     *
     * @param element Element to add
     */
    public void addToSequence(final T element) {
        if (sequenceList.size() >= sequenceSize) {
            // if we are at the sequence size limit remove the first element
            sequenceList.remove(0);
        }

        sequenceList.add(element);
    }

    /**
     * Returns the current sequence so long as it meets the size requirement specified when this {@link Sequence} was
     * created.
     *
     * @return the sequence if adequate elements are present, an empty optional otherwise
     */
    public Optional<List<T>> getSequence() {
        if (sequenceList.size() != sequenceSize) {
            // sequence does not have enough elements in it so return nothing
            return Optional.empty();
        }

        return Optional.of(new ArrayList<>(sequenceList));
    }

    /**
     * Clears out all elements in the sequence.
     */
    public void clear() {
        sequenceList.clear();
    }
}
