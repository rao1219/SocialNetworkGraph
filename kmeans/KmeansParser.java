package kmeans;

import java.io.FileWriter;
import java.util.ArrayList;
import java.util.List;

public class KmeansParser {

	private List<Punto> puntos;
	private KMeansResultado resultado;
	private int k;
	public KmeansParser(List<String[]> myEntries){
		puntos = new ArrayList<Punto>();
		for (String[] strings: myEntries) {
		    Punto p = new Punto(strings);
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
		try{
			saveFile();
		}catch(Exception e){}
	}
	public KMeansResultado getKmeansResult(int k){
		return this.resultado;
	}
	private void saveFile() throws Exception{
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
