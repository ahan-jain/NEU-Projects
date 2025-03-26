import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

/**
 * A mouse listener for handling mouse events in the Three Trios game.
 * This class defines the behavior for mouse interactions on game components.
 * This class is only defined as a blank MouseListener template so that
 * it is easy to override mouseClicked in GUIView via inheritance.
 */
public class TTMouseListener implements MouseListener {

  /**
   * Invoked when the mouse is clicked (pressed and released) on a component.
   * Handles clicks on components, determining which cell was clicked and triggering actions
   * such as highlighting or outputting cell information.
   *
   * @param e the object containing details of the mouse click.
   */
  @Override
  public void mouseClicked(MouseEvent e) {
    //no action needed


  }

  /**
   * Invoked when a mouse button has been pressed on a component.
   * This method is not implemented and performs no action.
   *
   * @param e the object containing details of the mouse press.
   */
  @Override
  public void mousePressed(MouseEvent e) {
    // No action on mouse press
  }

  /**
   * Invoked when a mouse button has been released on a component.
   * This method is not implemented and performs no action.
   *
   * @param e the object containing details of the mouse release.
   */
  @Override
  public void mouseReleased(MouseEvent e) {
    // No action on mouse release
  }

  /**
   * Invoked when the mouse enters a component.
   * This method is not implemented and performs no action.
   *
   * @param e the object containing details of the mouse entering.
   */
  @Override
  public void mouseEntered(MouseEvent e) {
    // No action on mouse enter
  }

  /**
   * Invoked when the mouse exits a component.
   * This method is not implemented and performs no action.
   *
   * @param e the object containing details of the mouse exiting.
   */
  @Override
  public void mouseExited(MouseEvent e) {
    // No action on mouse exit
  }
}
