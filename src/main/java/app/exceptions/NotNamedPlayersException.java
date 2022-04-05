package app.exceptions;

import app.utils.GameConstants;

public class NotNamedPlayersException extends Exception {
    @Override
    public String toString() {
        return GameConstants.NAME_PLAYERS;
    }
}
