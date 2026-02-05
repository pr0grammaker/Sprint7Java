import exeptions.base.WordleException;
import exeptions.game.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.fail;

public class WordleGameTest {

    private WordleDictionary dictionary;
    private WordleGame game;
    private PrintWriter pw;

    @BeforeEach
    public void setUp() {
        List<String> words = new ArrayList<>(Arrays.asList("молот","крыша","столб"));
        dictionary = new WordleDictionary(words);

        pw = new PrintWriter(System.out);

        // создаём игру с правильным словом "молот"
        game = new WordleGame(dictionary, "молот", pw);
    }

    @Test
    public void testValidWord() {
        try {
            game.validWord("молот"); // правильное слово
        } catch (WordleException e) {
            fail("Не должно было выбросить исключение для правильного слова");
        }
    }

    @Test
    public void testValidWordEmptyInput() {
        try {
            game.validWord("");
            fail("Ожидалось EmptyInputException");
        } catch (EmptyInputException e) {
            // всё норм
        } catch (WordleException e) {
            fail("Ожидалось EmptyInputException, а выброшено другое исключение");
        }
    }

    @Test
    public void testValidWordWrongLength() {
        try {
            game.validWord("мол"); // длина != 5
            fail("Ожидалось InvalidWordLengthException");
        } catch (InvalidWordLengthException e) {
            // всё верно
        } catch (WordleException e) {
            fail("Ожидалось InvalidWordLengthException, а выброшено другое исключение");
        }
    }

    @Test
    public void testValidWordInvalidCharacters() {
        try {
            game.validWord("abcde"); // латиница
            fail("Ожидалось InvalidCharactersException");
        } catch (InvalidCharactersException e) {
            // всё верно
        } catch (WordleException e) {
            fail("Ожидалось InvalidCharactersException, а выброшено другое исключение");
        }
    }

    @Test
    public void testValidWordNotInDictionary() {
        try {
            game.validWord("кранч"); // слово есть не в словаре
            fail("Ожидалось WordNotFoundInDictionaryException");
        } catch (WordNotFoundInDictionaryException e) {
            // всё верно
        } catch (WordleException e) {
            fail("Ожидалось WordNotFoundInDictionaryException, а выброшено другое исключение");
        }
    }

    @Test
    public void testMakeStepСorrectWord() {
        try {
            String hint = game.makeStep("молот"); // правильное слово
            if (!hint.equals("+++++")) {
                fail("Подсказка должна быть '+++++' для правильного слова");
            }
        } catch (WordleException e) {
            fail("Не должно было выбросить исключение для правильного слова");
        }
    }

    @Test
    public void testMakeStepIncorrectWord() {
        try {
            String hint = game.makeStep("столб"); // неправильное слово но в словаре
            if (hint.length() != 5) {
                fail("Подсказка должна быть длиной 5 символов");
            }
        } catch (WordleException e) {
            fail("Не должно было выбросить исключение для слова из словаря");
        }
    }

    @Test
    public void testIsGameOverAfterSixSteps() {
        try {
            for (int i = 0; i < 6; i++) {
                game.makeStep("столб");
            }
        } catch (WordleException e) {
            fail("Не должно выбросить исключение во время шагов");
        }

        if (!game.isGameOver()) {
            fail("Игра должна быть окончена после 6 попыток");
        }
    }



    @Test
    public void testCorrectAnswerTrue() {
        if (!game.correctAnswer("молот")) {
            fail("Слово должно быть правильным");
        }
    }

    @Test
    public void testCorrectAnswerFalse() {
        if (game.correctAnswer("столб")) {
            fail("Слово не должно быть правильным");
        }
    }



    @Test
    public void testGiveHintAllCorrect() {
        try {
            String hint = game.giveHint("молот");
            if (!hint.equals("+++++")) {
                fail("Если слово угадано полностью — должна быть подсказка +++++");
            }
        } catch (WordleException e) {
            fail("Не должно быть исключения");
        }
    }

    @Test
    public void testGiveHintPartialMatch() {
        try {
            String hint = game.giveHint("молок");

            if (!hint.equals("++++-")) {
                fail("Последняя буква отсутствует, ожидалось ++++-");
            }
        } catch (WordleException e) {
            fail("Не должно быть исключения");
        }
    }

    @Test
    public void testGiveHintWrongLength() {
        try {
            game.giveHint("моло");
            fail("Ожидалось исключение из-за неправильной длины");
        } catch (WordleException e) {
            // всё правильно
        }
    }


    @Test
    public void testKnowledgeAfterStep() {
        try {
            game.makeStep("столб"); // часть букв совпадёт

            String suggestion = game.getHintWord();

            if (suggestion == null || suggestion.length() != 5) {
                fail("getHintWord должен возвращать слово длиной 5");
            }

        } catch (WordleException e) {
            fail("Не должно быть исключения");
        }
    }


    @Test
    public void testGetHintWordReturnsDictionaryWord() {
        try {
            game.makeStep("столб");

            String hintWord = game.getHintWord();

            if (!dictionary.getWords().contains(hintWord)) {
                fail("Подсказка должна быть словом из словаря");
            }

        } catch (WordleException e) {
            fail("Не должно быть исключения");
        }
    }







}
