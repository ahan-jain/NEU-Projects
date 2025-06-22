import java.util.List;

/**
 * Checks the four corners of the grid from top left, top right, bottom left, bottom right and
 * making the best move accordingly.
 */
public class CheckCorners implements AIPlayerStrategy {
  private final Player player;
  private final TTModel model;

  /**
   * Takes in the player this strategy is being used for, as well as the model for operations.
   *
   * @param player the player making the move
   * @param model  the game model
   */
  public CheckCorners(Player player, TTModel model) {
    this.player = player;
    this.model = model;
  }

  @Override
  public void makeMove() {
    if (isCornerAvailable(0, 0)) {
      int cardIndex = getBestCardIndexCorner("topLeft");
      model.placeCard(cardIndex, 0, 0);
      return;
    }
    if (isCornerAvailable(0, model.getGrid().getGridCols() - 1)) {
      int cardIndex = getBestCardIndexCorner("topRight");
      model.placeCard(cardIndex, 0, model.getGrid().getGridCols() - 1);
      return;
    }
    if (isCornerAvailable(model.getGrid().getGridRows() - 1, 0)) {
      int cardIndex = getBestCardIndexCorner("bottomLeft");
      model.placeCard(cardIndex, model.getGrid().getGridRows() - 1, 0);
      return;
    }
    if (isCornerAvailable(model.getGrid().getGridRows() - 1,
            model.getGrid().getGridCols() - 1)) {
      int cardIndex = getBestCardIndexCorner("bottomRight");
      model.placeCard(cardIndex, model.getGrid().getGridRows() - 1,
              model.getGrid().getGridCols() - 1);
      return;
    }

    int[] openPosition = model.getGrid().getFirstOpenPosition();
    if (openPosition != null) {
      model.placeCard(0, openPosition[0], openPosition[1]);
    }
  }

  private boolean isCornerAvailable(int row, int col) {
    return model.getGrid().getCell(row, col) instanceof CardCell
            && model.getGrid().getCell(row, col).isEmpty();
  }

  private int getBestCardIndexCorner(String position) {
    int highAverage = 0;
    int highIndex = 0;
    List<Card> hand = player == Player.RED ? model.getHand(Player.RED) : model.getHand(Player.BLUE);

    for (int i = 0; i < hand.size(); i++) {
      Card card = hand.get(i);
      int average = getCornerAverage(position, card);
      if (average > highAverage) {
        highAverage = average;
        highIndex = i;
      }
    }
    return highIndex;
  }

  private int getCornerAverage(String position, Card card) {
    int average = 0;
    switch (position) {
      case "topLeft":
        average += card.getEast().getValue() + card.getSouth().getValue();
        break;
      case "topRight":
        average += card.getWest().getValue() + card.getSouth().getValue();
        break;
      case "bottomLeft":
        average += card.getNorth().getValue() + card.getEast().getValue();
        break;
      case "bottomRight":
        average += card.getNorth().getValue() + card.getWest().getValue();
        break;
      default:
        // do nothing
    }
    return average;
  }
}
