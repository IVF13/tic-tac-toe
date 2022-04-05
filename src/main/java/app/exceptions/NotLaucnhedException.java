package app.exceptions;

import app.utils.GameConstants;

public class NotLaucnhedException extends Exception {
    @Override
    public String toString() {
        return GameConstants.LAUNCH_AT_FIRST;
    }
}
