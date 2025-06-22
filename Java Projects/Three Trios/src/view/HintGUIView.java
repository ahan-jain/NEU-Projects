import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import cs3500.controllers.TTViewFeatures;
import cs3500.model.Player;

/**
 * A GUI view with the ability to show hints. Functions as a decorator for TTGUIView.
 */
public class HintGUIView implements TTGUIView {

  private final TTGUIView delegate;

  /**
   * Constructor that takes in a delegate and initializes with hint functionality.
   * @param delegate the TTGUIView to decorate
   */
  public HintGUIView(TTGUIView delegate) {
    this.delegate = delegate;

    this.delegate.addKeyListener(new KeyListener() {
      @Override
      public void keyTyped(KeyEvent e) {
        // Do nothing
      }

      @Override
      public void keyPressed(KeyEvent e) {
        if (e.getKeyChar() == 'h') {
          System.out.println("Toggling hints");
          toggleHints();
        }
      }

      @Override
      public void keyReleased(KeyEvent e) {
        // Do nothing
      }
    });
  }

  /**
   * Toggles hints in the GUI.
   */
  private void toggleHints() {
    if (delegate instanceof GUIView) {
      ((GUIView) delegate).getGridPanel().toggleHints();
    } else {
      System.err.println("Delegate does not support hint toggling.");
    }
  }

  @Override
  public void updateView() {
    delegate.updateView();
  }

  @Override
  public void showError(String message) {
    delegate.showError(message);
  }

  @Override
  public void addMouseListeners() {
    delegate.addMouseListeners();
  }

  @Override
  public void addViewListener(TTViewFeatures listener) {
    delegate.addViewListener(listener);
  }

  @Override
  public void removeViewListener(TTViewFeatures listener) {
    delegate.removeViewListener(listener);
  }

  @Override
  public void showGameOver(Player winner) {
    delegate.showGameOver(winner);
  }

  @Override
  public void setVisible(boolean b) {
    delegate.setVisible(b);
  }

  @Override
  public void addKeyListener(KeyListener listener) {
    // do nothing
  }
}