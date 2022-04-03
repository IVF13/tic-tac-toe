package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Models.Gameboard;
import TicTacToeApp.Models.Step;
import org.springframework.http.ResponseEntity;

public interface GameboardService {

    Gameboard getGameboard();

    void create();

    String read();

    boolean update(int playerId, int cell);

    boolean delete();

    ResponseEntity<String> toModifyCell(int playerId, Step step);
}
