import java.io.FileWriter;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Random;

public class simpMult {
  private static int primitiveOperationCount = 0;

  public static BigInteger mult(BigInteger x, BigInteger y, int n) {
    BigInteger result = BigInteger.ZERO;
    primitiveOperationCount++; // Assignment for result

    primitiveOperationCount++; // first assignment for i = 0
    for (int i = 0; i < n; i++) {
      primitiveOperationCount ++; // Comparison (i < n)

      BigInteger partialProduct = BigInteger.ZERO;
      BigInteger carry = BigInteger.ZERO;
      primitiveOperationCount += 2; // Assignment for partialProduct and carry

      BigInteger digitMultiplier = y.divide(BigInteger.TEN.pow(i)).mod(BigInteger.TEN);
      primitiveOperationCount += 4; // Assignment, division, exponentiation, modulo

      primitiveOperationCount++; // Assignment for j = 0
      for (int j = 0; j < n; j++) {
        primitiveOperationCount ++; // Comparison (j < n)

        BigInteger digitMultiplicand = x.divide(BigInteger.TEN.pow(j)).mod(BigInteger.TEN);
        primitiveOperationCount += 4; // Assignment, division, exponentiation, modulo

        BigInteger product = digitMultiplier.multiply(digitMultiplicand);
        primitiveOperationCount += 2; // Assignment, multiplication
        
        partialProduct = partialProduct.add(product.mod(BigInteger.TEN).multiply(BigInteger.TEN.pow(j+i)));
        primitiveOperationCount += 6; // Assignment, addition, modulo, multiplication, addition j+i, exponentiation

        carry = carry.add(product.divide(BigInteger.TEN).multiply(BigInteger.TEN.pow(j+i+1)));
        primitiveOperationCount += 7; // Assignment, addition, division, multiplication, 2 addition (j+i+1),exponentiation

        primitiveOperationCount += 2; // Addition, assignment for counter (j++)
      }
      primitiveOperationCount += 2; // last comparison of j < n to exit loop

      System.out.println("Partial Product :" + partialProduct);
      System.out.println("Carry           :" + carry + "\n");

      result = result.add(partialProduct).add(carry);
      primitiveOperationCount += 3; // 2 Addition, assignment

      primitiveOperationCount += 2; // Addition, assignment for counter (i++)
    }
    primitiveOperationCount += 2; // last comparison of i < n to exit loop

    primitiveOperationCount++; // return
    return result;
  }

  public static void main(String[] args) {
    Random random = new Random();

    //specify value n here
    //for experiment, n <= 500
    for (int n = 1; n <= 3; n++) {

      System.out.println("\n=============== Number of digits for multiplicand and multiplier: " + n + " =============== \n");
      try {
        //Create separate FileWriter objects for each variable
        FileWriter xWriter = new FileWriter("x_output.txt", true);
        FileWriter yWriter = new FileWriter("y_output.txt", true);
        FileWriter nWriter = new FileWriter("n_output.txt", true);
        FileWriter operationWriter = new FileWriter("operation_output.txt", true);
        
        for (int count = 1; count <= 10; count++) {

          // Generate random multiplicand
          BigInteger x = BigInteger.valueOf(random.nextInt(9) + 1); // Ensure first digit is not zero
          for (int i = 1; i < n; i++) {
            x = x.multiply(BigInteger.TEN).add(BigInteger.valueOf(random.nextInt(10))); 
            // Digits from 0 to 9
          }

          // Generate random multiplier
          BigInteger y = BigInteger.valueOf(random.nextInt(9) + 1); // Ensure first digit is not zero
          for (int i = 1; i < n; i++) {
            y = y.multiply(BigInteger.TEN).add(BigInteger.valueOf(random.nextInt(10))); 
            // Digits from 0 to 9
          }

          System.out.println("Multiplication #" + count + ": \n");

          // Perform multiplication using generated x and multiplier
          BigInteger expected = x.multiply(y);
          BigInteger product = mult(x, y, n);

          //Print the results
          System.out.println("Multiplicand :" + x);
          System.out.println("Multiplier   :" + y);
          System.out.println("Expected     :" + expected);
          System.out.println("Actual       :" + product + "\n");
          System.out.println("Total primitive operations: " + primitiveOperationCount);

          System.out.println("\n====================================================\n");

          nWriter.write(n + "\n");
          xWriter.write(x + "\n");
          yWriter.write(y + "\n");
          operationWriter.write(primitiveOperationCount + "\n");

          // Reset counter
          primitiveOperationCount = 0;

          // Ensure the correctness of the algorithm
          assert expected.equals(product);
        }
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