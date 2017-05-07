/**
  * file: AES.java
  * author: Antonino Tan-Marcello
  * course: MSCS 630
  * assignment: Project
  * due date: March 9, 2017
  * 
  * This file contains the main AES system to run the driver
  * jPanel, FileCrypt.java.
  */

package Crypt;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;

/**
 * AES
 * 
 * This class implements the core AES code. It contains all algorithms required
 * to successfully complete encryption and decryption procedures.
 */
public class AES {

    /**
     * Mode version declaration.
     */
    public static enum Mode {
        ECB, CBC
    };

    /**
     * 2D Array representation of an S-BOX table.
     */
    public static final int[][] sbox = {
        {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76},
        {0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0},
        {0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15},
        {0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75},
        {0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84},
        {0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf},
        {0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8},
        {0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2},
        {0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73},
        {0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb},
        {0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79},
        {0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08},
        {0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a},
        {0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e},
        {0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf},
        {0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16}};
    
    /**
     * 2D Array representation of an inverse S-BOX table.
     */
    public static final int[][] invsbox = {
        {0x52, 0x09, 0x6a, 0xd5, 0x30, 0x36, 0xa5, 0x38, 0xbf, 0x40, 0xa3, 0x9e, 0x81, 0xf3, 0xd7, 0xfb},
        {0x7c, 0xe3, 0x39, 0x82, 0x9b, 0x2f, 0xff, 0x87, 0x34, 0x8e, 0x43, 0x44, 0xc4, 0xde, 0xe9, 0xcb},
        {0x54, 0x7b, 0x94, 0x32, 0xa6, 0xc2, 0x23, 0x3d, 0xee, 0x4c, 0x95, 0x0b, 0x42, 0xfa, 0xc3, 0x4e},
        {0x08, 0x2e, 0xa1, 0x66, 0x28, 0xd9, 0x24, 0xb2, 0x76, 0x5b, 0xa2, 0x49, 0x6d, 0x8b, 0xd1, 0x25},
        {0x72, 0xf8, 0xf6, 0x64, 0x86, 0x68, 0x98, 0x16, 0xd4, 0xa4, 0x5c, 0xcc, 0x5d, 0x65, 0xb6, 0x92},
        {0x6c, 0x70, 0x48, 0x50, 0xfd, 0xed, 0xb9, 0xda, 0x5e, 0x15, 0x46, 0x57, 0xa7, 0x8d, 0x9d, 0x84},
        {0x90, 0xd8, 0xab, 0x00, 0x8c, 0xbc, 0xd3, 0x0a, 0xf7, 0xe4, 0x58, 0x05, 0xb8, 0xb3, 0x45, 0x06},
        {0xd0, 0x2c, 0x1e, 0x8f, 0xca, 0x3f, 0x0f, 0x02, 0xc1, 0xaf, 0xbd, 0x03, 0x01, 0x13, 0x8a, 0x6b},
        {0x3a, 0x91, 0x11, 0x41, 0x4f, 0x67, 0xdc, 0xea, 0x97, 0xf2, 0xcf, 0xce, 0xf0, 0xb4, 0xe6, 0x73},
        {0x96, 0xac, 0x74, 0x22, 0xe7, 0xad, 0x35, 0x85, 0xe2, 0xf9, 0x37, 0xe8, 0x1c, 0x75, 0xdf, 0x6e},
        {0x47, 0xf1, 0x1a, 0x71, 0x1d, 0x29, 0xc5, 0x89, 0x6f, 0xb7, 0x62, 0x0e, 0xaa, 0x18, 0xbe, 0x1b},
        {0xfc, 0x56, 0x3e, 0x4b, 0xc6, 0xd2, 0x79, 0x20, 0x9a, 0xdb, 0xc0, 0xfe, 0x78, 0xcd, 0x5a, 0xf4},
        {0x1f, 0xdd, 0xa8, 0x33, 0x88, 0x07, 0xc7, 0x31, 0xb1, 0x12, 0x10, 0x59, 0x27, 0x80, 0xec, 0x5f},
        {0x60, 0x51, 0x7f, 0xa9, 0x19, 0xb5, 0x4a, 0x0d, 0x2d, 0xe5, 0x7a, 0x9f, 0x93, 0xc9, 0x9c, 0xef},
        {0xa0, 0xe0, 0x3b, 0x4d, 0xae, 0x2a, 0xf5, 0xb0, 0xc8, 0xeb, 0xbb, 0x3c, 0x83, 0x53, 0x99, 0x61},
        {0x17, 0x2b, 0x04, 0x7e, 0xba, 0x77, 0xd6, 0x26, 0xe1, 0x69, 0x14, 0x63, 0x55, 0x21, 0x0c, 0x7d}};

