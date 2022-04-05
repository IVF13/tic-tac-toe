package app.exceptions;

import app.utils.GameConstants;

public class BadCellException extends Exception {
    @Override
    public String toString() {
        return GameConstants.INVALID_VALUE + ", valid values: 1-9";
    }
}
