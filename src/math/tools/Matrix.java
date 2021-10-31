package math.tools;

import java.util.Random;


public class Matrix {
	public float[][]m;
	private Random random= new Random();
	public Matrix(int size) {
		m=new float[size][size];
		random.setSeed(Defaults.seed);
	}
	public Matrix(int rows,int col) {
		m= new float[rows][col];
		random.setSeed(Defaults.seed);
	}
	public Matrix(float[][]m) {
		this.m=m;
		random.setSeed(Defaults.seed);
		initializeZeros();
	}
	public void initializeZeros() {
		for(int i=0;i<m.length; i++) {
			for(int j=0;j<m[0].length; j++) {
				m[i][j]=0f;
			}
		}

	}
	public void initializeOnes() {
		for(int i=0;i<m.length; i++) {
			for(int j=0;j<m[0].length; j++) {
				m[i][j]=1f;
			}
		}

	}
	public void initializeRandom(float max) {
		for(int i=0;i<m.length; i++) {
			for(int j=0;j<m[0].length; j++) {
				m[i][j]=(float) (random.nextGaussian()*max);
			}
		}

	}
	public Matrix sum(Matrix matrix) {
		Matrix matrixNew = new Matrix(m.length,m[0].length);
		if(this.equalSize(matrix)) {
		for(int i=0;i<matrix.m.length; i++) {
			for(int j=0;j<matrix.m[0].length; j++) {
				matrixNew.m[i][j]=m[i][j]+matrix.m[i][j];
			}
		}
		}else {
			ErrorHandler.throwException("Must have same size");
		}
		return matrixNew;
	}
	public Matrix subtract(Matrix matrix) {
		Matrix matrixNew = new Matrix(m.length,m[0].length);
		if(this.equalSize(matrix)) {
		for(int i=0;i<matrix.m.length; i++) {
			for(int j=0;j<matrix.m[0].length; j++) {
				matrixNew.m[i][j]=m[i][j]-matrix.m[i][j];
			}
		}
		}else {
			ErrorHandler.throwException("Must have same size");
		}
		return matrixNew;
	}
	public Matrix hadamardProduct(Matrix mm) {
		Matrix matrixNew = new Matrix(m.length,m[0].length);
		if(equalSize(mm)) {
		for(int i=0;i<mm.m.length; i++) {
			for(int j=0;j<mm.m[0].length; j++) {
				matrixNew.m[i][j]=m[i][j]*mm.m[i][j];
			}
		}
		}else {
			ErrorHandler.throwException("Must be equal size");
		}
		return matrixNew;
	}
	public Matrix scale(float scale) {
		Matrix matrixNew = new Matrix(m.length,m[0].length);
		for(int i=0;i<m.length; i++) {
			for(int j=0;j<m[0].length; j++) {
				matrixNew.m[i][j]=m[i][j]*scale;
			}
		}
		return matrixNew;
	}
	public Matrix transpose() {
		Matrix matrixNew = new Matrix(m[0].length,m.length);
	
		for(int i=0;i<m.length; i++) {
			for(int j=0;j<m[0].length; j++) {
				matrixNew.m[j][i]=m[i][j];
			}
		}
		
		return matrixNew;
	}
	public boolean equalSize(Matrix mat) {
		if(mat.m.length==m.length&&mat.m[0].length==m[0].length) {
			return true;
		}else {
			return false;
		}
	}

	public boolean isSquare() {
		if(m.length==m[0].length) {
			return true;
		}else {
			return false;
		}
	}
	public Matrix dot(Matrix matrix) {
		Matrix matrixNew = new Matrix(m.length,matrix.m[0].length);
		if(m[0].length==matrix.m.length) {
			for(int i=0;i<m.length; i++) {

				
				for(int k=0;k<matrix.m[0].length;k++) {
					float result = 0f;
					for(int j=0;j<m[0].length; j++) {

						result += m[i][j]*matrix.m[j][k];

					}
					matrixNew.m[i][k]=result;

				}	

			}
		}else {
			ErrorHandler.throwException("Dimension doesnt match");
		}
		return matrixNew;
	}
	public Vector dot(Vector v) {
		Vector vectorNew = new Vector(m.length);
		if(m[0].length==v.v.length) {
			for(int i=0;i<m.length; i++) {

				float result = 0f;
				for(int j=0;j<m[0].length; j++) {

					result += m[i][j]*v.v[j];

				}
				vectorNew.v[i]=result;

			}	

		}else {
			ErrorHandler.throwException("Dimension doesnt match");
		}
		return vectorNew;
	}
	public Vector toVector() {
		Vector v = new Vector(m.length*m[0].length);
		int c=0;
		for(int i=0;i<m.length; i++) {
			for(int j=0;j<m[0].length; j++) {
				v.v[c]=m[i][j];
				c++;
			}
		}
		return v;

	}
	public void printSize() {
		System.out.println(m.length+" x "+m[0].length);
	}
	public void print() {
		System.out.println();
		for(int i=0;i<m.length; i++) {
			System.out.print(" | ");
			for(int j=0;j<m[0].length; j++) {
				System.out.print( " "+m[i][j]+" ");
			}
			System.out.print(" | ");
			System.out.println();
		}
		System.out.println();
	}
	}
