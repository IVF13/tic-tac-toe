package TicTacToeApp;

import TicTacToeApp.Objects.Player;
import TicTacToeApp.Objects.Step;
import TicTacToeApp.Parsers.Parser;
import TicTacToeApp.Parsers.ParserJSON;
import TicTacToeApp.Parsers.ParserXML;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;


public class Logger {

    public static void toWriteScores(String playerName, int finishChecker) throws IOException {
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


    public static void toWriteTheLog(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        Scanner in = new Scanner(System.in);
        ParserJSON parserJSON;
        Parser parserXML;

        System.out.println("Выберите формат записи лога: ");
        System.out.println("1 - XML File");
        System.out.println("2 - JSON File");
        System.out.println("3 - XML & JSON Files");
        System.out.println("4 - JSON File & String");

        int menuItemNum;
        menuItemNum = in.nextInt();

        switch (menuItemNum) {
            case (1) -> {
                parserXML = new ParserXML();
                parserXML.toWriteFile(players, stepsToWrite, finishChecker);
            }
            case (2) -> {
                parserJSON = new ParserJSON();
                parserJSON.toWriteFile(players, stepsToWrite, finishChecker);
            }
            case (3) -> {
                parserXML = new ParserXML();
                parserJSON = new ParserJSON();
                parserXML.toWriteFile(players, stepsToWrite, finishChecker);
                parserJSON.toWriteFile(players, stepsToWrite, finishChecker);
            }
            case (4) -> {
                parserJSON = new ParserJSON();
                parserJSON.toWriteFile(players, stepsToWrite, finishChecker);
                System.out.println(parserJSON.toWriteJSONString(players, stepsToWrite, finishChecker));
            }
            default -> {
            }
        }

    }
}
