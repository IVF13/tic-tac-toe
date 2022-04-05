package app.exceptions;

import app.utils.GameConstants;

public class AlreadyFinishedException extends Exception {
    @Override
    public String toString() {
        return GameConstants.ALREADY_FINISHED;
    }
}
