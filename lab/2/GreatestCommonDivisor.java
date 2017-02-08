/**
 * file: GreatestCommonDivisor.java
 * author: Antonino Tan-Marcello
 * course: MSCS 630
 * assignment: lab 2 (part 1)
 * due date: February 8, 2017
 * 
 * This file contains the implementation of 
 * a method that finds the greatest common
 * divisor of two numbers, also know as the 
 * Euclidean Algorithm. 
 */

import java.util.Scanner;

/**
 * ExtEuclidAlgo
 * 
 * This class implements methods to compute the greatest
 * common divisor, also known as the Euclid algorithm,
 * of two numbers and prints the results.
 */
public class GreatestCommonDivisor {

  /**
    * euclidAlg
    * 
    * This functions computes the greatest common divisor 
    * of two numbers. 
    * 
    * Parameters:
    *   a, b: the numbers in which to compute the greatest common divisor.
    *   
    * Return value: 
    *   The greatest common divisor of a and b.
    */
  public static long euclidAlg(long a, long b) {
    long c;
    while (b != 0) {
      c=a%b;
      a=b;
      b=c;
    }
    return a;
  }

  /**
   * main 
   *
   * Main method that creates a scanner class in order to
   * receive user input. With this input, the main method
   * calls the euclidAlg function.
   */
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while(sc.hasNext()) {  
      long num1 = sc.nextInt();
      long num2 = sc.nextInt();
      System.out.println(euclidAlg(num1,num2));
    }
  }

}
