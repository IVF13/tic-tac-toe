package TicTacToeApp.Objects;

public class Step {
    private int stepNum;
    private int playerId;
    private int cell;

    public Step() {

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

}
