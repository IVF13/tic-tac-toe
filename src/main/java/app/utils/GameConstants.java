package app.utils;

import java.io.File;
import java.util.Date;

public class GameConstants {

    public static final String X = "X";
    public static final String O = "O";
    public static final String ENTER_FIRST_NAME = "Enter the name of the first player: ";
    public static final String ENTER_SECOND_NAME = "Enter the name of the second player: ";
    public static final String INVALID_VALUE = "Invalid value entered";
    public static final String SELECT_CELL = "Select cell(1-9): \n";
    public static final String DRAW = "Draw!";
    public static final String CELL_TAKEN = "Error, cell is already taken";

    public static final String SELECT_THE_LOG_TO_WRITE = """
            Select log format:\s
            1 - XML File\s
            2 - JSON File\s
            3 - XML & JSON Files\s
            4 - JSON File & String\s
            5 - All formats\s
            default: TXT\s""";

    public static final String SELECT_THE_LOG_TO_READ = "Select the log by which the game will be played: \n"
            + "1 - XML changed: " + new Date(new File("src/main/resources/gameplay.xml").lastModified())
            + "\n"
            + "2 - JSON changed: " + new Date(new File("src/main/resources/gameplay.json").lastModified())
            + "\n";

    //rest
    public static final String START = "Game started\nEnter the name of the first player";
    public static final String RESTART = "Game restarted\nEnter the name of the first player";
    public static final String SECOND_PLAYER_NAME = "Enter the name of the second player";
    public static final String GET_STARTED = "Get started \n";
    public static final String PLAYER_WAS_DELETED = "Player was deleted, create the new one";
    public static final String STEP_WAS_DELETED = "\nStep was deleted";
    public static final String RESULTS_WERE_DELETED = "Results were deleted";
    public static final String NOT_FINISHED = "The game is not finished";
    public static final String SUCCESSFULLY_WRITTEN = "The log successfully written\n";
    public static final String MUST_BE_FINISHED_AT_FIRST = "The game must be finished at first";
    public static final String RESULTS_WERE_SAVED = "Results were saved to the database\n";
    public static final String ERROR = "An error occurred";
    public static final String GAMEPLAY_DATA_WAS_DELETED = "The gameplay data was deleted from database";
    public static final String ALL_GAMEPLAY_DATA_WAS_DELETED = "All the gameplay data was deleted from database";
    public static final String LAUNCH_AT_FIRST = "Launch the game at first";
    public static final String ALREADY_FINISHED = "The game is over, you can restart it";
    public static final String NAME_PLAYERS = "Name the players";
    public static final String NOT_YOUR_TURN = "Error, now is not your turn";
}
