package hufmanncompression;

public class Node {
    public char myChar;
    public int myFrequency = 1;
    public Node myLeft, myRight;

    public Node(char c){
        this.myChar = c;
    }

    @Override
    public String toString() {return "char: " + myChar +", frequency: " + myFrequency;
    }
}
