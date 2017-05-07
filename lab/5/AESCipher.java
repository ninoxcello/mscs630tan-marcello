/**
  * file: AESCipher.java
  * author: Antonino Tan-Marcello
  * course: MSCS 630
  * assignment: lab 5
  * due date: May 9, 2017
  * 
  * This file contains functions that transforms a hex String of length 16
  * into 11 separate round keys used in AES. The resulting round keys are returned in
  * an array. The program also contains an AES encryption algorithm, which will convert
  * a given plaintext into a ciphertext.
  */

public class AESCipher {

  /**
   * aesRoundKeys
   * 
   * This function receives a length 16 key, adds it to a 4x4 matrix, and uses
   * it to generate all the round keys. First, round zero is generated, which
   * is similar to the given key, next the remaining rounds are generated,
   * with the construction of a temporary vector array , which then shifts,
   * followed by byte transformation of the vector elements by using the SBox
   * 2D array and XORing the corresponding round constant, using the RCon 2d
   * array. Once all of the round keys have been created, they are stored into
   * an array and returned by this function.
   *
   * Parameters: 
   * KeyHex: the 16 length hex string key representation to be converted into round keys.
   * 
   * Return Value: An array of size 11, containing all round keys.
   */
  public static String[] aesRoundKeys(String KeyHex) {
    int x = 0;
    int y = 0;
    int blockMatrixSize = (int) Math.sqrt(KeyHex.length() / 2);
    String[][] MatrixKey = new String[blockMatrixSize][blockMatrixSize];
    for (int i = 0; i < KeyHex.length(); i += 2) {
      MatrixKey[x][y] = "" + KeyHex.charAt(i) + KeyHex.charAt(i + 1);
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
        for (int rw = 0; rw < 4; rw++) {
          allRndsMatrix[rw][i] = exclusiveOrOp(allRndsMatrix[rw][i - 4], allRndsMatrix[rw][i - 1]);
        }
      } else {
        String[] arrVec = new String[4];
        for (int arrRow = 0; arrRow < 4; arrRow++) {
          arrVec[((arrRow - 1) + 4) % 4] = aesSBox(allRndsMatrix[arrRow][i - 1]);
        }
        String roundCon = aesRcon(i);
        arrVec[0] = exclusiveOrOp(roundCon, arrVec[0]);
        for (int rw = 0; rw < 4; rw++) {
          allRndsMatrix[rw][i] = exclusiveOrOp(allRndsMatrix[rw][i - 4], arrVec[rw]);
        }
      }
    }
    String strArrKey[] = storeInStringArr(allRndsMatrix);
    return strArrKey;
  }

  /**
   * exclusiveOrOp
   * 
   * This function represents the exclusive or (XOR) operation. More
   * specifically this method takes two hex numbers of length 2, converts them
   * into integers in base 16, and XORs them. The resulting integer is then
   * formatted back into a hex string and returned by the function.
   * 
   * Parameters: 
   *   num1: the first hex number used in XOR operation. 
   *   num2: the second number used in the XOR operation.
   *
   * Return Value: The XORed hex value of the input parameters.
   */
  public static String exclusiveOrOp(String num1, String num2) {
    int val1, val2;
    val1 = Integer.parseInt(num1, 16);
    val2 = Integer.parseInt(num2, 16);
    int xorresult = val1 ^ val2;
    String hexVal = Integer.toHexString(xorresult);
    return hexVal;
  }

  /**
   * storeInStringArr
   * 
   * This function converts a 2D array containing the values of the new round
   * keys and adds each key to an index of a new array of size 11.
   * 
   * Parameters: 
   *   twoDimKey [][]: the 2D array containing the new values, in which to be transfered.
   * 
   * Return Value: The array containing all the round keys.
   * 
   */
  public static String[] storeInStringArr(String[][] twoDimKey) {
    String aesRndKeys = "";
    String[] oneDimKey = new String[11];
    int b = 0;
    for (int mat = 0; mat < 11; mat++) {
      for (int x1 = (mat * 4); x1 < (mat * 4) + 4; x1++) {
        for (int y1 = 0; y1 < 4; y1++) {
          String formatNum = twoDimKey[y1][x1].toUpperCase();
          if (formatNum.length() == 1)
            formatNum = "0" + formatNum;
          aesRndKeys += formatNum;
        }
      }
      oneDimKey[mat] = aesRndKeys.substring(b);
      b = b + 32;
    }
    return oneDimKey;
  }

  /**
   * aesSBox
   * 
   * This method to refers to the SBox table using the given hex value whose
   * row index corresponds to the first digit of the input and column index
   * corresponds to the second index. The method then returns the selected
   * SBox value.
   * 
   * Parameters: 
   *   inHex: the hex String used in the SBox table lookup.
   * 
   * Return value: The SBox value of the given input.
   * 
   */
  public static String aesSBox(String inHex) {
    int i, j;
    String conversion = String.format("%2s", inHex).replace(' ', '0');
    i = Integer.parseInt("" + conversion.charAt(0), 16);
    j = Integer.parseInt("" + conversion.charAt(1), 16);
    return SBox[i][j];
  }

  /**
   * SBox
   * 
   * This table is a 2D array representation of an SBox chart. Original table
   * has been retrieved from http://snipplr.com/view/67929/ and modified to a
   * 2D String array for this program.
   */
  public static String[][] SBox = {
      { "63", "7C", "77", "7B", "F2", "6B", "6F", "C5", "30", "01", "67", "2B", "FE", "D7", "AB", "76" },
      { "CA", "82", "C9", "7D", "FA", "59", "47", "F0", "AD", "D4", "A2", "AF", "9C", "A4", "72", "C0" },
      { "B7", "FD", "93", "26", "36", "3F", "F7", "CC", "34", "A5", "E5", "F1", "71", "D8", "31", "15" },
      { "04", "C7", "23", "C3", "18", "96", "05", "9A", "07", "12", "80", "E2", "EB", "27", "B2", "75" },
      { "09", "83", "2C", "1A", "1B", "6E", "5A", "A0", "52", "3B", "D6", "B3", "29", "E3", "2F", "84" },
      { "53", "D1", "00", "ED", "20", "FC", "B1", "5B", "6A", "CB", "BE", "39", "4A", "4C", "58", "CF" },
      { "D0", "EF", "AA", "FB", "43", "4D", "33", "85", "45", "F9", "02", "7F", "50", "3C", "9F", "A8" },
      { "51", "A3", "40", "8F", "92", "9D", "38", "F5", "BC", "B6", "DA", "21", "10", "FF", "F3", "D2" },
      { "CD", "0C", "13", "EC", "5F", "97", "44", "17", "C4", "A7", "7E", "3D", "64", "5D", "19", "73" },
      { "60", "81", "4F", "DC", "22", "2A", "90", "88", "46", "EE", "B8", "14", "DE", "5E", "0B", "DB" },
      { "E0", "32", "3A", "0A", "49", "06", "24", "5C", "C2", "D3", "AC", "62", "91", "95", "E4", "79" },
      { "E7", "C8", "37", "6D", "8D", "D5", "4E", "A9", "6C", "56", "F4", "EA", "65", "7A", "AE", "08" },
      { "BA", "78", "25", "2E", "1C", "A6", "B4", "C6", "E8", "DD", "74", "1F", "4B", "BD", "8B", "8A" },
      { "70", "3E", "B5", "66", "48", "03", "F6", "0E", "61", "35", "57", "B9", "86", "C1", "1D", "9E" },
      { "E1", "F8", "98", "11", "69", "D9", "8E", "94", "9B", "1E", "87", "E9", "CE", "55", "28", "DF" },
      { "8C", "A1", "89", "0D", "BF", "E6", "42", "68", "41", "99", "2D", "0F", "B0", "54", "BB", "16" } };

  /**
   * aesRcon
   * 
   * This method retrieves the Round Constant value by referring to the RCon
   * chart, which lookup value is given by a parameter that represents the
   * current round being generated.
   * 
   * Parameters: 
   *   round: the round being generated that will be looked up within the RCon table.
   * 
   * Return value: The RCon value, depending on the round value.
   */
  public static String aesRcon(int round) {
    return RCon[0][round / 4];
  }

  /**
   * RCon
   * 
   * This table is a 2D array representation of a Round Constant chart.
   * Original table has been retrieved from
   * https://en.wikipedia.org/wiki/Rijndael_key_schedule and modified to a 2D
   * String array for this program.
   */
  public static String[][] RCon = {
      { "8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A" },
      { "2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39" },
      { "72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94", "33", "66", "CC", "83", "1D", "3A" },
      { "74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B", "36", "6C", "D8" },
      { "AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF" },
      { "C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94", "33", "66", "CC" },
      { "83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20", "40", "80", "1B" },
      { "36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35", "6A", "D4", "B3" },
      { "7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F", "25", "4A", "94" },
      { "33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04", "08", "10", "20" },
      { "40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63", "C6", "97", "35" },
      { "6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD", "61", "C2", "9F" },
      { "25", "4A", "94", "33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D", "01", "02", "04" },
      { "08", "10", "20", "40", "80", "1B", "36", "6C", "D8", "AB", "4D", "9A", "2F", "5E", "BC", "63" },
      { "C6", "97", "35", "6A", "D4", "B3", "7D", "FA", "EF", "C5", "91", "39", "72", "E4", "D3", "BD" },
      { "61", "C2", "9F", "25", "4A", "94", "33", "66", "CC", "83", "1D", "3A", "74", "E8", "CB", "8D" } };

  /**
   * rearrange
   * 
   * This method receives a 2D matrix, interchanges its values and stores
   * those values in a new array.
   * 
   * Parameters: 
   *   inputMatrix: the matrix that needs its values relocated
   * 
   * Return value: the newly rearranged matrix
   * 
   */
  private static String[][] rearrange(String[][] inputMatrix) {
    String[][] newMatrix = new String[4][4];
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        newMatrix[x][y] = inputMatrix[y][x];
      }
    }
    return newMatrix;
  }

  /**
   * AESStateXOR
   *
   * This method receives an input String 4x4 matrix and an input hex key 4x4
   * matrix and XOR's their corresponding values.
   * 
   * Parameters: 
   *   sHex: the input string that in a 4x4 matrix format 
   *   keyHex: the input hex key in a 4x4 matrix format
   * 
   * Return value: a 4x4 matrix of the XORed results of the 4x4 input
   *   matrices' corresponding values.
   */
  public static String[][] AESStateXOR(String[][] sHex, String[][] keyHex) {
    String[][] xoredMatrix = new String[4][4];
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        xoredMatrix[y][x] = exclusiveOrOp(sHex[y][x], keyHex[x][y]);
      }
    }
    return xoredMatrix;
  }

  /**
   * AESNibbleSub
   * 
   * This function receives an input matrix containing hex values, runs the matrix's values through
   * the AES Sbox to substitute the values.
   * 
   * Parameters:
   *   inStateHex: the matrix who values are to be substituted within the Sbox table.
   * 
   * Return Value: a matrix after Sbox substitution of the input matrix
   */
  public static String[][] AESNibbleSub(String[][] inStateHex) {
    String[][] sboxedMatrix = new String[4][4];
    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        sboxedMatrix[x][y] = aesSBox(inStateHex[x][y]);
      }
    }
    return sboxedMatrix;
  }

    /**
     * AESShiftRow
     * 
     * This method receives an input matrix and performs a row shifting operation.
     * 
     * Parameters:
     *   inStateHex: the input matrix to have its rows shifted
     *   
     * Return value: a new matrix after successfully performing a row shift operation.
     */
  public static String[][] AESShiftRow(String[][] inStateHex) {
    String[][] rearrangedMatrix = rearrange(inStateHex);
    String[][] shiftedMatrix = new String[4][4];
    shiftedMatrix[0] = rearrangedMatrix[0];
    for (int x = 1; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        shiftedMatrix[x][y] = rearrangedMatrix[x][(y + x) % 4];
      }
    }
    return rearrange(shiftedMatrix);
  }

    /**
     * aesMixColumn
     * 
     * This method take a 4x4 matrix input and performs the mix column operation using the mix column
     * lookup tables. This method then returns a new 4x4 matrix following the the completion of the operation.
     * 
     * Parameters:
     *   inStateHex: the 4x4 matrix in which to perform the mix column operation on
     *   
     * Return value: a new 4x4 matrix after successfully completing the mix column operation
     */
  public static String[][] aesMixColumn(String[][] inStateHex) {
    String[][] mixcolMatrix = new String[4][4];
    int[] arr1 = new int[4];
    int[] arr2 = new int[4];

    for (int x = 0; x < 4; x++) {
      for (int y = 0; y < 4; y++) {
        arr1[y] = Integer.parseInt(inStateHex[x][y], 16);
      }

      arr2[0] = mixCol2[arr1[0]] ^ mixCol3[arr1[1]] ^ arr1[2] ^ arr1[3];
      arr2[1] = arr1[0] ^ mixCol2[arr1[1]] ^ mixCol3[arr1[2]] ^ arr1[3];
      arr2[2] = arr1[0] ^ arr1[1] ^ mixCol2[arr1[2]] ^ mixCol3[arr1[3]];
      arr2[3] = mixCol3[arr1[0]] ^ arr1[1] ^ arr1[2] ^ mixCol2[arr1[3]];

      for (int z = 0; z < 4; z++) {
        mixcolMatrix[x][z] = Integer.toHexString(arr2[z]);
      }
    }
    return mixcolMatrix;
  }

  /**
   * aes
   * 
   * This method implements the AES encryptryption algorithm. This method receives a hex value key and
   * a plaintext string to be converted. The plain text is converted to a ciphertext and returned.
   * 
   * Parameters:
   *   pTextHex: the plaintext string to be converted to a ciphertext
   *   keyHex: the hex value key used as part of the encryptryption
   *   
   * Return value: the ciphertext string
   */
  public static String aes(String pTextHex, String keyHex) {
    int squareMatrixDims = (int) Math.sqrt(pTextHex.length() / 2);
    String[][] pTextHexMat = rearrange(keyToMatrix(pTextHex, squareMatrixDims));
    String[][] wMatrix = makeRndKeyMatrix(keyHex, squareMatrixDims);
    String[][] encrypt = new String[4][4];
    String[][] roundKey = (keyToMatrix(keyHex, squareMatrixDims));
    encrypt = AESStateXOR(pTextHexMat, roundKey);
    for (int x = 1; x < 11; x++) {
      for (int y = 0; y < 4; y++) {
        for (int z = 0; z < 4; z++) {
          roundKey[z][y] = wMatrix[z][(x * 4) + y];
        }
      }
      encrypt = AESNibbleSub(encrypt);
      encrypt = AESShiftRow(encrypt);
      if (x < 10)
        encrypt = aesMixColumn(encrypt);
      encrypt = AESStateXOR(encrypt, roundKey);
    }
    StringBuilder cipherString = new StringBuilder();
    for (String[] x : encrypt) {
      for (String y : x) {
        if (y.length() == 1)
          y = "0" + y;
        cipherString.append(y);
      }
    }
    return cipherString.toString();
  }

  /**
   * makeRndKeyMatrix
   * 
   * This method takes the input key and the dimensions for the key matrix to
   * be created and converts the original key into 11 round keys, and store
   * those keys in a two dimensional array.
   * 
   * Parameters: 
   *   KeyHex: the length 16 hex representation of the key
   *   blockMatrixSize: size of the matrix for input key
   * 
   * Return value: all resulting round keys within a 2D String array
   */
  private static String[][] makeRndKeyMatrix(String KeyHex, int blockMatrixSize) {
    int x = 0;
    int y = 0;
    String[][] MatrixKey = new String[blockMatrixSize][blockMatrixSize];
    for (int i = 0; i < KeyHex.length(); i += 2) {
      MatrixKey[x][y] = "" + KeyHex.charAt(i) + KeyHex.charAt(i + 1);
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
        for (int rw = 0; rw < 4; rw++) {
          allRndsMatrix[rw][i] = exclusiveOrOp(allRndsMatrix[rw][i - 4], allRndsMatrix[rw][i - 1]);
        }
      } else {
        String[] arrVec = new String[4];
        for (int arrRow = 0; arrRow < 4; arrRow++) {
          arrVec[((arrRow - 1) + 4) % 4] = aesSBox(allRndsMatrix[arrRow][i - 1]);
        }
        String roundCon = aesRcon(i);
        arrVec[0] = exclusiveOrOp(roundCon, arrVec[0]);
        for (int rw = 0; rw < 4; rw++) {
          allRndsMatrix[rw][i] = exclusiveOrOp(allRndsMatrix[rw][i - 4], arrVec[rw]);
        }
      }
    }
    return allRndsMatrix;
  }

  /**
   * keyToMatrix
   * 
   * This method receives the input key and transforms it into a 2D array.
   * 
   * Parameters: 
   *   KeyHex: the 16 length hex string key representation
   *   blockMatrixSize: size of the new matrix for the input key
   * 
   * Return value: 2D array representation of the key
   */
  private static String[][] keyToMatrix(String KeyHex, int blockMatrixSize) {
    int x = 0;
    int y = 0;
    String[][] MatrixKey = new String[blockMatrixSize][blockMatrixSize];
    for (int i = 0; i < KeyHex.length(); i += 2) {
      MatrixKey[x][y] = "" + KeyHex.charAt(i) + KeyHex.charAt(i + 1);
      x++;
      if (x == 4) {
        x = 0;
        y++;
      }
    }
    return MatrixKey;
  }

  /**
   * mixCol2
   * 
   * This array is a mix column table used in the mix column operation for AES encryption.
   */
  public static final int[] mixCol2 = { 0x00, 0x02, 0x04, 0x06, 0x08, 0x0a, 0x0c, 0x0e, 0x10, 0x12, 0x14, 0x16, 0x18,
      0x1a, 0x1c, 0x1e, 0x20, 0x22, 0x24, 0x26, 0x28, 0x2a, 0x2c, 0x2e, 0x30, 0x32, 0x34, 0x36, 0x38, 0x3a, 0x3c,
      0x3e, 0x40, 0x42, 0x44, 0x46, 0x48, 0x4a, 0x4c, 0x4e, 0x50, 0x52, 0x54, 0x56, 0x58, 0x5a, 0x5c, 0x5e, 0x60,
      0x62, 0x64, 0x66, 0x68, 0x6a, 0x6c, 0x6e, 0x70, 0x72, 0x74, 0x76, 0x78, 0x7a, 0x7c, 0x7e, 0x80, 0x82, 0x84,
      0x86, 0x88, 0x8a, 0x8c, 0x8e, 0x90, 0x92, 0x94, 0x96, 0x98, 0x9a, 0x9c, 0x9e, 0xa0, 0xa2, 0xa4, 0xa6, 0xa8,
      0xaa, 0xac, 0xae, 0xb0, 0xb2, 0xb4, 0xb6, 0xb8, 0xba, 0xbc, 0xbe, 0xc0, 0xc2, 0xc4, 0xc6, 0xc8, 0xca, 0xcc,
      0xce, 0xd0, 0xd2, 0xd4, 0xd6, 0xd8, 0xda, 0xdc, 0xde, 0xe0, 0xe2, 0xe4, 0xe6, 0xe8, 0xea, 0xec, 0xee, 0xf0,
      0xf2, 0xf4, 0xf6, 0xf8, 0xfa, 0xfc, 0xfe, 0x1b, 0x19, 0x1f, 0x1d, 0x13, 0x11, 0x17, 0x15, 0x0b, 0x09, 0x0f,
      0x0d, 0x03, 0x01, 0x07, 0x05, 0x3b, 0x39, 0x3f, 0x3d, 0x33, 0x31, 0x37, 0x35, 0x2b, 0x29, 0x2f, 0x2d, 0x23,
      0x21, 0x27, 0x25, 0x5b, 0x59, 0x5f, 0x5d, 0x53, 0x51, 0x57, 0x55, 0x4b, 0x49, 0x4f, 0x4d, 0x43, 0x41, 0x47,
      0x45, 0x7b, 0x79, 0x7f, 0x7d, 0x73, 0x71, 0x77, 0x75, 0x6b, 0x69, 0x6f, 0x6d, 0x63, 0x61, 0x67, 0x65, 0x9b,
      0x99, 0x9f, 0x9d, 0x93, 0x91, 0x97, 0x95, 0x8b, 0x89, 0x8f, 0x8d, 0x83, 0x81, 0x87, 0x85, 0xbb, 0xb9, 0xbf,
      0xbd, 0xb3, 0xb1, 0xb7, 0xb5, 0xab, 0xa9, 0xaf, 0xad, 0xa3, 0xa1, 0xa7, 0xa5, 0xdb, 0xd9, 0xdf, 0xdd, 0xd3,
      0xd1, 0xd7, 0xd5, 0xcb, 0xc9, 0xcf, 0xcd, 0xc3, 0xc1, 0xc7, 0xc5, 0xfb, 0xf9, 0xff, 0xfd, 0xf3, 0xf1, 0xf7,
      0xf5, 0xeb, 0xe9, 0xef, 0xed, 0xe3, 0xe1, 0xe7, 0xe5 };

  /**
   * mixCol3
   * 
   * This array is a mix column table used in the mix column operation for AES encryption.
   */
  public static final int[] mixCol3 = { 0x00, 0x03, 0x06, 0x05, 0x0c, 0x0f, 0x0a, 0x09, 0x18, 0x1b, 0x1e, 0x1d, 0x14,
      0x17, 0x12, 0x11, 0x30, 0x33, 0x36, 0x35, 0x3c, 0x3f, 0x3a, 0x39, 0x28, 0x2b, 0x2e, 0x2d, 0x24, 0x27, 0x22,
      0x21, 0x60, 0x63, 0x66, 0x65, 0x6c, 0x6f, 0x6a, 0x69, 0x78, 0x7b, 0x7e, 0x7d, 0x74, 0x77, 0x72, 0x71, 0x50,
      0x53, 0x56, 0x55, 0x5c, 0x5f, 0x5a, 0x59, 0x48, 0x4b, 0x4e, 0x4d, 0x44, 0x47, 0x42, 0x41, 0xc0, 0xc3, 0xc6,
      0xc5, 0xcc, 0xcf, 0xca, 0xc9, 0xd8, 0xdb, 0xde, 0xdd, 0xd4, 0xd7, 0xd2, 0xd1, 0xf0, 0xf3, 0xf6, 0xf5, 0xfc,
      0xff, 0xfa, 0xf9, 0xe8, 0xeb, 0xee, 0xed, 0xe4, 0xe7, 0xe2, 0xe1, 0xa0, 0xa3, 0xa6, 0xa5, 0xac, 0xaf, 0xaa,
      0xa9, 0xb8, 0xbb, 0xbe, 0xbd, 0xb4, 0xb7, 0xb2, 0xb1, 0x90, 0x93, 0x96, 0x95, 0x9c, 0x9f, 0x9a, 0x99, 0x88,
      0x8b, 0x8e, 0x8d, 0x84, 0x87, 0x82, 0x81, 0x9b, 0x98, 0x9d, 0x9e, 0x97, 0x94, 0x91, 0x92, 0x83, 0x80, 0x85,
      0x86, 0x8f, 0x8c, 0x89, 0x8a, 0xab, 0xa8, 0xad, 0xae, 0xa7, 0xa4, 0xa1, 0xa2, 0xb3, 0xb0, 0xb5, 0xb6, 0xbf,
      0xbc, 0xb9, 0xba, 0xfb, 0xf8, 0xfd, 0xfe, 0xf7, 0xf4, 0xf1, 0xf2, 0xe3, 0xe0, 0xe5, 0xe6, 0xef, 0xec, 0xe9,
      0xea, 0xcb, 0xc8, 0xcd, 0xce, 0xc7, 0xc4, 0xc1, 0xc2, 0xd3, 0xd0, 0xd5, 0xd6, 0xdf, 0xdc, 0xd9, 0xda, 0x5b,
      0x58, 0x5d, 0x5e, 0x57, 0x54, 0x51, 0x52, 0x43, 0x40, 0x45, 0x46, 0x4f, 0x4c, 0x49, 0x4a, 0x6b, 0x68, 0x6d,
      0x6e, 0x67, 0x64, 0x61, 0x62, 0x73, 0x70, 0x75, 0x76, 0x7f, 0x7c, 0x79, 0x7a, 0x3b, 0x38, 0x3d, 0x3e, 0x37,
      0x34, 0x31, 0x32, 0x23, 0x20, 0x25, 0x26, 0x2f, 0x2c, 0x29, 0x2a, 0x0b, 0x08, 0x0d, 0x0e, 0x07, 0x04, 0x01,
      0x02, 0x13, 0x10, 0x15, 0x16, 0x1f, 0x1c, 0x19, 0x1a };

}
