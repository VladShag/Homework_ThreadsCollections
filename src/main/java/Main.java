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
            int maxA = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    int numberOfAInText = countNumberOfLetter(searchingForA.take(), 'a');
                    if (numberOfAInText > maxA) {
                        maxA = numberOfAInText;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество букв a:" + maxA);

        });
        searchingForAThread.start();
        Thread searchingForBThread = new Thread(() -> {
            int maxB = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    int numberOfBInText = countNumberOfLetter(searchingForB.take(), 'b');
                    if (numberOfBInText > maxB) {
                        maxB = numberOfBInText;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество букв b:" + maxB);

        });
        searchingForBThread.start();
        Thread searchingForCThread = new Thread(() -> {
            int maxC = 0;
            for (int i = 0; i < 10_000; i++) {
                try {
                    int numberOfCInText = countNumberOfLetter(searchingForC.take(), 'c');
                    if (numberOfCInText > maxC) {
                        maxC = numberOfCInText;
                    }
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
            }
            System.out.println("Максимальное количество букв c:" + maxC);

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

    public static int countNumberOfLetter(String text, char letter) {
        int resultCount = 0;
        for (int i = 0; i < text.length(); i++) {
            if (text.charAt(i) == letter) {
                resultCount++;
            }
        }
        return resultCount;
    }
}