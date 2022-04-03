package TicTacToeApp.RestAPI.Services;

import TicTacToeApp.Models.GameplayData;
import TicTacToeApp.Repository.GameplayDataRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class GameplayDataService{

    @Autowired
    private GameplayDataRepository gameplayDataRepository;

    @Transactional
    public GameplayData findById(Long id) {
        GameplayData gameplayData = gameplayDataRepository.findById(id).get();
        // To load lazy association roles.
        gameplayData.getPlayers().size();
        gameplayData.getStepsToWrite().size();
        gameplayData.getGameResult().size();
        return gameplayData;
    }

}