import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

/**
 * Represents a grid in this game.
 * The grid is a 2D array of TTCell objects that holds the state of each cell on the game board.
 * The grid operates on a 0-index coordinate system.
 */
public class Grid {
  private TTCell[][] grid;

  /**
   * A constructor that initializes grid to a 2D array of set size.
   *
   * @param numRows the number of rows
   * @param numCols the number of columns
   */
  public Grid(int numRows, int numCols) {
    if (numRows < 0 || numCols < 0) {
      throw new IllegalArgumentException("invalid arguments! size cannot be negative.");
    }
    this.grid = new TTCell[numRows][numCols];
  }

  public Grid(TTCell[][] grid) {
    this.grid = grid;
  }


  /**
   * Adds a cell to the grid at the given location.
   *
   * @param row  the row
   * @param col  the column num
   * @param cell the cell to be added
   */
  public void addCell(int row, int col, TTCell cell) {
    checkRowColConditions(row, col);
    grid[row][col] = Objects.requireNonNull(cell);
  }

  /**
   * Place a card at the given location.
   *
   * @param row  the row num
   * @param col  the col num
   * @param card the card to be added
   * @throws IllegalArgumentException if row and col are not within bounds.
   * @throws IllegalStateException    if the cell is a hole or already full
   */
  public void placeCardHelper(int row, int col, Card card) {
    checkRowColConditions(row, col);
    this.getGrid()[row][col].placeCard(Objects.requireNonNull(card));
    this.comboFlip(row, col);
  }

  private boolean checkRowColConditions(int row, int col) {
    if (row >= grid.length || col >= grid[0].length || row < 0 || col < 0) {
      throw new IllegalArgumentException("row and col must be within grid size!");
    }
    return true;
  }

  /**
   * Returns the count of all card Cells found in the grid.
   *
   * @return this count
   */
  public int countCardCells() {
    int count = 0;
    for (TTCell[] c : grid) {
      for (TTCell tc : c) {
        if (tc instanceof CardCell) {
          count++;
        }
      }
    }
    return count;
  }

  /**
   * Counts the amount of cards that belong to the given player.
   *
   * @param player the player we're counting for
   * @return the card count
   */
  protected int playerCardCount(Player player) {
    int count = 0;
    for (TTCell[] c : grid) {
      for (TTCell tc : c) {
        if (!(tc instanceof CardCell) || tc.isEmpty()) {
          continue;
        }
        if (tc.getCard().getPlayer() == player) {
          count++;
        }
      }
    }
    return count;
  }


  /**
   * Flips cards recursively based on their neighbors when a card is placed.
   * This method checks the card placed at the given row and column, then examines its neighboring
   * cards (North, South, East, West). If the placed card "wins" over a neighboring card (based on
   * game-specific logic), the neighboring card is flipped. The process is repeated for any
   * flipped card, thus creating a chain reaction or "combo flip".
   *
   * @param row the row number of the placed card.
   * @param col the column number of the placed card.
   * @throws IllegalArgumentException if row or col are out of bounds.
   */
  public void comboFlip(int row, int col) {
    checkRowColConditions(row, col);
    Card placedCard = getCell(row, col).getCard();
    Card[] neighbors = getNeighbors(placedCard);
    for (int i = 0; i < 4; i++) {
      Card curCard = neighbors[i];

      if (curCard == null || (curCard.getPlayer() == placedCard.getPlayer())) {
        continue;
      }

      if (placedCard.win(curCard, this.getDirection(i))) {
        curCard.flip();
        int[] location = getLocation(curCard);
        comboFlip(location[0], location[1]);
      }
    }
  }

  /**
   * Finds the location of the given card on the grid. It iterates over the grid to match the
   * card's name and returns its coordinates as an array.
   *
   * @param card the card whose location is to be found.
   * @return an array of two integers representing the row and column of the card.
   *         If the card is not found, returns an empty array.
   */
  int[] getLocation(Card card) {
    for (int i = 0; i < this.grid.length; i++) {
      for (int j = 0; j < this.grid[0].length; j++) {
        if (grid[i][j] instanceof CardCell) {
          Card curCard;
          try {
            curCard = grid[i][j].getCard();
          } catch (IllegalStateException e) {
            continue;
          }
          if (curCard.getCardName().equals(card.getCardName())) {
            return new int[]{i, j};
          }
        }
      }
    }
    return new int[0];
  }


  /**
   * Retrieves the neighboring cards (North, East, West, South) of the given card on the grid.
   * Neighbors are determined by looking one step in each direction from the card's location.
   * If a neighboring cell is valid and contains a card, the card is added to the array.
   * If a neighboring cell is invalid, out of bounds, or empty, null is placed in the array.
   * The indices  of the return array correspond to the directions:
   * - 0: North
   * - 1: East
   * - 2: West
   * - 3: South
   *
   * @param card the card for which neighbors are being sought.
   * @return an array of four `Card` objects representing the neighboring cards.
   */
  protected Card[] getNeighbors(Card card) {
    int[] arr = getLocation(card);
    Card[] neighbors = new Card[4];
    ArrayList<Integer> coordinates = new ArrayList<>(Arrays.asList(arr[0] - 1, arr[1]
            , arr[0], arr[1] + 1,
            arr[0], arr[1] - 1,
            arr[0] + 1, arr[1]));

    int count = 0;
    while (!coordinates.isEmpty()) {
      if (checkNeighbor(coordinates.get(0), coordinates.get(1))) {
        neighbors[count] = this.grid[coordinates.get(0)][coordinates.get(1)].getCard();
      } else {
        neighbors[count] = null;
      }
      count++;
      coordinates.remove(0);
      coordinates.remove(0);
    }
    return neighbors;
  }


