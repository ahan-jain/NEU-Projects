import cs3500.providers.model.ICard;
import cs3500.providers.model.ICell;
import cs3500.providers.model.IGrid;
import cs3500.providers.model.IPlayer;
import cs3500.providers.model.PlayerType;

/**
 * Adapts the class Grid to IGrid, ensuring that all needed functionality for provider .
 */
public class GridAdapter implements IGrid {
  private final Grid grid;
  private final TTModel model;

  public GridAdapter(Grid grid, TTModel model) {
    this.model = model;
    this.grid = grid;
  }

  @Override
  public void playCardToCell(int row, int col, ICard card, IPlayer owner) {
    grid.placeCardHelper(row, col, CardAdapter.convertICardToCard(card, owner));
  }

  @Override
  public ICell getCell(int row, int col) {
    return new CellAdapter(model, grid.getCell(row, col));
  }

  @Override
  public void setCell(int row, int col, ICell cell) {
    grid.getGrid()[row][col] = CellAdapter.convertICellToTTCell(cell);
  }

  @Override
  public IGrid getGridCopy() {
    return new GridAdapter(new Grid(copy2DArray(grid.getGrid())), model);
  }

  private static TTCell[][] copy2DArray(TTCell[][] original) {
    TTCell[][] copy = new TTCell[original.length][];
    for (int i = 0; i < original.length; i++) {
      copy[i] = new TTCell[original[i].length];
      for (int j = 0; j < original[i].length; j++) {
        if (original[i][j] != null) {
          copy[i][j] = original[i][j];
        }
      }
    }
    return copy;
  }


  @Override
  public boolean isGridFilled() {
    return grid.getAllEmptyPositions().length == 0;
  }

  @Override
  public int numCellsBelongingTo(IPlayer player) {
    return grid.playerCardCount(player.getType() == PlayerType.RED ? Player.RED : Player.BLUE);
  }
}
