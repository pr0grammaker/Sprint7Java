import exeptions.system.DictionaryFileNotFoundException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class WordleDictionaryLoader {
    public static WordleDictionary loadFromFile(String filename) {
        List<String> words = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filename),
                StandardCharsets.UTF_8))) {
            String line;
            while ((line = br.readLine()) != null) {
                String normalized = line.trim().toLowerCase().replace('ё', 'е');
                if (normalized.length() == 5) {
                    words.add(normalized);
                }
            }
        } catch (IOException e) {
            throw new DictionaryFileNotFoundException();
        }
        return new WordleDictionary(words);
    }
}