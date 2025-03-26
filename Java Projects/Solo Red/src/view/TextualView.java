import java.io.IOException;

import cs3500.model.Card;
import cs3500.model.CardCell;
import cs3500.model.Hole;
import cs3500.model.Player;
import cs3500.model.TTCell;
import cs3500.model.TTReadOnlyModel;

/**
 * Represents a Textual output of a specific point in the Three Trios Game.
 * It outputs the current player's turn along with the state of the grid, with "_" meaning
 * empty card cells, " " meaning holes, and "R" or "B" denoting that players card.
 * It also outputs the hand of the player with the active turn.
 */
public class TextualView implements TTView {

  private final TTReadOnlyModel model;
  protected Appendable ap;

  /**
   * A constructor that takes in a model.
   *
   * @param model the model to render
   */
  public TextualView(TTReadOnlyModel model) {
    this.model = model;
    this.ap = new StringBuilder();
  }

  /**
   * A constructor that takes in a model and an appendable for testing.
   *
   * @param model the model to render
   * @param log   the appendable to log to
   */
  public TextualView(TTReadOnlyModel model, Appendable log) {
    this.model = model;
    this.ap = log;
  }

  @Override
  public String toString() {
    StringBuilder output = new StringBuilder();

    Player currentPlayer = model.getTurn();
    output.append("Player: ").append(currentPlayer).append("\n");

    TTCell[][] grid = model.getGrid().getGrid();
    for (TTCell[] cells : grid) {
      for (int col = 0; col < grid[0].length; col++) {
        TTCell cell = cells[col];
        if (cell instanceof Hole) {
          output.append(" ");
        } else {
          if (cell instanceof CardCell && cell.isEmpty()) {
            output.append("_");
          } else {
            Card card = cell.getCard();
            if (card.getPlayer().equals(Player.BLUE)) {
              output.append("B");
            } else if (card.getPlayer().equals(Player.RED)) {
              output.append("R");
            }
          }
        }
      }
      output.append("\n");
    }

    output.append("Hand:\n");
    for (int i = 0; i < model.getHand(currentPlayer).size(); i++) {
      Card card = model.getHand(currentPlayer).get(i);
      output.append(card.getCardName()).append(" ")
              .append(card.getNorth().getStringValue()).append(" ")
              .append(card.getSouth().getStringValue()).append(" ")
              .append(card.getEast().getStringValue()).append(" ")
              .append(card.getWest().getStringValue());
      if (i != model.getHand(currentPlayer).size() - 1) {
        output.append("\n");
      }
    }

    return output.toString();
  }

  @Override
  public void render() throws IOException {
    this.ap.append(this.toString());
  }
}
