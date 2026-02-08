package exeptions.system;

public class DictionaryEmptyException extends RuntimeException {
    public DictionaryEmptyException() {
        super("Словарь пуст! Игра невозможна!");
    }
}
