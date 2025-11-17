import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reverse {
    public static void main(String [] args){
        try {
            String content = Reversal();
            System.out.println("it works");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static String Reversal() throws FileNotFoundException {

        // read file
        Scanner fileScan = new Scanner(new File("raw.txt"));
        String data = fileScan.useDelimiter("\\A").next(); // read entire file as one token then go to next string
        fileScan.close();

        KeyRandom keyRandom = new KeyRandom();

        // get the key from keyrandom      
        String keyOG = keyRandom.theKey();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("theKey.txt"))) {
            writer.write(keyOG);
            System.out.println("Go find the key...");
        } catch (IOException e) {
            System.err.println("Oopsies:" + e.getMessage());
        }

        String key = (keyOG.substring(keyOG.indexOf(":")+1));
        //System.out.println(key);
        int addToIndex = Integer.parseInt(keyOG.substring(0,keyOG.indexOf(":")));
        //System.out.println(addToIndex);

        // Parse numbers
        String[] parts = key.split("\\."); // split the key w/ delimitor
        List<Integer> indexes = new ArrayList<>();

        for (String p:parts) {
            if (!p.isEmpty()) {
                indexes.add(Integer.parseInt(p));
            }
        }
        //make "data" turn into a ceaser 13 shift
        data = Ceaser.Ceaser(data);
        // Reverse segments
        StringBuilder modified = new StringBuilder(data); // cuz string builer is easier to modify
        for (int l = 0; l<addToIndex; l++){
        for (int i = 0; i < indexes.size() - 1; i++) {
            int start = Math.min(indexes.get(i), indexes.get(i+1))+l;
            int end = Math.max(indexes.get(i), indexes.get(i+1)) + 1+l; //the +1 is just incase start and end end up being the same 

            // safety check so i dont have to deal with exeptions :)
            if (start<0 || end>modified.length()) {
                continue;
            }

            String segment = modified.substring(start, end);
            String reversed = new StringBuilder(segment).reverse().toString();

            // replace in the string
            modified.replace(start, end, reversed); //starts at var start, goes till (var) end, replaces that with reversed
        }
        }
        String fileName = "encrypted.txt";
        String content = modified.toString();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileName))) {
            writer.write(content);
            System.out.println("Layer one complete... " + fileName);
        } catch (IOException e) {
            System.err.println("Oopsies:" + e.getMessage());
        }
        return content;
    }
}