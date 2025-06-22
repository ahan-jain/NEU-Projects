import Player;

/**
 * Interface for defining the actions a player can perform in the game.
 */
public interface PlayerActions {
  /**
   * Allows the player to make their move during their turn.
   */
  void makeMove();

  /**
   * Gets the player who this PlayerActions object serves.
   *
   * @return the player color
   */
  Player getPlayer();
}
