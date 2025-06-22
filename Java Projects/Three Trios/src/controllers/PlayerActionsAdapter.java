import cs3500.providers.listeners.PlayerListener;
import cs3500.providers.PlayerActions;
import cs3500.providers.model.PlayerType;

import java.util.ArrayList;
import java.util.List;

/**
 * A wrapper class for PlayerActions that supports a list of PlayerListeners.
 * Delegates actions to the underlying PlayerActions and notifies all registered listeners.
 */
public class PlayerActionsAdapter implements PlayerActions {
  private final PlayerActions delegate;
  private final List<PlayerListener> listeners;

  /**
   * Constructs a FeatureAddingPlayerActions wrapper.
   *
   * @param delegate the PlayerActions instance to wrap
   */
  public PlayerActionsAdapter(PlayerActions delegate) {
    this.delegate = delegate;
    this.listeners = new ArrayList<>();
  }

  /**
   * Adds a PlayerListener to the list of listeners.
   *
   * @param listener the listener to add
   */
  public void addPlayerListener(PlayerListener listener) {
    this.listeners.add(listener);
  }

  @Override
  public void createPlay(int index, int row, int col) {
    delegate.createPlay(index, row, col);

    for (PlayerListener listener : listeners) {
      listener.runPlay(index, row, col);
    }
  }

  @Override
  public void addFeatureListener(PlayerListener listener) {
    addPlayerListener(listener);
  }

  @Override
  public PlayerType getPlayerType() {
    return delegate.getPlayerType();
  }
}
