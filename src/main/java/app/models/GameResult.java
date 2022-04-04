package app.models;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name="GameResult")
public class GameResult {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer id;

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

}
