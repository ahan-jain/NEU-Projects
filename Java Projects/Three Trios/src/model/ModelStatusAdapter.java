import cs3500.controllers.TTModelFeatures;
import cs3500.providers.listeners.ModelStatus;

/**
 * An adapter for ModelStatus that adapts it to simulate TTModelFeatures.
 */
public class ModelStatusAdapter implements TTModelFeatures {
  private final ModelStatus delegate;

  /**
   * A constructor that takes in a delegate to simulate TTModelFeatures operations.
   *
   * @param delegate a delegate of type ModelStatus.
   */
  public ModelStatusAdapter(ModelStatus delegate) {
    this.delegate = delegate;
  }

  @Override
  public void onTurn(Player player) {
    delegate.turnChanged();
  }

  @Override
  public void onGameOver(Player winner) {
    delegate.gameOver();
  }
}
