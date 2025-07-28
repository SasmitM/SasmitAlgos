package com.Sasmit;

import java.util.Arrays;

public class StrassenMatrixMultiplication {

    // Main method to demonstrate Strassen Matrix Multiplication
    public static void main(String[] args) {
        // Example matrices (can be non-square or non-power-of-2)
        int[][] matrixA = {
                {1, 2, 3},
                {4, 5, 6},
                {7, 8, 9}
        };

        int[][] matrixB = {
                {9, 8, 7},
                {6, 5, 4},
                {3, 2, 1}
        };

        System.out.println("Matrix A:");
        printMatrix(matrixA);
        System.out.println("\nMatrix B:");
        printMatrix(matrixB);

        try {
            // Perform Strassen multiplication
            int[][] resultMatrix = strassenMultiply(matrixA, matrixB);

            System.out.println("\nResult of Strassen Matrix Multiplication (A * B):");
            printMatrix(resultMatrix);

            // You can also compare with standard multiplication for verification
            int[][] standardResult = standardMultiply(matrixA, matrixB);
            System.out.println("\nResult of Standard Matrix Multiplication (for comparison):");
            printMatrix(standardResult);

            // Test with different dimensions (e.g., 2x3 * 3x2)
            int[][] matrixC = {
                    {1, 2, 3},
                    {4, 5, 6}
            };

            int[][] matrixD = {
                    {7, 8},
                    {9, 1},
                    {2, 3}
            };

            System.out.println("\nMatrix C:");
            printMatrix(matrixC);
            System.out.println("\nMatrix D:");
            printMatrix(matrixD);

            int[][] resultCD = strassenMultiply(matrixC, matrixD);
            System.out.println("\nResult of Strassen Matrix Multiplication (C * D):");
            printMatrix(resultCD);

        } catch (IllegalArgumentException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    /**
     * Performs Strassen Matrix Multiplication on two matrices.
     * This method handles padding for matrices whose dimensions are not powers of 2.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @return The resulting matrix from A * B.
     * @throws IllegalArgumentException if matrices cannot be multiplied (invalid dimensions).
     */
    public static int[][] strassenMultiply(int[][] A, int[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int rowsB = B.length;
        int colsB = B[0].length;

        // Check for valid multiplication dimensions
        if (colsA != rowsB) {
            throw new IllegalArgumentException("Matrices dimensions are not compatible for multiplication. " +
                    "Columns of A must match rows of B.");
        }

        // Determine the maximum dimension among all matrices
        int maxDim = Math.max(rowsA, Math.max(colsA, Math.max(rowsB, colsB)));

        // Find the next power of 2 for padding
        int n = 1;
        while (n < maxDim) {
            n <<= 1; // Equivalent to n = n * 2
        }

        // Pad matrices A and B to the new square dimension n x n
        int[][] paddedA = padMatrix(A, n);
        int[][] paddedB = padMatrix(B, n);

        // Perform recursive Strassen multiplication on padded matrices
        int[][] paddedResult = strassenRecursive(paddedA, paddedB);

        // Trim the result back to the original dimensions (rowsA x colsB)
        return trimMatrix(paddedResult, rowsA, colsB);
    }

    /**
     * Recursive helper function for Strassen's algorithm.
     * Assumes input matrices are square and their dimensions are powers of 2.
     *
     * @param A The first matrix (must be n x n, where n is a power of 2).
     * @param B The second matrix (must be n x n, where n is a power of 2).
     * @return The resulting n x n matrix.
     */
    private static int[][] strassenRecursive(int[][] A, int[][] B) {
        int n = A.length; // Dimension of the square matrices

        // Base case: If matrix is 1x1, perform simple multiplication
        if (n == 1) {
            return new int[][]{{A[0][0] * B[0][0]}};
        }

        // Divide matrices into sub-matrices
        int halfN = n / 2;

        int[][] A11 = new int[halfN][halfN];
        int[][] A12 = new int[halfN][halfN];
        int[][] A21 = new int[halfN][halfN];
        int[][] A22 = new int[halfN][halfN];

        int[][] B11 = new int[halfN][halfN];
        int[][] B12 = new int[halfN][halfN];
        int[][] B21 = new int[halfN][halfN];
        int[][] B22 = new int[halfN][halfN];

        // Populate sub-matrices
        splitMatrix(A, A11, 0, 0);
        splitMatrix(A, A12, 0, halfN);
        splitMatrix(A, A21, halfN, 0);
        splitMatrix(A, A22, halfN, halfN);

        splitMatrix(B, B11, 0, 0);
        splitMatrix(B, B12, 0, halfN);
        splitMatrix(B, B21, halfN, 0);
        splitMatrix(B, B22, halfN, halfN);

        // Strassen's 7 products
        // P1 = (A11 + A22)(B11 + B22)
        int[][] P1 = strassenRecursive(add(A11, A22), add(B11, B22));
        // P2 = (A21 + A22)B11
        int[][] P2 = strassenRecursive(add(A21, A22), B11);
        // P3 = A11(B12 - B22)
        int[][] P3 = strassenRecursive(A11, subtract(B12, B22));
        // P4 = A22(B21 - B11)
        int[][] P4 = strassenRecursive(A22, subtract(B21, B11));
        // P5 = (A11 + A12)B22
        int[][] P5 = strassenRecursive(add(A11, A12), B22);
        // P6 = (A21 - A11)(B11 + B12)
        int[][] P6 = strassenRecursive(subtract(A21, A11), add(B11, B12));
        // P7 = (A12 - A22)(B21 + B22)
        int[][] P7 = strassenRecursive(subtract(A12, A22), add(B21, B22));

        // Calculate C sub-matrices using P products
        // C11 = P1 + P4 - P5 + P7
        int[][] C11 = add(subtract(add(P1, P4), P5), P7);
        // C12 = P3 + P5
        int[][] C12 = add(P3, P5);
        // C21 = P2 + P4
        int[][] C21 = add(P2, P4);
        // C22 = P1 - P2 + P3 + P6
        int[][] C22 = add(subtract(add(P1, P3), P2), P6);

        // Join C sub-matrices into the final result matrix
        int[][] C = new int[n][n];
        joinMatrix(C, C11, 0, 0);
        joinMatrix(C, C12, 0, halfN);
        joinMatrix(C, C21, halfN, 0);
        joinMatrix(C, C22, halfN, halfN);

        return C;
    }

    /**
     * Adds two matrices of the same dimensions.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @return A new matrix representing A + B.
     */
    private static int[][] add(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] + B[i][j];
            }
        }
        return C;
    }

