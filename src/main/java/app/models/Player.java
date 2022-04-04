package app.models;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

@Entity
@Table(name = "Player")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private Integer playerId;
    private String name;
    private String symbol;

    @ManyToOne(fetch = FetchType.LAZY,
            cascade = CascadeType.ALL)
    @JoinColumn(name = "Players")
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
        return "{" +
                "playerId=" + playerId +
                ", name='" + name + '\'' +
                ", symbol='" + symbol + '\'' +
                '}';
    }
}
