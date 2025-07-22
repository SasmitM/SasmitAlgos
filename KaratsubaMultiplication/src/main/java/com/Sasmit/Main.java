package com.Sasmit;
import java.math.*; // Required for handling large numbers

public class Main {

    /**
     * Implements the Karatsuba multiplication algorithm for two large numbers.
     * This algorithm recursively multiplies numbers by splitting them into halves,
     * reducing the number of recursive multiplications from 4 to 3.
     *
     * @param x The first BigInteger number.
     * @param y The second BigInteger number.
     * @return The product of x and y as a BigInteger.
     */
    public static BigInteger karatsubaMultiply(BigInteger x, BigInteger y) {
        // Base case: If either number is single digit, multiply directly
        // The problem statement implies this base case for direct multiplication.
        // A single-digit number is less than 10.
        if (x.compareTo(BigInteger.TEN) < 0 || y.compareTo(BigInteger.TEN) < 0) {
            return x.multiply(y);
        }

        // Convert BigIntegers to strings to easily determine length and split
        String sX = x.toString();
        String sY = y.toString();

        // Determine the maximum length (n) of the two numbers
        int n = Math.max(sX.length(), sY.length());

        // Ensure n is even for splitting and pad with leading zeros if necessary.
        // This makes sure both numbers have the same length, and that length is even.
        if (n % 2 != 0) {
            n++; // Increment n to make it even
        }
        // Pad sX and sY with leading zeros to achieve the uniform length 'n'
        sX = String.format("%" + n + "s", sX).replace(' ', '0');
        sY = String.format("%" + n + "s", sY).replace(' ', '0');

        // Recreate BigIntegers from the padded strings to ensure consistent length
        x = new BigInteger(sX);
        y = new BigInteger(sY);

        int halfN = n / 2; // Half the length for splitting

        // Split x into two halves: a (most significant half) and b (least significant half)
        // Example: if x = "1234", a = "12", b = "34"
        BigInteger a = new BigInteger(sX.substring(0, halfN));
        BigInteger b = new BigInteger(sX.substring(halfN));

        // Split y into two halves: c (most significant half) and d (least significant half)
        // Example: if y = "5678", c = "56", d = "78"
        BigInteger c = new BigInteger(sY.substring(0, halfN));
        BigInteger d = new BigInteger(sY.substring(halfN));

        // Recursively calculate the three products needed for Karatsuba
        // 1. ac = a * c
        BigInteger ac = karatsubaMultiply(a, c);
        // 2. bd = b * d
        BigInteger bd = karatsubaMultiply(b, d);
        // 3. (a+b)*(c+d)
        BigInteger apb_cpd = karatsubaMultiply(a.add(b), c.add(d));

        // Calculate (ad + bc) using Karatsuba's trick: (a+b)(c+d) - ac - bd
        BigInteger ad_plus_bc = apb_cpd.subtract(ac).subtract(bd);

        // Combine the results using powers of 10
        // The formula is: result = ac * 10^n + (ad + bc) * 10^(n/2) + bd
        BigInteger term1 = ac.multiply(BigInteger.TEN.pow(n));
        BigInteger term2 = ad_plus_bc.multiply(BigInteger.TEN.pow(halfN));
        BigInteger result = term1.add(term2).add(bd);

        return result;
    }

    public static void main(String[] args) {
        System.out.println("--- Karatsuba Multiplication Examples ---");

        // Small test cases for Karatsuba
        BigInteger num1 = new BigInteger("12");
        BigInteger num2 = new BigInteger("34");
        System.out.println("12 * 34 = " + karatsubaMultiply(num1, num2)); // Expected: 408

        num1 = new BigInteger("56");
        num2 = new BigInteger("78");
        System.out.println("56 * 78 = " + karatsubaMultiply(num1, num2)); // Expected: 4368

        num1 = new BigInteger("1234");
        num2 = new BigInteger("5678");
        System.out.println("1234 * 5678 = " + karatsubaMultiply(num1, num2)); // Expected: 7006652

        // Test with odd number of digits (will be padded internally by the algorithm)
        num1 = new BigInteger("123");
        num2 = new BigInteger("456");
        System.out.println("123 * 456 = " + karatsubaMultiply(num1, num2)); // Expected: 56088

        // The 64-digit numbers from the assignment
        BigInteger largeNum1 = new BigInteger("3141592653589793238462643383279502884197169399375105820974944592");
        BigInteger largeNum2 = new BigInteger("2718281828459045235360287471352662497757247093699959574966967627");

        System.out.println("\nMultiplying the 64-digit numbers:");
        System.out.println("Num1: " + largeNum1);
        System.out.println("Num2: " + largeNum2);
        BigInteger product = karatsubaMultiply(largeNum1, largeNum2);
        System.out.println("Product: " + product);
    }
}
