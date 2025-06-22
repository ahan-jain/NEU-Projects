import java.util.List;

/**
 * An interface that holds all public methods for a model that can only be read from, not modified.
 */
public interface TTReadOnlyModel {

  /**
   * Checks if the game is over.
   *
   * @return whether the game is over
   * @throws IllegalStateException if the game has not started
   */
  boolean isGameOver();

  /**
   * Finds the current winning player. Returns null in the case of a tie.
   *
   * @return the winning player
   * @throws IllegalStateException if the game has not started or is not over
   */
  Player findWinningPlayer();

  /**
   * returns the specified player's hand.
   *
   * @return the given player's hand
   * @throws NullPointerException  if the player is null
   * @throws IllegalStateException if the game has not started or is over
   */
  List<Card> getHand(Player player);

  /**
   * outputs the score of the inputted player in the game,
   * by giving the sum of the number of cards owned by the player on the board
   * and the number of cards in the player's hand.
   *
   * @return the score of this player
   */
  int getScore(Player player);

  /**
   * Gets the card at the given location.
   *
   * @param row the 0-indexed row
   * @param col the 0-indexed col
   * @return the card stored there, and null if there is no card.
   */
  Card getContentsAt(int row, int col);

  /**
   * Shows which player's turn it is.
   *
   * @return the player that has to play their turn
   * @throws IllegalStateException if the game has not started or is over
   */
  Player getTurn();

  /**
   * Outputs the grid.
   *
   * @return the grid
   * @throws IllegalStateException if the game has not started or is over
   */
  Grid getGrid();
}
