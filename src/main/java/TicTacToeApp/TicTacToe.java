package TicTacToeApp;

import TicTacToeApp.Objects.Gameboard;
import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import TicTacToeApp.Parsers.Parser;
import TicTacToeApp.Parsers.ParserXML;

import javax.xml.stream.XMLStreamException;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TicTacToe {
    private static List<Step> stepsToWrite = new ArrayList<>();
    private static List<Player> players = new ArrayList<>();

    public static void toPlayTicTacToe() throws IOException, XMLStreamException {
        int finishChecker = 0;
        int step = 0;

        Gameboard gameboard = new Gameboard();

        players = toIntroduce();

        while (finishChecker == 0) {

            gameboard.toPrintField();

            System.out.println("Ход игрока " + players.get(step % 2).getName());

            int cell = gameboard.setCell(step, players);

            stepsToWrite.add(new Step(step + 1, (step % 2) + 1, cell));

            step++;

            finishChecker = toCheckWin(gameboard, step);

            if (finishChecker == 1 || finishChecker == 2) {
                Parser parserXML = new ParserXML();
                toCongratulate(players.get((step + 1) % 2).getName(), finishChecker);
                toWriteTXTScores(players.get((step + 1) % 2).getName(), finishChecker);
                gameboard.toPrintField();
                parserXML.toWriteFile(finishChecker, stepsToWrite, players);
                toRestartTheGame();
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

    public static void toWriteTXTScores(String playerName, int finishChecker) throws IOException {
        File file = new File("src/main/resources/scores.txt");
        FileWriter writer = new FileWriter(file, true);
        if (finishChecker == 2) {
            writer.write("Ничья\n");
        } else {
            writer.write(playerName + " победил\n");
        }
        writer.flush();
        writer.close();
    }

    public static void toRestartTheGame() throws IOException, XMLStreamException {
        Scanner in = new Scanner(System.in);
        System.out.println("Вы хотите сыграть заново? (+/-)");
        if (in.next().equals("+")) {
            toPlayTicTacToe();
        }

    }

    public static void toCongratulate(String playerName, int finishChecker) {
        if (finishChecker == 2) {
            System.out.println("Ничья");
        } else {
            System.out.println(playerName + " победил");
        }
    }

}
