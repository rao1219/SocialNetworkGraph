package Model;


import java.net.*;

import org.jgrapht.*;
import org.jgrapht.graph.*;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.graph.DefaultEdge;

public class GraTest {
	public static void main(String[] args){
		int[] a = new int[3];
		int[][] b = new int[2][3];
		a[1] = 2;
		a[2] = 1;
		b[0] = a;
		b[1] = a;
		System.out.println(b[1][1]);
	}
}
