import java.io.IOException;

/**
 * Represents the view component in the MVC (Model-View-Controller) architecture,
 * for the Three Trios game.This interface defines the operations required for rendering a
 * view of the game.
 */
public interface TTView {

  /**
   * renders a textual view of the Three Trios game according to the current player's turn.
   */
  void render() throws IOException;
}
