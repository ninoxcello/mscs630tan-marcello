/**
  * file: DriverAES.java
  * author: Antonino Tan-Marcello
  * course: MSCS 630
  * assignment: lab 4
  * due date: March 5, 2017
  * 
  * This file contains a program that receives a length 16 hex String and passes 
  * it through the aesRoundKeys() function within the AESCipher.java file, in order
  * to get the round keys of given input. This file is for testing the AESCipher.java
  * program.
  */

import java.util.Scanner;

/**
  * DriverAES
  * 
  * This class implements the main method to run the program and call aesRoundKeys() function
  * using the given input. The expected results is the round keys of the given input, printed out.
  *
  */
public class DriverAES {

/**
  * main
  * 
  * This main method uses a scanner object to receive user input. This input is then passed through
  * the aesRoundKeys() function in the AESCipher.java file and return an array containing all the round
  * keys. The array is then iterated through, printing each key round.
  */
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
    while (sc.hasNext()) {
      String KeyHex = sc.nextLine();
      String[] a = AESCipher.aesRoundKeys(KeyHex);
        for (String s : a) {
          System.out.print(s + "\n");
        }
        System.out.println();
    }
    sc.close();
  }
  
}
//Test example: 5468617473206D79204B756E67204675

