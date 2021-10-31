package math.tools;

import java.io.Serializable;
import java.util.Random;




public class Vector implements Serializable {
	public float[] v;
	private Random random = new Random();

	public Vector(float[] v) {
		random.setSeed(Defaults.seed);
		this.v = v;
	}

	public Vector(int size) {
		random.setSeed(Defaults.seed);
		v = new float[size];
		initializeZeros();
	}

	public void initializeZeros() {
		for (int i = 0; i < v.length; i++) {
			v[i] = 0f;
		}
	}
	public void initializeOnes() {
		for (int i = 0; i < v.length; i++) {
			v[i] = 1f;
		}
	}
	public void initializeRandom(float max) {
		for (int i = 0; i < v.length; i++) {
			v[i] = (float) (random.nextGaussian()*max);
		}
	}

	public Vector sum(Vector vec) {
		Vector vecNew = new Vector(v.length);
		if (vec.v.length == v.length) {
			for (int i = 0; i < vec.v.length; i++) {
				vecNew.v[i] = vec.v[i] + v[i];
			}
		} else {
			ErrorHandler.throwException("Must be same size");
		}
		return vecNew;
	}

	public Vector substract(Vector vec) {
		Vector vecNew = new Vector(v.length);
		if (vec.v.length == v.length) {
			for (int i = 0; i < vec.v.length; i++) {
				vecNew.v[i] =  v[i]-vec.v[i];
			}
		} else {
			ErrorHandler.throwException("Must be same size");
		}
		return vecNew;
	}

	public Vector multiply(Vector vec) {
		Vector vecNew = new Vector(v.length);
		if (vec.v.length == v.length) {
			for (int i = 0; i < vec.v.length; i++) {
				vecNew.v[i] = vec.v[i] * v[i];
			}
		} else {
			ErrorHandler.throwException("Must be same size");
		}
		return vecNew;
	}
	public Vector divide(Vector vec) {
		Vector vecNew = new Vector(v.length);
		if (vec.v.length == v.length) {
			for (int i = 0; i < vec.v.length; i++) {
				vecNew.v[i] =  v[i]/vec.v[i];
			}
		} else {
			ErrorHandler.throwException("Must be same size");
		}
		return vecNew;
	}

	public Vector transpose() {
		Vector vecNew = new Vector(v.length);
		for (int i = 0; i < v.length; i++) {
			vecNew.v[v.length - i - 1] = v[i];
		}
		return vecNew;
	}

	public void print() {
		System.out.println();
		for (int i = 0; i < v.length; i++) {
			System.out.println(" | " + v[i] + " | ");
		}
		System.out.println();
	}

	public Vector scale(float s) {
		Vector vecNew = new Vector(v.length);
		for (int i = 0; i < v.length; i++) {
			vecNew.v[i] = v[i] * s;
		}

		return vecNew;
	}

	public float scalar(Vector vec) {
		float value = 0f;
		if (vec.v.length == v.length) {

			for (int i = 0; i < v.length; i++) {
				value += vec.v[i] + v[i];
			}
		} else {
			ErrorHandler.throwException("Must be same size");
		}
		return value;
	}

	// Matrix with rows and 1 column
	public Matrix toMatrix() {
		Matrix mNew= new Matrix(v.length,1);
		for(int i=0;i<v.length;i++) {
			mNew.m[i][0]=v[i];
		}
		return mNew;
	}
	public boolean sameSize(Vector v2) {
		if(v.length==v2.v.length) {
			return true;
		}else {
			return false;
		}
	}
	public float sumUp() {
		float total=0f;
		for(int i=0;i<v.length;i++) {
			total+=v[i];
		}
		return total;
	}
	//Length of vector
	public int L() {
		return v.length;
	}
	
	//Apply logarithm
	public Vector applyLogarithm() {
		Vector vecNew = new Vector(v.length);
		for (int i = 0; i < v.length; i++) {
			vecNew.v[i] = (float) Math.log10(v[i]);
		}
	
		return vecNew;
	}



}
