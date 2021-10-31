package math.tools;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;




public class Matrix3 {
	public List<float[][]> m = new ArrayList<float[][]>();
	public Random random;

	// This is only for zero padding
	public enum PaddingZero {
		Valid, Full, Same
	}

	public enum Pooling {
		Average, Max
	}

	public Matrix3(int depth, int width, int height) {
		m = new ArrayList<float[][]>();
		random.setSeed(Defaults.seed);
		generateMatrix(depth, width, height);
		initializeZeros();

	}

	public Matrix3(float[][] mat) {
		m.add(mat);
	}

	public Matrix3(List<float[][]> m) {
		this.m = m;
		random.setSeed(Defaults.seed);

	}
	

	public void generateMatrix(int depth, int width, int height) {

		for (int i = 0; i < depth; i++) {
			m.add(new float[width][height]);
		}
	}

	public void initializeZeros() {
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < m.get(0).length; j++) {
				for (int k = 0; k < m.get(0)[0].length; k++) {
					m.get(i)[j][k] = 0f;
				}
			}
		}
	}

	public void initializeRandom(float max) {
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < m.get(0).length; j++) {
				for (int k = 0; k < m.get(0)[0].length; k++) {
					m.get(i)[j][k] = (float) (random.nextGaussian() * max);
				}
			}
		}

	}

	public void initializeOnes() {
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < m.get(0).length; j++) {
				for (int k = 0; k < m.get(0)[0].length; k++) {
					m.get(i)[j][k] = 1f;
				}
			}
		}

	}

	public Matrix3 scale(float scale) {
		Matrix3 newMatrix = new Matrix3(m.size(), m.get(0).length, m.get(0)[0].length);
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < m.get(0).length; j++) {
				for (int k = 0; k < m.get(0)[0].length; k++) {
					newMatrix.m.get(i)[j][k] = m.get(i)[j][k] * scale;
				}
			}
		}
		return newMatrix;
	}

	public Matrix3 hadamardProduct(Matrix3 matrix) {
		Matrix3 newMatrix = new Matrix3(m.size(), m.get(0).length, m.get(0)[0].length);
		if (haveSameDimensions(newMatrix)) {
			for (int i = 0; i < m.size(); i++) {
				for (int j = 0; j < m.get(0).length; j++) {
					for (int k = 0; k < m.get(0)[0].length; k++) {
						newMatrix.m.get(i)[j][k] = m.get(i)[j][k] * matrix.m.get(i)[j][k];
					}
				}
			}
		} else {
			ErrorHandler.throwException("Doesn't have same dimensions");
		}
		return newMatrix;
	}

	public Matrix3 sum(Matrix3 matrix) {
		Matrix3 newMatrix = new Matrix3(m.size(), m.get(0).length, m.get(0)[0].length);
		if (haveSameDimensions(newMatrix)) {
			for (int i = 0; i < m.size(); i++) {
				for (int j = 0; j < m.get(0).length; j++) {
					for (int k = 0; k < m.get(0)[0].length; k++) {
						newMatrix.m.get(i)[j][k] = m.get(i)[j][k] + matrix.m.get(i)[j][k];
					}
				}
			}
		} else {
			ErrorHandler.throwException("Doesn't have same dimensions");
		}
		return newMatrix;
	}

	public Matrix3 difference(Matrix3 matrix) {
		Matrix3 newMatrix = new Matrix3(m.size(), m.get(0).length, m.get(0)[0].length);
		if (haveSameDimensions(newMatrix)) {
			for (int i = 0; i < m.size(); i++) {
				for (int j = 0; j < m.get(0).length; j++) {
					for (int k = 0; k < m.get(0)[0].length; k++) {
						newMatrix.m.get(i)[j][k] = m.get(i)[j][k] - matrix.m.get(i)[j][k];
					}
				}
			}
		} else {
			ErrorHandler.throwException("Doesn't have same dimensions");
		}
		return newMatrix;
	}

	public boolean haveSameChannel(Matrix3 matrix) {
		return m.size() == matrix.m.size();
	}

	public boolean haveSameDimensions(Matrix3 matrix) {
		return m.size() == matrix.m.size() && m.get(0).length == matrix.m.get(0).length
				&& m.get(0)[0].length == matrix.m.get(0)[0].length;
	}

	/**
	 * This method is widely used for convolutional neural networks. It will create
	 * an output image with an input image and a determined amount of filters
	 * 
	 * @param input   The input map we want to process
	 * @param filters The filters we want to apply to the input map. Each filter
	 *                will create a new channel for the output image
	 * @param stride  The step increase we will do when we want to move the filter
	 *                through the image
	 * @param padding The zero padding we will add using different ways; we have
	 *                'valid' 'same' and 'full',
	 * @return The output image with the operation applied
	 */
	public static Matrix3 convolution(Matrix3 input, List<Filter> filters, int stride, PaddingZero padding) {
		// Parameters
		int widthFilter = filters.get(0).m.get(0).length;
		int heightFilter = filters.get(0).m.get(0)[0].length;
		int depthFilter = filters.get(0).m.size();
		int filterCount = filters.size();
		int widthInputMap = input.m.get(0).length;
		int heightInputMap = input.m.get(0)[0].length;
		int depthInputMap = input.m.size();

		int startPadding = 0;
		int endPadding = 0;

		// Check depths
		if (depthInputMap != depthFilter) {
			System.out.println("Depth must match");
			return null;
		}

		// Calculate output size of feature map
		int outputSize = (widthInputMap - widthFilter + startPadding + endPadding) / stride + 1;
		Matrix3 outputMap = new Matrix3(filterCount, outputSize, outputSize);

		// Calculate padding depending on mode
		if (padding == PaddingZero.Valid) {
			startPadding = 0;
			endPadding = 0;
		}
		if (padding == PaddingZero.Same) {
			float size = ((widthFilter - stride) / 2f);
			if (((widthFilter - stride) % 2 == 0)) {
				startPadding = (int) size;
				endPadding = (int) size;
			} else {
				startPadding = (int) (size - 1f);
				endPadding = (int) (size + 1f);
			}

		}
		if (padding == PaddingZero.Full) {
			endPadding = widthFilter - 1;
			for (int i = 0; i <= endPadding; i++) {
				if ((widthInputMap - widthFilter + i + endPadding) % stride == 0) {
					startPadding = i;
					System.out.println(startPadding);
				}
			}
		}
		// Convolution operation
		for (int f = 0; f < filterCount; f++) {
			Matrix m = new Matrix(outputSize, outputSize);
			for (int i = 0; i < widthInputMap - widthFilter; i += stride) {
				for (int j = 0; j < heightInputMap - heightFilter; j += stride) {
					for (int k = 0; k < widthFilter; k++) {
						for (int l = 0; l < heightFilter; l++) {
							float sum = 0f;
							for (int c = 0; c < depthFilter; c++) {
								sum += filters.get(f).m.get(c)[i][l] * input.m.get(c)[i + k][j + l];
							}
							m.m[i + startPadding][j + startPadding] = sum;
						}
					}
				}
			}
			outputMap.addMatrix2D(m);
		}

		return outputMap;
	}
	/**
	 * This method is widely used for convolutional neural networks. It will create
	 * an output image with an input image to reduce the size and operations of the network
	 * 
	 * @param input   The input map we want to process
	 * @param poolingType The method to reduce the spatial of the image; 'average' or 'max'
	 *                
	 * @param stride  The step increase we will do when we want to move the filter
	 *                through the image
	 * @param sizeKernel How big is the kernel we want to apply the pooling
	 *          
	 * @return The output image with the operation applied
	 */
	public static Matrix3 pooling(Matrix3 input, Pooling poolingType, int stride, int sizeKernel) {
		// Parameters
		int widthInputMap = input.m.get(0).length;
		int heightInputMap = input.m.get(0)[0].length;
		int depth = input.m.size();
		int finalSizeWidth = (widthInputMap - sizeKernel) / stride;
		int finalSizeHeight = (heightInputMap - sizeKernel) / stride;
		float iterationsKernel = sizeKernel * sizeKernel;
		// Create output matrix
		Matrix3 outputMap = new Matrix3(depth, widthInputMap, heightInputMap);

		// Pooling
		for (int c = 0; c < depth; c++) {
			Matrix mat = new Matrix(widthInputMap, heightInputMap);
			for (int i = 0; i < finalSizeWidth; i++) {
				for (int j = 0; j < finalSizeHeight; j++) {
					float value = -9999999999999999f;
					for (int k = 0; k < sizeKernel; k++) {
						for (int l = 0; l < sizeKernel; l++) {
							float currentValue = input.m.get(c)[i + k][j + l];
							if (Pooling.Max == poolingType) {
								value = currentValue > value ? currentValue : value;
							}
							if (Pooling.Average == poolingType) {
								value += currentValue;
							}
						}
					}
					if (Pooling.Max == poolingType) {
						mat.m[i][j] = value;
					}
					if (Pooling.Average == poolingType) {
						mat.m[i][j] = value / ((float) iterationsKernel);
					}
				}
			}
			outputMap.addMatrix2D(mat);
		}
		return outputMap;

	}

	public Matrix getChannel(int id) {
		return new Matrix(m.get(id));
	}

	public void addMatrix2D(Matrix matrix) {
		m.add(matrix.m);
	}

	public Matrix3 flip180() {
		Matrix3 newMatrix = new Matrix3(m.size(), m.get(0).length, m.get(0)[0].length);
		for (int i = 0; i < m.size(); i++) {
			for (int j = 0; j < m.get(0).length; j++) {
				for (int k = 0; k < m.get(0)[0].length; k++) {
					newMatrix.m.get(i)[j][k] = m.get(i)[m.get(0).length - j][m.get(0)[0].length - k];
				}
			}
		}
		return newMatrix;
	}

	public Matrix3 transpose() {
		List<Matrix> matrixs = new ArrayList<Matrix>();
		for (int i = 0; i < m.size(); i++) {
			matrixs.add(getMatrix(i).transpose());
		}
		return Matrix3.create(matrixs);
	}

	public static Matrix3 create(Matrix matrix, int times) {
		Matrix3 newMatrix = new Matrix3(times, matrix.m.length, matrix.m[0].length);
		for (int i = 0; i < times; i++) {
			newMatrix.m.add(matrix.m);
		}
		return newMatrix;
	}

	public static Matrix3 extend(Matrix matrix, int times) {
		Matrix3 newMatrix = new Matrix3(times, matrix.m.length, matrix.m[0].length);
		for (int i = 0; i < times; i++) {
			newMatrix.m.add(matrix.m);
		}
		return newMatrix;
	}

	public Matrix getMatrix(int channelPosition) {
		return new Matrix(m.get(channelPosition));
	}

	public static Matrix3 create(List<Matrix> matrixs) {
		Matrix3 newMatrix = new Matrix3(matrixs.size(), matrixs.get(0).m.length, matrixs.get(0).m[0].length);
		for (int i = 0; i < matrixs.size(); i++) {
			newMatrix.addMatrix2D(matrixs.get(0));
		}
		return newMatrix;
	}
}
