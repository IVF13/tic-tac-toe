import java.util.ArrayList;

public interface Parser {
    ArrayList<Player> players = new ArrayList<>();
    ArrayList<Step> stepsToRead = new ArrayList<>();
    StringBuilder gameResult = new StringBuilder();

    void toReadFile();

    void toWriteFile(int finishChecker);

}
