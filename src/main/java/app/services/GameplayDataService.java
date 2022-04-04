package app.services;

import app.models.GameResult;
import app.models.GameplayData;
import app.models.Player;

import java.util.List;

public interface GameplayDataService {

    GameplayData findById(Long id);

    List<GameplayData> findAll();

    List<GameplayData> findByPlayer(Player player);

    List<GameplayData> findByGameResult(GameResult gameResult);
}
