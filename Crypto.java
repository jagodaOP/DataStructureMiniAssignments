/**
 * * Description:
 * This program implements a simple encryption and decryption system based on the
 * historical Washington Cipher. It reads encoding and cipher data from external files
 * ("WashingtonCode.txt" and "WashingtonCipher.txt"), allowing users to encrypt and
 * decrypt messages. The program provides a user-friendly interface for input and
 * displays the results of the encryption and decryption operations.
 * Author: Amar Sahbazovic
 * Date: December 7th, 2023
 */
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Crypto {

    public static void main(String[] args) {
        char[] encipherArray = new char[26];
        char[] decipherArray = new char[26];

        initializeWashingtonCipher(encipherArray, decipherArray);

        HashTable table = new HashTable();
        String[] decode = new String[763];

        initializeWashingtonCode(table, decode);

        Scanner scanner = new Scanner(System.in);

        boolean continueLoop = true;

        System.out.println("Welcome to the crypto Program!");

        do {
            processUserInput(encipherArray, decipherArray, table, scanner, decode);
            boolean isValid;
            do {
                printSeparator("*", 50);
                System.out.println("Would you like to encrypt/decrypt another message? Y/N");
                System.out.print("Your selection: ");

                String yesOrNo = scanner.nextLine().toLowerCase();

                if (yesOrNo.equals("y")) {
                    isValid = true;
                } else if (yesOrNo.equals("n")) {
                    continueLoop = false;
                    isValid = true;
                    printSeparator("*", 50);
                    System.out.println("Thanks for using the Crypto program! Have a great day!");
                } else {
                    System.out.println("Invalid selection. Please enter 'Y' or 'N'.");
                    isValid = false;
                }
            } while (!isValid);

        } while (continueLoop);

        scanner.close();
    }

    // Process user input for encryption or decryption
    private static void processUserInput(char[] encipherArray, char[] decipherArray, HashTable table, Scanner scanner, String[] decode) {
        
       // Flag to control the input loop
        boolean isValid = false;

        do {
            // Display separator and menu
            printSeparator("*", 50);
            System.out.println("To encrypt, press '1'");
            System.out.println("To decrypt, press '2'");
            System.out.print("Your selection: ");

            try {
                // Read user input and process
                int encryptOrDecrypt = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character

                // Display separator based on user input
                printSeparator("*", 50);

                switch (encryptOrDecrypt) {
                    case 1:
                        // Encrypt the user-entered message
                        System.out.println("Enter the message you would like to encrypt: ");
                        String message = scanner.nextLine();
                        String result = encryptMessage(message, table, encipherArray, decipherArray);
                        printSeparator("*", 50);
                        System.out.println("Here is your encrypted message:\n" + result);
                        isValid = true;
                        break;
                    case 2:
                        // Decrypt the user-entered message
                        System.out.println("Enter the message you would like to decrypt: ");
                        String decryptedMessage = decryptMessage(scanner.nextLine(), table, encipherArray, decipherArray, decode);
                        printSeparator("*", 50);
                        System.out.println("Here is your decrypted message:\n" + decryptedMessage);
                        isValid = true;
                        break;
                    default:
                        System.out.println("Invalid selection. Please enter either '1' or '2'.");
                }
            } catch (java.util.InputMismatchException e) {
                // Handle invalid input
                System.out.println("Invalid input. Please enter a valid integer.");
                scanner.nextLine(); // Consume the invalid input to avoid an infinite loop
            }

        } while (!isValid);
    }

    // Decrypt the user-entered message
    private static String decryptMessage(String message, HashTable table, char[] encipherArray, char[] decipherArray, String[] decode) {
        message = message.toUpperCase();
        String[] words = message.split(" ");

        String[] modifiedWords = new String[words.length];

        for (int i = 0; i < words.length; i++) {
            if (containsOnlyDigits(words[i])) {
                // If the word contains only digits, use decoding array
                if (Integer.parseInt(words[i]) <= decode.length) {
                    modifiedWords[i] = decode[Integer.parseInt(words[i]) - 1];
                } else {
                    // Handle words not in the decoding array
                    modifiedWords[i] = "UNKNOWN";
                }
            } else {
                // If the word contains characters, perform decryption using decipher array
                char[] characters = words[i].toCharArray();
                for (int j = 0; j < characters.length; j++) {
                    char originalChar = characters[j];
                    if (originalChar >= 'A' && originalChar <= 'Z') {
                        int index = indexOf(decipherArray, originalChar);
                        if (index != -1) {
                            characters[j] = (char) ('A' + index);
                        }
                    }
                }
                modifiedWords[i] = String.valueOf(characters);
            }
        }

        return String.join(" ", modifiedWords);
    }

    // Helper method to find the index of a character in an array
    private static int indexOf(char[] array, char target) {
        for (int i = 0; i < array.length; i++) {
            if (array[i] == target) {
                return i;
            }
        }
        return -1;
    }

    // Check if a word contains only digits
    private static boolean containsOnlyDigits(String word) {
        for (char c : word.toCharArray()) {
            if (!Character.isDigit(c)) {
                return false;
            }
        }
        return true;
    }

    // Encrypt the user-entered message
    private static String encryptMessage(String message, HashTable table, char[] encipherArray, char[] decipherArray) {
        message = message.replaceAll("\\p{Punct}", "");
        message = message.toUpperCase();

        String[] words = message.split(" ");
        String[] modifiedWords = new String[words.length];

        for (int i = 0; i < words.length; i++) {
            if (table.access(words[i])) {
                modifiedWords[i] = String.valueOf(table.accessCode(words[i]));
            } else {
                char[] characters = words[i].toCharArray();
                for (int j = 0; j < characters.length; j++) {
                    char originalChar = characters[j];
                    if (originalChar >= 'A' && originalChar <= 'Z') {
                        int index = originalChar - 'A';
                        characters[j] = decipherArray[index];
                    }
                }
                modifiedWords[i] = String.valueOf(characters);
            }
        }

        return String.join(" ", modifiedWords);
    }    

    private static void initializeWashingtonCode(HashTable table, String[] decode) {
        int index = 0;
        try {
            // Read from the WashingtonCode file (without the .txt extension)
            File washingtonCodeFile = new File("WashingtonCode");
            Scanner fileScanner = new Scanner(washingtonCodeFile);
    
            // Process each line in the file
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                String[] parts = line.split(" ");
    
                // Check if there are at least two parts in the line
                if (parts.length >= 2) {
                    try {
                        // Insert into hash table
                        table.insert(parts[0], Integer.parseInt(parts[1]));
                        // Populate decoding array
                        decode[index] = parts[0];
                        index++;
                    } catch (NumberFormatException e) {
                        // Handle parsing error
                        System.out.println("Error parsing integer from: " + line);
                    }
                } else {
                    // Handle invalid line format
                    System.out.println("Invalid line format in WashingtonCode: " + line);
                }
            }
    
            // Close the file scanner
            fileScanner.close();
        } catch (FileNotFoundException e) {
            // Handle file read error
            handleFileReadError("WashingtonCode", e);
        }
    }        

    // Initialize the encipher and decipher arrays from the WashingtonCipher.txt file
    private static void initializeWashingtonCipher(char[] encipherArray, char[] decipherArray) {
        int index = 0;
        try {
            // Read from the WashingtonCipher.txt file
            File washingtonCipherFile = new File("WashingtonCipher");
            Scanner fileScanner = new Scanner(washingtonCipherFile);

            // Process each line in the file
            while (fileScanner.hasNextLine()) {
                String line = fileScanner.nextLine();
                encipherArray[index] = line.charAt(0);
                decipherArray[index] = line.charAt(2);
                index++;
            }

            // Close the file scanner
            fileScanner.close();
        } catch (FileNotFoundException e) {
            // Handle file read error
            handleFileReadError("WashingtonCipher.txt", e);
        }
    }

    // Handle file read errors
    private static void handleFileReadError(String fileName, FileNotFoundException e) {
        System.out.println("An error occurred while reading " + fileName + ".");
        e.printStackTrace();
    }

    // Print a separator line using the specified symbol and length
    private static void printSeparator(String symbol, int length) {
        for (int i = 0; i < length; i++) {
            System.out.print(symbol);
        }
        System.out.println();
    }
    
}