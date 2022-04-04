package TicTacToeApp.Repository;

import TicTacToeApp.Models.GameplayData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GameplayDataRepository extends JpaRepository<GameplayData, Long> {


}
