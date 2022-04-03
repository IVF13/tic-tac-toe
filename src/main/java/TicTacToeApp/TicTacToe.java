package TicTacToeApp;

import TicTacToeApp.Models.Gameboard;
import TicTacToeApp.Models.Player;
import TicTacToeApp.Models.Step;
import TicTacToeApp.Parsers.ParserTXT;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicTacToe {
    private static List<Step> stepsToWrite;
    private static List<Player> players;

    public static void toPlayTicTacToeInConsole() throws IOException, XMLStreamException {
        stepsToWrite = new ArrayList<>();
        players = new ArrayList<>();
        int finishChecker = 0;
        int step = 0;

        Gameboard gameboard = new Gameboard();

        players = toIntroduce();

        while (finishChecker == 0) {

            System.out.println(gameboard.toPrintField());

            System.out.println("Ход игрока " + players.get(step % 2).getName());

            int cell = gameboard.setCellForConsoleGame(step, players);

            stepsToWrite.add(new Step(step + 1, (step % 2) + 1, cell));

            step++;

            finishChecker = toCheckWin(gameboard, step);

            if (finishChecker == 1 || finishChecker == 2) {
                int winnerIndex = (step + 1) % 2;
                toCongratulate(players.get(winnerIndex).getName(), finishChecker);
                gameboard.toPrintField();
                ParserTXT.toWriteFile(players.get(winnerIndex).getName(), finishChecker);
                Logger.toWriteTheLog(players, stepsToWrite, finishChecker);
                toRestartTheGameInConsole();
            }

        }
    }

    public static List<Player> toIntroduce() {
        Scanner in = new Scanner(System.in);

        System.out.print("Введите имя 1 игрока: ");
        players.add(0, new Player(1, in.next(), "X"));
        System.out.println();
        System.out.print("Введите имя 2 игрока: ");
        players.add(1, new Player(2, in.next(), "O"));
        System.out.println();

        return players;
    }

    public static int toCheckWin(Gameboard gameboard, int steps) {

        for (int j = 0; j < gameboard.getField().length; j++) {
            if (gameboard.getField()[j][0].equals(gameboard.getField()[j][1])
                    && gameboard.getField()[j][0].equals(gameboard.getField()[j][2]) ||
                    gameboard.getField()[0][j].equals(gameboard.getField()[1][j])
                            && gameboard.getField()[0][j].equals(gameboard.getField()[2][j]) ||
                    gameboard.getField()[0][0].equals(gameboard.getField()[1][1])
                            && gameboard.getField()[0][0].equals(gameboard.getField()[2][2]) ||
                    gameboard.getField()[2][0].equals(gameboard.getField()[1][1])
                            && gameboard.getField()[2][0].equals(gameboard.getField()[0][2])) {
                return 1;
            }
        }

        return steps == 9 ? 2 : 0;
    }

    public static void toCongratulate(String playerName, int finishChecker) {
        if (finishChecker == 2) {
            System.out.println("Ничья");
        } else {
            System.out.println(playerName + " победил");
        }

    }

    public static void toRestartTheGameInConsole() throws IOException, XMLStreamException {
        Scanner in = new Scanner(System.in);

        System.out.println("Вы хотите сыграть заново? (+/-)");
        if (in.next().equals("+")) {
            toPlayTicTacToeInConsole();
        }

    }

}
