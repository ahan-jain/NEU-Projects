package cs3500.solored;

import cs3500.solored.model.hw04.AdvancedSoloRedGameModel;
import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.RedGameModel;
import org.junit.Assert;
import org.junit.Test;

/**
 * Test class for the AdvancedSoloRedGameModel.
 */
public class AdvancedModelTest extends AbstractSoloRedGameModelTest {

  @Override
  protected RedGameModel<Cards> createModel() {
    return new AdvancedSoloRedGameModel();
  }

  @Test
  public void testExtraDraw() {
    model.startGame(deckExample, false, 3, 5);
    model.playToCanvas(2);
    model.playToPalette(2, 0);
    model.drawForHand();
    Assert.assertEquals("Should draw an extra card if the canvas card number is greater",
            5, model.getHand().size());
  }

  @Test
  public void testNoExtraDraw() {
    model.startGame(deckExample, false, 3, 5);
    model.playToCanvas(1);
    model.playToPalette(2, 0);

    model.drawForHand();
    Assert.assertEquals("Should not draw an extra card if the canvas card number is " +
            "not greater", 4, model.getHand().size());
  }

  @Test
  public void testDrawsOneAfterExtraDraw() {
    model.startGame(deckExample, false, 3, 5);
    model.playToCanvas(3);
    model.playToPalette(1, 0);
    model.drawForHand();
    Assert.assertEquals("Hand size should have increased by 2 due to the extra draw " +
            "condition", 5, model.getHand().size());

    model.playToPalette(2, 0);
    model.drawForHand();
    Assert.assertEquals("Hand size should increase by only 1 after the extra draw " +
            "condition resets", 5, model.getHand().size());
  }

  @Test
  public void testDrawConditionTrue() {
    model.startGame(deckExample, false, 3, 5);
    model.playToCanvas(4);
    int canvasCardNumber = model.getCanvas().getNumber();
    int winningPaletteSize = model.getPalette(model.winningPaletteIndex()).size();
    Assert.assertTrue("Draw condition should be true when the canvas card number is " +
            "greater than the winning palette size", canvasCardNumber > winningPaletteSize);
  }

  @Test
  public void testDrawConditionFalse() {
    model.startGame(deckExample, false, 3, 5);
    model.playToCanvas(1);
    int canvasCardNumber = model.getCanvas().getNumber();
    int winningPaletteSize = model.getPalette(model.winningPaletteIndex()).size();
    Assert.assertFalse("Draw condition should be false when the canvas card number is " +
            "not greater than the winning palette size", canvasCardNumber > winningPaletteSize);
  }

}
