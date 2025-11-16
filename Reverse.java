import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Reverse {
    public static void main(String[] args) throws FileNotFoundException {

        // read file
        Scanner fileScan = new Scanner(new File("filename.txt"));
        String data = fileScan.useDelimiter("\\A").next(); // read entire file as one token then go to next string
        fileScan.close();

        // read key scanner
        //TODO make this call key random.java
        Scanner keyScan = new Scanner(System.in);
        String keyOG = keyScan.nextLine();
        String key = (keyOG.substring(keyOG.indexOf(":")+1));
        //System.out.println(key);
        int addToIndex = Integer.parseInt(keyOG.substring(0,keyOG.indexOf(":")));
        //System.out.println(addToIndex);
        keyScan.close();

        // Parse numbers
        String[] parts = key.split("\\."); // split the key w/ delimitor
        List<Integer> indexes = new ArrayList<>();

        for (String p:parts) {
            if (!p.isEmpty()) {
                indexes.add(Integer.parseInt(p));
            }
        }

        // Reverse segments
        StringBuilder modified = new StringBuilder(data); // cuz string builer is easier to modify
        for (int l = 0; l<addToIndex; l++){
        for (int i = 0; i < indexes.size() - 1; i++) {
            int start = Math.min(indexes.get(i), indexes.get(i+1))+l;
            int end = Math.max(indexes.get(i), indexes.get(i+1)) + 1+l; //the +1 is just incase start and end end up being the same 

            // safety check so i dont have to deal with exeptions :)
            if (start<=-1 || end>modified.length()) {
                System.out.print("skiped");
                continue;
            }

            String segment = modified.substring(start, end);
            String reversed = new StringBuilder(segment).reverse().toString();

            // replace in the string
            modified.replace(start, end, reversed); //starts at var start, goes till (var) end, replaces that with reversed
        }
        }
        System.out.println(modified.toString());
    }
}