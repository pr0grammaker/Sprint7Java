package exeptions.game;

import exeptions.base.WordleException;

public class InvalidCharactersException extends WordleException {
    public InvalidCharactersException() {
        super("Можно вводить только русские буквы!");
    }
}