    /**
     * Subtracts two matrices of the same dimensions.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @return A new matrix representing A - B.
     */
    private static int[][] subtract(int[][] A, int[][] B) {
        int n = A.length;
        int[][] C = new int[n][n];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                C[i][j] = A[i][j] - B[i][j];
            }
        }
        return C;
    }

    /**
     * Splits a larger matrix into a smaller sub-matrix.
     *
     * @param sourceMatrix         The original larger matrix.
     * @param destinationSubMatrix The sub-matrix to populate.
     * @param rowOffset            The starting row index in the source matrix.
     * @param colOffset            The starting column index in the source matrix.
     */
    private static void splitMatrix(int[][] sourceMatrix, int[][] destinationSubMatrix, int rowOffset, int colOffset) {
        for (int i = 0; i < destinationSubMatrix.length; i++) {
            for (int j = 0; j < destinationSubMatrix[0].length; j++) {
                destinationSubMatrix[i][j] = sourceMatrix[i + rowOffset][j + colOffset];
            }
        }
    }

    /**
     * Joins a sub-matrix into a larger destination matrix.
     *
     * @param destinationMatrix The larger matrix to join into.
     * @param sourceSubMatrix   The sub-matrix to join.
     * @param rowOffset         The starting row index in the destination matrix.
     * @param colOffset         The starting column index in the destination matrix.
     */
    private static void joinMatrix(int[][] destinationMatrix, int[][] sourceSubMatrix, int rowOffset, int colOffset) {
        for (int i = 0; i < sourceSubMatrix.length; i++) {
            for (int j = 0; j < sourceSubMatrix[0].length; j++) {
                destinationMatrix[i + rowOffset][j + colOffset] = sourceSubMatrix[i][j];
            }
        }
    }

    /**
     * Pads a given matrix with zeros to a new square dimension.
     *
     * @param originalMatrix The matrix to pad.
     * @param newSize        The desired new square dimension (e.g., 4 for a 3x3 matrix).
     * @return A new padded matrix of size newSize x newSize.
     */
    private static int[][] padMatrix(int[][] originalMatrix, int newSize) {
        int rows = originalMatrix.length;
        int cols = originalMatrix[0].length;
        int[][] paddedMatrix = new int[newSize][newSize];

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                paddedMatrix[i][j] = originalMatrix[i][j];
            }
        }
        // Remaining elements are already 0 by default for int arrays
        return paddedMatrix;
    }

    /**
     * Trims a matrix to the specified original dimensions.
     * Used after Strassen's algorithm on padded matrices.
     *
     * @param paddedMatrix The matrix to trim.
     * @param originalRows The original number of rows.
     * @param originalCols The original number of columns.
     * @return A new matrix with the original dimensions.
     */
    private static int[][] trimMatrix(int[][] paddedMatrix, int originalRows, int originalCols) {
        int[][] trimmedMatrix = new int[originalRows][originalCols];
        for (int i = 0; i < originalRows; i++) {
            for (int j = 0; j < originalCols; j++) {
                trimmedMatrix[i][j] = paddedMatrix[i][j];
            }
        }
        return trimmedMatrix;
    }

    /**
     * Helper method to print a matrix to the console.
     *
     * @param matrix The matrix to print.
     */
    public static void printMatrix(int[][] matrix) {
        if (matrix == null || matrix.length == 0) {
            System.out.println("[]");
            return;
        }
        for (int[] row : matrix) {
            System.out.println(Arrays.toString(row));
        }
    }

    /**
     * Standard matrix multiplication for comparison.
     *
     * @param A The first matrix.
     * @param B The second matrix.
     * @return The resulting matrix from A * B.
     * @throws IllegalArgumentException if matrices cannot be multiplied.
     */
    public static int[][] standardMultiply(int[][] A, int[][] B) {
        int rowsA = A.length;
        int colsA = A[0].length;
        int rowsB = B.length;
        int colsB = B[0].length;

        if (colsA != rowsB) {
            throw new IllegalArgumentException("Matrices dimensions are not compatible for multiplication.");
        }

        int[][] C = new int[rowsA][colsB];
        for (int i = 0; i < rowsA; i++) {
            for (int j = 0; j < colsB; j++) {
                for (int k = 0; k < colsA; k++) {
                    C[i][j] += A[i][k] * B[k][j];
                }
            }
        }
        return C;
    }
}
