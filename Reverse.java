import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner;
public class Reverse{

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        String data = "";

        File myObj = new File("filename.txt");

    // try-with-resources: Scanner will be closed automatically
    try (Scanner myReader = new Scanner(myObj)) {
      while (myReader.hasNextLine()) {
        data = myReader.nextLine();
        System.out.println(data);
      }
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }

    Scanner keyScan = new Scanner(System.in);

    String key = keyScan.nextLine();
    int dotSeperator[] = new int [50];
    dotSeperator[0] = key.indexOf(".");

    for(int i = 0; i<dotSeperator.length-2; i++){
        dotSeperator[i+1] = key.indexOf(".", dotSeperator[i]+1);
    }
    dotSeperator[49] = key.indexOf(".", dotSeperator[48]);
    String newData = "";
    for(int k = 0; k<49; k++){
        int startIndex = Math.min(dotSeperator[k], dotSeperator[k+1]);
        int lastIndex = Math.max(dotSeperator[k], dotSeperator[k+1]);
        int charecters = lastIndex - startIndex;
        for(int j = lastIndex; j>charecters; j--){
            newData += data.substring(j, j-1);
        }
    }
    System.out.println(newData);
    
    }
}
//what this does is the key is 50 numbesr, seperated bt dots. what this is suppost to do is take those numbers and reverse the segments of text that is inbetween those two indexes. so 10.20.30.40 reverses the letters from 10 to 20, then from 20 to 30, then 30 to 40