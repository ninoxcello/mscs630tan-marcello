/**
  * file: MatrixDeterminant.java
  * author: Antonino Tan-Marcello
  * course: MSCS 630
  * assignment: lab 3 (part 1)
  * due date: March 1, 2017
  * 
  * This file contains a program that computes the 
  * determinant of a given matrix in a specified modulo. 
  */

import java.util.Scanner;

/**
 * MatrixDeterminant
 * 
 * This class implements functions that compute the determinant of
 * a given matrix in terms of a specified modulo.
 */
public class MatrixDeterminant {

  /**
    * cofModDet
    * 
    * This functions calculates the determinant of a given
    * matrix (array) by calling the determinant() function, and computes the 
    * results in terms of modulo m. 
    * 
    * Parameters:
    *   m: the modulo value in which to compute the values of the array and resulting determinant in. 
    *   A[][]: the array to calculate the determinant of.
    *   
    * Return value: 
    *   The determinant of the matrix, in terms of modulo m.
    */
  public static int cofModDet(int m, int[][] A) {
    int k;
    for (int j = 0; j<A[0].length; j++) {
      for (int i = 0; i<A.length; i++) {
        int p = A[i][j];
          k = p % m;
          A[i][j] = k;
        }
    }
    int g = A.length;
    int ans = determinant(A, g) % m;
    return ans;
  }

  /**
    * determinant
    * 
    * This functions calculates the determinant of a given
    * matrix (array). 
    * 
    * Parameters:   
    *   A[][]: the array to calculate the determinant of.
    *   sz: the size of the given array (N x N).
    *   
    * Return value: 
    *   The determinant of the matrix.
    */
  public static int determinant(int A[][], int sz) {    
    int dt=0;
    if(sz == 1) {
      dt = A[0][0];
    }
    else if (sz == 2) {
      dt = A[0][0]*A[1][1] - A[1][0]*A[0][1];
    }
    else {      
      dt=0;
      for(int j1=0;j1<sz;j1++) {
        int[][] m = new int[sz-1][];
        for(int k=0;k<(sz-1);k++) {              
          m[k] = new int[sz-1];
        }
        for(int i=1;i<sz;i++) {
          int j2=0;
          for(int j=0;j<sz;j++) {
            if(j == j1)
              continue;
              m[i-1][j2] = A[i][j];
              j2++;
          }
        }
        dt += Math.pow(-1.0,1.0+j1+1.0)* A[0][j1] * determinant(m, sz-1);                
      }
    }
    return dt;
  }

  /**
    * main 
    *
    * Main method that creates a scanner object in order to
    * receive user input. The program expects to receive a value for the modulo,
    * the size of the matrix array (N x N), as well as the array elements. With this
    * input, the program calls cofModDet() to compute the determinant of the input array
    * in terms of specified modulo.
    */
  public static void main(String[] args) {
    Scanner input = new Scanner(System.in);
    int f = input.nextInt();
    int n = input.nextInt();
    int[][] mtx = new int[n][n];
    for(int i = 0; i < n; i++) {
      for (int j = 0; j<n;j++) {
        mtx[i][j] = input.nextInt();
      }
    }
    System.out.println(cofModDet(f,mtx));
    input.close();
  }

}