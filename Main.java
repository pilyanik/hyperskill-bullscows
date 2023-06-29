package bullscows;

import java.util.*;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Input the length of the secret code: ");
        String codeLengthStr = scanner.nextLine();
        int codeLength;

        if (codeLengthStr.matches("\\d+")) {
            codeLength = Integer.parseInt(codeLengthStr);
        } else {
            System.out.printf("Error: \"%s\" isn't a valid number.", codeLengthStr);
            return;
        }

        System.out.println("Input the number of possible symbols in the code: ");
        int numCharacters = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character

        if (codeLength > numCharacters || codeLength == 0) {
            System.out.printf("Error: it's not possible to generate a code with a length of %d with %d unique symbols.", codeLength, numCharacters);
            return;
        }

        if (numCharacters > 36) {
            System.out.println("Error: maximum number of possible symbols in the code is 36 (0-9, a-z).");
            return;
        }

        String secretCode = generateSecretCode(codeLength, numCharacters);
        System.out.println(secretCode);

        if (numCharacters < 10) {
            System.out.println("The secret is prepared: " + secretCode.replaceAll(".", "*") +
                    " (" + digitsRange(numCharacters) + ").");
        } else {
            System.out.println("The secret is prepared: " + secretCode.replaceAll(".", "*") +
                    " (" + digitsRange(numCharacters) + "," + " " + alphabetsRange(numCharacters) + ").");
        }

        System.out.println("Okay, let's start a game!");

        boolean guessed = false;
        int turn = 1;

        while (!guessed) {
            System.out.printf("Turn %d:\n", turn);
            String guess = scanner.nextLine().toLowerCase();

            if (guess.length() != codeLength) {
                System.out.println("Invalid guess length. It should be " + codeLength + " characters.");
                continue;
            }

            int bulls = countBulls(secretCode, guess);
            int cows2 = countCows(secretCode, guess);
            int cows = cows2 - bulls;


            if (bulls > 0 && cows == 0 && bulls != codeLength) {
                if (bulls > 1 && cows == 0 && bulls != codeLength) {
                    System.out.println("Grade: " + bulls + " bulls. ");
                    turn++;
                } else {
                    System.out.println("Grade: " + bulls + " bull. ");
                    turn++;
                }
            } else if (bulls == 0 && cows > 0) {
                if (cows > 1) {
                    System.out.println("Grade: " + cows + " cows. ");
                    turn++;
                } else {
                    System.out.println("Grade: " + cows + " cow. ");
                    turn++;
                }
            } else if (bulls == 0 && cows == 0) {
                System.out.println("Grade: None. ");
                turn++;
            } else if (bulls == codeLength) {
                if (bulls == codeLength && bulls == 1) {
                    System.out.println("Grade: " + bulls + " bull\nCongratulations! You guessed the secret code.");
                    guessed = true;
                    //break;
                } else {
                    System.out.println("Grade: " + bulls + " bulls\nCongratulations! You guessed the secret code.");
                    guessed = true;
                    //break;
                }
            } else {
                System.out.println("Grade: " + bulls + " bull(s) and " + cows + " cow(s). ");
                turn++;
            }
        }
    }

    public static String generateSecretCode(int numCharacters, int codeLength) {
        StringBuilder secretCode = new StringBuilder();
        List<Character> characters = new ArrayList<>();

        // Add all possible characters to the list
        for (int i = 0; i < codeLength; i++) {
            if (i < 10) {
                characters.add((char) ('0' + i));
            } else {
                characters.add((char) ('a' + i - 10));
            }
        }

        // Shuffle the list
        Collections.shuffle(characters);

        // Append unique characters to the secret code
        for (int i = 0; i < numCharacters; i++) {
            secretCode.append(characters.get(i % numCharacters));
        }

        return secretCode.toString();
    }

    public static int countBulls(String secretCode, String guess) {
        int bulls = 0;

        for (int i = 0; i < secretCode.length(); i++) {
            if (secretCode.charAt(i) == guess.charAt(i)) {
                bulls++;
            }
        }

        return bulls;
    }

    public static int countCows(String secretCode, String guess) {
        int cows = 0;
        int[] secretCodeChars = new int[36];
        int[] guessChars = new int[36];

        for (int i = 0; i < secretCode.length(); i++) {
            char secretChar = secretCode.charAt(i);
            char guessChar = guess.charAt(i);

            if (secretChar >= '0' && secretChar <= '9') {
                secretCodeChars[secretChar - '0']++;
            } else {
                secretCodeChars[secretChar - 'a' + 10]++;
            }

            if (guessChar >= '0' && guessChar <= '9') {
                guessChars[guessChar - '0']++;
            } else {
                guessChars[guessChar - 'a' + 10]++;
            }
        }

        for (int i = 0; i < 36; i++) {
            cows += Math.min(secretCodeChars[i], guessChars[i]);
        }

        return cows;
    }

    public static String digitsRange(int numCharacters) {
        if (numCharacters < 10) {
            return "0-" + (numCharacters);
        } else {
            return "0-9";
        }
    }

    public static String alphabetsRange(int numCharacters) {
        if (numCharacters == 10) {
            char startChar = 'a';
            return String.format("%c", startChar);
        } else {
            char startChar = 'a';
            char endChar = (char) (startChar + Math.min(25, numCharacters - 11));
            return String.format("%c-%c", startChar, endChar);
        }
    }
}
