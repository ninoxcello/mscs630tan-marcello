/**
  * file: DriverAES.java
  * author: Antonino Tan-Marcello
  * course: MSCS 630
  * assignment: lab 5
  * due date: May 9, 2017
  * 
  * This file contains a program that receives a length 16 hex String and a length
  * 16 plaintext String and passes those inputs into the aes encryption algorithm
  * within the AESCipher.java file in order to receive the desired ciphertext.
  * This file is the driver class for the AESCipher.java file.
  */

import java.util.Scanner;

/**
 * DriverAES
 * 
 * This class implements the main method to run the program and call the aes() function
 * using the given input. The expected results is the ciphertext of the given input plaintext.
 *
 */
public class DriverAES {
  
  /**
    * main
    * 
    * This main method uses a scanner object to receive user input. The first input is the desired
    * key of length 16 followed by the desired plaintext, also length 16. This input is then passed 
    * through the aes() function in the AESCipher.java file and returns the ciphertext of the plaintext.
    */
  public static void main(String[] args) {
    Scanner sc = new Scanner(System.in);
      String keyHex = sc.nextLine().toUpperCase();
      String pTextHex = sc.nextLine().toUpperCase();
      System.out.println(AESCipher.aes(pTextHex, keyHex).toUpperCase());
    } 
  }


