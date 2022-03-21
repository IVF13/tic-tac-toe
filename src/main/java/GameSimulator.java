import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import java.io.IOException;

class GameSimulator {

    public static void toSimulateGame()
            throws ParserConfigurationException, IOException, SAXException, InterruptedException {
        Gameboard gameboard = new Gameboard();
        Parser.toReadXMLFile();

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

        if (Parser.gameResult.equals("Draw!")) {
            System.out.println("Ничья");
        } else {
            System.out.println(Parser.players.get(2).getName() + " победил");
        }

    }
}
