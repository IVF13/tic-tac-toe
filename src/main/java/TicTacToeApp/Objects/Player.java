package TicTacToeApp.Objects;

public class Player {
    private String name;
    private int playerId;
    private String symbol;

    public Player(int playerId, String name, String character) {
        this.setPlayerId(playerId);
        this.setName(name);
        this.setCharacter(character);
    }

    public Player() {

    }

    public String getName() {
        return name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setCharacter(String character) {
        this.symbol = character;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }
}
