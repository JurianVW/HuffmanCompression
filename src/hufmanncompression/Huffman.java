package hufmanncompression;

import oracle.jrockit.jfr.events.Bits;

import java.io.*;
import java.util.*;

//Write in file: First few bytes say size of tree, then give the tree, then de text
//https://algs4.cs.princeton.edu/55compression/Huffman.java.html
public class Huffman {

    private Node tree;
    private BitSet data;
    private String fileDest = "bytes.file";

    public Huffman(String text, boolean encode) {
        if (encode) encode(text);
        else decode();
    }

    private void encode(String text) {
        char[] input = text.toCharArray();
        Node root = createTree(input);
        printTree(root);

        HashMap<Character, String> map = new HashMap();
        generateBinaryCodes(map, root, "");

        writeFile(generateCode(input, map), root);
    }

    private void decode() {
        readFile();

        if (tree != null && data != null) {
            System.out.println(decodeData());
        }
    }


    private Node createTree(char[] input) {
        PriorityQueue<Node> queue = new PriorityQueue<>();
        for (char c : input) {
            boolean exists = false;
            for (Node n : queue) {
                if (n.myChar == c) {
                    queue.remove(n);
                    n.myFrequency++;
                    queue.add(n);
                    exists = true;
                    break;
                }
            }
            if (!exists) {
                queue.add(new Node(c));
            }
        }

        while (queue.size() > 1) {
            Node left = queue.poll();
            Node right = queue.poll();
            Node parent = new Node(left, right);
            queue.add(parent);
        }

        return queue.poll();
    }

    //Vraag 4a: Dit is erg snel en je hoeft niks op te zoeken, je loopt langs elke node maar 1 keer dus het is erg snel en kost niet veel geheugen om te doen.
    //Vraag 4b: Dit is slomer om te doen, zeker bij een lange text is het opzoeken in een boom van een value erg zwaar.
    private void generateBinaryCodes(HashMap map, Node node, String s) {
        if (!node.isLeaf()) {
            generateBinaryCodes(map, node.myLeft, s + '0');
            generateBinaryCodes(map, node.myRight, s + '1');
        } else {
            map.put(node.myChar, s);
        }
    }

    private BitSet generateCode(char[] input, HashMap<Character, String> map) {
        BitSet bitset = new BitSet();
        Integer index = 0;

        for (char c : input) {
            String code = map.get(c);
            for (int i = 0; i < code.length(); i++) {
                if (code.charAt(i) == '0') {
                    bitset.set(index, false);
                    index++;
                } else if (code.charAt(i) == '1') {
                    bitset.set(index, true);
                    index++;
                }
            }
        }
        return bitset;
    }

    private String decodeData() {
        StringBuilder sb = new StringBuilder();
        Node current = tree;

        for (int i = 0; i < data.length(); i++) {
            if (data.get(i)) {
                current = current.myRight;
            } else {
                current = current.myLeft;
            }

            if (current.isLeaf()) {
                sb.append(current.myChar);
                current = tree;
            }
        }
        return sb.toString();
    }

    private void writeFile(BitSet bitset, Node tree) {
        try (FileOutputStream fos = new FileOutputStream(fileDest); ObjectOutputStream oos = new ObjectOutputStream(fos)) {
            oos.writeObject(tree);
            oos.writeObject(bitset.toByteArray());
        } catch (Exception ex) {
        }
    }

    private void readFile() {
        try (FileInputStream fis = new FileInputStream(fileDest); ObjectInputStream ois = new ObjectInputStream(fis)) {
            tree = (Node) ois.readObject();
            data = BitSet.valueOf((byte[]) ois.readObject());
        } catch (Exception ex) {
        }
    }

    private void printTree(Node node) {
        if (node.isLeaf()) System.out.println(node.toString());
        if (node.myLeft != null) printTree(node.myLeft);
        if (node.myRight != null) printTree(node.myRight);
    }
}

