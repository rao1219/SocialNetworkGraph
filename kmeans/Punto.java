package kmeans;
import java.util.ArrayList;
import java.util.List;

public class Punto implements Comparable<Punto>{
    private Double[] data;
    private int label;
    private int clu;
    public Punto(Double[] data,int label) {
    	this.data = data;
    	this.label = label;
    }
    public int getLabel(){
    	return this.label;
    }
    public void setClass(int c){
    	this.clu = c;
    }
    public int getCluster(){
    	return this.clu;
    }

    public double get(int dimension) {
    	return data[dimension];
    }

    public int getGrado() {
    	return data.length;
    }

    @Override
    public String toString() {
		StringBuilder sb = new StringBuilder();
		sb.append(data[0]);
		for (int i = 1; i < data.length; i++) {
		    sb.append(", ");
		    sb.append(data[i]);
		}
		return sb.toString();
    }

    public Double distanciaEuclideana(Punto destino) {
		Double d = 0d;
		for (int i = 0; i < data.length; i++) {
		    d += Math.pow(data[i] - destino.get(i), 2);
		}
		return Math.sqrt(d);
    }

    @Override
    public boolean equals(Object obj) {
		Punto other = (Punto) obj;
		for (int i = 0; i < data.length; i++) {
		    if (data[i] != other.get(i)) {
			return false;
		    }
		}
		return true;
    }
	public int compareTo(Punto p) {
		// TODO Auto-generated method stub
		return this.getLabel()-p.getLabel();
	}
	
}