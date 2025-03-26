import java.util.List;

/**
 * Makes the move that has the lowest chance of being flipped, picking the appropriate
 * best card and row for this.
 */
public class BestDefense implements AIPlayerStrategy {
  private final Player player;
  private final TTModel model;

  /**
   * Takes in the player this strategy is being used for, as well as the model for operations.
   *
   * @param player the player making the move
   * @param model  the game model
   */
  public BestDefense(Player player, TTModel model) {
    this.player = player;
    this.model = model;
  }

  @Override
  public void makeMove() {
    int bestRow = -1;
    int bestCol = -1;
    Card bestCard = null;
    int minFlipRisk = Integer.MAX_VALUE;
    List<Card> aiCards = model.getHand(player);
    Grid grid = model.getGrid();

    for (int cardIndex = 0; cardIndex < aiCards.size(); cardIndex++) {
      Card card = aiCards.get(cardIndex);
      for (int[] position : grid.getAllEmptyPositions()) {
        int row = position[0];
        int col = position[1];
        int flipRisk = calculateFlipRisk(card, grid);

        if (flipRisk < minFlipRisk ||
                (flipRisk == minFlipRisk && (row < bestRow || (row == bestRow && col < bestCol))) ||
                (flipRisk == minFlipRisk && row == bestRow && col == bestCol
                        && cardIndex < aiCards.indexOf(bestCard))) {
          minFlipRisk = flipRisk;
          bestRow = row;
          bestCol = col;
          bestCard = card;
        }
      }
    }

    if (bestRow == -1 || bestCol == -1 || bestCard == null) {
      int[] openPosition = grid.getFirstOpenPosition();
      bestRow = openPosition[0];
      bestCol = openPosition[1];
      bestCard = aiCards.get(0);
    }

    model.placeCard(aiCards.indexOf(bestCard), bestRow, bestCol);
  }

  private int calculateFlipRisk(Card card, Grid grid) {
    int flipRisk = 0;
    Card[] neighbors = grid.getNeighbors(card);

    for (int i = 0; i < neighbors.length; i++) {
      Card neighborCard = neighbors[i];
      if (neighborCard == null || neighborCard.getPlayer() == card.getPlayer()) {
        continue;
      }

      if (neighborCard.win(card, grid.getDirection(i))) {
        switch (grid.getDirection(i)) {
          case "north":
            flipRisk += neighborCard.getSouth().getValue();
            break;
          case "south":
            flipRisk += neighborCard.getNorth().getValue();
            break;
          case "east":
            flipRisk += neighborCard.getWest().getValue();
            break;
          case "west":
            flipRisk += neighborCard.getEast().getValue();
            break;
          default:
            //do nothing
        }
      }
    }
    return flipRisk;
  }
}
