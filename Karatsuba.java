import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;


public class Karatsuba {
    private static int primitiveOperationCount = 0;

    public static BigInteger mult(BigInteger x, BigInteger y) {

        primitiveOperationCount += 5; // Comparison operation and method call
        if (x.compareTo(BigInteger.TEN) < 0 && y.compareTo(BigInteger.TEN) < 0) {
            primitiveOperationCount += 2; // Multiplication operation and return
            return x.multiply(y);
        }

        int noOneLength = numLength(x);
        primitiveOperationCount += 2; // Assignment operation and method call
        int noTwoLength = numLength(y);
        primitiveOperationCount += 2; // Assignment operation and method call

        int maxNumLength = Math.max(noOneLength, noTwoLength);
        primitiveOperationCount += 2; // Assignment and method call

        Integer halfMaxNumLength = (maxNumLength / 2) + (maxNumLength % 2);
        primitiveOperationCount += 4; // Integer division, modulo, addition, assignment

        BigInteger maxNumLengthTen = BigInteger.TEN.pow(halfMaxNumLength);
        primitiveOperationCount += 2; // Power operation and assignment

        // Compute the expressions
        BigInteger a = x.divide(maxNumLengthTen);
        primitiveOperationCount += 2;
        BigInteger b = x.remainder(maxNumLengthTen);
        primitiveOperationCount += 2;
        BigInteger c = y.divide(maxNumLengthTen);
        primitiveOperationCount += 2;
        BigInteger d = y.remainder(maxNumLengthTen);
        primitiveOperationCount += 2;

        BigInteger z0 = mult(a, c);
        primitiveOperationCount += 2; // Method call and assignment
        BigInteger z1 = mult(a.add(b), c.add(d));
        primitiveOperationCount += 4; // Method call, and assignment
        BigInteger z2 = mult(b, d);
        primitiveOperationCount += 2; // Method call and assignment

        BigInteger ans = z0.multiply(BigInteger.TEN.pow(halfMaxNumLength * 2))
                .add(z1.subtract(z0).subtract(z2).multiply(BigInteger.TEN.pow(halfMaxNumLength))
                        .add(z2));
        primitiveOperationCount += 10; // Power operation, multiplication, addition, subtraction, and assignment

        primitiveOperationCount++;  // Return
        return ans;
    }


    public static int numLength(BigInteger n)
    {
        int noLen = 0;
        primitiveOperationCount += 1; // Assignment

        primitiveOperationCount++; // First comparison of while loop

        while (!n.equals(BigInteger.ZERO)) {
            noLen++;
            n = n.divide(BigInteger.TEN);
            primitiveOperationCount += 5; // Increment, Division, Assignment, Loop comparison
        }

        // Returning length of number n
        primitiveOperationCount++; // Return
        return noLen;
    }

    public static void main(String[] args) {

        Random random = new Random();

        // Specify the value of n here
        // n=500 is used for the experiment
        for (int n = 1; n <= 10; n++) {

            System.out.println("\n=============== Number of digits for multiplicand and multiplier: " + n + " ===============\n");
            try {
                // Create separate FileWriter objects for each variable
                FileWriter nWriter = new FileWriter("n_value.txt", true);
                FileWriter xWriter = new FileWriter("x_output.txt", true);
                FileWriter yWriter = new FileWriter("y_output.txt", true);
                FileWriter operationWriter = new FileWriter("operation_output.txt", true);

                for (int count = 1; count <= 10; count++) {
                    // Generate random multiplicand
                    BigInteger x = BigInteger.valueOf(random.nextInt(9) + 1);
                    for (int i = 1; i < n; i++) {
                        x = x.multiply(BigInteger.TEN).add(BigInteger.valueOf(random.nextInt(10)));
                    }

                    // Generate random multiplier
                    BigInteger y = BigInteger.valueOf(random.nextInt(9) + 1);
                    for (int i = 1; i < n; i++) {
                        y = y.multiply(BigInteger.TEN).add(BigInteger.valueOf(random.nextInt(10)));
                    }

                    // Perform multiplication using generated multiplicand and multiplier
                    BigInteger expectedProduct = x.multiply(y);
                    BigInteger actualProduct = mult(x, y);

                    // Print the results
                    System.out.println("Multiplication #" + count + ": \n");
                    System.out.println("Multiplicand : " + x);
                    System.out.println("Multiplier   : " + y);
                    System.out.println("Expected     : " + expectedProduct);
                    System.out.println("Actual       : " + actualProduct + "\n");
                    System.out.println("Total primitive operations: " + primitiveOperationCount);

                    System.out.println("\n====================================================\n");

                    // Write the values of x, y, and actualProduct to separate files
                    nWriter.write(n + "\n");
                    xWriter.write(x + "\n");
                    yWriter.write(y + "\n");
                    operationWriter.write(primitiveOperationCount + "\n");

                    // Reset counter
                    primitiveOperationCount = 0;

                    // Ensure the correctness of the algorithm
                    assert expectedProduct.equals(actualProduct);
                }

                // Close the FileWriter objects
                nWriter.close();
                xWriter.close();
                yWriter.close();
                operationWriter.close();

                System.out.println("Data has been written to x_output.txt, y_output.txt, and operation_output.txt");
            } catch (IOException e) {
                System.out.println("An error occurred while writing to the file: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }
}