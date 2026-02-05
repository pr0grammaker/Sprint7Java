import exeptions.base.WordleException;
import exeptions.game.WordNotFoundInDictionaryException;
import exeptions.system.GameLogicException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

public class Wordle {

    public static void main(String[] args) {

        try (Scanner sc = new Scanner(System.in);
             PrintWriter printWriter = new PrintWriter(new FileWriter("log.txt"), true)) {


            WordleDictionary dictionary = WordleDictionaryLoader.loadFromFile("dictionary.txt");
            printWriter.println("Словарь загружен. Количество слов: " + dictionary.getWords().size());

            String correctWord = dictionary.getRandomWord();

            System.out.println(dictionary.getWords());//
            System.out.println("Количество слов в словаре: " + dictionary.getWords().size());//

            WordleGame wordleGame = new WordleGame(dictionary, correctWord, printWriter);
            System.out.println("Добро пожаловать в игру WordleGame!");

            while (!wordleGame.isGameOver()) {
                System.out.println(correctWord);//
                System.out.println("Введите слово которое мы загадали (5 букв):");
                String word = sc.nextLine().trim().toLowerCase().replace('ё', 'е');


                if(word.isEmpty()){
                    word = wordleGame.getHintWord();
                    System.out.println("Подсказка от компьютера: " + word);
                }

                printWriter.println("Игрок ввел слово -> " + word);

                try {
                    wordleGame.validWord(word);
                    String hint = wordleGame.makeStep(word);
                    System.out.println(hint);

                    printWriter.println("Подсказка: " + hint);

                    if (wordleGame.correctAnswer(word)) {
                        System.out.println("Вы угадали! Победа!");
                        printWriter.println("Игрок угадал слово!");
                        return;
                    }

                    System.out.printf("Осталось попыток -> %d \n", wordleGame.getCOUNT_STEPS());
                } catch (WordNotFoundInDictionaryException e) {
                    System.out.println(e.getMessage());
                    printWriter.println("Некорректный ввод: " + e.getMessage());
                } catch (WordleException e) {
                    throw new RuntimeException(e);
                }
            }
            if (wordleGame.isGameOver()) {
                System.out.println("Вам не удалось угадать слово! К сожалению, вы проиграли.");
                System.out.printf("Загаданное слово было %s \n", wordleGame.getCorrectAnswer());
                printWriter.println("Игрок не угадал слово. Игра окончена.");
            }

        } catch (IOException e) {
            System.out.println(e.getMessage());
        } catch (GameLogicException e) {
            System.out.println("Ошибка программы. Игра завершена.");
        }

    }
}
