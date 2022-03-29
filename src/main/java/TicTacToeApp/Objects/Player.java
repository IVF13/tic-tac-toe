package TicTacToeApp.Objects;

public class Player {
    private int playerId;
    private String name;
    private String symbol;

    public Player() {

    }

    public Player(String name) {
        this.setName(name);
    }

    public Player(int playerId, String name, String character) {
        this.setPlayerId(playerId);
        this.setName(name);
        this.setCharacter(character);
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
