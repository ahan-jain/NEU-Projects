import cs3500.model.Card;

import java.awt.Color;
import java.awt.Graphics2D;

/**
 * Represents a panel in the Triple Trios game, defining the operations required
 * for handling game-related visuals and interactions on a panel.
 */
public interface TTPanel {

  /**
   * Gets the height of a single cell within the panel.
   *
   * @return the height of a cell in pixels.
   */
  int getCellHeight();

  /**
   * Gets the width of a single cell within the panel.
   *
   * @return the width of a cell in pixels.
   */
  int getCellWidth();

  /**
   * Highlights a card on the panel by changing its background color.
   *
   * @param g2d           the object used to render the highlight.
   * @param card          the card to be highlighted.
   * @param originalColor the original color of the card before highlighting.
   */
  void highlightCard(Graphics2D g2d, Card card, Color originalColor);
}
