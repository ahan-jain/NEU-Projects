
package cs3500.solored;

import java.io.InputStreamReader;

import cs3500.solored.controller.SoloRedTextController;
import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.model.hw04.RedGameCreator;

/**
 * Main class for running different variants of the Solo Red Game.
 */
public final class SoloRed {

  /**
   * Represents the main method where the user inputs the command to launch,
   * a basic or advanced version of the Solo Red game.
   * @param args is the parameter which stores the input given by the player.
   */
  public static void main(String[] args) {

    RedGameCreator.GameType gameType;
    String gameTypeStr;
    int numPalettes = 0;
    int handSize = 0;

    if (args.length == 0) {
      return;
    }

    gameTypeStr = args[0];

    SoloRedTextController controller = new SoloRedTextController(new InputStreamReader(System.in),
            System.out);

    switch (gameTypeStr.toLowerCase()) {
      case "basic":
        gameType = RedGameCreator.GameType.BASIC;
        break;
      case "advanced":
        gameType = RedGameCreator.GameType.ADVANCED;
        break;
      default:
        throw new IllegalArgumentException("Unknown game type: " + gameTypeStr);
    }

    if (args.length == 1) {
      numPalettes = 4;
      handSize = 7;
    }

    if (args.length >= 2) {
      try {
        numPalettes = Integer.parseInt(args[1]);
      } catch (NumberFormatException ignore) {
        return;
      }
      if (args.length == 2) {
        handSize = 7;
      }
    }
    if (args.length >= 3) {
      try {
        handSize = Integer.parseInt(args[2]);
      } catch (NumberFormatException ignore) {
        return;
      }
    }

    if (numPalettes < 2 || handSize <= 0 || (numPalettes + handSize) > 35) {
      return;
    }

    RedGameModel<Cards> game = RedGameCreator.createGame(gameType);
    controller.playGame(game, game.getAllCards(), true, numPalettes, handSize);
  }
}
