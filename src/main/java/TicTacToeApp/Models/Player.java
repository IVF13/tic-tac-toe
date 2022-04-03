package TicTacToeApp.Models;

import javax.persistence.*;

@Entity
@Table(name="Player")
public class Player {
    @Id
    @Column(name="playerId")
    private Integer playerId;
    @Column(name="name")
    private String name;
    @Column(name="symbol")
    private String symbol;

    @ManyToOne (fetch=FetchType.LAZY,
            cascade=CascadeType.ALL)
    @JoinColumn(name="Players")
    private GameplayData gameplayData;

    public Player() {
    }

    public Player(String name) {
        this.setName(name);
    }

    public Player(int playerId, String name, String symbol) {
        this.setPlayerId(playerId);
        this.setName(name);
        this.setSymbol(symbol);
    }

    public Player(Integer playerId, String name, String symbol, GameplayData gameplayData) {
        this.gameplayData = gameplayData;
        this.playerId = playerId;
        this.name = name;
        this.symbol = symbol;
    }

    public Integer getPlayerId() {
        return playerId;
    }

    public void setPlayerId(Integer playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSymbol() {
        return symbol;
    }

    public void setSymbol(String symbol) {
        this.symbol = symbol;
    }

    public GameplayData getGameplayData() {
        return gameplayData;
    }

    public void setGameplayData(GameplayData gameplayData) {
        this.gameplayData = gameplayData;
    }

    @Override
    public String toString() {
        return "Player{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
