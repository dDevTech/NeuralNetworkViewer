package math.tools;

import java.util.List;

public class ArrayTools {
	public static float[]flattenArray(float[][]arrayToFlatten){
		float[]flattened=new float[arrayToFlatten.length*arrayToFlatten[0].length];
		int c=0;
		for(int i=0;i<arrayToFlatten.length;i++) {
			for(int j=0;j<arrayToFlatten[0].length;j++) {
				flattened[c]=arrayToFlatten[i][j];
				c++;
			}
		}
		return flattened;
		
	}
	public float[][]reshape(float[][]arrayToFlatten){
		return null;
		
	}
	public static Object calcGradient(List<Object>list) {
		if(list.get(0) instanceof Vector) {
			Vector model=(Vector)list.get(0);
			Vector out=new Vector(model.v.length);
			out.initializeZeros();
			for(Object v:list) {
				Vector item=(Vector)v;
				out=out.sum(item);
			}
			return out;
		}
		if(list.get(0) instanceof Matrix) {
			Matrix model=(Matrix)list.get(0);
			Matrix out=new Matrix(model.m.length,model.m[0].length);
			out.initializeZeros();
			for(Object m:list) {
				Matrix item=(Matrix)m;
				out=out.sum(item);
			}
			return out;
		}
		return null;
	}
}
