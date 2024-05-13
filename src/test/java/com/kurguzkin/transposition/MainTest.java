package com.kurguzkin.transposition;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import java.util.List;

import static com.kurguzkin.transposition.Main.*;
import static org.junit.jupiter.api.Assertions.*;

class MainTest {

    @Test
    void notesReadingTest() throws JsonProcessingException {
        var lists = readNotes("[[1,2],[3,4]]");
        assertEquals(2, lists.size());
        assertEquals(List.of(List.of(1, 2), List.of(3, 4)), lists);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
             0 |  1 |  1 |  0 |  2
             0 | 12 |  1 |  1 |  1
             0 |  1 | -1 | -1 | 12
            -1 | 12 |  1 |  0 |  1
            -1 |  1 | 12 |  0 |  1
             0 |  1 |-12 | -1 |  1
            """)
    void shiftNoteTest(int octave, int halfTone, int shift, int expectedOctave, int expectedHalfTone) {
        List<Integer> newNote = shiftNote(List.of(octave, halfTone), shift);
        assertEquals(List.of(expectedOctave, expectedHalfTone), newNote);
    }

    @ParameterizedTest
    @CsvSource(delimiter = '|', textBlock = """
             0 |  1 |  100
             5 |  1 |  1
             0 |  1 | -100
            -3 | 10 | -1
            """)
    void shiftNoteOutOfKeyboardTest(int octave, int halfTone, int shift) {
        assertThrows(OutOfKeyboardException.class, () -> shiftNote(List.of(octave, halfTone), shift));
    }

    @Test
    void proposedTest() throws JsonProcessingException {
        var input = "[[2,1],[2,6],[2,1],[2,8],[2,1],[2,9],[2,1],[2,6],[2,1],[2,8],[2,1],[2,9],[2,1],[2,11],[2,1],[2,8],[2,1],[2,9],[2,1],[2," +
                "11],[2,1],[3,1],[2,1],[2,9],[2,1],[2,11],[2,1],[3,1],[2,1],[3,2],[2,1],[2,11],[2,1],[3,1],[2,1],[2,9],[2,1],[2,11],[2," +
                "1],[2,8],[2,1],[2,9],[2,1],[2,6],[2,1],[2,8],[2,1],[2,5],[2,1],[2,6],[2,1],[2,1],[2,1],[2,2],[2,1],[1,11],[2,1],[2,1],[" +
                "2,1],[1,9],[2,1],[1,11],[2,1],[1,8],[2,1],[1,9],[2,1],[1,6],[2,1],[1,11],[2,1],[1,8],[2,1],[1,9],[2,1],[1,6],[2,1],[1," +
                "8],[2,1],[1,5],[2,1],[1,6]]";
        
        var expected = "[[1,10],[2,3],[1,10],[2,5],[1,10],[2,6],[1,10],[2,3],[1,10],[2,5],[1,10],[2,6],[1,10],[2,8],[1,10],[2,5],[1,10],[2," +
                "6],[1,10],[2,8],[1,10],[2,10],[1,10],[2,6],[1,10],[2,8],[1,10],[2,10],[1,10],[2,11],[1,10],[2,8],[1,10],[2,10],[1," +
                "10],[2,6],[1,10],[2,8],[1,10],[2,5],[1,10],[2,6],[1,10],[2,3],[1,10],[2,5],[1,10],[2,2],[1,10],[2,3],[1,10],[1,10]," +
                "[1,10],[1,11],[1,10],[1,8],[1,10],[1,10],[1,10],[1,6],[1,10],[1,8],[1,10],[1,5],[1,10],[1,6],[1,10],[1,3],[1,10],[1" +
                ",8],[1,10],[1,5],[1,10],[1,6],[1,10],[1,3],[1,10],[1,5],[1,10],[1,2],[1,10],[1,3]]";

        var inputList = readNotes(input);
        var expectedList = readNotes(expected);

        var transposed = transpose(inputList, -3);
        assertEquals(expectedList, transposed);
    }
}