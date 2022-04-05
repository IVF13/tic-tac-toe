package app.exceptions;

import app.utils.GameConstants;

public class NotFoundException extends Exception {
    @Override
    public String toString() {
        return GameConstants.NOT_FOUND;
    }
}
