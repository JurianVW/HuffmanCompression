package hufmanncompression;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.BitSet;
import java.util.HashMap;
import java.util.Random;


class HuffmanTest {
    private static final String inputText = "Eerie eyes seen near lake.";
    private  Huffman huffman;
    private static char[] input;
    private static String filename = "bytesTest.file";

    private static String shortString;
    private static String longString;

    @BeforeAll
    static void beforeAll() {
        input = inputText.toCharArray();
        shortString = generateRandomWords(10_000);
        longString = generateRandomWords(1_000_000);
    }

    @BeforeEach
    void beforeEach(){
        huffman = new Huffman(inputText, true, filename);
    }

    @Test
    void mainTest() {
        Node tree = huffman.createTree(input);
        HashMap<Character, String> map = new HashMap<>();
        huffman.generateBinaryCodes(map, tree, "");
        BitSet bitSet = huffman.generateCode(input, map);

        Assertions.assertTrue(inputText.equals(huffman.decodeData(bitSet, tree)));
    }

    @Test
    void testEncodeDecode() {
        huffman = new Huffman(inputText, true, filename);
        huffman = new Huffman(inputText, false, filename);
    }

    @Test
    void fileTests(){
        Node tree = huffman.createTree(input);
        HashMap<Character, String> map = new HashMap<>();
        huffman.generateBinaryCodes(map, tree, "");
        BitSet bitSet = huffman.generateCode(input, map);

        huffman.writeFile(bitSet, tree);
        Assertions.assertTrue(inputText.equals(huffman.decodeData(huffman.readFile(), huffman.outputTree)));
    }

    @Test
    void encodePerformance(){
        long startShort = System.nanoTime();
        huffman = new Huffman(shortString, true, filename);
        long durationShort = System.nanoTime() - startShort;

        long startLong = System.nanoTime();
        huffman = new Huffman(longString, true, filename);
        long durationLong = System.nanoTime() - startLong;

        System.out.println("Encode: " + "Short String: " + durationShort / 1000000 + ", Long String:" + durationLong / 1000000 + " (In miliseconds)");
    }

    @Test
    void decodePerformance(){
        long startShort = System.nanoTime();
        huffman = new Huffman(shortString, false, filename);
        long durationShort = System.nanoTime() - startShort;

        long startLong = System.nanoTime();
        huffman = new Huffman(longString, false, filename);
        long durationLong = System.nanoTime() - startLong;

        System.out.println("Decode: " + "Short String: " + durationShort  + ", Long String:" + durationLong  + " (In nanotime)");
    }



    public static String generateRandomWords(int numberOfWords) {
        String[] randomStrings = new String[numberOfWords];
        Random random = new Random();
        for (int i = 0; i < numberOfWords; i++) {
            char[] word = new char[random.nextInt(8) + 3]; // words of length 3 through 10. (1 and 2 letter words are boring.)
            for (int j = 0; j < word.length; j++) {
                word[j] = (char) ('a' + random.nextInt(26));
            }
            randomStrings[i] = new String(word);
        }
        StringBuilder builder = new StringBuilder();
        for (String s : randomStrings) {
            builder.append(s + " ");
        }
        return builder.toString();
    }
}