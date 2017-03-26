package model;

import Jama.Matrix;

import Jama.Matrix;
import Jama.SingularValueDecomposition;

public class Calculator {
	
	public double[] getEigenValues(double[][] weights){
		Matrix A = new Matrix(weights);
		return A.eig().getRealEigenvalues();
	}
	public double[][] getEigenVectors(double[][] weights){
		Matrix A = new Matrix(weights);
		return A.eig().getV().getArray();
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
}
