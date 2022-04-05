package app.services;

import app.models.Gameboard;
import app.models.Step;
import app.utils.GameConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
        return GameConstants.SELECT_CELL + gameboard.toPrintField();
    }

    @Override
    public boolean update(int playerId, int cell) {

        if (cell < 1 || cell > 9) {
            return false;
        }

        int[] coordinates = gameboard.toTransformCellNum(cell);
        int x = coordinates[0];
        int y = coordinates[1];

        if (gameboard.getField()[y][x].equals(GameConstants.X) || gameboard.getField()[y][x].equals(GameConstants.O)) {
            return false;
        }

        if (playerId == 1) {
            gameboard.setCellForSimulating(cell, GameConstants.X);
            return true;
        } else if (playerId == 2) {
            gameboard.setCellForSimulating(cell, GameConstants.O);
            return true;
        }

        return false;
    }

    @Override
    public boolean delete() {
        gameboard = null;
        return true;
    }

    @Override
    public ResponseEntity<String> toModifyCell(int playerId, Step step) {
        if (!this.update(playerId, step.getCell())) {
            return new ResponseEntity<>(this.read() + "\n" + GameConstants.INVALID_VALUE, HttpStatus.OK);
        }
        return null;
    }
}
