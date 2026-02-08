package exeptions.game;

import exeptions.base.WordleException;

public class EmptyInputException extends WordleException {
    public EmptyInputException() {
        super("Вы ничего не ввели!");
    }
}
