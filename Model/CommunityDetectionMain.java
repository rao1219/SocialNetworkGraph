package Model;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

import Model.JGraphVistuallization.Edge;
import Model.Algorithm;
import Model.Algorithm.Tuple;

public class CommunityDetectionMain {
	
    private ArrayList<String> nodeList;
    private ArrayList<Edge> edgeList;
    private Algorithm alg;
    private int[] weight;
    public CommunityDetectionMain() {
		this.nodeList = new ArrayList();
		this.edgeList = new ArrayList();
		this.alg = new Algorithm();
	}
	public CommunityDetectionMain(ArrayList<String> nodeList, ArrayList<Edge> edgeList, Algorithm alg) {
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
		alg.getAnyTwoShortestPath();
		this.weight = alg.getNodeWeights();
		return this.weight;
	}
	public static void main(String[] args) throws Exception{
		String DATA_INPUT = "football";
		
		CommunityDetectionMain cdm = new CommunityDetectionMain();
		cdm.readDataFromFile(DATA_INPUT);
		JGraphVistuallization jgx = new JGraphVistuallization();
		jgx.generateGraph(cdm.nodeList, cdm.edgeList);
		int[] weight = cdm.getWeights();
		jgx.run("test");
		for(int i:weight){
			System.out.print(" "+i);
		}
	}
}
