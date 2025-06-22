import java.util.List;

/**
 * An adapter for the TTModel that only allows for reading from the model.
 */
public class ViewModel implements TTReadOnlyModel {
  private final TTReadOnlyModel model;

  public ViewModel(TTReadOnlyModel model) {
    this.model = model;
  }

  @Override
  public boolean isGameOver() {
    return model.isGameOver();
  }

  @Override
  public Player findWinningPlayer() {
    return model.findWinningPlayer();
  }

  @Override
  public List<Card> getHand(Player player) {
    return model.getHand(player);
  }

  @Override
  public int getScore(Player player) {
    return model.getScore(player);
  }

  @Override
  public Card getContentsAt(int row, int col) {
    return model.getContentsAt(row, col);
  }

  @Override
  public Player getTurn() {
    return model.getTurn();
  }

  @Override
  public Grid getGrid() {
    return model.getGrid();
  }
}