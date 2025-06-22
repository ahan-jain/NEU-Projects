import cs3500.controllers.TTViewFeatures;
import cs3500.providers.listeners.ViewFeatures;
import cs3500.providers.model.PlayerType;

/**
 * Implements the providers' ViewFeatures and simulates the necessary actions
 * using our TTViewFeatures class.
 */
public class ViewFeaturesAdapter implements ViewFeatures {
  private final TTViewFeatures delegate;

  public ViewFeaturesAdapter(TTViewFeatures delegate, PlayerType player) {
    this.delegate = delegate;
  }


  @Override
  public void selectedCard(int index, PlayerType owner) {
    delegate.selectCard(index);
  }

  @Override
  public void selectedCell(int row, int col) {
    delegate.selectPosition(row, col - 1);
  }
}
