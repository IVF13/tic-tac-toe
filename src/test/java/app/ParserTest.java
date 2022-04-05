package app;

import app.models.Player;
import app.models.Step;
import app.parsers.Parser;
import app.parsers.ParserJSON;
import app.parsers.ParserXML;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class ParserTest {

    @BeforeEach
    void toPrepareForTests(){
        Parser.toClearParserData();
    }

    @Test
    void parserJSONTest() {
        ParserJSON parserJSON = new ParserJSON();

        Player player1 = new Player(1, "Ivan", "X");
        Player player2 = new Player(2, "Roman", "O");

        Step step1 = new Step(1, 1, 5);
        Step step2 = new Step(2, 2, 9);
        Step step3 = new Step(3, 1, 1);

        parserJSON.toWriteFile(List.of(player1, player2), List.of(step1, step2, step3), 2);

        assertTrue(Parser.players.isEmpty());
        assertTrue(Parser.stepsToRead.isEmpty());
        assertTrue(Parser.gameResult.isEmpty());

        parserJSON.toReadFile();

        assertEquals(List.of(player1, player2).toString(), Parser.players.toString());
        assertEquals(List.of(step1, step2, step3).toString(), Parser.stepsToRead.toString());
        assertEquals("[Draw!]", Parser.gameResult.toString());

        String expectedJSONString = "{\"id\":null," +
                "\"players\":[{\"playerId\":1,\"name\":\"Ivan\",\"symbol\":\"X\",\"gameplayData\":null}," +
                "{\"playerId\":2,\"name\":\"Roman\",\"symbol\":\"O\",\"gameplayData\":null}]," +
                "\"stepsToWrite\":[{\"stepNum\":1,\"playerId\":1,\"cell\":5,\"gameplayData\":null}," +
                "{\"stepNum\":2,\"playerId\":2,\"cell\":9,\"gameplayData\":null}," +
                "{\"stepNum\":3,\"playerId\":1,\"cell\":1,\"gameplayData\":null}]," +
                "\"gameResult\":[{\"id\":null,\"result\":\"Draw!\"}]}";

        String actualJSONString = parserJSON.toWriteJSONString(List.of(player1, player2), List.of(step1, step2, step3), 2);

        assertEquals(expectedJSONString, actualJSONString);
    }

    @Test
    void parserXMLTest() {
        Parser parserXML = new ParserXML();

        Player player1 = new Player(1, "Ivan", "X");
        Player player2 = new Player(2, "Roman", "O");

        Step step1 = new Step(1, 1, 5);
        Step step2 = new Step(2, 2, 9);
        Step step3 = new Step(3, 1, 1);

        parserXML.toWriteFile(List.of(player1, player2), List.of(step1, step2, step3), 2);

        assertTrue(Parser.players.isEmpty());
        assertTrue(Parser.stepsToRead.isEmpty());
        assertTrue(Parser.gameResult.isEmpty());

        parserXML.toReadFile();

        assertEquals(List.of(player1, player2).toString(), Parser.players.toString());
        assertEquals(List.of(step1, step2, step3).toString(), Parser.stepsToRead.toString());
        assertEquals("Draw!", Parser.gameResult.toString());
    }

}
