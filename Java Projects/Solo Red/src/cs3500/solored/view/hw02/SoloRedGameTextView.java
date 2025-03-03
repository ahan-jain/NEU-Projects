package cs3500.solored.view.hw02;

import java.io.IOException;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * Displays the Solo Red game in a text format.
 */
public class SoloRedGameTextView<C extends Card> implements RedGameView {
  private final RedGameModel<C> model;
  private String output;
  private Appendable appendable;

  /**
   * represents the constructor for the SoloRedGameTextView view class.
   * @param model represents the model component
   * @param appendable represents an appendable object
   */
  public SoloRedGameTextView(RedGameModel<C> model, Appendable appendable) {
    if (appendable == null) {
      throw new IllegalArgumentException("null appendable");
    }
    this.appendable = appendable;
    this.model = model;
    this.output = "";
  }

  /**
   * Constructor for the view class.
   * @param model refers to the game model.
   */
  public SoloRedGameTextView(RedGameModel<C> model) {
    this.model = model;
    this.output = "";
  }

  @Override
  public String toString() {
    C canvasCard = model.getCanvas();
    String s = "";
    output = "Canvas: " + canvasCard.toString().charAt(0) + "\n";

    for (int i = 0; i < model.numPalettes(); i++) {
      if (i == model.winningPaletteIndex()) {
        output += "> ";
      }
      for (int j = 0; j < model.getPalette(i).size(); j++) {
        s += " " + model.getPalette(i).get(j);
      }
      if (s.isEmpty()) {
        output += "P" + (i + 1) + ": \n";
      } else {
        output += "P" + (i + 1) + ":" + s + "\n";
      }
      s = "";
    }

    for (int x = 0; x < model.getHand().size(); x++) {
      s += " " + model.getHand().get(x);
    }

    if (s.isEmpty()) {
      output += "Hand: ";
    } else {
      output += "Hand:" + s;

    }
    return output;
  }

  @Override
  public void render() throws IOException {
    this.appendable.append(toString());
  }
}
