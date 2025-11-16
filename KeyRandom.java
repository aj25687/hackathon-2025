import java.util.Random;

public class KeyRandom {
    public static void main(String[] args) {
        Random r = new Random();
        String[] key = new String [50];
        for (int i = 0; i < 50; i++){
            key[i] = String.valueOf(r.nextInt(0,500));
        }
        System.out.println(key);
    }
}
