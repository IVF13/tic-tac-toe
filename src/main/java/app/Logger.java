package app;

import app.models.Player;
import app.models.Step;
import app.parsers.Parser;
import app.parsers.ParserJSON;
import app.parsers.ParserTXT;
import app.parsers.ParserXML;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.Scanner;


public class Logger {

    public static void toWriteTheLog(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        Scanner in = new Scanner(System.in);
        ParserJSON parserJSON;
        Parser parserXML;

        System.out.println("Select log format: ");
        System.out.println("1 - XML File");
        System.out.println("2 - JSON File");
        System.out.println("3 - XML & JSON Files");
        System.out.println("4 - JSON File & String");
        System.out.println("5 - All formats");
        System.out.println("default: TXT");

        int menuItemNum;
        menuItemNum = in.nextInt();

        toWriteTheLog(players, stepsToWrite, finishChecker, menuItemNum);
    }

    public static void toWriteTheLog(List<Player> players, List<Step> stepsToWrite, int finishChecker, int menuItemNum) {
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
                System.out.println(parserJSON.toWriteJSONString(players, stepsToWrite, finishChecker));
            }
            case (5) -> {
                parserXML = new ParserXML();
                parserJSON = new ParserJSON();
                parserXML.toWriteFile(players, stepsToWrite, finishChecker);
                parserJSON.toWriteFile(players, stepsToWrite, finishChecker);
                System.out.println(parserJSON.toWriteJSONString(players, stepsToWrite, finishChecker));
            }
            default -> {
                ParserTXT.toWriteFile(players.get((stepsToWrite.size()+1)%2).getName(), finishChecker);
            }
        }
    }

    public static void toReadTheLog() {
        Scanner in = new Scanner(System.in);
        Parser parserJSON;
        Parser parserXML;

        System.out.println("Select the log by which the game will be played: ");
        System.out.println("1 - XML changed: "
                + new Date(new File("src/main/resources/gameplay.xml").lastModified()));
        System.out.println("2 - JSON changed: "
                + new Date(new File("src/main/resources/gameplay.json").lastModified()));

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
