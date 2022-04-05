package app.services;

import app.models.Player;
import app.utils.GameConstants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PlayerServiceImpl implements PlayerService {
    private static final Map<Integer, Player> PLAYER_MAP = new HashMap<>();

    @Override
    public void create(int id, String name, String symbol) {
        PLAYER_MAP.put(id, new Player(id, name, symbol));
    }

    @Override
    public List<Player> readAll() {
        return new ArrayList<>(PLAYER_MAP.values());
    }

    @Override
    public Player read(int id) {
        return PLAYER_MAP.get(id);
    }

    @Override
    public void deleteAll() {
        PLAYER_MAP.clear();
    }

    @Override
    public boolean delete(int id) {
        return PLAYER_MAP.remove(id) != null;
    }

    @Override
    public ResponseEntity<String> toCheckIsGameInProcess(GameboardService gameboardService,
                                                         GameResultService gameResultService) {
        if (gameboardService.getGameboard() == null) {
            return new ResponseEntity<>(GameConstants.LAUNCH_AT_FIRST, HttpStatus.LOCKED);
        } else if (gameResultService.getFinishChecker() != 0) {
            return new ResponseEntity<>((GameConstants.ALREADY_FINISHED), HttpStatus.LOCKED);
        }
        return null;
    }

}
