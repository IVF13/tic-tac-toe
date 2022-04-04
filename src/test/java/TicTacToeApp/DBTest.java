package TicTacToeApp;

import TicTacToeApp.Models.GameResult;
import TicTacToeApp.Models.GameplayData;
import TicTacToeApp.Models.Player;
import TicTacToeApp.Models.Step;
import TicTacToeApp.Repository.GameplayDataRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DBTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameplayDataRepository gameplayDataRepository;

    @Test
    public void should_find_no_gameplayData_if_repository_is_empty() {
    }

    @Test
    public void should_store_a_gameplayData() {
    }

    @Test
    public void should_find_all_gameplayData() {
    }

    @Test
    public void should_find_gameplayData_by_id() {
    }

    @Test
    public void should_find_published_gameplayData() {
    }

    @Test
    public void should_find_gameplayData_by_title_containing_string() {
    }

    @Test
    public void should_update_gameplayData_by_id() {
    }

    @Test
    public void should_delete_gameplayData_by_id() {
    }

    @Test
    public void should_delete_all_gameplayData() {
    }

    @Test
    void repositoryTest() throws Exception {
        GameplayData gameplayData = new GameplayData();

        Player player1 = new Player(1, "Ivan", "X", gameplayData);
        Player player2 = new Player(2, "Roman", "O", gameplayData);

        Step step1 = new Step(1, 1, 1, gameplayData);
        Step step2 = new Step(2, 2, 2, gameplayData);
        Step step3 = new Step(3, 1, 4, gameplayData);
        Step step4 = new Step(4, 2, 5, gameplayData);
        Step step5 = new Step(5, 1, 8, gameplayData);
        Step step6 = new Step(6, 2, 7, gameplayData);
        Step step7 = new Step(7, 1, 9, gameplayData);
        Step step8 = new Step(8, 2, 3, gameplayData);
        Step step9 = new Step(9, 1, 6, gameplayData);

        gameplayData.setPlayers(List.of(player1, player2));
        gameplayData.setStepsToWrite(List.of(step1, step2, step3, step4, step5, step6, step7, step8, step9));
        gameplayData.setGameResult(List.of(new GameResult("Draw!")));
        this.entityManager.persist(gameplayData);

        Optional<GameplayData> gameplayData1 = this.gameplayDataRepository.findById(gameplayData.getId());

        assertEquals(1, this.gameplayDataRepository.count());
        assertFalse(this.gameplayDataRepository.findAll().isEmpty());
        assertTrue(gameplayData1.toString().contains(gameplayData.toString()));

        this.entityManager.remove(gameplayData);

        assertEquals(0, this.gameplayDataRepository.count());
        assertTrue(this.gameplayDataRepository.findAll().isEmpty());
    }

}