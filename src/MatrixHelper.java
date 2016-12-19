import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

/**
 * Created by skyin on 12/18/2016.
 */
public class MatrixHelper {
	public static void printMatrix(int[][] m) {
		if (m == null || m.length == 0 || m[0].length == 0) {
			System.out.println("printMatrix: Invalid matrix");
			return;
		}
		String s;
		for (int i = 0; i < m.length; i++) {
			s = "";
			for (int j = 0; j < m[0].length; j++) {
				s += String.format("%5d ", m[i][j]);
			}
			System.out.println(s);
		}
	}

	public static void printMatrixArray(int[][] m) {
		if (m == null || m.length == 0 || m[0].length == 0) {
			System.out.println("printMatrix: Invalid matrix");
			return;
		}
		String s = Arrays.deepToString(m);
		s = s.replace("[", "{");
		s = s.replace("]", "}");
		System.out.println(s);
	}

	public static int[][] genMatrix(int n, int max) {
		int[][] m = new int[n][n];
		Random random = new Random();
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m[i][j] = random.nextInt(max);
			}
		}
		return m;
	}

	public static int[][] readMatrix(String name) throws FileNotFoundException {
		Scanner scanner = new Scanner(new File(name));
		int n = scanner.nextInt();
		scanner.nextInt();
		int[][] m = new int[n][n];
		for (int i = 0; i < n; i++) {
			for (int j = 0; j < n; j++) {
				m[i][j] = scanner.nextInt();
			}
		}

		return m;
	}

	public static boolean isPosValid(int i, int j, int[][] m) {
		return m != null && m.length > 0 && m[0].length > 0 &&
				i >= 0 && j >= 0 && i < m.length && j < m[0].length;
	}

}
