import exeptions.system.DictionaryEmptyException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class WordleDictionaryTest {

    private WordleDictionary dictionary;
    private List<String> words;

    @BeforeEach
    public void start() {

        words = new ArrayList<>(Arrays.asList("молот","столб"));
        dictionary = new WordleDictionary(words);
    }

    @Test
    public void testGetWords() {
        if (!dictionary.getWords().contains("молот") || !dictionary.getWords().contains("столб")) {
            fail("Слова не сохранились в словаре!");
        }
    }

    @Test
    public void testCheckWord() {
        if (!dictionary.checkWord("молот")) {
            fail("Слово 'молот' должно быть в словаре!");
        }
        if (dictionary.checkWord("berry")) {
            fail("Слово 'berry' не должно быть в словаре!");
        }
        if (dictionary.checkWord("мол")) {
            fail("Слово длиной не 5 не должно проходить!");
        }
        if (dictionary.checkWord(null)) {
            fail("null не должно проходить!");
        }
    }

    @Test
    public void testGetRandomWord() {
        String randomWord = dictionary.getRandomWord();
        if (!words.contains(randomWord)) {
            fail("Случайное слово должно быть из словаря!");
        }
    }

    @Test
    public void testGetRandomWord_emptyDictionary_throwsException() {
        WordleDictionary emptyDictionary = new WordleDictionary(new ArrayList<>());

        try {
            emptyDictionary.getRandomWord();
            fail("Ожидалось DictionaryEmptyException, но исключение не было выброшено!");
        } catch (DictionaryEmptyException e) {
            // Всё верно, словарь пустой — тест проходит
        }
    }
}
