import org.junit.jupiter.api.Test;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

public class WordRecommenderTest {

    @Test
    public void testGetSimilarity() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");

        // Examples from the assignment.
        assertEquals(2.5, recommender.getSimilarity("oblige", "oblivion"), 0.000001);
        assertEquals(1.5, recommender.getSimilarity("aghast", "gross"), 0.000001);

        // Identical words and words with no matching characters.
        assertEquals(3.0, recommender.getSimilarity("cat", "cat"), 0.000001);
        assertEquals(0.0, recommender.getSimilarity("abc", "xyz"), 0.000001);
    }

    @Test
    public void testGetWordSuggestions() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");

        ArrayList<String> suggestions = recommender.getWordSuggestions("morbit", 2, 0.5, 4);

        assertEquals(4, suggestions.size());
        assertEquals("morbid", suggestions.get(0));

        // These three words have equal similarity scores.
        assertTrue(suggestions.contains("forbid"));
        assertTrue(suggestions.contains("hobbit"));
        assertTrue(suggestions.contains("sorbet"));
    }

    @Test
    public void testGetWordSuggestionsTopOne() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");

        ArrayList<String> suggestions = recommender.getWordSuggestions("morbit", 2, 0.5, 1);

        assertEquals(1, suggestions.size());
        assertEquals("morbid", suggestions.get(0));
    }

    @Test
    public void testGetWordSuggestionsNoMatches() {
        WordRecommender recommender = new WordRecommender("engDictionary.txt");

        ArrayList<String> suggestions = recommender.getWordSuggestions("sleepyyyyyyyyyyyyyyyy", 2, 0.5, 4);

        assertNotNull(suggestions);
        assertTrue(suggestions.isEmpty());
    }
}
