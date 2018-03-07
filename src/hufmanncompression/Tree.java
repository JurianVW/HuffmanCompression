package hufmanncompression;

import java.awt.*;
import java.util.PriorityQueue;

public class Tree {

    public Tree(String text) {
        PriorityQueue<Node> queue = new PriorityQueue<>(new NodeComparator());
        for (char c : text.toCharArray()) {
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

        while(queue.size() != 0){
            System.out.println(queue.poll().toString());
        }
    }
}
