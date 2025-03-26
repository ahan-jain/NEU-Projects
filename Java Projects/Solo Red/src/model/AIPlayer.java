import cs3500.controllers.PlayerActions;

/**
 * Represents an AI player in the game.
 */
public class AIPlayer implements PlayerActions {
  private final Player player;
  private final AIPlayerStrategy strategy;

  public AIPlayer(Player player, AIPlayerStrategy strategy) {
    this.player = player;
    this.strategy = strategy;
  }

  @Override
  public void makeMove() {
    System.out.println("AI player (" + player + ") is making a move...");
    try {
      strategy.makeMove();
    } catch (Exception e) {
      System.out.println("AI encountered an error: " + e.getMessage());
    }
  }

  @Override
  public Player getPlayer() {
    return this.player;
  }
}
