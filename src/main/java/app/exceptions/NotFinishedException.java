package app.exceptions;

import app.utils.GameConstants;

public class NotFinishedException extends Exception {
    @Override
    public String toString() {
        return GameConstants.NOT_FINISHED;
    }
}
