package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.jgrapht.*;

public class Algorithm {
	private HashMap<String, Integer> userToIndex;
	private ArrayList<String> indexToUser;
	private ArrayList<ArrayList<Tuple<Integer, Integer>>> graph;
	private HashSet<Tuple<Integer, Integer>> edgeSet;
	private int[][] dist;
	private boolean isNewest;
	private int nodeNum = 0;
	public Algorithm(){
		nodeNum = 0;
		userToIndex = new HashMap<>();
		indexToUser = new ArrayList<>();
		edgeSet = new HashSet<>();
		graph = new ArrayList<>();
		isNewest = true;
	}
	public ArrayList getNodeList(){
		return indexToUser;
	}
	public HashSet<Tuple<Integer, Integer>> getEdgeList(){
		return edgeSet;
	}
	public int addNode(String name, boolean isAddEdge){
		if(userToIndex.containsKey(name) == false){
			userToIndex.put(name, nodeNum);
			indexToUser.add(name);
			graph.add(new ArrayList<Tuple<Integer, Integer>>());
			nodeNum+=1;
		}
		return userToIndex.get(name);
	}
	public int addNode(String name){
		if(userToIndex.containsKey(name) == false){
			userToIndex.put(name, nodeNum);
			indexToUser.add(name);
			graph.add(new ArrayList<Tuple<Integer, Integer>>());
			nodeNum+=1;
		}else{
			System.out.format("The name(%s) is already exist!\n", name);
		}
		return userToIndex.get(name);
	}
	public String getNodeName(int u){
		return indexToUser.get(u);
	}
	public void addEdge(String na, String nb, int weight){
		int u = addNode(na), v = addNode(nb);
		if(u > v) swap(u, v);
		if(edgeSet.contains(new Tuple<>(u, v))){
			System.out.format("The edge(%d, %d) is already exist\n", indexToUser.get(u), indexToUser.get(v));
			return ;
		}
		edgeSet.add(new Tuple<>(u, v));
		graph.get(u).add(new Tuple<>(v, weight));
		graph.get(v).add(new Tuple<>(u, weight));
		isNewest = false;
	}
	public HashMap<String, HashMap<String, Double>> getJGraphData(){
		HashMap<String, HashMap<String, Double>> res = new HashMap<>();
		for(int i = 0; i < nodeNum; i++){
			HashMap<String, Double> tmp = new HashMap<>();
			for(int j = 0; j < graph.get(i).size(); j++){
				int v = graph.get(i).get(j)._1();
				int w = graph.get(i).get(j)._2();
				tmp.put(indexToUser.get(v), (double) w);
			}
			res.put(indexToUser.get(i), tmp);
		}
		return res;
	}
	public int[][] getAdjMatrix(){
		int[][] gMat = new int[nodeNum][nodeNum];
		for(int i = 0; i < nodeNum; i++){
			for(int j = 0; j < graph.get(i).size(); j++){
				gMat[i][graph.get(i).get(j)._1()] += graph.get(i).get(j)._2();
			}
		}
		return gMat;
	}
	public int[] SPFA(int s){
		int[] d = new int[nodeNum];
		boolean[] inq = new boolean[nodeNum];
		
		for(int i = 0; i < nodeNum; i++) d[i] = (int) 1e9;
		d[s] = 0;
		Queue<Integer> que = new LinkedList<Integer>();
		que.offer(s);
		inq[s] = true;
		while(!que.isEmpty()){
			int x=que.poll();
	        inq[x]=false;
	        for(int i = 0; i < graph.get(x).size(); i++){
	        	Tuple tmp = graph.get(x).get(i);
	        	int v = (int) tmp._1(), w = (int) tmp._2();
	            if(d[v]>d[x]+w)  
	            {  
	            	d[v] = d[x]+w;  
	                if(!inq[v])  
	                {  
	                    inq[v]=true;  
	                    que.offer(v);  
	                }  
	            }  
	        }
		}
		return d;
	}
	public void getAnyTwoShortestPath(){
		dist = new int[nodeNum][nodeNum];
		for(int i = 0; i < nodeNum; i++){
			dist[i] = SPFA(i);
		}
		isNewest = true;
	}
	public int[] getNodeWeights() throws Exception{
		if(!isNewest){
			throw new Exception("shortest path is outdated, please calculate it again!(run getAnyTwoShortestPath before this)");
		}
		int[] weight = new int[nodeNum];
		for(int x = 0; x < nodeNum; x++){
			for(int u = 0; u < nodeNum; u++){
				for(int v = 0; v < nodeNum; v++){
					if(dist[u][v] == dist[u][x] + dist[x][v])
						weight[x] += 1;
				}
			}
		}
		return weight;
	}
	public static <T> void swap(T t1, T t2){  
	    T tmp = t1;  
	    t1 = t2;  
	    t2 = tmp;
	}  
	static class Tuple<A, B>{
		private A _1;
		private B _2;
		public Tuple(A _1, B _2) {
			super();
			this._1 = _1;
			this._2 = _2;
		}
		public A _1() {
			return _1;
		}
		public void set_1(A _1) {
			this._1 = _1;
		}
		public B _2() {
			return _2;
		}
		public void set_2(B _2) {
			this._2 = _2;
		}
		public int hashCode()  
	    {  
	        return _1.hashCode()*9997+_2.hashCode();  
	    }  
	    public boolean equals(Object obj)  
	    {
	    	if(!(obj instanceof Tuple))  
	            return false;  
	        Tuple p = (Tuple)obj;
	    	return _1.equals(p._1()) && _2.equals(p._2());
	    }     
	}
}
