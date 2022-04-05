package app.exceptions;

import app.utils.GameConstants;

public class TurnsOrderException extends Exception {
    @Override
    public String toString() {
        return GameConstants.NOT_YOUR_TURN;
    }
}
