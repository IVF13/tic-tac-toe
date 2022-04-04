package TicTacToeApp;

import TicTacToeApp.Models.Gameboard;
import TicTacToeApp.Models.Step;
import TicTacToeApp.Parsers.Parser;

class GameSimulator {

    public static void toSimulateGameInConsole()
            throws InterruptedException {
        Gameboard gameboard = new Gameboard();

        Logger.toReadTheLog();

        Thread.sleep(1000);
        System.out.print("Enter the name of the first player: ");
        Thread.sleep(2000);
        System.out.println(Parser.players.get(0).getName());
        System.out.println();
        Thread.sleep(1000);
        System.out.print("Enter the name of the second player: ");
        Thread.sleep(2000);
        System.out.println(Parser.players.get(1).getName());
        System.out.println();
        gameboard.toPrintField();
        Thread.sleep(500);

        for (Step step : Parser.stepsToRead) {
            System.out.println(Parser.players.get(step.getPlayerId() - 1).getName()+" player's turn");
            System.out.println("Select cell(1-9): ");
            Thread.sleep(1000);
            System.out.println(step.getCell());
            gameboard.setCellForSimulating(step.getCell(), Parser.players.get(step.getPlayerId() - 1).getSymbol());
            Thread.sleep(500);
            System.out.println(gameboard.toPrintField());
        }

        if (Parser.gameResult.toString().contains("Draw!")) {
            System.out.println("Draw!");
        } else {
            int winnerIndex = (Parser.stepsToRead.size() + 1) % 2;
            System.out.println(Parser.players.get(winnerIndex).getName() + " won");
        }

    }


}
