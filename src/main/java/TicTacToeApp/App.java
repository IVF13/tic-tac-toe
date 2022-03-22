package TicTacToeApp;

import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args)
            throws IOException, InterruptedException, XMLStreamException {
        Scanner in = new Scanner(System.in);
        int menuItemNum;
        System.out.println("Выберите вариант из списка:");
        System.out.println("1 - Сыграть в крестики-нолики");
        System.out.println("2 - Воспроизвести последнюю партию");
        System.out.println("3 - Сыграть в крестики-нолики и воспроизвести выбранную партию");
        menuItemNum = in.nextInt();

        switch (menuItemNum) {
            case (1) -> TicTacToe.toPlayTicTacToe();
            case (2) -> GameSimulator.toSimulateGame();
            case (3) -> {
                TicTacToe.toPlayTicTacToe();
                GameSimulator.toSimulateGame();
            }
            default -> {
            }
        }

    }

}
