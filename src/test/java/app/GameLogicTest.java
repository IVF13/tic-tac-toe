package app;

import app.game.TicTacToe;
import app.models.Gameboard;
import org.junit.jupiter.api.Test;

import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class GameLogicTest {

    @Test
    void toCheckWinTest() {
        assertEquals(1, TicTacToe.toCheckWin
                (new Gameboard(new String[][]{{"X", "2", "3"}, {"4", "X", "6"}, {"7", "8", "X"}}), 9));
        assertEquals(2, TicTacToe.toCheckWin
                (new Gameboard(new String[][]{{"O", "2", "3"}, {"4", "X", "6"}, {"7", "8", "X"}}), 9));
    }

    @Test
    void toTransformCellNumTest() {
        assertEquals(Arrays.toString(new int[]{0, 1}), Arrays.toString(new Gameboard().toTransformCellNum(4)));
    }

}
