package app.utils;

import app.models.Player;
import app.models.Step;
import app.parsers.Parser;
import app.parsers.ParserJSON;
import app.parsers.ParserTXT;
import app.parsers.ParserXML;

import java.util.List;
import java.util.Scanner;


public class Logger {

    public static void toWriteTheLog(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        Scanner in = new Scanner(System.in);

        System.out.println(GameConstants.SELECT_THE_LOG_TO_WRITE);

        int menuItemNum;
        menuItemNum = in.nextInt();

        System.out.println(toWriteTheLog(players, stepsToWrite, finishChecker, menuItemNum));
    }

    public static String toWriteTheLog(List<Player> players, List<Step> stepsToWrite, int finishChecker, int menuItemNum) {
        ParserJSON parserJSON;
        Parser parserXML;
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
                return parserJSON.toWriteJSONString(players, stepsToWrite, finishChecker);
            }
            case (5) -> {
                parserXML = new ParserXML();
                parserJSON = new ParserJSON();
                parserXML.toWriteFile(players, stepsToWrite, finishChecker);
                parserJSON.toWriteFile(players, stepsToWrite, finishChecker);
                ParserTXT.toWriteFile(players.get((stepsToWrite.size() + 1) % 2).getName(), finishChecker);
                return parserJSON.toWriteJSONString(players, stepsToWrite, finishChecker);
            }
            default -> {
                ParserTXT.toWriteFile(players.get((stepsToWrite.size() + 1) % 2).getName(), finishChecker);
            }
        }

        return "";
    }

    public static void toReadTheLog() {
        Scanner in = new Scanner(System.in);

        System.out.println(GameConstants.SELECT_THE_LOG_TO_READ);

        int menuItemNum;
        menuItemNum = in.nextInt();

        toReadTheLog(menuItemNum);
    }

    public static void toReadTheLog(int menuItemNum) {
        Parser parserJSON;
        Parser parserXML;

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
