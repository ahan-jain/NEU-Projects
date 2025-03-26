import cs3500.controllers.PlayerActions;

/**
 * Holds needed behaviors for a human player in the game.
 */
public class HumanPlayer implements PlayerActions {
  private final Player player;

  /**
   * A constructor for the HumanPlayer class that takes in a Player.
   *
   * @param player the color for this player
   */
  public HumanPlayer(Player player) {
    this.player = player;
  }

  @Override
  public void makeMove() {
    // Do nothing, but this stub is needed to ensure the AI player functions properly.
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }
}
