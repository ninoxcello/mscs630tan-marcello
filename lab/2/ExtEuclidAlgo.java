/**
 * file: ExtEuclidAlgo.java
 * author: Antonino Tan-Marcello
 * course: MSCS 630
 * assignment: lab 2 (part 2)
 * due date: February 8, 2017
 * 
 * This file contains the implementation of 
 * the extended Euclidean algorithm. 
 */

import java.util.Scanner;

/**
 * ExtEuclidAlgo
 * 
 * This class implements the extended Euclidean algorithm
 * in order to find the greatest common divisor between two
 * numbers as well as compute the values for d, x, and y, from
 * the equation d=ax+by where d=gcd(a,b). This class also 
 * supports the printing of the results.
 */
public class ExtEuclidAlgo {

  /**
    * euclidAlgExt
    * 
    * This functions computes the greatest common divisor (d) 
    * of two numbers by calling the function gcd()
    * as well as computing the values for x and y
    * that satisfy the equation d=ax+by.
    * 
    * Parameters:
    *   a, b: the numbers in which to computes d, x and y.
    *   
    * Return value: 
    *   An array containing the values of d, x and y.
    */
  public static long[] euclidAlgExt(long a, long b) {
    long[] result = new long[3];
    long c = gcd(a,b);
    result[0] = c;
    long x = 0, x2 = 1;
    long y = 1, y2 = 0; 
    long tem;
    while (b != 0) {
      long quo = a / b;
      long r = a % b;
      a = b;
      b = r;
      tem = x;
      x = x2 - quo * x;
      x2 = tem; 
      tem = y;
      y = y2 - quo * y;
      y2 = tem;
    }
    result[1] = x2;
    result[2] = y2;
    return result; 
  }

  /**
   * gcd
   * 
   * This function computes the greatest common divisor 
   * of the two given inputs.
   * 
   * Parameters:
   *   a, b: the numbers in which to compute the gcd.
   * 
   * Return value: the gcd of a and b.
   */
  public static long gcd(long a, long b) {
    long c;
    while (b != 0) {
      c=a%b;
      a=b;
      b=c;
    }
    return a;
  }

  /**
   * printArray
   * 
   * This function iterates through an array of type integer,
   * printing each value it contains.
   * 
   * Parameters:
   *   a[]: the array to iterate and display values.
   * 
   * Return value: void (print function).
   */
  public static void printArray(long a[]) {
    for (long s : a) {
      System.out.print(s + " ");
    }
    System.out.println();
  }

  /**
   * main 
   *
   * Main method that creates a scanner class in order to
   * receive user input. With this input, the main method
   * calls the euclidAlgExt and printArray functions.
   */
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    while(input.hasNext()) {  
      long num1 = input.nextInt();
      long num2 = input.nextInt();
      printArray(euclidAlgExt(num1,num2));
    }

  }

}