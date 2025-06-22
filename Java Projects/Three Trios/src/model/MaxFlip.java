import java.util.List;

/**
 * A strategy that looks for the move that flips the max amount of cards.
 */
public class MaxFlip implements AIPlayerStrategy {
  private final Player player;
  private final TTModel model;

  /**
   * Takes in the player this strategy is being used for, as well as the model for operations.
   *
   * @param player the player making the move
   * @param model  the game model
   */
  public MaxFlip(Player player, TTModel model) {
    this.player = player;
    this.model = model;
  }

  @Override
  public void makeMove() {
    List<Card> hand = player == Player.RED ? model.getHand(Player.RED) : model.getHand(Player.BLUE);
    int maxFlips = -1;
    int bestCardIndex = -1;
    int bestRow = -1;
    int bestCol = -1;
    for (int cardIndex = 0; cardIndex < hand.size(); cardIndex++) {
      for (int row = 0; row < model.getGrid().getGridRows(); row++) {
        for (int col = 0; col < model.getGrid().getGridCols(); col++) {
          if (model.getGrid().getCell(row, col).isEmpty()) {
            int flips = model.countFlip(row, col, cardIndex, player);

            if (flips > maxFlips) {
              maxFlips = flips;
              bestCardIndex = cardIndex;
              bestRow = row;
              bestCol = col;
            }
          }
        }
      }
    }

    if (bestRow == -1 || bestCol == -1) {
      int[] openPosition = model.getGrid().getFirstOpenPosition();
      bestRow = openPosition[0];
      bestCol = openPosition[1];
      bestCardIndex = 0;
    }

    model.placeCard(bestCardIndex, bestRow, bestCol);
  }
}
