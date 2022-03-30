package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Objects.Gameboard;
import org.springframework.stereotype.Service;


@Service
public class GameboardServiceImpl implements GameboardService {
    private static Gameboard GAMEBOARD;

    public Gameboard getGAMEBOARD() {
        return GAMEBOARD;
    }

    @Override
    public void create() {
        GAMEBOARD = new Gameboard();
    }

    @Override
    public String read() {
        return "Выберите ячейку(1-9): \n" + GAMEBOARD.toPrintField();
    }

    @Override
    public boolean update(int playerId, int cell) {
        int x;
        int y;

        if (cell < 1 || cell > 9) {
            return false;
        }

        if (cell >= 1 && cell <= 3) {
            y = 0;
        } else if (cell >= 4 && cell <= 6) {
            y = 1;
        } else {
            y = 2;
        }

        x = (cell - 1) % 3;

        if (GAMEBOARD.getField()[y][x].equals("X") || GAMEBOARD.getField()[y][x].equals("O")) {
            return false;
        }

        if (playerId == 1) {
            GAMEBOARD.setCellForSimulating(cell, "X");
            return true;
        } else if (playerId == 2) {
            GAMEBOARD.setCellForSimulating(cell, "O");
            return true;
        }

        return false;
    }

    @Override
    public boolean delete() {
        GAMEBOARD = null;
        return true;
    }
}
