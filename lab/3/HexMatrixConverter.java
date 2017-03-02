/**
  * file: HexMatrixConverter.java
  * author: Antonino Tan-Marcello
  * course: MSCS 630
  * assignment: lab 3 (part 2)
  * due date: March 1, 2017
  * 
  * This file contains a program that converts each character of a  
  * string input into hexadecimal values, stores them in a 4 x 4
  * matrix (array) and prints the the array. 
  */

import java.util.Scanner;

/**
  * HexMatrixConverter
  * 
  * This class implements methods that will convert characters of
  * an input string to their hexadecimal values and store the results
  * in an array. The class also implements a print method to
  * display the results in a grid-like formation.
  */
public class HexMatrixConverter {

  /**
    * getHexMatP
    * 
    * This function converts a string argument into a decimal value, and stores
    * the values in a 4x4 2-dimensional array of type integer. If the string is less
    * than 16 characters the substitution character argument will be converted to a decimal
    * value and fill up the remaining vacant spots in the array. This method then returns the filled 
    * 4x4 integer array.
    * 
    * Parameters:
    *   c: the substitution character used to fill the remaining array spots, after being
    *      converted to a decimal value. 
    *   s: the string to have its characters converted to their decimal values and
    *      stored into the array.
    *   
    * Return value: 
    *   The full 4x4 integer array (matrix), storing the decimal values of the input string.
    */
  public static int[][] getHexMatP(char c,String s) {
  int[][] arr = new int[4][4];
  String z = s;
  String substr = Character.toString(c);
  while (z.length()<16) {
    z = z + substr;
  }
  int k = 0;
  for (int i = 0; i<arr.length; i++) {
    for (int j = 0; j < arr[i].length; j++) {
      char ch = z.charAt(k);
      int decVal = (int) ch;
      arr[i][j] = decVal;
      k++;
    }
  }
  return arr;  
}

  /**
   * decToHex
   * 
   * This method converts the values of an integer array to its corresponding
   * hexadecimal value, and stores those values in a new 4x4 String array (matrix), 
   * in the same, corresponding. Also any hexadecimal values that contain
   * alphabetic characters in lower-case by default are capitalized.
   * 
   * Parameters:
   *   int[][]: an integer array containing decimal values to be converted. 
   *   
   * Return value: 
   *   A 4x4 String matrix containing the corresponding hexadecimal values of the 
   *   input array elements.
   */
  public static String[][] decToHex(int[][] dec) {
    String[][] strarr = new String[4][4];    
    for (int i = 0; i<strarr.length; i++) {
      for (int j = 0; j < strarr[i].length; j++) {
        String str = Integer.toHexString(dec[i][j]).toUpperCase();
        strarr[i][j] = str;
      }
    }
    return strarr;
  }

  /**
   * displayMatrix
   * 
   * This functions iterates through a 2d String array and displays the 
   * elements in a grid-like formation.
   * 
   * Parameters: 
   *   a[][]: the array to iterate and display its stored contents.
   *   
   * Return value: 
   *   Void. (Print function displays the array.)
   */
  public static void displayMatrix(String a[][]) {  
    for (int row = 0; row < a.length; row++) {                   
      for (int column = 0; column < a[row].length; column++) {         
        System.out.print( a[column][row]);        
        System.out.print(" ");      
      }     
      System.out.println(" ");      
    }     
    System.out.println();    
  } 

  /**
   * main 
   *
   * Main method that creates a scanner object in order to
   * receive user input. The program expects to receive a value for desired substitution
   * character followed by the String that must be converted. The main method separates
   * the String input into substrings of 16 characters long, unless if the input is less than
   * 16 characters. Any remainder of an input that was greater than 16 characters
   * are grouped into a substring. Each substring is then passed
   * through the methods getHexMatP, decToHex and displayMatrix, in order to generate and display
   * the final matrix.
   */
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    String input1 = sc.nextLine();
    String input2 = sc.nextLine();
    String a = input1;
    char y = a.charAt(0);        
    if (input2.length()>16) {
      int b = 0;
      int e = b + 16;      
      while (input2.substring(b,input2.length()).length() > 16) {
        displayMatrix(decToHex(getHexMatP(y, input2.substring(b, e))));
        if (e + 16 <= input2.length()){
        b=b+16;
        e = e +16;
        }
        else { 
          break; 
        }
      }
      displayMatrix(decToHex(getHexMatP(y, input2.substring(e))));
    } 
    else { 
      displayMatrix(decToHex(getHexMatP(y, input2)));
    }
    sc.close();
  }
}