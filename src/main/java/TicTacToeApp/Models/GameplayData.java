package TicTacToeApp.Models;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "GameplayData")
public class GameplayData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Column(name = "Players")
    @ElementCollection
    private List<Player> players;

    @Column(name = "Steps")
    @ElementCollection
    private List<Step> stepsToWrite;

    @Column(name = "GameResult")
    @ElementCollection
    private List<GameResult> gameResult;


    public GameplayData() {

    }

    public GameplayData(List<Player> players, List<Step> stepsToWrite) {
        this.players = players;
        this.stepsToWrite = stepsToWrite;
        this.gameResult = List.of(new GameResult("Draw!"));
    }

    public GameplayData(List<Player> players, List<Step> stepsToWrite, List<GameResult> gameResult) {
        this.players = players;
        this.stepsToWrite = stepsToWrite;
        this.gameResult = gameResult;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "gameplayData",
            cascade = CascadeType.ALL)
    public List<Player> getPlayers() {
        return this.players;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "gameplayData",
            cascade = CascadeType.ALL)
    public List<Step> getStepsToWrite() {
        return this.stepsToWrite;
    }

    @Access(AccessType.PROPERTY)
    @OneToMany(fetch = FetchType.LAZY,
            mappedBy = "resultId",
            cascade = CascadeType.ALL)
    public List<GameResult> getGameResult() {
        return this.gameResult;
    }

    public void setPlayers(List<Player> players) {
        this.players = players;
    }

    public void setStepsToWrite(List<Step> stepsToWrite) {
        this.stepsToWrite = stepsToWrite;
    }

    public void setGameResult(List<GameResult> gameResult) {
        this.gameResult = gameResult;
    }

    public long getId() {
        return id;
    }

    @Override
    public String toString() {
        return "GameplayData{" +
                "id=" + id +
                ", players=" + players +
                ", stepsToWrite=" + stepsToWrite +
                ", gameResult=" + gameResult +
                '}';
    }
}
