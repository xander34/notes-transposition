package com.kurguzkin.transposition;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.ArrayList;
import java.util.List;

public class Main {

    public static final int lowestKey = -3 * 12 + 10 - 1 ;
    public static final int highestKey = 5 * 12 + 1 - 1;

    private final static ObjectMapper mapper = new ObjectMapper();

    public static void main(String[] args) {

        if (args.length < 2) {
            System.out.println("Usage: java -jar target/transpose.java <list> <shift>");
            return;
        }

        var shift = Integer.parseInt(args[1]);

        var notesList = new ArrayList<List<Integer>>();
        try {
            notesList.addAll(readNotes(args[0]));
        } catch (JsonProcessingException e) {
            System.out.println("Error reading list of notes: " + e.getMessage());
            return;
        }

        try {
            var result = transpose(notesList, shift);
            System.out.println("Transposed notes: " + result);
        } catch (OutOfKeyboardException e) {
            System.out.println("Transposed note is out of the keyboard");
        }
    }

    static List<List<Integer>> readNotes(String s) throws JsonProcessingException {
        var typeRef = new TypeReference<List<List<Integer>>>() {};
        return mapper.readValue(s, typeRef);
    }

    static List<List<Integer>> transpose(List<List<Integer>> notesList, int shift) {
        return notesList.stream()
                .map(note -> shiftNote(note, shift))
                .toList();
    }

    static List<Integer> shiftNote(List<Integer> note, int shift) {

        var index = note.get(0) * 12 + note.get(1) - 1 + shift; // zero index is [0, 1]

        if (index < lowestKey || index > highestKey) {
            throw new OutOfKeyboardException();
        }

        int octave = (index >= 0) ? index / 12 : (index - 11) / 12;
        int position = (index % 12 + 12) % 12 + 1;

        return List.of(octave, position);
    }
}
