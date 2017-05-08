/**
 * file: StringToInt.java
 * author: Antonino Tan-Marcello
 * course: MSCS 630
 * assignment: Lab 1
 * due date: January 25, 2017
 * 
 * This file contains the implementation of methods 
 * that takes a string of input, converts each value 
 * to an array of integers, and prints their new integer value.
 */

import java.io.BufferedInputStream;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

/** StringToInt
 * 
 * This class implements functions that takes a String input (plaintext),
 * converts each letter to a numerical value (using a hash map), stores
 * those values in an array of type integer, and prints the 
 * contents of the array (ciphertext).
 */
public class StringToInt {

  /**
   * str2int
   * 
   * This function takes a String argument, and converts each letter to
   * a specific numerical value and stores those integers in an array.
   * The function then returns the array.
   * 
   * Parameters:
   *   plaintext: The string to be converted.
   * 
   * Return value: An array of type integer of the converted String.
   */
  public static int[] str2int(String plaintext) {
    Map<Character, Integer> map;
    String str = plaintext;
    int[] arr = new int[plaintext.length()];  // Create an array 
    map = new HashMap<>();
    map.put('a', 0);
    map.put('b', 1);
    map.put('c', 2);
    map.put('d', 3);
    map.put('e', 4);
    map.put('f', 5);
    map.put('g', 6);
    map.put('h', 7);
    map.put('i', 8);
    map.put('j', 9);
    map.put('k', 10);
    map.put('l', 11);
    map.put('m', 12);
    map.put('n', 13);
    map.put('o', 14);
    map.put('p', 15);
    map.put('q', 16);
    map.put('r', 17);
    map.put('s', 18);
    map.put('t', 19);
    map.put('u', 20);
    map.put('v', 21);
    map.put('w', 22);
    map.put('x', 23);
    map.put('y', 24);
    map.put('z', 25);
    map.put('A', 0);
    map.put('B', 1);
    map.put('C', 2);
    map.put('D', 3);
    map.put('E', 4);
    map.put('F', 5);
    map.put('G', 6);
    map.put('H', 7);
    map.put('I', 8);
    map.put('J', 9);
    map.put('K', 10);
    map.put('L', 11);
    map.put('M', 12);
    map.put('N', 13);
    map.put('O', 14);
    map.put('P', 15);
    map.put('Q', 16);
    map.put('R', 17);
    map.put('S', 18);
    map.put('T', 19);
    map.put('U', 20);
    map.put('V', 21);
    map.put('W', 22);
    map.put('X', 23);
    map.put('Y', 24);
    map.put('Z', 25);
    map.put(' ', 26);
    int i = 0;
    for (char c : str.toCharArray()) {
      int val = map.get(c);
      arr[i] = val;
      i++;
    }
    return arr;
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
  public static void printArray(int a[]) {
    for (int s : a) {
      System.out.print(s + " ");
    }
    System.out.println();
  }
  
  /**
   * main 
   *
   * Main method that creates a scanner class in order to
   * receive user input. With this input, the main method
   * calls the str2int and printArray functions.
   */
  public static void main(String[] args) {      
    Scanner input = new Scanner(System.in);
    String aLine = "";
    int ct = 0;
    while (input.hasNext()) {
      aLine = input.nextLine();
      ct++;
      printArray(str2int(aLine));
    }
  }

}