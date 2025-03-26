import java.util.ArrayList;
import java.util.List;

import cs3500.providers.listeners.ModelStatus;
import cs3500.providers.model.ICard;
import cs3500.providers.model.ICell;
import cs3500.providers.model.IGrid;
import cs3500.providers.model.IPlayer;
import cs3500.providers.model.PlayerType;
import cs3500.providers.model.ReadOnlyThreeTriosModel;
import cs3500.providers.model.ThreeTriosModel;

/**
 * Adapts the providers' model to ensure that our model can work with the provided views.
 */
public class TTModelAdapter implements ThreeTriosModel<ICard> {
  private final TTModel delegate;
  private final String cardPath;
  private final String gridPath;

  /**
   * A constructor that takes in a delegate and adapts that delegate to ThreeTriosModel.
   *
   * @param delegate the class to be adapted, an object of TTModel.
   */
  public TTModelAdapter(TTModel delegate, String cardPath, String gridPath) {
    this.delegate = delegate;
    this.cardPath = cardPath;
    this.gridPath = gridPath;
  }

  @Override
  public void addFeatureListener(ModelStatus status) {
    delegate.addModelListener(new ModelStatusAdapter(status));
  }

  @Override
  public IPlayer currentPlayer() {
    Player player = delegate.getTurn();
    return new PlayerAdapter(delegate, player);
  }

  @Override
  public IPlayer winningPlayer() {
    Player player = delegate.findWinningPlayer();
    return new PlayerAdapter(delegate, player);
  }

  @Override
  public int gridHeight() {
    return delegate.getGrid().getGridCols();
  }

  @Override
  public int gridWidth() {
    return delegate.getGrid().getGridRows();
  }

  @Override
  public ICell cellContent(int row, int col) {
    return new CellAdapter(delegate, delegate.getGrid().getCell(row, col));
  }

  @Override
  public IPlayer ownsCell(int row, int col) {
    Card c = delegate.getContentsAt(row, col);
    if (c != null) {
      return new PlayerAdapter(delegate, c.getPlayer());
    }
    return null;
  }

  @Override
  public boolean canPlayAt(int row, int col) {
    return delegate.getGrid().getCell(row, col).isEmpty();
  }

  @Override
  public int canFlipNumCards(ICard cardToPlay, int rowToPlay, int colToPlay) {
    return 0;
  }

  @Override
  public int scoreOf(IPlayer player) {
    return player.getType() == PlayerType.RED ?
            delegate.getScore(Player.RED) : delegate.getScore(Player.BLUE);
  }

  @Override
  public boolean isGameOver() {
    return delegate.isGameOver();
  }

  @Override
  public List<ICard> getPlayerRHand() {
    List<Card> oldHand = delegate.getHand(Player.RED);
    List<ICard> newHand = new ArrayList<>();
    for (Card c : oldHand) {
      newHand.add(new CardAdapter(c));
    }
    return newHand;
  }

  @Override
  public List<ICard> getPlayerBHand() {
    List<Card> oldHand = delegate.getHand(Player.BLUE);
    List<ICard> newHand = new ArrayList<>();
    for (Card c : oldHand) {
      newHand.add(new CardAdapter(c));
    }
    return newHand;
  }

  @Override
  public String[][] getGridStringView() {
    TTCell[][] grid = delegate.getGrid().getGrid();
    String[][] output = new String[grid.length][grid[0].length];
    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        TTCell cell = grid[row][col];
        if (cell instanceof Hole) {
          output[row][col] = " ";
        } else {
          if (cell instanceof CardCell && cell.isEmpty()) {
            output[row][col] = "_";
          } else {
            Card card = cell.getCard();
            if (card.getPlayer().equals(Player.BLUE)) {
              output[row][col] = "B";
            } else if (card.getPlayer().equals(Player.RED)) {
              output[row][col] = "R";
            }
          }
        }
      }
    }
    return output;
  }

  @Override
  public IGrid getGrid() {
    return new GridAdapter(delegate.getGrid(), delegate);
  }

  @Override
  public void startGame() {
    delegate.startGame(gridPath, cardPath);
  }

  @Override
  public void playToCell(int index, int row, int col) {
    delegate.placeCard(index, row, col);
  }

  @Override
  public ReadOnlyThreeTriosModel<ICard> getReadOnlyCopy() {
    return this;
  }
}
