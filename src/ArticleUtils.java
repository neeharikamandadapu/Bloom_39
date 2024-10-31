package asuHelloWorldJavaFX;

import java.util.Random;

public class ArticleUtils {
    public static long generateRandomId() {
        Random random = new Random();
        return 10000000L + random.nextInt(90000000);  // Generates a number between 10000000 and 99999999
    }
}