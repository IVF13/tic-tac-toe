package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Objects.Player;
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
    public boolean delete(int id) {
        return PLAYER_MAP.remove(id) != null;
    }

}
