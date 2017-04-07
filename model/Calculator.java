package model;

import java.util.List;
import java.io.ObjectInputStream.GetField;
import java.util.ArrayList;
import java.util.Collections;

import Jama.Matrix;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class Calculator {
	public Algorithm getAlg() {
		return alg;
	}
	public void setAlg(Algorithm alg) {
		this.alg = alg;
	}
	Algorithm alg;

	public Calculator(Algorithm alg) {
		super();
		this.alg = alg;
	}
	public Calculator() {
		super();
	}
	/**
	 * 得到mat的标准化矩阵
	 * @param mat 
	 * @param axis 0:行标准化 1:列标准化
	 * @return
	 */
	public double[][] norm(double[][] mat, int axis){
		if(mat.length == 0 || mat[0].length == 0){
			return null;
		}
		double[][] tmp = new double[mat.length][mat[0].length];
		double[] row2 = new double[mat.length];
		double[] col2 = new double[mat[0].length];
		for(int i = 0; i < mat.length; i++){
			for(int j = 0; j < mat[0].length; j++){
				row2[i] += mat[i][j]*mat[i][j];
				col2[j] += mat[i][j]*mat[i][j];
			}
		}
		for(int i = 0; i < mat.length; i++){
			for(int j = 0; j < mat[0].length; j++){
				if(axis == 0) tmp[i][j] = mat[i][j] / Math.sqrt(row2[i]);
				else tmp[i][j] = mat[i][j] / Math.sqrt(col2[j]);
			}
		}
		return tmp;
	}
	public static double[][] getT(double[][] mat){
		double[][] tmp = new double[mat.length][mat[0].length];
		for(int i= 0 ; i < mat.length; i++)
			for(int j = 0; j < mat[0].length; j++){
				tmp[i][j] = mat[j][i];
			}
		return tmp;
	}
	public static double[] getEigenValues(double[][] weights){
		Matrix A = new Matrix(weights);
		return A.eig().getRealEigenvalues();
	}
	public static double[][] getEigenVectors(double[][] weights){
		Matrix A = new Matrix(weights);
		return A.eig().getV().getArray();
	}
	public double getQ(int[] c){
		int n = alg.getNodeNum();
		int edgeNum = alg.getEdgeList().size();
		int[] deg = alg.getDeg();
		int[][] adjMatrix = alg.getAdjMatrix();
		double innerEdgeNum = 0;
		
		for(int i = 0; i < n; i++){
			for(int j = 0; j < n; j++){
				if(c[i] == c[j]){
					innerEdgeNum += adjMatrix[i][j] - (double)deg[i]*deg[j]/(2.0*edgeNum);
				}
			}
		}
		return innerEdgeNum / (2.0*edgeNum);
	}
	public void spectral_bisection(double[][] mat, int g){
		double[][] mat_n = norm(mat, 0);
		double[] eValues = getEigenValues(mat_n);
		double[][] eVectors = getEigenVectors(mat_n);
		List<Eigen> eigens = new ArrayList<Eigen>();
		for(int i = 0; i < eValues.length; i++){
			eigens.add(new Eigen(eValues[i], eVectors[i]));
		}
		Collections.sort(eigens);
		
		/*
		 * TODO 得到待分类的向量集合
		 */
		
		for(int i = 1; i < g; i++){
			
			/*
			 * TODO kmeasn 聚类
			 */
			
		}
		
	}
	public static void main(String[] args) {
		Calculator cal = new Calculator();
		double[][] d = new double[2][2];
		for(int i=0;i<2;i++){
			for(int j=0;j<2;j++){
				d[i][j] = 1;
			}
		}
//		double[] eigens = cal.getEigenValues(d);
//		double[][] eigenVevtors = cal.getEigenVectors(d);
//		for(int i=0;i<eigens.length;i++){
//			System.out.println("eigen value:"+'\t'+eigens[i]);
//			System.out.print("Vector:");
//			for(int j=0;j<eigenVevtors[i].length;j++){
//				System.out.print(eigenVevtors[i][j]+",");
//			}
//			System.out.println();
//		}
		
	}
	public class Eigen implements Comparable{
		double eValue;
		double[] eVector;
		public Eigen(double eigenValue, double[] eigenVector) {
			super();
			this.eValue = eigenValue;
			this.eVector = eigenVector;
		}
		@Override
		public int compareTo(Object o) {
			// TODO Auto-generated method stub
			Eigen b = (Eigen)o;
			if(this.eValue == b.eValue) return 0;
			if(this.eValue > b.eValue) return 1;
			return -1;
		}
		
	}
}
