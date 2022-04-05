package app.services;

import app.exceptions.AlreadyTakenCellException;
import app.exceptions.BadCellException;
import app.exceptions.InvalidValueException;
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

        try {
            if (cell < 1 || cell > 9) {
                throw new BadCellException();
            }
        } catch (BadCellException e) {
            e.printStackTrace();
            return false;
        }


        int[] coordinates = gameboard.toTransformCellNum(cell);
        int x = coordinates[0];
        int y = coordinates[1];


        try {
            if (gameboard.getField()[y][x].equals(GameConstants.X)
                    || gameboard.getField()[y][x].equals(GameConstants.O)) {
                throw new AlreadyTakenCellException();
            }
        } catch (AlreadyTakenCellException e) {
            e.printStackTrace();
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

        try {
            if (!this.update(playerId, step.getCell())) {
                throw new InvalidValueException();
            }
        } catch (InvalidValueException e) {
            e.printStackTrace();
            return new ResponseEntity<>(this.read() + "\n" + GameConstants.INVALID_VALUE, HttpStatus.LOCKED);
        }

        return null;
    }
}
