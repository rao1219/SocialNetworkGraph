package Model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.Queue;

import org.jgrapht.*;

public class Graph {
	private HashMap<String, Integer> userToIndex;
	private ArrayList<ArrayList<Tuple<Integer, Integer>>> graph;
	private HashSet<Tuple<Integer, Integer>> edgeSet;
	private int nodeNum = 0;
	public Graph(){
		nodeNum = 0;
		userToIndex = new HashMap<>();
		graph = new ArrayList<>();
	}
	public int addNode(String name, boolean isAddEdge){
		if(userToIndex.containsKey(name) == false){
			userToIndex.put(name, nodeNum);
			graph.add(new ArrayList<Tuple<Integer, Integer>>());
			nodeNum+=1;
		}
		return userToIndex.get(name);
	}
	public int addNode(String name){
		if(userToIndex.containsKey(name) == false){
			userToIndex.put(name, nodeNum);
			graph.add(new ArrayList<Tuple<Integer, Integer>>());
			nodeNum+=1;
		}else{
			System.out.format("The name(%s) is already exist!", name);
		}
		return userToIndex.get(name);
	}
	public void addEdge(String na, String nb, int weight){
		int u = addNode(na), v = addNode(nb);
		if(u > v) swap(u, v);
		if(edgeSet.contains(new Tuple(u, v))){
			System.out.format("The edge(%d, %d) is already exist", u, v);
			return ;
		}
		edgeSet.add(new Tuple(u, v));
		graph.get(u).add(new Tuple(v, weight));
		graph.get(v).add(new Tuple(u, weight));
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
