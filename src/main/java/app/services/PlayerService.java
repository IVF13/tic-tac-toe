package app.services;

import app.models.Player;
import org.springframework.http.ResponseEntity;

import java.util.List;

public interface PlayerService {
    void create(int id, String name, String symbol);

    List<Player> readAll();

    Player read(int id);

    void deleteAll();

    boolean delete(int id);

    ResponseEntity<String> toCheckIsGameInProcess(GameboardService gameboardService,
                                                  GameResultService gameResultService);
}
