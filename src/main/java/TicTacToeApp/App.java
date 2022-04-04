package TicTacToeApp;

import TicTacToeApp.Models.GameResult;
import TicTacToeApp.Models.GameplayData;
import TicTacToeApp.Models.Player;

import TicTacToeApp.Models.Step;
import TicTacToeApp.Repository.GameplayDataRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.transaction.Transactional;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class App {
    public static void main(String[] args)
            throws IOException, InterruptedException, XMLStreamException {

        Scanner in = new Scanner(System.in);
       int menuItemNum;

       System.out.println("Choose an option from the list:");
      System.out.println("1 - Play tic-tac-toe");
       System.out.println("2 - Play a recorded part (one of the recorded parts)");
       System.out.println("3 - Play tic-tac-toe and play one of the recorded games");
       System.out.println("4 - Launch REST API");

        menuItemNum = in.nextInt();

        switch (menuItemNum) {
            case (1) -> TicTacToe.toPlayTicTacToeInConsole();
            case (2) -> GameSimulator.toSimulateGameInConsole();
            case (3) -> {
               TicTacToe.toPlayTicTacToeInConsole();
               GameSimulator.toSimulateGameInConsole();
            }
            case (4) -> SpringApplication.run(App.class, args);
            default -> {
            }
        }

    }

//    @Bean
//    @Transactional
//    public CommandLineRunner demo(GameplayDataRepository gameplayDataRepository,
//                                  GameplayDataService gameplayDataService) {
//        return (args) -> {
//            GameplayData gameplayData = new GameplayData();
//
//            Player player1 = new Player(1, "Ivan", "X", gameplayData);
//            Player player2 = new Player(2, "Roman", "O", gameplayData);
//
//            Step step1 = new Step(1, 1, 5, gameplayData);
//            Step step2 = new Step(2, 2, 9, gameplayData);
//            Step step3 = new Step(3, 1, 1, gameplayData);
//
//            gameplayData.setPlayers(List.of(player1, player2));
//            gameplayData.setStepsToWrite(List.of(step1, step2, step3));
//            gameplayData.setGameResult(List.of(new GameResult(player1.toString())));
//
//            System.out.println(gameplayData);
//
//            GameplayData savedGameplay = gameplayDataRepository.save(gameplayData);
//
//            savedGameplay = gameplayDataService.findById(savedGameplay.getId());
//
//            Iterable<GameplayData> gameplayData333 = gameplayDataService.findAll();
//
//            for(GameplayData g : gameplayData333){
//                System.out.println("!!!" + g);
//            }
//
//            System.out.println(gameplayDataRepository.count());
//
//            System.out.println(savedGameplay);
//
//            gameplayDataRepository.delete(savedGameplay);
//
//            System.out.println(gameplayDataRepository.count());
//
//        };
//    }

}
