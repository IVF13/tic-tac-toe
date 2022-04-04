package app.models;

import javax.persistence.*;

@Entity
@Table(name="Step")
public class Step {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

    private int stepNum;
    private int playerId;
    private int cell;

    @ManyToOne(fetch= FetchType.LAZY,
            cascade= CascadeType.ALL)
    @JoinColumn(name="Players")
    private GameplayData gameplayData;

    public Step() {

    }

    public Step(int cell) {
        this.cell = cell;
    }

    public Step(int stepNum, int playerId) {
        this.setStepNum(stepNum);
        this.setPlayerId(playerId);
    }

    public Step(int stepNum, int playerId, int cell) {
        this.setStepNum(stepNum);
        this.setPlayerId(playerId);
        this.setCell(cell);
    }

    public Step(int stepNum, int playerId, int cell, GameplayData gameplayData) {
        this.gameplayData = gameplayData;
        this.stepNum = stepNum;
        this.playerId = playerId;
        this.cell = cell;
    }

    public int getStepNum() {
        return stepNum;
    }

    public void setStepNum(int stepNum) {
        this.stepNum = stepNum;
    }

    public int getPlayerId() {
        return playerId;
    }

    public void setPlayerId(int playerId) {
        this.playerId = playerId;
    }

    public int getCell() {
        return cell;
    }

    public void setCell(int cell) {
        this.cell = cell;
    }

    public GameplayData getGameplayData() {
        return gameplayData;
    }

    public void setGameplayData(GameplayData gameplayData) {
        this.gameplayData = gameplayData;
    }

    @Override
    public String toString() {
        return "Step{" +
                "stepNum=" + stepNum +
                ", playerId=" + playerId +
                ", cell=" + cell +
                '}';
    }
}
