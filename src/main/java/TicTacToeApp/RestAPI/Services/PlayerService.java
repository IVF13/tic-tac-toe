package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Objects.Player;

import java.util.List;

public interface PlayerService {
    void create(int id, String name, String symbol);

    List<Player> readAll();

    Player read(int id);

    boolean delete(int id);
}
