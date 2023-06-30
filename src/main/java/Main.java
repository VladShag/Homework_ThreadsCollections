import java.util.Random;
import java.util.concurrent.ArrayBlockingQueue;

public class Main {

    public static ArrayBlockingQueue<String> searchingForA = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> searchingForB = new ArrayBlockingQueue<>(100);
    public static ArrayBlockingQueue<String> searchingForC = new ArrayBlockingQueue<>(100);

    public static void main(String[] args) {

        Thread generateTextThread = new Thread(() -> {
            for (int i = 0; i < 10_000; i++) {
                String textToAdd = generateText("abc", 100_000);
                try {
                    searchingForA.put(textToAdd);
                    searchingForB.put(textToAdd);
                    searchingForC.put(textToAdd);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        generateTextThread.start();
        Thread searchingForAThread = new Thread(() -> {
            maxNumberOfLetter(searchingForA, 'a');

        });
        searchingForAThread.start();
        Thread searchingForBThread = new Thread(() -> {
            maxNumberOfLetter(searchingForB, 'b');

        });
        searchingForBThread.start();
        Thread searchingForCThread = new Thread(() -> {
            maxNumberOfLetter(searchingForC, 'c');

        });
        searchingForCThread.start();
        try {
            generateTextThread.join();
            searchingForAThread.join();
            searchingForBThread.join();
            searchingForCThread.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public static String generateText(String letters, int length) {
        Random random = new Random();
        StringBuilder text = new StringBuilder();
        for (int i = 0; i < length; i++) {
            text.append(letters.charAt(random.nextInt(letters.length())));
        }
        return text.toString();
    }

    public static void maxNumberOfLetter(ArrayBlockingQueue<String> queue, char letter) {
        int max = 0;
        String text = "";
        for (int i = 0; i < 10_000; i++) {
            int resultCount = 0;
            try {
                text = queue.take();
                for (int j = 0; j < text.length(); j++) {
                    if (text.charAt(j) == letter) {
                        resultCount++;
                    }
                }

                if (resultCount > max) {
                    max = resultCount;
                }
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        System.out.println("Максимальное количество букв " + letter + ":" + max);
    }
}