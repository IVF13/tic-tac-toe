package app.repository;

import app.models.GameResult;
import app.models.GameplayData;
import app.models.Player;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.List;

@Repository
public interface GameplayDataRepository extends JpaRepository<GameplayData, Long> {

    default List<GameplayData> findByPlayer(Player player){
        List<GameplayData> gameplayData = this.findAll();
        List<GameplayData> relevant = new ArrayList<>();

        for(GameplayData data : gameplayData){
            if(data.getPlayers().contains(player)){
                relevant.add(data);
            }
        }

        return relevant;
    };

    default List<GameplayData> findByPlayers(List<Player> players) {
        List<GameplayData> gameplayData = this.findAll();
        List<GameplayData> relevant = new ArrayList<>();

        for(GameplayData data : gameplayData){
            if(data.getPlayers().contains(players.get(0)) && data.getPlayers().contains(players.get(1))){
                relevant.add(data);
            }
        }

        return relevant;
    };

    default List<GameplayData> findByGameResult(GameResult gameResult) {
        List<GameplayData> gameplayData = this.findAll();
        List<GameplayData> relevant = new ArrayList<>();

        for(GameplayData data : gameplayData){
            if(data.getGameResult().get(0).getResult().equals(gameResult.getResult())){
                relevant.add(data);
            }
        }

        return relevant;
    };

}
