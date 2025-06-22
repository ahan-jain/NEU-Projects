import cs3500.providers.listeners.PlayerListener;


/**
 * Adapts the provider's PlayerListener interface to work with our controller.
 */
public class PlayerListenerAdapter implements PlayerListener {
  private final AbstractController controller;
  private final PlayerActions delegate;

  /**
   * Constructs a PlayerListenerAdapter.
   *
   * @param controller the controller to adapt the PlayerListener to
   */
  public PlayerListenerAdapter(AbstractController controller, PlayerActions delegate) {
    this.controller = controller;
    this.delegate = delegate;
  }

  @Override
  public void runPlay(int index, int row, int col) {
    controller.selectCard(index);
    controller.selectPosition(row, col);
    delegate.makeMove();
  }
}
