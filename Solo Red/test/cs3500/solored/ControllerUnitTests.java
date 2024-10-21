package cs3500.solored;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.Color;


/**
 * Unit tests for SoloRedTextController with valid user input.
 */
public class ControllerUnitTests {

  private StringBuilder output;
  private MockSoloRedGameModel mockModel;
  private List<Card> mockDeck;
  private StringBuilder log;

  @Before
  public void setUp() {
    output = new StringBuilder();
    log = new StringBuilder();
    mockDeck = Arrays.asList(new Cards(Color.RED, 1), new Cards(Color.BLUE, 2));
    mockModel = new MockSoloRedGameModel(log);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullReadable() {
    new SoloRedTextController(null, output);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testConstructorWithNullAppendable() {
    new SoloRedTextController(new StringReader("q"), null);
  }

  @Test
  public void testStartGame() {
    SoloRedTextController controller = new SoloRedTextController(new StringReader("q"), output);
    controller.playGame(mockModel, mockDeck, true, 2, 5);
    Assert.assertEquals("startGame called with deck size: 2, shuffle: true, " +
            "numPalettes: 2, handSize: 5\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "numOfCardsInDeck called\n" +
            "isGameOver called\n" +
            "isGameWon called\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "numOfCardsInDeck called\n", log.toString());
  }

  @Test
  public void testGameWon() {
    mockModel.gameLost = false;

    SoloRedTextController controller = new SoloRedTextController(new StringReader("q"), output);
    controller.playGame(mockModel, mockDeck, false, 5, 6);
    //game starts and immediately ends because the mock has the game always winning
    Assert.assertEquals("startGame called with deck size: 2, shuffle: false, " +
            "numPalettes: 5, handSize: 6\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "numOfCardsInDeck called\n" +
            "isGameOver called\n" +
            "isGameWon called\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "numOfCardsInDeck called\n", log.toString());
  }

  @Test
  public void testGameLost() {
    mockModel.gameLost = true;

    SoloRedTextController controller = new SoloRedTextController(new StringReader("q"), output);
    controller.playGame(mockModel, mockDeck, false, 5, 6);
    //same result as testGameWon because the mock is always winning
    Assert.assertEquals("startGame called with deck size: 2, shuffle: false, " +
            "numPalettes: 5, handSize: 6\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "numOfCardsInDeck called\n" +
            "isGameOver called\n" +
            "isGameWon called\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "numOfCardsInDeck called\n", log.toString());
  }
}


