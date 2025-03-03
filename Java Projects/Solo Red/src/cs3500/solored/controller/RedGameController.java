package cs3500.solored.controller;

import java.util.List;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * represents the interface for the controller in the MVC model for the Solo Red card game.
 */
public interface RedGameController {

  /**
   * Plays a new game of Solo Red using the provided model.
   * @param model       the model to use for the game
   * @param deck        the deck of cards to use for the game
   * @param shuffle     whether to shuffle the deck
   * @param numPalettes the number of palettes to use
   * @param handSize    the size of the player's hand
   * @throws IllegalArgumentException if the provided model is null or the game cannot be started
   * @throws IllegalStateException    if the controller is unable to successfully receive input or
   *                                  transmit output
   */
  <C extends Card> void playGame(RedGameModel<C> model, List<C> deck, boolean shuffle,
                                 int numPalettes, int handSize);
}
