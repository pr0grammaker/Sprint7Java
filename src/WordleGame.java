import exeptions.base.WordleException;
import exeptions.game.EmptyInputException;
import exeptions.game.InvalidCharactersException;
import exeptions.game.InvalidWordLengthException;
import exeptions.game.WordNotFoundInDictionaryException;
import exeptions.system.GameLogicException;

import java.io.PrintWriter;
import java.util.*;

public class WordleGame {
    private final WordleDictionary dictionary;
    private int COUNT_STEPS = 6;
    private final String correctAnswer;
    private PrintWriter printWriter;

    private final List<String> previousWords = new ArrayList<>();
    private final List<String> previousHints = new ArrayList<>();

    private final Set<Character> mustHave = new HashSet<>();
    private final Set<Character> mustNotHave = new HashSet<>();
    private final Map<Integer, Character> correctPositions = new HashMap<>();
    private final Map<Integer, Set<Character>> incorrectPositions = new HashMap<>();

    public WordleGame(WordleDictionary dictionary, String correctAnswer, PrintWriter printWriter) {
        this.dictionary = dictionary;
        this.correctAnswer = correctAnswer;
        this.printWriter = printWriter;
    }


    public WordleDictionary getDictionary() {
        return dictionary;
    }

    public String getCorrectAnswer() {
        return correctAnswer;
    }

    public int getCOUNT_STEPS() {
        return COUNT_STEPS;
    }

    public void validWord(String answer) throws WordleException {
        if (answer == null || answer.isBlank()) {
            throw new EmptyInputException();
        }

        if (answer.length() != 5) {
            throw new InvalidWordLengthException();
        }

        if (!answer.matches("[а-яё]+")) {
            throw new InvalidCharactersException();
        }

        if (!getDictionary().checkWord(answer)) {
            throw new WordNotFoundInDictionaryException(answer);
        }
    }

    public String makeStep(String answer) throws WordleException {
        if (COUNT_STEPS <= 0) {
            throw new GameLogicException("Попытки ушли в минус! Логика игры сломана!");
        }
        COUNT_STEPS--;

        String hint = giveHint(answer);
        previousWords.add(answer);
        previousHints.add(hint);
        updateHintKnowledge(answer, hint);

        return hint;
    }

    public boolean isGameOver() {
        if (COUNT_STEPS < 0) {
            throw new GameLogicException("Попытки ушли в минус!");
        }
        return COUNT_STEPS <= 0;
    }

    public boolean correctAnswer(String answer) {
        return correctAnswer.equals(answer);
    }

    public String giveHint(String answer) throws WordleException {
        if (answer == null || answer.length() != correctAnswer.length()) {
            throw new WordleException("Слово для подсказки должно быть длиной " + correctAnswer.length());
        }

        StringBuilder result = new StringBuilder("-----");
        answer = answer.toLowerCase().replace('ё', 'е');

        String realAnswer = correctAnswer;

        boolean[] usedInAnswer = new boolean[5];

        // +
        for (int i = 0; i < 5; i++) {
            if (answer.charAt(i) == realAnswer.charAt(i)) {
                result.setCharAt(i, '+');
                usedInAnswer[i] = true;
            }
        }

        // ^
        for (int i = 0; i < 5; i++) {
            if (result.charAt(i) == '+') continue;

            for (int j = 0; j < 5; j++) {
                if (!usedInAnswer[j] && answer.charAt(j) == realAnswer.charAt(j)) {
                    result.setCharAt(i, '^');
                    usedInAnswer[i] = true;
                    break;
                }
            }
        }

        return result.toString();

//        for (int i = 0; i < correctAnswer.length(); i++) {
//            char a = answer.charAt(i);
//            char c = correctAnswer.charAt(i);
//
//            if (a == c) {
//                result.append("+");
//            } else if (correctAnswer.contains(String.valueOf(a))) {
//                result.append("^");
//            } else {
//                result.append("-");
//            }
//        }
//        return result.toString();
    }

    private void updateHintKnowledge(String answer, String hint) {
        for (int i = 0; i < hint.length(); i++) {
            char h = hint.charAt(i);
            char a = answer.charAt(i);

            if (h == '+') {
                mustHave.add(a);
                correctPositions.put(i, a);
            } else if (h == '^') {
                mustHave.add(a);
                if (!incorrectPositions.containsKey(i)) {
                    incorrectPositions.put(i, new HashSet<>());
                }
                incorrectPositions.get(i).add(a);
            } else if (h == '-') {
                if (!mustNotHave.contains(a)) {
                    mustNotHave.add(a);
                }
            }
        }
    }

    public String getHintWord() {
        List<String> candidates = new ArrayList<>();

        for (String word : getDictionary().getWords()) {
            boolean valid = true;

            for (Map.Entry<Integer, Character> entry : correctPositions.entrySet()) {
                if (word.charAt(entry.getKey()) != entry.getValue()) {
                    valid = false;
                    break;
                }
            }
            if (!valid) continue;

            // проверка запрещённых букв
            for (char c : mustNotHave) {
                if (word.indexOf(c) >= 0) {
                    valid = false;
                    break;
                }
            }
            if (!valid) continue;

            // проверка обязательных букв
            for (char c : mustHave) {
                if (word.indexOf(c) < 0) {
                    valid = false;
                    break;
                }
            }

            for (Map.Entry<Integer, Set<Character>> entry : incorrectPositions.entrySet()) {
                if (entry.getValue().contains(word.charAt(entry.getKey()))) {
                    valid = false;
                    break;
                }
            }

            if (valid) candidates.add(word);

        }
        if (candidates.isEmpty()) return getDictionary().getRandomWord();
        return candidates.get(new Random().nextInt(candidates.size()));

    }
}

