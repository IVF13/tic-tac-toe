package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Objects.Gameboard;
import org.springframework.stereotype.Service;


@Service
public class GameboardServiceImpl implements GameboardService {
    private static Gameboard gameboard;

    public Gameboard getGameboard() {
        return gameboard;
    }

    @Override
    public void create() {
        gameboard = new Gameboard();
    }

    @Override
    public String read() {
        return "Выберите ячейку(1-9): \n" + gameboard.toPrintField();
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

        if (gameboard.getField()[y][x].equals("X") || gameboard.getField()[y][x].equals("O")) {
            return false;
        }

        if (playerId == 1) {
            gameboard.setCellForSimulating(cell, "X");
            return true;
        } else if (playerId == 2) {
            gameboard.setCellForSimulating(cell, "O");
            return true;
        }

        return false;
    }

    @Override
    public boolean delete() {
        gameboard = null;
        return true;
    }
}
