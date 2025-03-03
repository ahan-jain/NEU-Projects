package cs3500.solored.view.hw02;

import java.io.IOException;

/**
 * represents the interface for the view component in the MVC model of the Solo Red card game.
 */
public interface RedGameView {


  /**
   * Renders a model in some manner (e.g. as text, or as graphics, etc.).
   * @throws IOException if the rendering fails for some reason
   */
  void render() throws IOException;

}