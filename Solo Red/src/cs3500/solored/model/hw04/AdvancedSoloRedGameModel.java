package cs3500.solored.model.hw04;

import java.util.Random;

import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * Represents the advanced model of the Solo Red Card Game with additional drawing rules.
 */
public class AdvancedSoloRedGameModel extends SoloRedGameModel implements RedGameModel<Cards> {
  private boolean drawCondition;
  private int cardsToDraw;

  /**
   * Default constructor initializing the advanced game model.
   */
  public AdvancedSoloRedGameModel() {
    super();
    drawCondition = false;
    cardsToDraw = -1;
  }

  /**
   * Constructor with Random for testing randomness.
   *
   * @param random Random object for shuffling.
   */
  public AdvancedSoloRedGameModel(Random random) {
    super(random);
    drawCondition = false;
    cardsToDraw = -1;
  }


  /**
   * checks if the card played to the canvas has a number greater than the number,
   * of cards in the winning palette.
   *
   * @return true if the extra draw condition is met, otherwise false.
   */
  protected boolean checkExtraDrawCondition() {
    int canvasCardNumber = getCanvas().getNumber();
    int winningPaletteSize = getPalette(winningPaletteIndex()).size();
    return canvasCardNumber > winningPaletteSize;
  }

  @Override
  public void drawForHand() {
    if (cardsToDraw == -1) {
      cardsToDraw = super.handSize;
    } else {
      cardsToDraw = 1;
    }
    if (!this.playedToCanvas && drawCondition) {
      cardsToDraw = 2;
    }
    while (this.hand.size() < this.handSize && cardsToDraw > 0 && !this.deck.isEmpty()) {
      Cards card = this.deck.remove(0);
      this.hand.add(card);
      cardsToDraw--;
    }
  }

  @Override
  public void playToCanvas(int cardIdxInHand) {
    super.playToCanvas(cardIdxInHand);
    drawCondition = checkExtraDrawCondition();
  }

}

