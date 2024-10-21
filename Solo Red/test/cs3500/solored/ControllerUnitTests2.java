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
 * Unit tests for SoloRedTextController with invalid inputs using MockSoloRedGameModel2.
 */
public class ControllerUnitTests2 {

  private StringBuilder log;
  private StringBuilder output;
  private MockSoloRedGameModel2 mockModel;
  private List<Card> mockDeck;

  @Before
  public void setUp() {
    log = new StringBuilder();
    output = new StringBuilder();
    mockDeck = Arrays.asList(new Cards(Color.RED, 1), new Cards(Color.BLUE, 2));
    mockModel = new MockSoloRedGameModel2(log);
  }

  @Test
  public void testPlayToPalette() {
    SoloRedTextController controller = new SoloRedTextController(
            new StringReader("palette 0 1 q"), output);
    controller.playGame(mockModel, mockDeck, false, 2, 5);

    Assert.assertTrue(log.toString().contains("playToPalette called with paletteIdx: -1, "
           + "cardIdxInHand: 0\n"));

  }

  @Test
  public void testPlayToCanvas() {
    SoloRedTextController controller = new SoloRedTextController(new StringReader("canvas 1 q"),
            output);
    controller.playGame(mockModel, mockDeck, false, 2, 5);
    Assert.assertTrue(log.toString().contains("playToCanvas called"));
  }

  @Test
  public void testDrawForHand() {
    SoloRedTextController controller = new SoloRedTextController(
            new StringReader("palette 1 2 q"), output);
    controller.playGame(mockModel, mockDeck, false, 2, 5);

    Assert.assertTrue(log.toString().contains("drawForHand called\n"));
  }

  @Test
  public void testPlayToPaletteAndDrawForHand() {
    SoloRedTextController controller = new SoloRedTextController(
            new StringReader("palette 1 2 q"), output);
    controller.playGame(mockModel, mockDeck, false, 2, 5);

    Assert.assertTrue(log.toString().contains("drawForHand called\n"));
    Assert.assertTrue(log.toString().contains("playToPalette called with paletteIdx: 0, "
            + "cardIdxInHand: 1\n"));
  }

  @Test
  public void testInvalidCommand() {
    SoloRedTextController controller = new SoloRedTextController(new StringReader("invalid q"),
            output);
    controller.playGame(mockModel, mockDeck, false, 2, 5);

    Assert.assertFalse(log.toString().contains("playToPalette"));
    Assert.assertFalse(log.toString().contains("playToCanvas"));
  }
}