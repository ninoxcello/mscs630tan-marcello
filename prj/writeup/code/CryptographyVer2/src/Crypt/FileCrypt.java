/**
 * file: FileCrypt.java
 * author: Antonino Tan-Marcello
 * course: MSCS 630
 * assignment: Project
 * due date: March 9, 2017
 *
 * This file contains the main UI of the file encryption/decryption program.
 * It utilizes the core AES system, AES.java, in order to operate successfully.
 */
package Crypt;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

/**
 * FileCrypt
 *
 * This file contain all the functions, UI button actions and AES function calls
 * to properly run the AES encryption/decryption program.
 */
public class FileCrypt extends javax.swing.JFrame {

    /**
     * FileCrypt
     *
     * Constructor that creates new form FileCrypt, set the title of the jPanel
     * window and initializes all components of the jPanel.
     */
    public FileCrypt() {
        setTitle("FileCrypt: A File Encryption/Decryption Program");
        initComponents();
    }

    /**
     * initComponents
     *
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        file_path = new javax.swing.JTextField();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();
        jButton3 = new javax.swing.JButton();
        jLabel2 = new javax.swing.JLabel();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setMinimumSize(new java.awt.Dimension(600, 375));
        setSize(500,500);
        getContentPane().setLayout(null);
        getContentPane().add(file_path);
        file_path.setBounds(70, 150, 330, 30);

        jButton1.setText("Browse File...");
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton1ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton1);
        jButton1.setBounds(420, 150, 112, 32);

        jButton2.setText("Encrypt");
        jButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton2ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton2);
        jButton2.setBounds(190, 220, 77, 32);

        jButton3.setText("Decrypt");
        jButton3.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                jButton3ActionPerformed(evt);
            }
        });
        getContentPane().add(jButton3);
        jButton3.setBounds(340, 220, 79, 32);

        jLabel2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/Crypt/backgroundimage.jpg"))); // NOI18N
        jLabel2.setText("jLabel2");
        getContentPane().add(jLabel2);
        jLabel2.setBounds(0, 0, 600, 360);

        pack();
    }// </editor-fold>//GEN-END:initComponents
    File destinationFile;
    File initialFile;

    /**
     * jButton1ActionPerformed
     *
     * This jPanel button allows the user browse for a specific file on their
     * device.
     */
    private void jButton1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton1ActionPerformed
        JFileChooser choose = new JFileChooser();
        choose.showOpenDialog(null);
        File f = choose.getSelectedFile();
        file_path.setText(f.getAbsolutePath());
    }//GEN-LAST:event_jButton1ActionPerformed

    /**
     * readBFile
     *
     * This function will retrieve the contents of the file input path as bytes
     * and store those contents in an array.
     *
     * Parameters: path: the location of the chosen file, as type String.
     *
     * Return value: the file contents, stored in a byte array.
     */
    public byte[] readBFile(String path) {
        byte[] data = null;
        try {
            InputStream in = new FileInputStream(new File(path));
            data = new byte[in.available()];
            in.read(data);
            in.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } // assuming already present
        return data;
    }

    /**
     * writeFile
     *
     * This function writes a file to the desired directory location following
     * the completion of the encryption or decryption algorithm.
     *
     * Parameters: data: the contents of the file to be written, in type byte.
     * path: the desired destination of the newly written file.
     */
    public static void writeFile(byte[] data, String path) {
        try {
            FileOutputStream out = new FileOutputStream(path);
            out.write(data);
            out.close();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    /**
     * jButton2ActionPerformed
     *
     * This jPanel button uses the file path from jButton1ActionPerformed to
     * locate the desired file to be used in the AES encryption algorithm. The
     * function then calls the encryption method, passing the bytes of the
     * target file and the given key k, as parameters.
     *
     * @param evt
     */
    private void jButton2ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton2ActionPerformed
        try {
            long startTime = System.nanoTime();
            initialFile = new File(file_path.getText());
            String data = (new String(readBFile(file_path.getText())));
            System.out.println(data);
            String k = "5468617473206D79204B756E67204675";
            String enc = AES.AESEncrypt(data, k);
            System.out.println(enc);
            writeFile(enc.getBytes("UTF-8"), file_path.getText().concat(".enc"));
            initialFile.delete();
            long elapsedTime = (System.nanoTime() - startTime) / 1000000;
            JOptionPane.showMessageDialog(null, "File Encryption Complete. \nElapsed Time: " + elapsedTime + " ms");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_jButton2ActionPerformed

    /**
     * jButton3ActionPerformed
     *
     * This jPanel button uses the file path from jButton1ActionPerformed to
     * locate the desired file to be used in the AES decryption algorithm. The
     * function then calls the decryption method, passing the bytes of the
     * target file and the given key k, as parameters.
     *
     * @param evt
     */
    private void jButton3ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_jButton3ActionPerformed
        try {
            long startTime = System.nanoTime();
            initialFile = new File(file_path.getText());
            String data = (new String(readBFile(file_path.getText())));
            System.out.println(data);
            String k = "5468617473206D79204B756E67204675";
            String enc = AES.AESDecrypt(data, k);
            writeFile(enc.getBytes("UTF-8"), file_path.getText().toString().substring(0, file_path.getText().toString().length() - 4));
            initialFile.delete();
            long elapsedTime = (System.nanoTime() - startTime) / 1000000;
            JOptionPane.showMessageDialog(null, "File Decryption Complete. \nElapsed Time: " + elapsedTime + " ms");
        } catch (Exception e) {
            JOptionPane.showMessageDialog(null, e);
        }
    }//GEN-LAST:event_jButton3ActionPerformed

    /**
     * main
     *
     * The main method that creates and displays the jPanel UI of the program.
     */
    public static void main(String args[]) {
        /* Set the Nimbus look and feel */
        //<editor-fold defaultstate="collapsed" desc=" Look and feel setting code (optional) ">
        /* If Nimbus (introduced in Java SE 6) is not available, stay with the default look and feel.
         * For details see http://download.oracle.com/javase/tutorial/uiswing/lookandfeel/plaf.html 
         */

        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;
                }
            }
        } catch (ClassNotFoundException ex) {
            java.util.logging.Logger.getLogger(FileCrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (InstantiationException ex) {
            java.util.logging.Logger.getLogger(FileCrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (IllegalAccessException ex) {
            java.util.logging.Logger.getLogger(FileCrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        } catch (javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(FileCrypt.class.getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }
        //</editor-fold>

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                new FileCrypt().setVisible(true);
            }
        });
    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JTextField file_path;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JButton jButton3;
    private javax.swing.JLabel jLabel2;
    // End of variables declaration//GEN-END:variables
}