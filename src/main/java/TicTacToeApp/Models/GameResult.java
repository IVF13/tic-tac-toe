package TicTacToeApp.Models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GameResult")
public class GameResult {

    @Id
    private final Integer resultId = 1;

    @Column(name = "result")
    private String result;

    public GameResult() {
    }

    public GameResult(String result) {
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    @Override
    public String toString() {
        return this.result;
    }
}
