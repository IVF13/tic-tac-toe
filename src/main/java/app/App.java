package app;

import app.game.TicTacToe;
import app.utils.GameSimulator;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Scanner;

@Configuration
@EnableAutoConfiguration
@SpringBootApplication
public class App {
    public static void main(String[] args) throws IOException, InterruptedException, XMLStreamException {

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

}
