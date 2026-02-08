import exeptions.system.DictionaryEmptyException;

import java.util.List;
import java.util.Random;

public class WordleDictionary {
    private final List<String> words;
    private final Random random = new Random();
    private WordleDictionary dictionary;

    public WordleDictionary(List<String> words) {
        this.words = words;
    }

    public List<String> getWords() {
        return words;
    }

    public boolean checkWord(String word) {
        if (word == null) return false;
        if (word.length() != 5) return false;
        return words.contains(word);
    }

    public String getRandomWord() {
        if (words.isEmpty()) {
            throw new DictionaryEmptyException();
        }
        return words.get(random.nextInt(words.size()));
    }
}