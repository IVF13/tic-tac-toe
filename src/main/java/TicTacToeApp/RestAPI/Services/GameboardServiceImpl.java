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

        if (cell < 1 || cell > 9) {
            return false;
        }

        int[] coordinates = gameboard.toTransformCellNum(cell);
        int x = coordinates[0];
        int y = coordinates[1];

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
