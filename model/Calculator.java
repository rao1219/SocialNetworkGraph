package model;

import Jama.Matrix;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class Calculator {
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
	public double[] getEigenValues(double[][] weights){
		Matrix A = new Matrix(weights);
		return A.eig().getRealEigenvalues();
	}
	public double[][] getEigenVectors(double[][] weights){
		Matrix A = new Matrix(weights);
		return A.eig().getV().getArray();
	}
	public void spectral_bisection(double[][] mat){
		double[][] mat_n = norm(mat, 0);
		double[] eValues = getEigenValues(mat_n);
		double[][] eVectors = getEigenVectors(mat_n);
		Eigen[] eigen;
		
	}
	public static void main(String[] args) {
		Calculator cal = new Calculator();
		double[][] d = null;
		for(int i=0;i<2;i++){
			for(int j=0;j<2;j++){
				d[i][j] = 1;
			}
		}
		double[] eigens = cal.getEigenValues(d);
		double[][] eigenVevtors = cal.getEigenVectors(d);
		for(int i=0;i<eigens.length;i++){
			System.out.println("eigen value:"+'\t'+eigens[i]);
			System.out.print("Vector:");
			for(int j=0;j<eigenVevtors[i].length;j++){
				System.out.print(eigenVevtors[i][j]+",");
			}
			System.out.println();
		}
		
	}
	class Eigen implements Comparable{
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
