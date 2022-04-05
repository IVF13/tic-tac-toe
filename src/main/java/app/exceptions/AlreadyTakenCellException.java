package app.exceptions;

import app.utils.GameConstants;

public class AlreadyTakenCellException extends Exception {
    @Override
    public String toString() {
        return GameConstants.CELL_TAKEN;
    }
}
