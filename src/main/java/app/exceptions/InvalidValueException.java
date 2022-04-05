package app.exceptions;

import app.utils.GameConstants;

public class InvalidValueException extends Exception {
    @Override
    public String toString() {
        return GameConstants.INVALID_VALUE;
    }
}
