package exeptions.system;

public class DictionaryFileNotFoundException extends RuntimeException {
    public DictionaryFileNotFoundException() {
        super("Файл словаря не найден!");
    }
}

