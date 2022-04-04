package app.services;

import app.models.GameResult;
import app.models.GameplayData;
import app.models.Player;
import app.repository.GameplayDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
public class GameplayDataService {

    @Autowired
    private GameplayDataRepository gameplayDataRepository;

    @Transactional
    public GameplayData findById(Long id) {
        GameplayData gameplayDataList = gameplayDataRepository.findById(id).get();
        // To load lazy association roles.
        gameplayDataList.getPlayers().size();
        gameplayDataList.getStepsToWrite().size();
        gameplayDataList.getGameResult().size();

        return gameplayDataList;
    }

    @Transactional
    public List<GameplayData> findAll() {
        List<GameplayData> gameplayDataList = gameplayDataRepository.findAll();
        // To load lazy association roles.
        gameplayDataList.forEach(x -> {
            x.getPlayers().size();
            x.getStepsToWrite().size();
            x.getGameResult().size();
        });

        return gameplayDataList;
    }

    @Transactional
    public List<GameplayData> findByPlayer(Player player) {
        List<GameplayData> gameplayDataList = gameplayDataRepository.findByPlayer(player);

        // To load lazy association roles.
        gameplayDataList.forEach(x -> {
            x.getPlayers().size();
            x.getStepsToWrite().size();
            x.getGameResult().size();
        });

        return gameplayDataList;
    }

    @Transactional
    public List<GameplayData> findByPlayers(List<Player> players) {
        List<GameplayData> gameplayDataList = gameplayDataRepository.findByPlayers(players);

        // To load lazy association roles.
        gameplayDataList.forEach(x -> {
            x.getPlayers().size();
            x.getStepsToWrite().size();
            x.getGameResult().size();
        });

        return gameplayDataList;
    }

    @Transactional
    public List<GameplayData> findByGameResult(GameResult gameResult) {
        List<GameplayData> gameplayDataList = gameplayDataRepository.findByGameResult(gameResult);

        // To load lazy association roles.
        gameplayDataList.forEach(x -> {
            x.getPlayers().size();
            x.getStepsToWrite().size();
            x.getGameResult().size();
        });

        return gameplayDataList;
    }

}
