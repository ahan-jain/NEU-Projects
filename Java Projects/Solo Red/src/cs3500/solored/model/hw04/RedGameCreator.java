package cs3500.solored.model.hw04;

import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw02.SoloRedGameModel;

/**
 * Factory class for creating different variants of the Solo Red Game.
 */
public class RedGameCreator {

  /**
   * represents the versions of the Solo Red game.
   */
  public enum GameType {
    BASIC,
    ADVANCED
  }

  /**
   * Static method to create a game model based on the game type.
   *
   * @param type the type of game to create (BASIC or ADVANCED)
   * @return an instance of RedGameModel (either SoloRedGameModel or AdvancedSoloRedGameModel)
   */
  public static RedGameModel<Cards> createGame(GameType type) {
    switch (type) {
      case BASIC:
        return new SoloRedGameModel();
      case ADVANCED:
        return new AdvancedSoloRedGameModel();
      default:
        throw new IllegalArgumentException("Unknown game type: " + type);
    }
  }
}
