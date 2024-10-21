package cs3500.solored.view;

import java.io.IOException;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.view.hw02.RedGameView;

/**
 * represents a mock class for the view component of the Solo Red card game.
 * @param <C> is a generic type for the class which extends the Card interface
 */
public class MockSoloRedGameTextView<C extends Card> implements RedGameView {
  private final Appendable log;

  public MockSoloRedGameTextView(Appendable log) {
    this.log = log;
  }

  @Override
  public void render() throws IOException {
    log.append("render called\n");
  }

  @Override
  public String toString() {
    return "toString called";
  }
}
