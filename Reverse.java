import java.io.File;                  // Import the File class
import java.io.FileNotFoundException; // Import this class to handle errors
import java.util.Scanner;
public class Reverse{

    public static void main(String[] args){
        Scanner scan = new Scanner(System.in);

        scan.nextLine();

        System.out.println("reverse"); 
        System.out.println("test"); 
        System.out.println("oliver");

        File myObj = new File("filename.txt");

    // try-with-resources: Scanner will be closed automatically
    try (Scanner myReader = new Scanner(myObj)) {
      while (myReader.hasNextLine()) {
        String data = myReader.nextLine();
        System.out.println(data);
      }
    } catch (FileNotFoundException e) {
      System.out.println("An error occurred.");
      e.printStackTrace();
    }
    }

}