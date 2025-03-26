import java.awt.event.KeyListener;
import java.io.IOException;

import javax.swing.JOptionPane;

import cs3500.controllers.TTViewFeatures;
import cs3500.model.Player;
import cs3500.model.ViewModel;
import cs3500.providers.model.PlayerType;
import cs3500.providers.view.text.ThreeTriosView;

/**
 * An adapter from the ThreeTriosView interface to our view. Ensures all
 * functionality from the
 * TTGUIView interface is carried over and simulated with providers' view
 * implementation.
 */
public class ViewAdapter implements TTGUIView {

  private final ThreeTriosView delegate;

  private final ViewModel model;

  private boolean shown = false;

  /**
   * Initializes this view adapter with the needed fields.
   * 
   * @param delegate a delegate view made by the providers
   * @param model    a viewModel to read info from
   */
  public ViewAdapter(ThreeTriosView delegate, ViewModel model) {
    this.delegate = delegate;
    this.model = model;
  }

  @Override
  public void updateView() {
    try {
      delegate.render();
    } catch (IOException ignored) {

    }

  }

  @Override
  public void showError(String message) {
    delegate.error(message);
  }

  @Override
  public void addMouseListeners() {
    // method in ThreeTriosPanel

  }

  @Override
  public void addViewListener(TTViewFeatures listener) {
    delegate.addFeatureListener(new ViewFeaturesAdapter(listener,
        model.getTurn() == Player.RED ? PlayerType.RED : PlayerType.BLUE));
  }

  @Override
  public void removeViewListener(TTViewFeatures listener) {
    // cannot implement

  }

  @Override
  public void showGameOver(Player winner) {
    if (!shown) {
      String msg = "Game Over!\n" + model.findWinningPlayer().toString() + " won!";
      JOptionPane.showMessageDialog(null, msg);
      shown = true;
    }
  }

  @Override
  public void setVisible(boolean b) {
    // do nothing
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    // do nothing
  }
}
