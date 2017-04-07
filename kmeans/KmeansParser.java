package kmeans;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class KmeansParser {

	private List<Punto> puntos;
	private List<Punto> resPuntos;
	private KMeansResultado resultado;
	private int k;
	public KmeansParser(List<Double[]> myEntries){
		puntos = new ArrayList<Punto>();
		resPuntos = new ArrayList<Punto>();
		for (int i =0;i<myEntries.size();i++) {
		    Punto p = new Punto(myEntries.get(i),i);
		    this.puntos.add(p);
		}
	}
	public int getkValue(){
		return this.k;
	}
	public void doKmeans(int k){
		this.k = k;
		KMeans kmeans = new KMeans();
		this.resultado = kmeans.calcular(puntos, this.k);
		setClusterLabel();
	}
	public KMeansResultado getKmeansResult(int k){
		return this.resultado;
	}
	
	public void setClusterLabel(){
		int i = 0;
		for(Cluster cluster: resultado.getClusters()){
			for (Punto punto : cluster.getPuntos()) {
			    punto.setClass(i);
			    resPuntos.add(punto);
			}
			i++;
		}
	}
	/**
	 * @return cluster of each puntos
	 * */
	public int[] getResults(){
		int[] res = null;
		Collections.sort(resPuntos);
		int i=0;
		for(Punto p: resPuntos){
			res[i] = p.getCluster();
			i++;
		}
		return res;
	}
	
	public void saveFile() throws Exception{
		FileWriter writer = new FileWriter("kmeans_out.csv");
		writer.write("------- Con k=" + k + " ofv=" + resultado.getOfv()
		    + "-------\n");
	    int i = 0;
	    for (Cluster cluster : resultado.getClusters()) {
			i++;
			writer.write("-- Cluster " + i + " --\n");
			for (Punto punto : cluster.getPuntos()) {
			    writer.write(punto.toString() + "\n");
			}
			writer.write("\n");
			writer.write(cluster.getCentroide().toString());
			writer.write("\n\n");
	    }
	    writer.close();
	}
}
