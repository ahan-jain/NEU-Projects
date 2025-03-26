import cs3500.controllers.TTModelFeatures;

/**
 * An interface that holds all necessary public-facing methods for the Three Trios game that
 * modify the game state.
 */
public interface TTModel extends TTReadOnlyModel {


  /**
   * Executes the game of Three Trios according to the specified grid and cards configurations.
   * INVARIANT: Once the game has started, it cannot be started again.
   *
   * @throws IllegalStateException    if the game has already started or is over.
   * @throws IllegalArgumentException if any parameters are null.
   * @throws IllegalArgumentException if the deck size is not strictly greater than
   *                                  the number of card cells on the grid.
   */
  void startGame(String gridConfigPath, String cardConfigPath);

  void addModelListener(TTModelFeatures listener);

  void removeModelListener(TTModelFeatures listener);

  void notifyTurn(Player player);

  void notifyGameOver(Player winner);


  /**
   * Play a card from hand to the given location.
   * after each valid card placement
   *
   * @param cardIdxInHand the index in hand of the card being played
   * @param row           the row to play to
   * @param col           the col number
   * @throws IllegalStateException    if the game has not started
   * @throws IllegalArgumentException if the card index is invalid
   * @throws IllegalArgumentException if the row or col is invalid
   */
  void placeCard(int cardIdxInHand, int row, int col);

  /**
   * Counts the number of possible flips that a card from a player's hand would make
   * when placed at the given row and col.
   *
   * @param row           the row to place at
   * @param col           the col to place at
   * @param cardIdxInHand the index of the card to place in the player's hand
   * @param player        the player
   * @return the number of possible flips
   */
  int countFlip(int row, int col, int cardIdxInHand, Player player);

  /**
   * Gets the player who owns the card at the given location. Returns null if the location is empty.
   *
   * @param row the row number
   * @param col the col number
   * @return the player who owns the card there
   */
  Player getPlayerAt(int row, int col);

}
