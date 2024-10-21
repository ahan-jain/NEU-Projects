package cs3500.solored;

import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.Color;
import cs3500.solored.model.hw02.RedGameModel;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

/**
 * Abstract test class for testing common behaviors of RedGameModel variants.
 */
public abstract class AbstractSoloRedGameModelTest {
  protected RedGameModel<Cards> model;
  protected List<Cards> deckExample;

  /**
   * Creates a new instance of the specific RedGameModel being tested.
   *
   * @return a new instance of the model
   */
  protected abstract RedGameModel<Cards> createModel();

  @Before
  public void setup() {
    model = createModel();
    deckExample = model.getAllCards();
  }

  @Test
  public void testStartGameValidInput() {
    model.startGame(deckExample, false, 3, 5);
    Assert.assertTrue("Game should have started", !model.isGameOver());
    Assert.assertEquals("Correct number of palettes should be initialized", 3,
            model.numPalettes());
    Assert.assertEquals("Hand should have 5 cards after starting game", 5,
            model.getHand().size());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameBadDeck() {
    List<Cards> smallDeck = deckExample.subList(0, 7);
    model.startGame(smallDeck, false, 3, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameNullDeck() {
    model.startGame(null, false, 3, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidHand() {
    model.startGame(deckExample, false, 3, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameInvalidPalette() {
    model.startGame(deckExample, false, 1, 5);
  }

  @Test(expected = IllegalStateException.class)
  public void testStartGameWhenAlreadyStarted() {
    model.startGame(deckExample, false, 3, 5);
    model.startGame(deckExample, false, 3, 5);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testStartGameWithDuplicateCards() {
    List<Cards> deckWithDuplicates = new ArrayList<>(deckExample);
    deckWithDuplicates.add(new Cards(Color.RED, 1)); // Duplicate card
    model.startGame(deckWithDuplicates, false, 3, 5);
  }

  @Test
  public void testShuffle() {
    List<Cards> deckCopy = new ArrayList<>(deckExample);
    model.startGame(deckExample, true, 3, 5);
    Assert.assertNotEquals("Shuffled deck should not match the original order",
            deckCopy.subList(0, 3), model.getPalette(0));
  }

  @Test
  public void testNoShuffle() {
    List<Cards> deckCopy = new ArrayList<>(deckExample);
    model.startGame(deckExample, false, 3, 5);
    Assert.assertEquals("Unshuffled deck should match the original order",
            deckCopy.subList(0, 1), model.getPalette(0));
  }

  @Test
  public void testCardsAfterSetup() {
    model.startGame(deckExample, false, 3, 5);
    Assert.assertEquals("Remaining cards in the deck should match expected count",
            deckExample.size() - 3 - 5, model.numOfCardsInDeck());
  }

  @Test
  public void testMutation() {
    model.startGame(model.getAllCards(), false, 4, 5);
    List<Cards> getHand = model.getHand();
    getHand.remove(0);
    Assert.assertNotEquals("Hand should not be mutable", model.getHand(), getHand);

    List<Cards> getPalette = model.getPalette(0);
    getPalette.remove(0);
    Assert.assertNotEquals("Palette should not be mutable", model.getPalette(0), getPalette);
  }

  @Test
  public void testWinningRed() {
    model.startGame(deckExample, false, 3, 5);
    model.getPalette(0).add(new Cards(Color.RED, 6));
    model.getPalette(1).add(new Cards(Color.RED, 4));
    model.getPalette(2).add(new Cards(Color.RED, 7));
    int winningIndex = model.winningPaletteIndex();
    Assert.assertEquals("Palette with the highest card (Red 7) should win for " +
            "Red", 0, winningIndex);
  }

  @Test
  public void testWinningOrange() {
    model.startGame(deckExample, false, 3, 5);
    model.getPalette(0).add(new Cards(Color.ORANGE, 2));
    model.getPalette(0).add(new Cards(Color.RED, 2));
    model.getPalette(1).add(new Cards(Color.ORANGE, 3));
    model.getPalette(2).add(new Cards(Color.VIOLET, 2));
    int winningIndex = model.winningPaletteIndex();
    Assert.assertEquals("Palette with the most cards of the same number should win " +
            "for Orange", 0, winningIndex);
  }

  @Test
  public void testWinningBlue() {
    model.startGame(deckExample, false, 3, 5);
    model.getPalette(0).add(new Cards(Color.RED, 1));
    model.getPalette(0).add(new Cards(Color.ORANGE, 2));
    model.getPalette(0).add(new Cards(Color.BLUE, 3));
    model.getPalette(1).add(new Cards(Color.INDIGO, 4));
    int winningIndex = model.winningPaletteIndex();
    Assert.assertEquals("Palette with the most unique colors should win for Blue",
            0, winningIndex);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayToPaletteBeforeGameStart() {
    model.playToPalette(0, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidPaletteIndex() {
    model.startGame(deckExample, true, 3, 5);
    model.playToPalette(-1, 0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardIndex() {
    model.startGame(deckExample, false, 3, 5);
    model.playToPalette(0, -1);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayToCanvasBeforeGameStart() {
    model.playToCanvas(0);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayToCanvasWithOneCardInHand() {
    model.startGame(deckExample, true, 3, 1);
    model.drawForHand();
    model.playToCanvas(0);
  }

  @Test
  public void testPlayToPaletteValid() {
    model.startGame(deckExample, false, 3, 1);
    model.drawForHand();
    model.playToPalette(1, 0);
    Assert.assertEquals(0, model.getHand().size());
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayToPaletteWhenGameIsOver() {
    model.startGame(deckExample, true, 3, 5);
    while (!model.isGameOver()) {
      model.playToPalette(0, 0);
      model.drawForHand();
    }
    model.playToPalette(0, 0);
  }

  @Test(expected = IllegalStateException.class)
  public void testPlayToCanvasTwice() {
    model.startGame(deckExample, true, 3, 5);
    model.drawForHand();
    model.playToCanvas(0);
    model.playToCanvas(0);
  }

  @Test(expected = IllegalArgumentException.class)
  public void testPlayWithEmptyHand() {
    model.startGame(deckExample, true, 3, 0);
    model.playToPalette(0, 0);
  }
}