  /**
   * Checks if the given row and column refer to a valid neighboring cell.
   * A valid neighbor is a cell that is within bounds, is a `CardCell`, and is not empty.
   *
   * @param row the row index of the potential neighbor.
   * @param col the column index of the potential neighbor.
   * @return true if the cell is a valid neighbor; false otherwise.
   */
  private boolean checkNeighbor(int row, int col) {
    try {
      return checkRowColConditions(row, col) &&
              (this.grid[row][col] instanceof CardCell)
              && !this.grid[row][col].isEmpty();
    } catch (IllegalArgumentException iae) {
      return false;
    }
  }


  /**
   * Translates an integer index into a string representing the cardinal direction.
   * This method maps the direction numbers (0-3) used in the neighbor-checking logic
   * to their corresponding string representations.
   * - 0: North
   * - 1: East
   * - 2: West
   * - 3: South
   *
   * @param i the index representing the direction (0-3).
   * @return a string ("north", "east", "west", "south") representing the direction.
   * @throws IllegalArgumentException if the index is out of bounds.
   */
  String getDirection(int i) {
    switch (i) {
      case 0:
        return "north";
      case 1:
        return "east";
      case 2:
        return "west";
      case 3:
        return "south";
      default:
        throw new IllegalArgumentException("invalid direction " + i);
    }
  }


  /**
   * Returns a mutable version of the cell at the given location.
   *
   * @param row the row num
   * @param col the col num
   * @return the cell at the given location
   */
  public TTCell getCell(int row, int col) {
    if (row >= grid.length || col >= grid[0].length) {
      throw new IllegalArgumentException("row and col must be within grid size!");
    }
    return grid[row][col];
  }

  /**
   * Outputs an immutable copy of the grid.
   *
   * @return grid
   */
  public TTCell[][] getGrid() {
    return this.grid.clone();
  }

  /**
   * Counts the number of cards that would be flipped if the
   * given card were placed at the given location.
   *
   * @param row        the row to place at
   * @param col        the col to place at
   * @param placedCard the card to place
   * @return the number of cards that would be flipped
   */
  public int countFlip(int row, int col, Card placedCard, boolean gui) {
    Grid gridCopy = new Grid(this.getGrid());
    checkRowColConditions(row, col);
    if(!gui) {
      placeCardHelper(row, col, placedCard);
    }
    if (getCell(row, col) instanceof CardCell && getCell(row, col).isEmpty()) {
      gridCopy.placeCardHelper(row, col, placedCard);
    }
    return countFlipsRecursive(gridCopy, row, col, placedCard);
  }

  private int countFlipsRecursive(Grid grid, int row, int col, Card placedCard) {
    int count = 0;
    Card[] neighbors = grid.getNeighbors(placedCard);
    for (int i = 0; i < neighbors.length; i++) {
      Card neighbor = neighbors[i];
      if (neighbor == null || neighbor.getPlayer() == placedCard.getPlayer()) {
        continue;
      }
      if (placedCard.win(neighbor, grid.getDirection(i))) {
        int[] location = grid.getLocation(neighbor);
        Card card = grid.getContentsAt(location[0], location[1]);
        Card cardCopy = new Card(card.getCardName() + " " + card.getNorth().getStringValue()
                + " " + card.getSouth().getStringValue() + " " + card.getEast().getStringValue() +
                " " + card.getWest().getStringValue());
        grid.getGrid()[location[0]][location[1]] = new CardCell();
        grid.getGrid()[location[0]][location[1]].placeCard(cardCopy);
        count++;
        count += countFlipsRecursive(grid, location[0], location[1], neighbor);
      }
    }

    return count;
  }


  /**
   * Returns the card at the given location.
   *
   * @param row the row
   * @param col the col
   * @return the card stored there
   */
  public Card getContentsAt(int row, int col) {
    Card card = null;
    if (grid[row][col] instanceof CardCell) {
      if (!grid[row][col].isEmpty()) {
        card = grid[row][col].getCard();
      }
    }
    return card;
  }

  /**
   * outputs the number of rows in the grid.
   *
   * @return the number of rows
   */
  public int getGridRows() {
    return this.grid.length;
  }

  /**
   * outputs the number of columns in the grid.
   *
   * @return the number of columns
   */
  public int getGridCols() {
    return this.grid[0].length;
  }

  /**
   * Finds all empty positions on the grid.
   *
   * @return an array of empty positions
   */
  protected int[][] getAllEmptyPositions() {

    ArrayList<int[]> emptyPositions = new ArrayList<>();

    for (int row = 0; row < grid.length; row++) {
      for (int col = 0; col < grid[0].length; col++) {
        if (grid[row][col].isEmpty()) {
          emptyPositions.add(new int[]{row, col});
        }
      }
    }


    return emptyPositions.toArray(new int[0][]);
  }

  /**
   * Finds the first empty position on the grid.
   * @return an array of the first empty position
   */
  protected int[] getFirstOpenPosition() {
    for (int row = 0; row < getGridRows(); row++) {
      for (int col = 0; col < getGridCols(); col++) {
        if (getCell(row, col).isEmpty()) {
          return new int[]{row, col};
        }
      }
    }
    return new int[]{-1, -1}; // No empty positions, if needed
  }

}

