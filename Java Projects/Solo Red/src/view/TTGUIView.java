import java.awt.event.KeyListener;

import cs3500.controllers.TTViewFeatures;

import cs3500.model.Player;

/**
 * Represents a graphical user interface view for the Triple Trios game.
 * This interface defines methods to update the view based on changes in the game state.
 */
public interface TTGUIView {
  /**
   * Updates the display of the GUI to reflect the current state of the game.
   * This method should be called whenever there is a change in the game state
   * that needs to be reflected in the GUI, such as after a move is made or
   * when the game is initialized.
   */
  void updateView();

  /**
   * Displays the given error as a dialog box.
   * @param message the message to display
   */
  void showError(String message);

  /**
   * Registers mouse listeners for all view components, ensuring clicks lead to real action.
   */
  void addMouseListeners();

  /**
   * Adds a ViewFeatures listener to ensure that the controller registers itself as a listener for
   * the view.
   * @param listener the controller
   */
  void addViewListener(TTViewFeatures listener);

  /**
   * Removes a listener, if needed.
   * @param listener the listener to remove.
   */
  void removeViewListener(TTViewFeatures listener);

  /**
   * Shows the game over notification as a dialog box to this view.
   * @param winner the winner of the game!
   */
  void showGameOver(Player winner);

  /**
   * Makes this frame visible, depending on the boolean passed.
   * @param b whether or not to make this frame visible
   */
  void setVisible(boolean b);

  void addKeyListener(KeyListener listener);
}
