/**
  * file: AESCipher.java
  * author: Antonino Tan-Marcello
  * course: MSCS 630
  * assignment: lab 4
  * due date: March 5, 2017
  * 
  * This file contains a program that transforms a hex String of length 16
  * into 11 separate round keys used in AES. The resulting round keys are returned in
  * an array.
  */

/**
  * AESCipher 
  * 
  * This class implements methods, SBox and Round constant matrices, used to transform a hex String
  * of length 16 into 11 round keys, each key containing hexadecimal characters of length 16. The resulting 
  * round keys are then stored into a one dimensional array, each index holding a single round key.
  *
  */
public class AESCipher {
  
/**
  * aesRoundKeys
  * 
  * This function receives a length 16 key, adds it to a 4x4 matrix, and uses it to generate 
  * all the round keys. First, round zero is generated, which is similar to the given key, 
  * next the remaining rounds are generated, with the construction of a temporary vector array
  * , which then shifts, followed by byte transformation of the vector elements by using the SBox 
  * 2D array and XORing the corresponding round constant, using the RCon 2d array. Once all of the 
  * round keys have been created, they are stored into an array and returned by this function.
  *
  * Parameters: 
  *   KeyHex: the 16 length hex string key representation to be converted into round keys.
  * 
  * Return Value:
  *   An array of size 11, containing all round keys.
  */
  public static String[] aesRoundKeys (String KeyHex) {
    int x = 0;
    int y = 0;
    int blockMatrixSize = (int) Math.sqrt(KeyHex.length() / 2);
    String[][] MatrixKey = new String [blockMatrixSize][blockMatrixSize];
      for (int i = 0; i < KeyHex.length(); i +=2) {
        MatrixKey[x][y] = "" + KeyHex.charAt(i) + KeyHex.charAt(i+1);
        x++;
        if (x == 4) {
          x = 0;
          y++;
        }
      }
    String allRndsMatrix[][] = new String[4][11 * 4];
      for (int rndzerorow = 0; rndzerorow < 4; rndzerorow++) {
        System.arraycopy(MatrixKey[rndzerorow], 0, allRndsMatrix[rndzerorow], 0, 4);
      }
      for (int i = 4; i < 44; i++) {
        if (i % 4 != 0) {
          for(int rw = 0; rw < 4 ; rw++) {
            allRndsMatrix[rw][i] = exclusiveOrOp(allRndsMatrix[rw][i - 4], allRndsMatrix[rw][i-1]);            
          }
        } else {
            String[] arrVec = new String[4];
            for(int arrRow = 0; arrRow < 4; arrRow++) {
              arrVec[((arrRow-1) + 4) % 4] = aesSBox(allRndsMatrix[arrRow][i-1]);
            }
            String roundCon = aesRcon(i);
            arrVec[0] = exclusiveOrOp(roundCon, arrVec[0]);
            for(int rw = 0; rw < 4; rw++) {
              allRndsMatrix[rw][i] = exclusiveOrOp(allRndsMatrix[rw][i-4], arrVec[rw]);
            }
        }
      }
      String strArrKey [] = storeInStringArr(allRndsMatrix);
      return strArrKey;
  }
  
/**
  * exclusiveOrOp
  * 
  * This function represents the exclusive or (XOR) operation. More specifically
  * this method takes two hex numbers of length 2, converts them into integers
  * in base 16, and XORs them. The resulting integer is then formatted back into
  * a hex string and returned by the function.
  * 
  * Parameters:
  *   num1: the first hex number used in XOR operation.
  *   num2: the second number used in the XOR operation.
  *
  * Return Value:
  *   The XORed hex value of the input parameters.  
  */
  public static String exclusiveOrOp(String num1, String num2) {
    int val1, val2;
    val1 = Integer.parseInt(num1, 16);
    val2 = Integer.parseInt(num2, 16);
    int xorresult =  val1 ^ val2;
    String hexVal = Integer.toHexString(xorresult);
    return hexVal;
  }
  
/**
  * storeInStringArr
  * 
  * This function converts a 2D array containing the values of the new round keys
  * and adds each key to an index of a new array of size 11.
  * 
  * Parameters:
  *   twoDimKey [][]: the 2D array containing the new values, in which to be transfered.
  *   
  * Return Value:
  *   The array containing all the round keys.
  * 
  */
  public static String[] storeInStringArr (String[][] twoDimKey) {
    String aesRndKeys = "";
    String[] oneDimKey = new String [11];
    int b = 0;    
      for (int mat = 0; mat < 11; mat++) {
        for(int x1 = (mat*4); x1 <(mat*4) + 4; x1++) {
          for(int y1 = 0; y1 < 4; y1++) {
            String formatNum = twoDimKey[y1][x1].toUpperCase();
              if (formatNum.length() == 1)
                formatNum = "0" + formatNum;
            aesRndKeys += formatNum;
          }
        }
        oneDimKey[mat] = aesRndKeys.substring(b);   
        b=b+32;
      }
    return oneDimKey;
  }
  
/**
  * aesSBox 
  * 
  * This method to refers to the SBox table using the given hex value whose row index
  * corresponds to the first digit of the input and column index corresponds to the 
  * second index. The method then returns the selected SBox value.
  * 
  * Parameters:
  *   inHex: the hex String used in the SBox table lookup.
  *   
  * Return value:
  *   The SBox value of the given input.
  * 
  */
  public static String aesSBox (String inHex) {
    int i, j;
    String conversion = String.format("%2s", inHex).replace(' ', '0');
    i = Integer.parseInt(""+conversion.charAt(0), 16);
    j = Integer.parseInt(""+conversion.charAt(1), 16);
    return SBox[i][j];
  }

/**
  * SBox
  * 
  * This table is a 2D array representation of an SBox chart. Original table has
  * been retrieved from http://snipplr.com/view/67929/ and modified to a 2D String array
  * for this program.
  */
  public static String[][] SBox = {
    {"63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76"},
    {"CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0"},
    {"B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15"},
    {"04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75"},
    {"09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84"},
    {"53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF"},
    {"D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8"},
    {"51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2"},
    {"CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73"},
    {"60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB"},
    {"E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79"},
    {"E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08"},
    {"BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A"},
    {"70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E"},
    {"E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF"},
    {"8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16"} 
  };

/**
  * aesRcon
  * 
  * This method retrieves the Round Constant value by referring to the RCon chart, 
  * which lookup value is given by a parameter that represents the current round being generated.
  * 
  * Parameters:
  *   round: the round being generated that will be looked up within the RCon table. 
  *   
  * Return value:
  *   The RCon value, depending on the round value.
  */
  public static String aesRcon(int round) {
    return RCon[0][round/4];
  }
  
/**
  * RCon
  * 
  * This table is a 2D array representation of a Round Constant chart. Original table has
  * been retrieved from https://en.wikipedia.org/wiki/Rijndael_key_schedule and modified to 
  * a 2D String array for this program.
  */
  public static String[][] RCon = {
    {"8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A"},
    {"2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39"},
    {"72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94", "33", "66", "CC", "83", "1D", "3A"},
    {"74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B", "36", "6C", "D8"},
    {"AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF"},
    {"C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94", "33", "66", "CC"},
    {"83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B"},
    {"36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3"},
    {"7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94"},
    {"33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20"},
    {"40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35"},
    {"6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F"},
    {"25", "4A", "94", "33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04"},
    {"08", "10", "20", "40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63"},
    {"C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD"},
    {"61", "C2", "9F", "25", "4A", "94", "33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D"} 
  };

}