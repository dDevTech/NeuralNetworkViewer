package math.tools;

public class ErrorHandler {
	public static void throwException(String s) {

		try {

			throw new Exception(s);

		} catch (Exception e) {

			e.printStackTrace();

		}

	}

}
