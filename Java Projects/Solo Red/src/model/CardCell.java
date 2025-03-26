import java.util.List;
import java.util.Objects;

/**
 * Represents a cell which isn't a hole, and is either empty or occupied by a card,
 * in the three trios game.
 */
public class CardCell implements TTCell {
  private boolean empty;
  private Card card;
  private List<Card> neighbors;

  /**
   * A no-arg constructor for the class.
   */
  public CardCell() {
    this.empty = true;
    this.card = null;
  }

  @Override
  public void placeCard(Card card) {
    if (!empty) {
      throw new IllegalStateException("already full!");
    }
    this.card = Objects.requireNonNull(card);
    this.empty = false;
  }

  @Override
  public void flipCard() {
    if (!empty) {
      this.card.flip();
    } else {
      throw new IllegalStateException("cell is empty!");
    }
  }

  @Override
  public Card getCard() {
    if (empty) {
      throw new IllegalStateException("cell is empty!");
    }
    return this.card;
  }

  @Override
  public boolean isEmpty() {
    return this.empty;
  }
}
