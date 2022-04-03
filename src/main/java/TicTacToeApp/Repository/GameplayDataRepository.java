package TicTacToeApp.Repository;

import TicTacToeApp.Models.GameplayData;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameplayDataRepository extends CrudRepository<GameplayData, Long> {


}
