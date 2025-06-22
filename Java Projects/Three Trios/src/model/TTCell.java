/**
 * An interface that contains the necessary behaviors for all cells in the game.
 */
public interface TTCell {
  /**
   * If not a hole there is no card already on the cell, place the given card.
   *
   * @throws IllegalStateException if trying to place on a hole
   * @throws IllegalStateException if card already present
   * @throws IllegalArgumentException if card is null
   */
  void placeCard(Card card);

  /**
   * Changes which team this card belongs to.
   *
   * @throws IllegalStateException if a hole
   * @throws IllegalStateException if empty
   */
  void flipCard();

  /**
   * Gets a mutable version of this cell's card.
   * @return this card
   *
   * @throws IllegalStateException if a hole
   * @throws IllegalStateException if empty
   */
  Card getCard();

  /**
   * Returns if the cell is empty or not. If a hole, returns false.
   * @return this boolean
   */
  boolean isEmpty();

}
