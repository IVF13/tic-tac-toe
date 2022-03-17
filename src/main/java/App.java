import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.XMLStreamException;
import java.io.IOException;
import java.util.Scanner;

public class App {

    public static void main(String[] args)
            throws IOException, ParserConfigurationException, SAXException, InterruptedException, XMLStreamException {
        Scanner in = new Scanner(System.in);
        int menuItemNum = 0;
        System.out.println("Выберите вариант из списка:");
        System.out.println("1 - Сыграть в крестики-нолики");
        System.out.println("2 - Воспроизвести последнюю партию");
        System.out.println("3 - Сыграть в крестики-нолики и воспроизвести партию");
        menuItemNum = in.nextInt();

        switch (menuItemNum){
            case (1):
                TicTacToe.toPlayTicTacToe();
            case (2):
                GameSimulator.toSimulateGame();
            case (3):
                TicTacToe.toPlayTicTacToe();
                GameSimulator.toSimulateGame();
            default:
                break;
        }

    }

}
