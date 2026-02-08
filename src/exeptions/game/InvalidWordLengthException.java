package exeptions.game;

import exeptions.base.WordleException;

public class InvalidWordLengthException extends WordleException {
    public InvalidWordLengthException() {
        super("Слово должно быть из 5 букв!");
    }
}
