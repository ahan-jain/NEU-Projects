/**
 * Represents a hole in the Triple Trios game grid.
 * A hole is a type of cell that cannot contain any card.
 * It serves as an obstacle or barrier within the grid, preventing card placement.
 */
public class Hole implements TTCell {
  @Override
  public void placeCard(Card card) {
    throw new IllegalStateException("this is a hole!");
  }

  @Override
  public void flipCard() {
    throw new IllegalStateException("this is a hole!");
  }

  @Override
  public Card getCard() {
    throw new IllegalStateException("this is a hole!");
  }

  @Override
  public boolean isEmpty() {
    return false;
  }
}
