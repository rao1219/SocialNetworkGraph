package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import model.Algorithm;
import model.Algorithm.Tuple;
import model.Visuallizator.Edge;


public class Application {
	
    private ArrayList<String> nodeList;
    private ArrayList<Edge> edgeList;
    private Algorithm alg;
    private int[] weight;
    public Application() {
		this.nodeList = new ArrayList();
		this.edgeList = new ArrayList();
		this.alg = new Algorithm();
	}
	public Application(ArrayList<String> nodeList, ArrayList<Edge> edgeList, Algorithm alg) {
		super();
		this.nodeList = nodeList;
		this.edgeList = edgeList;
		this.alg = alg;
	}

	public void readDataFromFile(String dataset) throws FileNotFoundException{
    	String data_path = "src/data/data_"+dataset+".txt";
    	File data = new File(data_path);
    	Scanner inputs = new Scanner(data);
    	while(inputs.hasNext()){
    		String line = inputs.nextLine();
    		String[] names = line.split(",");
    		String u = names[0], v = names[1];
//    		String u = inputs.next();
//    		String v = inputs.next();
    		alg.addNode(u);
    		alg.addNode(v);
    		alg.addEdge(u, v, 1);
    	}
    	nodeList.addAll(alg.getNodeList());
    	HashSet<Tuple<Integer, Integer>> edgeSet = alg.getEdgeList();
    	for(Tuple s:edgeSet){
    		edgeList.add(new Edge(alg.getNodeName((int)s._1()), alg.getNodeName((int)s._2()), 1));
    	}
	}
	public int[] getWeights() throws Exception{
		if(nodeList.size() == 0){
			throw new Exception("The graph is empty!");
		}
		int[][] dist = alg.getAnyTwoShortestPath();
		this.weight = alg.getNodeWeights(dist);
		return this.weight;
	}
	
	public static void main(String[] args) throws Exception{
		
		String DATA_INPUT = "test";
		
		Application app = new Application();
		app.readDataFromFile(DATA_INPUT);
		Visuallizator jgx = new Visuallizator();
		jgx.generateGraph(app.nodeList, app.edgeList);
		Calculator cal = new Calculator(app.alg);
		int[] weight = app.getWeights();
		int[] deg = app.alg.getDeg();
		int[][] gMat = app.alg.getAdjMatrix();
		double[][] weightMatrix = app.alg.getWeightMatrix(deg, weight, gMat);
//		double[] eigens = cal.getEigenValues(weightMatrix);
//		double[][] eigenVevtors = cal.getEigenVectors(weightMatrix);
		for(int i=2;i<app.alg.getNodeNum();i++){
			ArrayList<Double> resqv = cal.spectral_bisection(weightMatrix, i);
			System.out.println("qv for "+i);
			double maqv = -1e9;
			for(Double qv:resqv){
				System.out.print(qv+",");
				maqv = Math.max(maqv, qv);
			}
			System.out.println("\nqv for "+i+", max="+maqv+"\n-----------------");
		}
//		int[] cc = {1,1,1,3,2,2,2,3,3,3};
//		System.out.print(cal.getQ(cc));
		jgx.run("test");
//		for(int i:weight){
//			System.out.print(" "+i);
//		}
//		System.out.println('\n'+"Eigen values:");
//		for(int i=0;i<eigens.length;i++){
//			System.out.println("eigen value:"+'\t'+eigens[i]);
//			System.out.print("Vector:");
//			for(int j=0;j<eigenVevtors[i].length;j++){
//				System.out.print(eigenVevtors[i][j]+",");
//			}
//			System.out.println();
//		}
	}
}
