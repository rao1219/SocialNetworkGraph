package model;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
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
	public ArrayList<String> getNodeList(){
		return this.nodeList;
	}
	public ArrayList<Edge> getEdgeList(){
		return this.edgeList;
	}
	
	public double[][] getEvaluateMatrix(int METHOD) throws Exception{
		int nodeNum = this.alg.getNodeNum();
		double[][] EM = null;
		int[] weight = this.getWeights();
		int[] deg = this.alg.getDeg();
		int[][] gMat = this.alg.getAdjMatrix();
		
		switch(METHOD){
			case 0:
				EM = this.alg.getWeightMatrix(deg, weight, gMat);
				break;
			case 1:
				double[] DBdeg = new double[nodeNum];
				double sum = 0;
				for(int a:deg) sum += a;
				for(int i = 0; i < nodeNum; i++) DBdeg[i] = deg[i] / sum;
				EM = this.alg.getWeightMatrix(deg, DBdeg, gMat); 
				break;
			case 2:
				int[] J = this.alg.getJ();
				int[] allDist = this.alg.getVToAllDist();
				double[] c = new double[nodeNum];
				for(int i = 0; i < nodeNum; i++){
					double fenmu = J[i] / (nodeNum-1);
					double fenzi = allDist[i] / J[i];
					c[i] = fenmu / fenzi;
				}
				EM = this.alg.getWeightMatrix(deg, c, gMat);
				break;
			case 3:
				double[][] dGmat = new double[gMat.length][gMat[0].length];
				for(int i=0;i<gMat.length;i++){
					for(int j=0;j<gMat[i].length;j++){
						dGmat[i][j] = (double)gMat[i][j];
					}
				}
				EM = dGmat;
				break;
		}
		return EM;
	}
	public void SaveWeightMatrixToFile(double[][] matrix,int method, String data_type){
		String fileName = data_type + "_" + method + ".txt";
		File file = new File("./",fileName);
		try{
			file.createNewFile();
			FileWriter out = new FileWriter(file);
			for(int i=0;i<matrix.length;i++){
				for(int j=0;j<matrix[i].length;j++){
					out.write(matrix[i][j]+"\t");
				}
				out.write("\n");
			}
			out.close();
		}catch (Exception ex){}
		
	}
	
	public static void main(String[] args) throws Exception{
		
		int METHOD = 3;
		String DATA_INPUT = "test";
		
		for(int m = 0;m<=3;m++){
			Application app = new Application();
			app.readDataFromFile(DATA_INPUT);
	//		Visuallizator jgx = new Visuallizator();
	//		jgx.generateGraph(app.nodeList, app.edgeList);
			Calculator cal = new Calculator(app.alg);
			
			double[][] evaluateMatrix = app.getEvaluateMatrix(m);
			for(int i=2;i<app.alg.getNodeNum();i++){
				ArrayList<Double> resqv = cal.spectral_bisection(evaluateMatrix, i);
				System.out.println("qv for "+i);
				double maqv = -1e9;
				for(Double qv:resqv){
					System.out.print(qv+",");
					maqv = Math.max(maqv, qv);
				}
				System.out.println("\nqv for "+i+", max="+maqv+"\n-----------------");
			}
			app.SaveWeightMatrixToFile(evaluateMatrix, m, DATA_INPUT);
			
		}
		
//		int[] cc = {1,1,1,3,2,2,2,3,3,3};
//		System.out.print(cal.getQ(cc));
//		jgx.run(DATA_INPUT);
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
