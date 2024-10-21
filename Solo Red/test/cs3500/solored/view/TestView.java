package cs3500.solored.view;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.List;

import cs3500.solored.MockSoloRedGameModel;
import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.SoloRedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the view class.
 */
public class TestView {
  private MockSoloRedGameModel mockModel;
  private MockSoloRedGameTextView<Card> mockView;
  private StringBuilder log;
  private SoloRedTextController controller;

  @Before
  public void setUp() {
    log = new StringBuilder();
    mockModel = new MockSoloRedGameModel(log);
    mockView = new MockSoloRedGameTextView<>(log);
    controller = new SoloRedTextController(new StringReader("q"), log); // Sample input for testing
  }

  @Test
  public void testToStringInitialSetup() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 2, 5);
    SoloRedGameTextView view = new SoloRedGameTextView(model);
    String viewOutput = view.toString();
    String expectedOutput = "Canvas: R\n> P1: R1\nP2: O1\nHand: B1 I1 V1 R2 O2";

    Assert.assertEquals(viewOutput, expectedOutput);
  }

  @Test
  public void testToStringAfterCanvasChange() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 2, 5);
    SoloRedGameTextView view = new SoloRedGameTextView(model);
    model.playToCanvas(0);
    String viewOutput = view.toString();
    Assert.assertTrue(viewOutput.contains("Canvas:"));
  }

  @Test
  public void testToStringWithWinningPalette() {
    SoloRedGameModel model = new SoloRedGameModel();
    model.startGame(model.getAllCards(), false, 2, 5);
    SoloRedGameTextView view = new SoloRedGameTextView(model);
    String viewOutput = view.toString();
    model.playToPalette(1, 0);

    Assert.assertTrue(viewOutput.contains("> P1"));
  }

  @Test
  public void testPlayGameWithRender() {
    List<Card> deck = new ArrayList<>();  // Fill this with mock cards as needed
    controller.playGame(mockModel, deck, false, 2, 5);

    assertEquals("startGame called with deck size: 0, shuffle: false, numPalettes: 2, " +
            "handSize: 5\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "Canvas: R\n" +
            "Hand: numOfCardsInDeck called\n" +
            "\n" +
            "Number of cards in deck: 0\n" +
            "isGameOver called\n" +
            "isGameWon called\n" +
            "Game won.\n" +
            "getCanvas called\n" +
            "numPalettes called\n" +
            "getHand called\n" +
            "Canvas: R\n" +
            "Hand: numOfCardsInDeck called\n" +
            "\n" +
            "Number of cards in deck: 0\n", log.toString());

  }

  @Test
  public void testRenderOnly() throws IOException {
    mockView.render();

    assertEquals("render called\n", log.toString());
  }
}

