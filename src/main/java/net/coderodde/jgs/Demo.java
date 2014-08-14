package net.coderodde.jgs;

import net.coderodde.jgs.model.Graph;
import net.coderodde.jgs.model.support.UndirectedGraphNode;

public class Demo {

    public static void main(final String... args) {
        Graph<UndirectedGraphNode> g = new Graph<>("Graph");
        UndirectedGraphNode a = new UndirectedGraphNode("A");
        UndirectedGraphNode b = new UndirectedGraphNode("B");
        
        g.addNode(a);
        g.addNode(b);
        
        a.connectTo(b);
        System.out.println("Yo!");
        a.connectTo(b);
    }
}
