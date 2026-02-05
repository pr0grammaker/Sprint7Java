package exeptions.game;

import exeptions.base.WordleException;

public class WordNotFoundInDictionaryException extends WordleException {
    public WordNotFoundInDictionaryException(String word) {
        super("Слова нет в словаре: " + word);
    }
}
