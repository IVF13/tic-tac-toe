package app;

import app.models.GameResult;
import app.models.GameplayData;
import app.models.Player;
import app.models.Step;
import app.repository.GameplayDataRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class DBTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private GameplayDataRepository gameplayDataRepository;

    private GameplayData gameplayData;

    @BeforeEach
    void toPrepareForTests() {
        gameplayData = new GameplayData();
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
    }

    @Test
    public void should_find_no_gameplayData_if_repository_is_empty() {
        assertTrue(gameplayDataRepository.findAll().isEmpty());
    }

    @Test
    public void should_store_a_gameplayData() {
        this.entityManager.persist(gameplayData);
        assertFalse(gameplayDataRepository.findAll().isEmpty());
    }

    @Test
    public void should_find_all_gameplayData() {
        this.entityManager.persist(gameplayData);

        GameplayData gameplayData2 = new GameplayData();
        Player player21 = new Player(1, "Max", "X", gameplayData2);
        Player player22 = new Player(2, "Sam", "O", gameplayData2);

        Step step21 = new Step(1, 1, 1, gameplayData2);
        Step step22 = new Step(2, 2, 2, gameplayData2);
        Step step23 = new Step(3, 1, 3, gameplayData2);
        Step step24 = new Step(4, 2, 4, gameplayData2);
        Step step25 = new Step(5, 1, 5, gameplayData2);
        Step step26 = new Step(6, 2, 6, gameplayData2);
        Step step27 = new Step(7, 1, 7, gameplayData2);

        gameplayData2.setPlayers(List.of(player21, player22));
        gameplayData2.setStepsToWrite(List.of(step21, step22, step23, step24, step25, step26, step27));
        gameplayData2.setGameResult(List.of(new GameResult(player21.toString())));

        this.entityManager.persist(gameplayData2);

        List<GameplayData> expected = List.of(gameplayData, gameplayData2);

        List<GameplayData> actual = gameplayDataRepository.findAll();

        assertEquals(expected, actual);
    }

    @Test
    public void should_find_gameplayData_by_id() {
        GameplayData savedData = this.entityManager.persist(gameplayData);

        assertEquals(savedData, gameplayDataRepository.findById(savedData.getId()).get());
    }

    @Test
    public void should_find_gameplayData_by_player() {
        GameplayData savedData = this.entityManager.persist(gameplayData);

        assertEquals(List.of(savedData), gameplayDataRepository.findByPlayer(gameplayData.getPlayers().get(0)));
    }

    @Test
    public void should_find_gameplayData_by_players() {
        GameplayData savedData = this.entityManager.persist(gameplayData);

        assertEquals(List.of(savedData), gameplayDataRepository.findByPlayers(gameplayData.getPlayers()));
    }

    @Test
    public void should_find_gameplayData_by_gameResult() {
        GameplayData savedData = this.entityManager.persist(gameplayData);

        assertEquals(List.of(savedData), gameplayDataRepository.findByGameResult(new GameResult("Draw!")));
    }

    @Test
    public void should_delete_gameplayData_by_id() {
        GameplayData savedData = this.entityManager.persist(gameplayData);
        assertFalse(gameplayDataRepository.findAll().isEmpty());

        gameplayDataRepository.deleteById(savedData.getId());
        assertTrue(gameplayDataRepository.findAll().isEmpty());
    }

    @Test
    public void should_delete_all_gameplayData() {
        this.entityManager.persist(gameplayData);

        GameplayData gameplayData2 = new GameplayData();
        Player player21 = new Player(1, "Max", "X", gameplayData2);
        Player player22 = new Player(2, "Sam", "O", gameplayData2);

        Step step21 = new Step(1, 1, 1, gameplayData2);
        Step step22 = new Step(2, 2, 2, gameplayData2);
        Step step23 = new Step(3, 1, 3, gameplayData2);
        Step step24 = new Step(4, 2, 4, gameplayData2);
        Step step25 = new Step(5, 1, 5, gameplayData2);
        Step step26 = new Step(6, 2, 6, gameplayData2);
        Step step27 = new Step(7, 1, 7, gameplayData2);

        gameplayData2.setPlayers(List.of(player21, player22));
        gameplayData2.setStepsToWrite(List.of(step21, step22, step23, step24, step25, step26, step27));
        gameplayData2.setGameResult(List.of(new GameResult(player21.toString())));

        this.entityManager.persist(gameplayData2);

        gameplayDataRepository.deleteAll(List.of(gameplayData, gameplayData2));
        gameplayDataRepository.flush();

        assertTrue(gameplayDataRepository.findAll().isEmpty());
    }

}