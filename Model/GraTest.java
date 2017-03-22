package Model;


import java.net.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class GraTest {
	public static void main(String[] args){
		Graph g = new Graph();
		g.addEdge("b", "a", 12);
		g.addEdge("b", "c", 1);
	}
}
