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
      // TODO: change this!
      return 0.0;
    }
  
    public ArrayList<String> getWordSuggestions(String word, int tolerance, double commonPercent, int topN) {
      // TODO: change this!
      return null;
    }
  
    // You can of course write other methods as well.
  }