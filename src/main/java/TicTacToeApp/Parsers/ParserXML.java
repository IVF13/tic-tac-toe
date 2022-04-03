package TicTacToeApp.Parsers;

import TicTacToeApp.Models.Player;
import TicTacToeApp.Models.Step;
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
import java.util.List;

public class ParserXML implements Parser {

    @Override
    public void toWriteFile(List<Player> players, List<Step> stepsToWrite, int finishChecker) {
        try {
            XMLOutputFactory factory = XMLOutputFactory.newFactory();
            XMLStreamWriter writer = factory
                    .createXMLStreamWriter(new FileOutputStream("src/main/resources/gameplay.xml"));
            writer.writeStartDocument();
            writer.writeCharacters("\n");
            writer.writeStartElement("Gameplay");
            writer.writeCharacters("\n");
            writer.writeCharacters("    ");
            writer.writeEmptyElement("Player");
            writer.writeAttribute("id", Integer.toString(players.get(0).getPlayerId()));
            writer.writeAttribute("name", players.get(0).getName());
            writer.writeAttribute("symbol", players.get(0).getSymbol());
            writer.writeCharacters("\n");
            writer.writeCharacters("    ");
            writer.writeEmptyElement("Player");
            writer.writeAttribute("id", Integer.toString(players.get(1).getPlayerId()));
            writer.writeAttribute("name", players.get(1).getName());
            writer.writeAttribute("symbol", players.get(1).getSymbol());
            writer.writeCharacters("\n");
            writer.writeCharacters("    ");
            writer.writeStartElement("Game");
            writer.writeCharacters("\n");

            for (Step step : stepsToWrite) {
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
                int indexOfWinner = (stepsToWrite.size() + 1) % 2;
                writer.writeEmptyElement("Player");
                writer.writeAttribute("id", Integer.toString(players.get(indexOfWinner).getPlayerId()));
                writer.writeAttribute("name", players.get(indexOfWinner).getName());
                writer.writeAttribute("symbol", players.get(indexOfWinner).getSymbol());
            } else if (finishChecker == 2) {
                writer.writeCharacters("Draw!");
            }

            writer.writeEndElement();
            writer.writeCharacters("\n");
            writer.writeEndDocument();
            writer.flush();
            writer.close();
        } catch (XMLStreamException | FileNotFoundException e) {
            e.printStackTrace();
        }

    }

    @Override
    public void toReadFile() {
        try {
            SAXParserFactory factory = SAXParserFactory.newInstance();
            SAXParser parser = factory.newSAXParser();
            XMLHandler handler = new XMLHandler();

            parser.parse(new File("src/main/resources/gameplay.xml"), handler);
        } catch (ParserConfigurationException | SAXException | IOException e) {
            e.printStackTrace();
        }

    }

    private static class XMLHandler extends DefaultHandler {
        boolean isEqualsStep = false;
        boolean isGameResult = false;
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
                int playerId = Integer.parseInt(attributes.getValue("playerId"));
                stepsToRead.add(new Step(stepNum, playerId));
                isEqualsStep = true;
            }

            if (qName.equals("GameResult")) {
                isGameResult = true;
            }

        }

        @Override
        public void characters(char[] ch, int start, int length) {
            if (isEqualsStep) {
                try {
                    stepsToRead.get(index).setCell(Integer.parseInt(new String(ch, start, length)));
                    index++;
                } catch (NumberFormatException ignored) {

                }
                isEqualsStep = false;
            }

            if (isGameResult) {
                gameResult.append(new String(ch, start, length));
                isGameResult = false;
            }

        }

    }

}
