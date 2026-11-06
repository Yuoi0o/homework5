
public class Util {

	public static final String DICTIONARY_PROMPT = "Please enter the name of a file to use as a dictionary.%n";
	public static final String FILE_OPENING_ERROR = "There was an error in opening that file.%n";
	public static final String DICTIONARY_SUCCESS_NOTIFICATION = "Using the dictionary at '%s'.%n";

	public static final String FILENAME_PROMPT = "Please enter the name of a file to be spell checked.%n";
	public static final String FILE_SUCCESS_NOTIFICATION = "Spell checking for '%s' will be output in '%s'.%n";

	public static final String MISSPELL_NOTIFICATION = "The word '%s' is misspelled.%n";
	public static final String FOLLOWING_SUGGESTIONS = "The following suggestions are available:%n";
	public static final String SUGGESTION_ENTRY = "%d. '%s'%n";
	public static final String THREE_OPTION_PROMPT = "Press 'r' to replace, 'a' to accept, and 't' to enter a replacement manually.%n";

	public static final String AUTOMATIC_REPLACEMENT_PROMPT = "Your word will be replaced with the suggestion you choose.%n"
			+ "Enter the number corresponding to the word that you want to use for replacement.%n";

	public static final String MANUAL_REPLACEMENT_PROMPT = "Please type the word that will be used as the replacement in the output file.%n";

	public static final String NO_SUGGESTIONS = "There are no suggestions in our dictionary for this word.%n";
	public static final String TWO_OPTION_PROMPT = "Press 'a' to accept, or press 't' to enter a replacement manually.%n";

	public static final String INVALID_RESPONSE = "Please choose one of the valid options.%n";
}
