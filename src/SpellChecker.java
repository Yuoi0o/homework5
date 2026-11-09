import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Scanner;

/**
 * Checks the words in a text file against a user-selected dictionary and
 * writes the user's corrections to a separate file.
 */
public class SpellChecker {
    private static final int LENGTH_TOLERANCE = 2;
    private static final double MINIMUM_COMMON_PERCENT = 0.5;
    private static final int MAXIMUM_SUGGESTIONS = 4;
    private static final String INPUT_SUFFIX = ".txt";
    private static final String OUTPUT_SUFFIX = "_chk.txt";

    // Use this field everytime you need to read user input
    private Scanner inputReader; // DO NOT MODIFY

    /** Creates the scanner used for all keyboard input. */
    public SpellChecker() {
        inputReader = new Scanner(System.in); // DO NOT MODIFY - must be included in this method
    }

    /** Runs one spell-checking session, then closes the keyboard scanner. */
    public void start() {
        HashSet<String> dictionary = new HashSet<String>();
        String dictionaryFilename = readDictionary(dictionary);
        WordRecommender recommender = new WordRecommender(dictionaryFilename);
        checkFile(dictionary, recommender);
        inputReader.close();  // DO NOT MODIFY - must be the last line of this method!
    }

    /**
     * Repeatedly asks for a dictionary until it can be read successfully.
     *
     * @param dictionary the set to fill with correctly spelled words
     * @return the filename of the successfully read dictionary
     */
    private String readDictionary(HashSet<String> dictionary) {
        while (true) {
            System.out.printf(Util.DICTIONARY_PROMPT);
            String filename = inputReader.nextLine().trim();

            try (Scanner dictionaryReader = new Scanner(new File(filename))) {
                dictionary.clear();
                while (dictionaryReader.hasNext()) {
                    dictionary.add(dictionaryReader.next());
                }
                // Scanner records read failures instead of throwing them.
                if (dictionaryReader.ioException() != null) {
                    throw dictionaryReader.ioException();
                }
                System.out.printf(Util.DICTIONARY_SUCCESS_NOTIFICATION, filename);
                return filename;
            } catch (IOException exception) {
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }

    /**
     * Opens the input and output files, retrying if a file cannot be opened.
     *
     * @param dictionary the set of correctly spelled words
     * @param recommender the object that supplies replacement suggestions
     */
    private void checkFile(HashSet<String> dictionary, WordRecommender recommender) {
        boolean finished = false;
        while (!finished) {
            System.out.printf(Util.FILENAME_PROMPT);
            String inputFilename = inputReader.nextLine().trim();
            String outputFilename = getOutputFilename(inputFilename);

            // Open the input first so a missing input does not create an output.
            try (Scanner fileReader = new Scanner(new File(inputFilename));
                 PrintWriter outputWriter = new PrintWriter(outputFilename)) {
                System.out.printf(Util.FILE_SUCCESS_NOTIFICATION,
                        inputFilename, outputFilename);
                writeCheckedWords(fileReader, outputWriter, dictionary, recommender);
                if (fileReader.ioException() != null) {
                    throw fileReader.ioException();
                }
                if (outputWriter.checkError()) {
                    throw new IOException("Could not write the corrected file.");
                }
                finished = true;
            } catch (IOException exception) {
                System.out.printf(Util.FILE_OPENING_ERROR);
            }
        }
    }

    /**
     * Builds the output filename, preserving the input file's directory.
     *
     * @param inputFilename the path entered by the user
     * @return the same path with its final .txt replaced by _chk.txt, or with
     *         _chk.txt appended if the path does not end in .txt
     */
    private String getOutputFilename(String inputFilename) {
        if (inputFilename.endsWith(INPUT_SUFFIX)) {
            return inputFilename.substring(0,
                    inputFilename.length() - INPUT_SUFFIX.length()) + OUTPUT_SUFFIX;
        }
        return inputFilename + OUTPUT_SUFFIX;
    }

    /**
     * Checks every word and writes the result using a single space between words.
     * Line breaks in the input count as word boundaries, as the assignment allows.
     *
     * @param fileReader the scanner reading the original text
     * @param outputWriter the writer for the corrected text
     * @param dictionary the set of correctly spelled words
     * @param recommender the object that supplies replacement suggestions
     */
    private void writeCheckedWords(Scanner fileReader, PrintWriter outputWriter,
                                   HashSet<String> dictionary, WordRecommender recommender) {
        boolean firstWord = true;
        while (fileReader.hasNext()) {
            String word = fileReader.next();
            if (!dictionary.contains(word)) {
                word = correctWord(word, recommender);
            }
            if (!firstWord) {
                outputWriter.print(" ");
            }
            outputWriter.print(word);
            firstWord = false;
        }
    }

    /**
     * Shows a misspelling and its suggestions, then reads a valid correction choice.
     * A manually entered replacement is used directly without checking it again.
     *
     * @param word the misspelled word
     * @param recommender the object that supplies replacement suggestions
     * @return the original word or the user's chosen replacement
     */
    private String correctWord(String word, WordRecommender recommender) {
        System.out.printf(Util.MISSPELL_NOTIFICATION, word);
        ArrayList<String> suggestions = recommender.getWordSuggestions(word,
                LENGTH_TOLERANCE, MINIMUM_COMMON_PERCENT, MAXIMUM_SUGGESTIONS);

        if (suggestions.isEmpty()) {
            System.out.printf(Util.NO_SUGGESTIONS);
            System.out.printf(Util.TWO_OPTION_PROMPT);
        } else {
            System.out.printf(Util.FOLLOWING_SUGGESTIONS);
            for (int i = 0; i < suggestions.size(); i++) {
                System.out.printf(Util.SUGGESTION_ENTRY, i + 1, suggestions.get(i));
            }
            System.out.printf(Util.THREE_OPTION_PROMPT);
        }

        while (true) {
            String choice = inputReader.nextLine().trim();
            if (choice.equals("a")) {
                return word;
            } else if (choice.equals("t")) {
                System.out.printf(Util.MANUAL_REPLACEMENT_PROMPT);
                return inputReader.nextLine().trim();
            } else if (choice.equals("r") && !suggestions.isEmpty()) {
                return chooseSuggestion(suggestions);
            }
            System.out.printf(Util.INVALID_RESPONSE);
        }
    }

    /**
     * Reads a suggestion number, retrying for text or out-of-range numbers.
     *
     * @param suggestions the numbered suggestions shown to the user
     * @return the selected suggestion
     */
    private String chooseSuggestion(ArrayList<String> suggestions) {
        System.out.printf(Util.AUTOMATIC_REPLACEMENT_PROMPT);
        while (true) {
            String response = inputReader.nextLine().trim();
            try {
                int number = Integer.parseInt(response);
                if (number >= 1 && number <= suggestions.size()) {
                    return suggestions.get(number - 1);
                }
            } catch (NumberFormatException exception) {
                // Invalid text and oversized integers both need another attempt.
            }
            System.out.printf(Util.INVALID_RESPONSE);
        }
    }
}
