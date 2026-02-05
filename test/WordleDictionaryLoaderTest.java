import exeptions.system.DictionaryFileNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class WordleDictionaryLoaderTest {

    private Path tempFile;

    @BeforeEach
    void setUp() throws IOException {
        tempFile = Files.createTempFile("wordle_test", ".txt");
        tempFile.toFile().deleteOnExit();
    }

    @Test
    void testLoadFromFile_correctWordsLoaded() throws IOException {

        try (BufferedWriter writer = Files.newBufferedWriter(tempFile)) {
            writer.write("Привет\n");    // длина != 5, должно пропуститься
            writer.write("ёжикё\n");     // длина == 5, ё заменяется на е
            writer.write("APPLE\n");     // длина == 5, нормализация к нижнему регистру
            writer.write("word\n");      // длина != 5, пропускается
        }

        WordleDictionary dictionary = WordleDictionaryLoader.loadFromFile(tempFile.toString());
        List<String> words = dictionary.getWords();

        assertEquals(2, words.size());
        assertTrue(words.contains("ежике"));
        assertTrue(words.contains("apple"));
    }


    @Test
    public void testLoadFromFile_fileNotFound_throwsException() {
        String fakeFile = "non_existing_file.txt";

        try {
            WordleDictionaryLoader.loadFromFile(fakeFile);

            fail("Ожидалось DictionaryFileNotFoundException, но исключение не было выброшено");

        } catch (DictionaryFileNotFoundException e) {
            // Если исключение тест проходит ничего не делаем
        }
    }

    @Test
    void testLoadFromFile_emptyFile_returnsEmptyDictionary() throws IOException {

        WordleDictionary dictionary = WordleDictionaryLoader.loadFromFile(tempFile.toString());
        List<String> words = dictionary.getWords();

        assertNotNull(words);
        assertTrue(words.isEmpty());
    }
}