    /**
     * Galois table used for the mixColumns function.
     */
    public static final int[][] galois = {
        {0x02, 0x03, 0x01, 0x01},
        {0x01, 0x02, 0x03, 0x01},
        {0x01, 0x01, 0x02, 0x03},
        {0x03, 0x01, 0x01, 0x02}};

    /**
     * Inverse Galois table used for invMixColumns function.
     */
    public static final int[][] invgalois = {
        {0x0e, 0x0b, 0x0d, 0x09},
        {0x09, 0x0e, 0x0b, 0x0d},
        {0x0d, 0x09, 0x0e, 0x0b},
        {0x0b, 0x0d, 0x09, 0x0e}};

    /**
     * RCon array used for Key Expansion.
     */
    public static final int[] rcon = {0x8d, 0x01, 0x02, 0x04, 0x08, 0x10,
        0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f,
        0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa,
        0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f,
        0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8,
        0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b,
        0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6,
        0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39,
        0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33,
        0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02,
        0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab,
        0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4,
        0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd,
        0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d,
        0x3a, 0x74, 0xe8, 0xcb, 0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20,
        0x40, 0x80, 0x1b, 0x36, 0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e,
        0xbc, 0x63, 0xc6, 0x97, 0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef,
        0xc5, 0x91, 0x39, 0x72, 0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25,
        0x4a, 0x94, 0x33, 0x66, 0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb,
        0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36,
        0x6c, 0xd8, 0xab, 0x4d, 0x9a, 0x2f, 0x5e, 0xbc, 0x63, 0xc6, 0x97,
        0x35, 0x6a, 0xd4, 0xb3, 0x7d, 0xfa, 0xef, 0xc5, 0x91, 0x39, 0x72,
        0xe4, 0xd3, 0xbd, 0x61, 0xc2, 0x9f, 0x25, 0x4a, 0x94, 0x33, 0x66,
        0xcc, 0x83, 0x1d, 0x3a, 0x74, 0xe8, 0xcb};

    static String key = "";
    static Mode mode;

    /**
     * Empty AES constructor.
     */
    public AES() {
    }
    
