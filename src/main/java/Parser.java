import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Parser {
    public static ArrayList<Player> players = new ArrayList<>();
    public static ArrayList<Step> stepsToRead = new ArrayList<>();

    public static void toWriteXMLFile(int finishChecker) throws FileNotFoundException, XMLStreamException {
        XMLOutputFactory factory = XMLOutputFactory.newFactory();
        XMLStreamWriter writer = factory
                .createXMLStreamWriter(new FileOutputStream("src/main/resources/gameplay.xml"));
        writer.writeStartDocument();
        writer.writeCharacters("\n");
        writer.writeStartElement("Gameplay");
        writer.writeCharacters("\n");
        writer.writeCharacters("    ");
        writer.writeEmptyElement("Player");
        writer.writeAttribute("id", Integer.toString(TicTacToe.players[0].getPlayerId()));
        writer.writeAttribute("name", TicTacToe.players[0].getName());
        writer.writeAttribute("symbol", TicTacToe.players[0].getSymbol());
        writer.writeCharacters("\n");
        writer.writeCharacters("    ");
        writer.writeEmptyElement("Player");
        writer.writeAttribute("id", Integer.toString(TicTacToe.players[1].getPlayerId()));
        writer.writeAttribute("name", TicTacToe.players[1].getName());
        writer.writeAttribute("symbol", TicTacToe.players[1].getSymbol());
        writer.writeCharacters("\n");
        writer.writeCharacters("    ");
        writer.writeStartElement("Game");
        writer.writeCharacters("\n");
        for (Step step : TicTacToe.stepsToWrite) {
            writer.writeCharacters("        ");
            writer.writeStartElement("Step");
            writer.writeAttribute("num", Integer.toString(step.getStepNum()));
            writer.writeAttribute("playerId", Integer.toString(step.getPlayerId()));
            writer.writeCharacters(Integer.toString(step.getCell()));
            writer.writeEndElement();
            writer.writeCharacters("\n");
        }
        writer.writeCharacters("    ");
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeCharacters("    ");
        writer.writeStartElement("GameResult");

        if (finishChecker == 1) {
            TicTacToe.players[2] = TicTacToe.players[(TicTacToe.stepsToWrite.size() + 1) % 2];
        } else if (finishChecker == 2) {
            TicTacToe.players[2] = new Player();
        }

        writer.writeEmptyElement("Player");
        writer.writeAttribute("id", Integer.toString(TicTacToe.players[2].getPlayerId()));
        writer.writeAttribute("name", TicTacToe.players[2].getName());
        writer.writeAttribute("symbol", TicTacToe.players[2].getSymbol());
        writer.writeEndElement();
        writer.writeCharacters("\n");
        writer.writeEndDocument();
        writer.flush();
        writer.close();
    }

    public static void toReadXMLFile() throws ParserConfigurationException, SAXException, IOException {
        SAXParserFactory factory = SAXParserFactory.newInstance();
        SAXParser parser = factory.newSAXParser();
        XMLHandler handler = new XMLHandler();

        parser.parse(new File("src/main/resources/gameplay.xml"), handler);
    }

    private static class XMLHandler extends DefaultHandler {
        boolean step = false;
        static int index = 0;

        @Override
        public void startElement(String uri, String localName, String qName, Attributes attributes) {
            if (qName.equals("Player")) {
                int playerId = Integer.parseInt(attributes.getValue("id"));
                String name = attributes.getValue("name");
                String symbol = attributes.getValue("symbol");
                players.add(new Player(playerId, name, symbol));
            }

            if (qName.equals("Step")) {
                int stepNum = Integer.parseInt(attributes.getValue("num"));
                ;
                int playerId = Integer.parseInt(attributes.getValue("playerId"));
                stepsToRead.add(new Step(stepNum, playerId));
                step = true;
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (step) {

                try {
                    stepsToRead.get(index).setCell(Integer.parseInt(new String(ch, start, length)));
                    index++;
                } catch (NumberFormatException ignored) {

                }

                step = false;
            }
        }

    }
}
