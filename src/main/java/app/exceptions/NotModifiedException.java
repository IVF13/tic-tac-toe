package app.exceptions;

import app.utils.GameConstants;

public class NotModifiedException extends Exception {
    @Override
    public String toString() {
        return GameConstants.NOT_MODIFIED;
    }
}
