import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Decrypt {
    public static void main(String[] args) {
        try {
            Scanner fileScan = new Scanner(new File("encrypted.txt"));
            String data = fileScan.useDelimiter("\\A").next();
            fileScan.close();
            String text = Decrypting(data);
            System.out.println(text);
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    public static String Decrypting(String data) throws FileNotFoundException {

        // read encrypted file
        
        // get key
        Scanner fileScan2 = new Scanner(new File("theKey.txt"));
        String keyOG = fileScan2.useDelimiter("\\A").next(); // read entire file as one token then go to next string
        fileScan2.close();

        // parse
        String key = keyOG.substring(keyOG.indexOf(":") + 1);
        int addToIndex = Integer.parseInt(keyOG.substring(0, keyOG.indexOf(":")));

        String[] parts = key.split("\\.");
        List<Integer> indexes = new ArrayList<>();
        for (String p : parts) {
            if (!p.isEmpty()) indexes.add(Integer.parseInt(p));
        }

        StringBuilder modified = new StringBuilder(data);

        // undo encryption loops
        for (int l = addToIndex - 1; l >= 0; l--) {
            for (int i = indexes.size() - 2; i >= 0; i--) {

                int start = Math.min(indexes.get(i), indexes.get(i + 1)) + l;
                int end = Math.max(indexes.get(i), indexes.get(i + 1)) + 1 + l;

                if (start < 0 || end > modified.length() || start >= end) continue;

                String seg = modified.substring(start, end);
                String rev = new StringBuilder(seg).reverse().toString();
                modified.replace(start, end, rev);
            }
        }
        //System.out.println(modified.toString());
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("decrypted.txt"))) {
            writer.write(modified.toString());
            System.out.println("Sucess! Find the decryted file in decyrpted.txt");
        } catch (IOException e) {
            System.err.println("Write error: " + e.getMessage());
        }
        return modified.toString();
    }
}