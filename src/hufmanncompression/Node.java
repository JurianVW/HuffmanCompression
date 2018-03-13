package hufmanncompression;

import java.io.Serializable;
import java.util.Comparator;

public class Node implements Serializable, Comparable<Node> {
    public char myChar;
    public int myFrequency = 1;
    public Node myLeft, myRight;

    public Node(char c) {
        this.myChar = c;
    }

    public Node(Node myLeft, Node myRight) {
        this.myFrequency = myLeft.myFrequency + myRight.myFrequency;
        this.myLeft = myLeft;
        this.myRight = myRight;
    }

    public boolean isLeaf() {
        return (myLeft == null) && (myRight == null);
    }

    @Override
    public String toString() {
        return "char: " + myChar + ", frequency: " + myFrequency;
    }

    @Override
    public int compareTo(Node o) {
        if (this.myFrequency > o.myFrequency) {
            return 1;
        } else if (this.myFrequency < o.myFrequency) {
            return -1;
        }
        return 0;
    }
}
