package app.utils;

import app.models.Gameboard;
import app.models.Step;
import app.parsers.Parser;

public class GameSimulator {

    public static void toSimulateGameInConsole() throws InterruptedException {
        Gameboard gameboard = new Gameboard();

        Logger.toReadTheLog();

        System.out.print(GameConstants.ENTER_FIRST_NAME);
        Thread.sleep(2000);
        System.out.println(Parser.players.get(0).getName() + "\n");
        Thread.sleep(1000);
        System.out.print(GameConstants.ENTER_SECOND_NAME);
        Thread.sleep(2000);
        System.out.println(Parser.players.get(1).getName() + "\n");
        System.out.println(gameboard.toPrintField());
        Thread.sleep(500);

        for (Step step : Parser.stepsToRead) {
            System.out.println(Parser.players.get(step.getPlayerId() - 1).getName() + " player's turn");
            System.out.print(GameConstants.SELECT_CELL);
            Thread.sleep(1000);
            System.out.println(step.getCell());
            gameboard.setCellForSimulating(step.getCell(), Parser.players.get(step.getPlayerId() - 1).getSymbol());
            Thread.sleep(500);
            System.out.println(gameboard.toPrintField());
        }

        if (Parser.gameResult.toString().contains(GameConstants.DRAW)) {
            System.out.println(GameConstants.DRAW);
        } else {
            int winnerIndex = (Parser.stepsToRead.size() + 1) % 2;
            System.out.println(Parser.players.get(winnerIndex).getName() + " won");
        }

    }

    public static String toBuildGameSimulation(int menuItemNum) {
        StringBuilder gameSimulation = new StringBuilder();
        Gameboard gameboard = new Gameboard();

        Logger.toReadTheLog(menuItemNum);

        gameSimulation.append(GameConstants.ENTER_FIRST_NAME);
        gameSimulation.append(Parser.players.get(0).getName() + "\n\n");
        gameSimulation.append(GameConstants.ENTER_SECOND_NAME);
        gameSimulation.append(Parser.players.get(1).getName() + "\n\n");
        gameSimulation.append(gameboard.toPrintField());

        for (Step step : Parser.stepsToRead) {
            gameSimulation.append(Parser.players.get(step.getPlayerId() - 1).getName() + " player's turn\n");
            gameSimulation.append(GameConstants.SELECT_CELL);
            gameSimulation.append(step.getCell() + "\n");
            gameboard.setCellForSimulating(step.getCell(), Parser.players.get(step.getPlayerId() - 1).getSymbol());
            gameSimulation.append(gameboard.toPrintField() + "\n");
        }

        if (Parser.gameResult.toString().contains(GameConstants.DRAW)) {
            gameSimulation.append(GameConstants.DRAW);
        } else {
            int winnerIndex = (Parser.stepsToRead.size() + 1) % 2;
            gameSimulation.append(Parser.players.get(winnerIndex).getName() + " won");
        }

        Parser.toClearParserData();

        return gameSimulation.toString();
    }


}
