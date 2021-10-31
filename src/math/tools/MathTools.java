package math.tools;

public class MathTools {
	public static float round(float number,int decimals) {
		float multiplier=10*decimals;
		float rounded=((int)(number*multiplier))/(float)multiplier;
		return rounded;
	}
}
