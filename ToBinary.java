import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.PrivateKey;
import java.security.PublicKey;

public class ToBinary {

    public static void main(String[] args) throws Exception {
        String data = Reverse.Reversal();
        Scanner scan = new Scanner(System.in);

        // read file
        Scanner fileScan = null;
        try {
            fileScan = new Scanner(new File("encrypted.txt"));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        //String data = fileScan.useDelimiter("\\A").next();
        //fileScan.close();

        byte[] bytes = data.getBytes();
        StringBuilder binary = new StringBuilder();

        // convert to reversed binary
        for (byte b : bytes) {
            int val = b;
            StringBuilder temp = new StringBuilder();

            for (int i = 0; i < 8; i++) {
                int bit = (val & 128) == 0 ? 0 : 1;
                val <<= 1;
                temp.append(bit);
            }

            temp.reverse();  // reverse each byte’s bit order
            binary.append(temp);
            binary.append(' ');
        }
        System.out.println("Layer Two Complete...");
        // AES key
        KeyGenerator aesGen = KeyGenerator.getInstance("AES");
        aesGen.init(128);
        SecretKey aesKey = aesGen.generateKey();

        // AES encrypt
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] aesEncrypted = aesCipher.doFinal(binary.toString().getBytes());
       // System.out.println("AES encrypted data: " + Arrays.toString(aesEncrypted));

        // RSA keys
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair pair = gen.generateKeyPair();
        PublicKey pub = pair.getPublic();
        PrivateKey priv = pair.getPrivate();

        // encrypt AES key
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, pub);

        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());
        //System.out.println("Encrypted AES key: " + Arrays.toString(encryptedAesKey));

        // decrypt everything INCLUDING the reversed binary
        String decryptedText = decryptData(aesEncrypted, encryptedAesKey, priv);
        
        String textString = new String(aesEncrypted, StandardCharsets.UTF_8);

        try (BufferedWriter writer = new BufferedWriter(new FileWriter("finalEncrypt.txt"))) {
            writer.write(textString);
            System.out.println("Sucess!! Encoded file is in " + "finalEncrypt.txt");
        } catch (IOException e) {
            System.err.println("Oopsies:" + e.getMessage());
        }

        System.out.println("Enter the password for the reversed text:");
        String userPassword = scan.nextLine();
        if(userPassword.equals("Programming Club...")){
            System.out.println(decryptedText);
        }
        System.out.println("Enter the second password for the complete text:");
        userPassword = scan.nextLine();
        if(userPassword.equals("...Is not chopped!")){
            String text = Decrypt.Decrypting(decryptedText);
            System.out.println(text);
        }        

    }

    // decrypt everything all the way back to readable text
    public static String decryptData(byte[] aesEncrypted, byte[] encryptedAesKey, PrivateKey priv) throws Exception {

        // RSA decrypt the AES key
        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, priv);

        byte[] decryptedAesKeyBytes = rsaCipher.doFinal(encryptedAesKey);
        SecretKey decryptedAesKey = new SecretKeySpec(decryptedAesKeyBytes, "AES");

        // AES decrypt the binary string
        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, decryptedAesKey);

        byte[] decrypted = aesCipher.doFinal(aesEncrypted);
        String binaryString = new String(decrypted).trim();

        // now decode binary back to original text
        String[] byteChunks = binaryString.split(" ");

        StringBuilder finalOutput = new StringBuilder();

        for (String chunk : byteChunks) {
            if (chunk.length() != 8) continue;

            // reverse the bits again to restore original byte
            String reversed = new StringBuilder(chunk).reverse().toString();

            // convert "01010101" -> actual byte
            int value = Integer.parseInt(reversed, 2);

            // append character
            finalOutput.append((char) value);
        }

        return finalOutput.toString();
    }
}