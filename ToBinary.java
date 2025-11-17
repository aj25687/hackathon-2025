import java.io.BufferedWriter;
import java.io.File;
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

        // STEP 1 Reverse original text and save SCRAMBLED FILE (encrypted.txt)
        String scrambledText = Reverse.Reversal();  
        Scanner scan = new Scanner(System.in);

        // STEP 2 Convert SCRAMBLED TEXT into reversed binary
        byte[] bytes = scrambledText.getBytes(StandardCharsets.UTF_8);
        StringBuilder binary = new StringBuilder();

        for (byte b : bytes) {
            int val = b;
            StringBuilder temp = new StringBuilder();

            for (int i = 0; i < 8; i++) {
                int bit = (val & 128) == 0 ? 0 : 1;
                val <<= 1;
                temp.append(bit);
            }

            temp.reverse();
            binary.append(temp).append(' ');
        }

        System.out.println("Layer Two Complete...");

        // STEP 3 AES encrypt the reversed binary
        KeyGenerator aesGen = KeyGenerator.getInstance("AES");
        aesGen.init(128);
        SecretKey aesKey = aesGen.generateKey();

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.ENCRYPT_MODE, aesKey);

        byte[] aesEncrypted = aesCipher.doFinal(binary.toString().getBytes(StandardCharsets.UTF_8));

        // STEP 4 RSA encrypt the AES key
        KeyPairGenerator gen = KeyPairGenerator.getInstance("RSA");
        gen.initialize(2048);
        KeyPair pair = gen.generateKeyPair();
        PublicKey pub = pair.getPublic();
        PrivateKey priv = pair.getPrivate();

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.ENCRYPT_MODE, pub);

        byte[] encryptedAesKey = rsaCipher.doFinal(aesKey.getEncoded());

        // STEP 5 Save AES-encrypted binary
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("withRSA.txt"))) {
            writer.write(new String(aesEncrypted, StandardCharsets.ISO_8859_1));
            System.out.println("Saved RSA-layer encrypted file as withRSA.txt");
        }

        // STEP 6 Undo RSA + AES + bit-reversal to get SCRAMBLED TEXT AGAIN
        String scrambledRecovered = decryptData(aesEncrypted, encryptedAesKey, priv);

        System.out.println("Enter the password to view scrambled text:");
        if (scan.nextLine().equals("Programming Club...")) {
            System.out.println(scrambledRecovered);
        }

        // STEP 7 IMPORTANT: LOAD THE REAL SCRAMBLED FILE BEFORE APPLYING Decrypting()
        // This fixes the broken final decryption.
        Scanner fileScan = new Scanner(new File("encrypted.txt"));
        String scrambledFromFile = fileScan.useDelimiter("\\A").next();
        fileScan.close();

        String result = "";

        System.out.println("Enter the second password for COMPLETE text:");
        if (scan.nextLine().equals("...Is not chopped!")) {
            result = Decrypt.Decrypting(scrambledFromFile);
            System.out.println(result);
            System.out.println("\n\n\n\nStill not fully decoded...\n\n");
        }
        if(scan.nextLine().equals("Seriously!!")){
            System.out.println(Ceaser.Ceaser(result));
        }
    }

    // Decrypt AES + RSA + bit reversal and RETURN SCRAMBLED TEXT
    public static String decryptData(byte[] aesEncrypted, byte[] encryptedAesKey, PrivateKey priv) throws Exception {

        Cipher rsaCipher = Cipher.getInstance("RSA");
        rsaCipher.init(Cipher.DECRYPT_MODE, priv);
        byte[] decryptedAesKeyBytes = rsaCipher.doFinal(encryptedAesKey);

        SecretKey aesKey = new SecretKeySpec(decryptedAesKeyBytes, "AES");

        Cipher aesCipher = Cipher.getInstance("AES");
        aesCipher.init(Cipher.DECRYPT_MODE, aesKey);

        byte[] decrypted = aesCipher.doFinal(aesEncrypted);
        String binaryString = new String(decrypted, StandardCharsets.UTF_8).trim();

        String[] byteChunks = binaryString.split(" ");
        StringBuilder result = new StringBuilder();

        // undo reversed bits
        for (String chunk : byteChunks) {
            if (chunk.length() != 8) continue;

            String fixedBits = new StringBuilder(chunk).reverse().toString();
            int value = Integer.parseInt(fixedBits, 2);

            result.append((char) value);
        }

        return result.toString();
    }
}