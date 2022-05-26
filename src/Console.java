import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class Console {

    public static BinarySearchTree binarySearchTree;

    public static void main(String[] args) {
        binarySearchTree = new BinarySearchTree();
        while (true) {
            printMenu();
            Scanner sysScanner = new Scanner(System.in);
            int userOption;
            try {
                userOption = Integer.parseInt(sysScanner.next());

            } catch (NumberFormatException e) {
                System.out.println("Invalid input");
                continue;
            }
            if (userOption == 0) break;
            handleOption(userOption);
        }
        System.out.println("Terminated");
    }

    /**
     * This method will coordinate the whole functionality of the console user interface
     * @param option the option inserted by the user
     */
    public static void handleOption(int option) {
        if (option == 1) {
            Scanner sysScanner = new Scanner(System.in);
            System.out.println("Insert here the file path");
            String path = sysScanner.nextLine();
            Scanner fileScanner;

            try {
                fileScanner = new Scanner(new File(path));
            } catch (FileNotFoundException e) {
                System.out.println("It was not possible to find the file\n");
                return;
            }
            readFileAndInsert(fileScanner);
        } else if (option == 2) {
            readUserInputAndInsert(new Scanner(System.in));
        } else if (option == 3) {
            String toSearch = getWord(new Scanner(System.in));
            toSearch = toSearch.toLowerCase();
            long startTime = System.nanoTime();
            List<Node> listNodes = binarySearchTree.getNodes(toSearch);
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);

            listNodes.forEach(n -> System.out.println(n.toString()));
            System.out.println("Operation took " + duration + " ns");
        } else if (option == 4) {
            String toRemove = getWord(new Scanner(System.in));
            toRemove = toRemove.toLowerCase();

            long startTime = System.nanoTime();
            boolean success = binarySearchTree.removeNode(toRemove, binarySearchTree.getRoot());
            long endTime = System.nanoTime();
            long duration = (endTime - startTime);
            if (success) System.out.println("Key removed in " + duration + " ns");
            else System.out.println("This key does not exist! Operation took " + duration + " ns");
        } else if (option == 5) {
            long startTime = System.nanoTime();
            binarySearchTree.balance();
            long duration = (System.nanoTime() - startTime);
            System.out.println("Tree balanced with Day Stout-Warren in " + duration + " ns");
        } else {
            System.out.println("Invalid option");
        }
    }

    /**
     * This method will read the intended key by the user
     * @param scanner containg the console
     * @return the word types by the user
     */
    public static String getWord(Scanner scanner) {
        System.out.println("What is the key?");
        String word = scanner.nextLine();
        return word;
    }

    /**
     * This method will read keys from a file
     * @param scanner the scanner containing the file
     */
    public static void readFileAndInsert(Scanner scanner) {
        while (scanner.hasNext()) {
            String sentence = scanner.nextLine();
            Arrays.asList(sentence.toLowerCase().split(" ")).forEach(w -> binarySearchTree.addNode(w));
        }
    }

    /**
     * This method will read user input from the console in a specific fashion
     * @param sysScanner scanner of the console
     */
    public static void readUserInputAndInsert(Scanner sysScanner) {
        System.out.println("Type the keys or press enter to terminate");
        while (true) {
            String sentence = sysScanner.nextLine();
            if (sentence.equals("")) break;
            System.out.println("Type the keys or press enter to terminate");
            Arrays.asList(sentence.toLowerCase().split(" ")).forEach(w -> binarySearchTree.addNode(w));
        }
    }

    /**
     * Prints the menu
     */
    public static void printMenu() {
        System.out.println("\n");
        System.out.println("1) Insert keys from a file");
        System.out.println("2) Insert keys from the console");
        System.out.println("3) Search for keys");
        System.out.println("4) Remove keys");
        System.out.println("5) Balance Tree");
        System.out.println("0) Terminate");
    }
}
