package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Objects.Gameboard;

public interface GameboardService {

    Gameboard getGAMEBOARD();

    void create();

    String read();

    boolean update(int playerId, int cell);

    boolean delete();
}