    /**
     * AESDecrypt
     * 
     * This method contains the decryption algorithm of AES, it receives the 
     * ciphertext string to be converted to a plaintext as well as the correct key.
     * 
     * Parameters:
     *   line: the plaintext string
     *   key: the key to be used for round key expansion and decryption
     * 
     * Return value: the plaintext string
     */
    public static String AESDecrypt(String line, String key) {
        int numRounds = 10 + (((key.length() * 4 - 128) / 32));
        int[][] state = new int[4][4];
        int[][] initvector = new int[4][4];
        int[][] nextvector = new int[4][4];
        int[][] keymatrix = keySchedule(key);
        if (mode == Mode.CBC) {		
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    initvector[j][i] = Integer.parseInt(key.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
                }
            }
        }
        String holder = "";
        String block = "";
        int counter = 0;
        int size = line.length() / 32;
        for (int h = 0; h < size; h++) {
            block = line.substring(counter, counter + 32);
            counter = counter + 32;
            System.out.println("block size " + block.length());
            state = new int[4][4];
            for (int i = 0; i < state.length; i++) {
                for (int j = 0; j < state[0].length; j++) {
                    state[j][i] = Integer.parseInt(block.substring(
                            (8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
                }
            }
            if (mode == Mode.CBC) {
                matrixDeepCopy(nextvector, state);
            }
            addRoundKey(state, roundKey(keymatrix, numRounds));
            for (int i = numRounds - 1; i > 0; i--) {
                invRowShift(state);
                invSubBytesSbox(state);
                addRoundKey(state, roundKey(keymatrix, i));
                invMixColumns(state);
            }
            invRowShift(state);
            invSubBytesSbox(state);
            addRoundKey(state, roundKey(keymatrix, 0));
            if (mode == Mode.CBC) {
                addRoundKey(state, initvector);
                matrixDeepCopy(initvector, nextvector);
            }
            String temp = MatrixToString(state);

            try {
                byte[] bytes = Hex.decodeHex(temp.toCharArray());
                temp = new String(bytes, "UTF-8").replaceAll("[^\\x20-\\x7e]",
                        "");
                System.out.println("decoded "
                        + temp);
            } catch (DecoderException | UnsupportedEncodingException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            holder = holder + temp;
        }
        System.out.println("final " + holder);
        return holder.trim();
    }

    /**
     * AESEncypt
     * 
     * This method contains the encryption algorithm of AES, it receives the 
     * plaintext string in which to be converted to a ciphertext as well as the 
     * desired key.
     * 
     * Parameters:
     *   line: the plaintext string
     *   key: the key to be used for round key expansion and encryption
     * 
     * Return value: the ciphertext string
     */
    public static String AESEncrypt(String line, String key) {
        String temp = "";
        int numRounds = 10 + (((key.length() * 4 - 128) / 32));
        int[][] state, initvector = new int[4][4];
        int[][] keymatrix = keySchedule(key);
        if (mode == Mode.CBC) {
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    initvector[j][i] = Integer.parseInt(key.substring((8 * i) + (2 * j), (8 * i)+ (2 * j + 2)), 16);
                }
            }
        }
        try {
            line = String.format("%040x",
                    new BigInteger(1, line.getBytes("UTF-8")));
        } catch (UnsupportedEncodingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        int size = line.length() / 32;
        if (line.length() % 32 > 0) {
            size++;
            for (int i = 0; i < line.length() % 32; i++) {
                line = line + "20";
            }
            System.out.println("remaining " + line.length() % 32);
        }
        int k = 0;
        System.out.println("line length " + line.length());
        String holder = "";
        String block = "";
        int counter = 0;
        System.out.println("size " + size);
        for (int h = 0; h < size; h++) {
            block = line.substring(counter, counter + 32);
            counter = counter + 32;
            System.out.println("block size " + block.length());
            state = new int[4][4];
            for (int i = 0; i < 4; i++) {
                for (int j = 0; j < 4; j++) {
                    state[j][i] = Integer.parseInt(block.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
                    System.out.println(k++);
                }
            }
            if (mode == Mode.CBC) {
                addRoundKey(state, initvector);
            }
            addRoundKey(state, roundKey(keymatrix, 0));
            for (int i = 1; i < numRounds; i++) {
                subBytesSbox(state);
                rowShift(state);
                mixColumns(state);
                addRoundKey(state, roundKey(keymatrix, i));
            }
            subBytesSbox(state);
            rowShift(state);
            addRoundKey(state, roundKey(keymatrix, numRounds));
            if (mode == Mode.CBC) {
                initvector = state;
            }
            temp = MatrixToString(state);
            holder = holder + temp;
            System.out.println("enc " + holder + "length " + holder.length());
        }
        return holder;
    }

    /**
     * matrixDeepCopy
     * 
     * This method implements a deep copy of a specific 2D array.
     * 
     * Parameters: 
     *   newArray: the copied array
     *   original: the array to be copied
     * 
     * Return value: a copied matrix
     */
    private static void matrixDeepCopy(int[][] newArray, int[][] original) {
        assert newArray.length == original.length
                && newArray[0].length == original[0].length;
        for (int i = 0; i < newArray.length; i++) {
            System.arraycopy(original[i], 0, newArray[i], 0,
                    newArray[0].length);
        }
    }

    /**
     * roundKey
     * 
     * This function retrieves the roundKey from the key generated from the keySchedule().
     *
     * Parameters:
     *   ksched: resulting key of the key schedule method.
     *   index: index of roundKey location.
     * 
     * Return value: 2 array of scheduled key.
     */
    private static int[][] roundKey(int[][] ksched, int index) {
        int[][] twodimarr = new int[4][4];
        for (int x = 0; x < twodimarr.length; x++) {
            for (int y = 0; y < twodimarr.length; y++) {
                twodimarr[x][y] = ksched[x][4 * index + y];
            }
        }
        return twodimarr;
    }

    /**
     * subBytesSbox
     * 
     * This method substitutes the elements of input array with values in sbox[][] table.
     * Utilizes sbox table.
     *
     * Parameters:
     *   originalArray: array to have elements substituted with sbox values.
     * 
     * Return value: array with newly replaced elements
     */
    public static void subBytesSbox(int[][] originalArray) {
        for (int x = 0; x < originalArray.length; x++) // Sub-Byte subroutine
        {
            for (int y = 0; y < originalArray[0].length; y++) {
                int hex = originalArray[y][x];
                originalArray[y][x] = sbox[hex / 16][hex % 16];
            }
        }
    }

    /**
     * invSubBytesSbox
     * 
     * This method implements the inverse version of the subBytesSbox method. Utilizes
     * the invsbox table.
     *
     * Parameters:
     *   originalArray: array to have elements substituted with invsbox values.
     * 
     * Return value: array with newly replaced elements.
     */
    public static void invSubBytesSbox(int[][] originalArray) {
        for (int i = 0; i < originalArray.length; i++) {
            for (int j = 0; j < originalArray[0].length; j++) {
                int hex = originalArray[j][i];
                originalArray[j][i] = invsbox[hex / 16][hex % 16];
            }
        }
    }

    /**
     * rowShift
     * 
     * This method conducts a left shift on each row of the given matrix.
     *
     * Parameters:
     *   inputArray: the array to have rows shifted.
     * 
     * Return value: array with successfully shifted rows.
     */
    public static void rowShift(int[][] inputArray) {
        for (int x = 1; x < inputArray.length; x++) {
            inputArray[x] = rotateLeft(inputArray[x], x);
        }
    }

    /**
     * leftRotate
     * 
     * This method rotates given array's elements to the left, providing number
     * of desired rotations.
     *
     * Parameters:
     *   inputArray: array to have its elements rotated.
     *   rotations: given number of rotations.
     * 
     * Return value: array with rotated values.
     */
    private static int[] rotateLeft(int[] inputArray, int rotations) {
        assert (inputArray.length == 4);
        if (rotations % 4 == 0) {
            return inputArray;
        }
        while (rotations > 0) {
            int temp = inputArray[0];
            for (int x = 0; x < inputArray.length - 1; x++) {
                inputArray[x] = inputArray[x + 1];
            }
            inputArray[inputArray.length - 1] = temp;
            --rotations;
        }
        return inputArray;
    }

    /**
     * invRowShift
     * 
     * This function holds the inverse version of the rowShift method.
     *
     * Parameters
     *   inputArray: the array to have rows shifted
     * 
     * Return value: array with successfully shifted rows
     */
    public static void invRowShift(int[][] inputArray) {
        for (int i = 1; i < inputArray.length; i++) {
            inputArray[i] = rotateRight(inputArray[i], i);
        }
    }

    /**
     * rotateRight
     * 
     * This method rotates given array's elements to the right, providing number
     * of desired rotations.
     *
     * Parameters:
     *   inputArray: array to have its elements rotated.
     *   rotations: given number of rotations.
     * 
     * Return value: array with rotated values.
     */
    private static int[] rotateRight(int[] inputArray, int rotations) {
        if (inputArray.length == 0 || inputArray.length == 1 || rotations % 4 == 0) {
            return inputArray;
        }
        while (rotations > 0) {
            int temp = inputArray[inputArray.length - 1];
            for (int i = inputArray.length - 1; i > 0; i--) {
                inputArray[i] = inputArray[i - 1];
            }
            inputArray[0] = temp;
            --rotations;
        }
        return inputArray;
    }

    /**
     * mixColumns
     * 
     * This function executes the mix columns operation on given array. 
     *
     * Parameters:
     *   inputArray: the array to calculate towards the galois field matrix.
     * 
     * Return value: the calculated mix columns value in the mixColHelper function.
     */
    public static void mixColumns(int[][] inputArray) {
        int[][] arr = new int[4][4];
        for (int x = 0; x < 4; x++) {
            System.arraycopy(inputArray[x], 0, arr[x], 0, 4);
        }
        for (int y = 0; y < 4; y++) {
            for (int z = 0; z < 4; z++) {
                inputArray[y][z] = mixColHelper(arr, galois, y, z);
            }
        }
    }

    /**
     * mixColHelper
     * 
     * This method helps the mixColumns method execute mix columns operation.
     *
     * Parameters:
     *   inputArray: the created 2D array in the mixColumns method
     *   gal: the given galois field matrix
     *   indexrow: the row index
     *   indexcol: the column index
     * 
     * Return value: the calculated mixColumns value
     */
    private static int mixColHelper(int[][] inputArray, int[][] gal, int indexrow, int indexcol) {
        int mixcolsum = 0;
        for (int z = 0; z < 4; z++) {
            int x = gal[indexrow][z];
            int y = inputArray[z][indexcol];
            mixcolsum ^= mixColCalc(x, y);
        }
        return mixcolsum;
    }

    /**
     * mixColCalc
     * 
     * This method helps in the mix columns calculations in the mixColHelper method
     * 
     * Parameters:
     *   x: first number input
     *   y: second number input
     * 
     * Return value: the statement with the satisfied condition.
     */
    private static int mixColCalc(int x, int y) { // Helper method for mixColHelper
        if (x == 1) {
            return y;
        } else if (x == 2) {
            return MixColumnTables.mixCol2[y / 16][y % 16];
        } else if (x == 3) {
            return MixColumnTables.mixCol3[y / 16][y % 16];
        }
        return 0;
    }

    /**
     * invMixColumns
     * 
     * This method is the inverse version of the mixColumns method.
     * 
     * Parameters:
     *   inputArray: the array to calculate towards the galois field matrix.
     * 
     * Return value:
     */
    public static void invMixColumns(int[][] inputArray) {
        int[][] arr = new int[4][4];
        for (int x = 0; x < 4; x++) {
            System.arraycopy(inputArray[x], 0, arr[x], 0, 4);
        }
        for (int y = 0; y < 4; y++) {
            for (int z = 0; z < 4; z++) {
                inputArray[y][z] = invMixColHelper(arr, invgalois, y, z);
            }
        }
    }

    /**
     * invMixColHelper
     * 
     * This method is the inverse version of the mixColHelper method.
     * 
     * Parameters:
     *   inputArray: the created 2D array in the mixColumns method
     *   gal: the given galois field matrix
     *   indexrow: the row index
     *   indexcol: the column index
     * 
     * Return value: the calculated mixColumns value
     */
    private static int invMixColHelper(int[][] inputArray, int[][] igalois, int indexrow, int indexcol) {
        int mixcolsum = 0;
        for (int z = 0; z < 4; z++) {
            int x = igalois[indexrow][z];
            int y = inputArray[z][indexcol];
            mixcolsum ^= invMixColCalc(x, y);
        }
        return mixcolsum;
    }

    /**
     * invMixColCalc
     * 
     * This method is the inverse version of the mixColCalc method.
     * 
     * Parameters:
     *   a: first number input
     *   b: second number input
     * 
     * Return value: the statement with the satisfied condition.
     */
    private static int invMixColCalc(int x, int y) {
        if (x == 9) {
            return MixColumnTables.mixCol9[y / 16][y % 16];
        } else if (x == 0xb) {
            return MixColumnTables.mixCol11[y / 16][y % 16];
        } else if (x == 0xd) {
            return MixColumnTables.mixCol13[y / 16][y % 16];
        } else if (x == 0xe) {
            return MixColumnTables.mixCol14[y / 16][y % 16];
        }
        return 0;
    }

    /**
     * keySchedule
     * 
     * This method receives a given key and uses it to generate the all the round
     * keys.
     *
     * Parameters:
     *   key: the input key to expand into round keys.
     * 
     * Return value: the round keys, stored in a matrix.
     */
    public static int[][] keySchedule(String key) {
        int keySizeBin = key.length() * 4;
        int columnsize = keySizeBin + 48 - (32 * ((keySizeBin / 64) - 2));
        int[][] keyMatrix = new int[4][columnsize / 4];
        int rConReference = 1;
        int[] t = new int[4];
        final int keyCount = keySizeBin / 32;
        int k;
        for (int i = 0; i < keyCount; i++) {      
            for (int j = 0; j < 4; j++) {
                keyMatrix[j][i] = Integer.parseInt(key.substring((8 * i) + (2 * j), (8 * i) + (2 * j + 2)), 16);
            }
        }
        int keypoint = keyCount;
        while (keypoint < (columnsize / 4)) {
            int temp = keypoint % keyCount;
            if (temp == 0) {
                for (k = 0; k < 4; k++) {
                    t[k] = keyMatrix[k][keypoint - 1];
                }
                t = schedule_core(t, rConReference++);
                for (k = 0; k < 4; k++) {
                    keyMatrix[k][keypoint] = t[k] ^ keyMatrix[k][keypoint - keyCount];
                }
                keypoint++;
            } else if (temp == 4) {
                for (k = 0; k < 4; k++) {
                    int hex = keyMatrix[k][keypoint - 1];
                    keyMatrix[k][keypoint] = sbox[hex / 16][hex % 16] ^ keyMatrix[k][keypoint - keyCount];
                }
                keypoint++;
            } else {
                int ktemp = keypoint + 3;
                while (keypoint < ktemp) {
                    for (k = 0; k < 4; k++) {
                        keyMatrix[k][keypoint] = keyMatrix[k][keypoint - 1] ^ keyMatrix[k][keypoint - keyCount];
                    }
                    keypoint++;
                }
            }
        }
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < columnsize / 4; j++) {
            }
        }
        return keyMatrix;
    }

    /**
     * schedule_core
     * 
     * This method holds the key scheduling core. A column is produced by utilizing 
     * sbox and XORing an Rcon number with the input array's first element.
     * 
     * Parameters:
     *   input: the array in which to compute the subsequent set of bytes for key
     *     expansion
     *   rconref: the specific element within the Rcon array to XOR with the
     *     first element in the parameter 'input'
     * 
     * Return value: the following column for the key schedule.
     */
    public static int[] schedule_core(int[] input, int rconref) {
        input = rotateLeft(input, 1);
        int hex;
        for (int x = 0; x < input.length; x++) {
            hex = input[x];
            input[x] = sbox[hex / 16][hex % 16];
        }
        input[0] ^= rcon[rconref];
        return input;
    }

    /**
     * addRoundKey
     * 
     * This method adds each of the new round keys. Here, the round key is combined with the state.
     * 
     * Parameters:
     *   byteMatrix: the input matrix of type byte in which addRoundKeys will be computed on.
     *   keyMatrix: a section of the expanded key
     */
    public static void addRoundKey(int[][] byteMatrix, int[][] keyMatrix) {
        for (int x = 0; x < byteMatrix.length; x++) {
            for (int y = 0; y < byteMatrix[0].length; y++) {
                byteMatrix[y][x] ^= keyMatrix[y][x];
            }
        }
    }

    /**
     * MatrixToString
     * 
     * This method converts a matrix into a String of hex characters. 
     * 
     * Parameters:
     *   m: input matrix to be converted
     * 
     * Return value: the string representation of the input matrix.
     */
    public static String MatrixToString(int[][] inputMatrix) {
        String str = "";
        for (int x = 0; x < inputMatrix.length; x++) {
            for (int y = 0; y < inputMatrix[0].length; y++) {
                String temp = Integer.toHexString(inputMatrix[y][x]).toUpperCase();
                if (temp.length() == 1) {
                    str += '0' + temp;
                } else {
                    str += temp;
                }
            }
        }
        return str;
    }
}