package app.game;

import app.models.Gameboard;
import app.models.Player;
import app.models.Step;
import app.utils.GameConstants;
import app.utils.Logger;

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

            System.out.print(players.get(step % 2).getName() + " player's turn");

            int cell = gameboard.setCellForConsoleGame(step, players);

            stepsToWrite.add(new Step(step + 1, (step % 2) + 1, cell));

            step++;

            finishChecker = toCheckWin(gameboard, step);

            if (finishChecker == 1 || finishChecker == 2) {
                int winnerIndex = (step + 1) % 2;
                System.out.println(gameboard.toPrintField());
                toCongratulate(players.get(winnerIndex).getName(), finishChecker);
                Logger.toWriteTheLog(players, stepsToWrite, finishChecker);
                toRestartTheGameInConsole();
            }

        }
    }

    public static List<Player> toIntroduce() {
        Scanner in = new Scanner(System.in);

        System.out.print(GameConstants.ENTER_FIRST_NAME);
        players.add(0, new Player(1, in.next(), GameConstants.X));
        System.out.println();
        System.out.print(GameConstants.ENTER_SECOND_NAME);
        players.add(1, new Player(2, in.next(), GameConstants.O));
        System.out.println();

        return players;
    }

    public static int toCheckWin(Gameboard gameboard, int step) {

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

        return step == 9 ? 2 : 0;
    }

    public static void toCongratulate(String playerName, int finishChecker) {
        if (finishChecker == 2) {
            System.out.println(GameConstants.DRAW);
        } else {
            System.out.println(playerName + " won");
        }

    }

    public static void toRestartTheGameInConsole() throws IOException, XMLStreamException {
        Scanner in = new Scanner(System.in);

        System.out.println("Do you want to play again? (+/-)");
        if (in.next().equals("+")) {
            toPlayTicTacToeInConsole();
        }

    }

}
