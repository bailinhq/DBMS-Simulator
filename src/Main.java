import java.util.Random;

public class Main {
    public static void main(String[] args){
        Random random = new Random(0);
        for (int i = 0; i < 10; i++){
            System.out.println(random.nextInt());
        }

    }
}
