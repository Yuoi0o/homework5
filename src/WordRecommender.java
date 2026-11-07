import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.HashSet;

public class WordRecommender {

    private ArrayList<String> dictionary;

    public WordRecommender(String dictionaryFile) {
      dictionary = new ArrayList<>();
      Scanner s = null;
      HashSet<String> addedWords = new HashSet<>();

      try{
          s = new Scanner(new FileInputStream(dictionaryFile));

          while (s.hasNextLine()){
              String word = s.nextLine();

              if (!addedWords.contains(word)){
                  addedWords.add(word);
                  dictionary.add(word);
              }
          }
      } catch (IOException e){
          throw new IllegalArgumentException(e);
      } finally{
          if (s != null){
              s.close();
          }
      }
    }
  
    public double getSimilarity(String word1, String word2) {
      int leftSimilarity = 0;
      int rightSimilarity = 0;

      int minDistance = Math.min(word1.length(), word2.length());

        // Left similarity refers to the number of common characters between two Strings when we left-align both Strings and compare characters in all positions
      for (int i = 0; i < minDistance; i++) {
          if (word1.charAt(i) == word2.charAt(i)) {
              leftSimilarity++;
          }

          // Right similarity refers to the number of common characters between two Strings when we right-align both Strings and compare characters in all positions
          if (word1.charAt(word1.length()-1-i) == word2.charAt(word2.length()-1-i)){
              rightSimilarity++;
          }
      }
      return (leftSimilarity + rightSimilarity) / 2.0;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      ArrayList<String> candidates = new ArrayList<>();
      ArrayList<Double> score = new ArrayList<>();
      ArrayList<String> suggestions = new ArrayList<>();

      // To calculate the percentage of characters in common between two Strings a and b
      // you should create a set of the letters in a called aSet and a set of the letters in b called bSet
      // Reminder: a set does not contain duplicates (hence, we use HashSet)
      HashSet<Character> letters = new HashSet<>();
      for (int i = 0; i < word.length(); i++){
          letters.add(word.charAt(i));
      }

      for (String candidate : dictionary){
          // A String candidate is a valid candidate for replacing word if both
          // 1. the difference in length between candidate and word is at most tolerance characters
          if (Math.abs(candidate.length() - word.length()) <= tolerance){
              HashSet<Character> candidateLetters = new HashSet<>();

              for (int i = 0; i < candidate.length(); i++){
                  candidateLetters.add(candidate.charAt(i));
              }

              // The percentage of characters in common between 2 sets is: # of characters in aSet AND in bSet / # of characters in aSet OR in bSet
              int commonLetters = 0;
              for (char letter : letters){
                  if (candidateLetters.contains(letter)){
                      commonLetters++;
                  }
              }

              int totalLetters = letters.size() + candidateLetters.size() - commonLetters;

              double percentage = (double) commonLetters / totalLetters;

              // 2. candidate and word have at least commonPercent % of their characters in common
              if (percentage >= commonPercent){
                  candidates.add(candidate);
                  score.add(getSimilarity(word,candidate));
              }
          }
      }

      // getting the candidate that is the most similar to the misspelled word
      // Then, get the candidate that is the second-most similar. Then the third-most
      // Repeat until the N-most similar
      while (suggestions.size() < topN && !candidates.isEmpty()){
          int index = 0;

          for (int i = 1; i < candidates.size(); i++){
              if (score.get(i) > score.get(index)){
                  index = i;
              }
          }

          suggestions.add(candidates.remove(index));
          score.remove(index);
      }
      return suggestions;
    }
  
    // You can of course write other methods as well.
  }