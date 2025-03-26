import Player;

/**
 * Features needed for the model to interact with the controller.
 */
public interface TTModelFeatures {
  /**
   * Notifies that it's the given player's turn.
   * @param player the player whose turn it is
   */
  void onTurn(Player player);

  /**
   * Notifies that the game is over and provides the winner.
   * @param winner the winner of the game, or null if it's a tie
   */
  void onGameOver(Player winner);
}
