import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner;
public class Reverse{

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        scan.nextLine();
        String data = "";

        System.out.println("reverse"); 
        System.out.println("test"); 
        System.out.println("oliver");

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
    System.out.println("break");
    System.out.println(dotSeperator[49]);
    System.out.println(dotSeperator[0]);
    }
}