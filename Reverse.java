import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class temp {
    public static void main(String[] args) throws FileNotFoundException {

        // Read file
        Scanner fileScan = new Scanner(new File("filename.txt"));
        String data = fileScan.useDelimiter("\\A").next(); // read entire file as one string
        fileScan.close();

        // Read key
        Scanner keyScan = new Scanner(System.in);
        String key = keyScan.nextLine();
        keyScan.close();

        // Parse numbers
        String[] parts = key.split("\\.");
        List<Integer> indexes = new ArrayList<>();

        for (String p : parts) {
            if (!p.isEmpty()) {
                indexes.add(Integer.parseInt(p));
            }
        }

        // Reverse segments
        StringBuilder modified = new StringBuilder(data);

        for (int i = 0; i < indexes.size() - 1; i++) {
            int start = Math.min(indexes.get(i), indexes.get(i+1));
            int end = Math.max(indexes.get(i), indexes.get(i+1));

            // safety check
            if (start < 0 || end > modified.length() || start >= end) {
                System.out.print("skip");
                continue;
            }

            String segment = modified.substring(start, end);
            String reversed = new StringBuilder(segment).reverse().toString();

            // replace in the string
            modified.replace(start, end, reversed);
        }

        System.out.println(modified.toString());
    }
}