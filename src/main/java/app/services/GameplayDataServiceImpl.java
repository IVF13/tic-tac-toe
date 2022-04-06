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
public class GameplayDataServiceImpl implements GameplayDataService{

    @Autowired
    private GameplayDataRepository gameplayDataRepository;

    @Transactional
    public GameplayData findById(Long id) {
        GameplayData gameplayData = gameplayDataRepository.findById(id).get();

        toLoadLazyAssociationRoles(gameplayData);

        return gameplayData;
    }

    @Transactional
    public List<GameplayData> findAll() {
        List<GameplayData> gameplayDataList = gameplayDataRepository.findAll();

        gameplayDataList.forEach(this::toLoadLazyAssociationRoles);

        return gameplayDataList;
    }

    @Transactional
    public List<GameplayData> findByPlayer(Player player) {
        List<GameplayData> gameplayDataList = gameplayDataRepository.findByPlayer(player);

        gameplayDataList.forEach(this::toLoadLazyAssociationRoles);

        return gameplayDataList;
    }

    @Transactional
    public List<GameplayData> findByGameResult(GameResult gameResult) {
        List<GameplayData> gameplayDataList = gameplayDataRepository.findByGameResult(gameResult);

        gameplayDataList.forEach(this::toLoadLazyAssociationRoles);

        return gameplayDataList;
    }

    // To load lazy association roles.
    private void toLoadLazyAssociationRoles(GameplayData gameplayData) {
        gameplayData.getPlayers().size();
        gameplayData.getStepsToWrite().size();
        gameplayData.getGameResult().size();
    }

}
