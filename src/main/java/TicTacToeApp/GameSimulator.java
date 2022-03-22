package TicTacToeApp;

import TicTacToeApp.Objects.Gameboard;
import TicTacToeApp.Objects.Step;
import TicTacToeApp.Parsers.Parser;
import TicTacToeApp.Parsers.ParserJSON;
import TicTacToeApp.Parsers.ParserXML;

import java.util.Scanner;

class GameSimulator {

    public static void toSimulateGame()
            throws InterruptedException {
        Gameboard gameboard = new Gameboard();

        toReadTheLog();

        Thread.sleep(1000);
        System.out.print("Введите имя 1 игрока: ");
        Thread.sleep(2000);
        System.out.println(Parser.players.get(0).getName());
        System.out.println();
        Thread.sleep(1000);
        System.out.print("Введите имя 2 игрока: ");
        Thread.sleep(2000);
        System.out.println(Parser.players.get(1).getName());
        System.out.println();
        gameboard.toPrintField();
        Thread.sleep(500);

        for (Step step : Parser.stepsToRead) {
            System.out.println("Ход игрока " + Parser.players.get(step.getPlayerId() - 1).getName());
            System.out.println("Выберите ячейку(1-9): ");
            Thread.sleep(1000);
            System.out.println(step.getCell());
            gameboard.setCellForSimulating(step.getCell(), Parser.players.get(step.getPlayerId() - 1).getSymbol());
            Thread.sleep(500);
            gameboard.toPrintField();
        }

        if (Parser.gameResult.toString().contains("Draw!")) {
            System.out.println("Ничья");
        } else {
            int winnerIndex = (Parser.stepsToRead.size() + 1) % 2;
            System.out.println(Parser.players.get(winnerIndex).getName() + " победил");
        }

    }

    public static void toReadTheLog() {
        Scanner in = new Scanner(System.in);
        Parser parserJSON;
        Parser parserXML;

        System.out.println("Выберите лог, по которому будет воспроизведена игра: ");
        System.out.println("1 - XML");
        System.out.println("2 - JSON");

        int menuItemNum;
        menuItemNum = in.nextInt();

        switch (menuItemNum) {
            case (1) -> {
                parserXML = new ParserXML();
                parserXML.toReadFile();
            }
            case (2) -> {
                parserJSON = new ParserJSON();
                parserJSON.toReadFile();
            }
            default -> {
            }
        }
    }

}
