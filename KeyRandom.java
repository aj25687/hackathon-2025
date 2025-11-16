import java.util.Random;

public class KeyRandom {
public static void main(String[] args) {
    //String key = theKey();
    //System.out.println(key);
}

    public String theKey(){
        Random r = new Random();
        String[] key = new String [100];
        String fullKey = "";
        fullKey += String.valueOf(r.nextInt(30));
        fullKey += ":0.";
        for (int i = 0; i < 100; i++){
            key[i] = String.valueOf(r.nextInt(0,1500));
            fullKey += key[i] + ".";
        }
        fullKey = fullKey.substring(0, fullKey.length()-1);
        return fullKey;
    }
}
