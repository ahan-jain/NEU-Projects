package cs3500.solored.controller;

import java.io.IOException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Scanner;

import cs3500.solored.model.hw02.Card;
import cs3500.solored.model.hw02.RedGameModel;
import cs3500.solored.view.hw02.SoloRedGameTextView;


/**
 * represents the controller for the Solo Red card game.
 */
public class SoloRedTextController implements RedGameController {
  private final Appendable ap;
  private final Scanner sc;
  private String command;
  private Boolean quit;

  /**
   * represents the constructor for the SoloRedTextController controller class.
   *
   * @param rd represents the readable object
   * @param ap represents the appendable object
   */
  public SoloRedTextController(Readable rd, Appendable ap) {
    if (rd == null || ap == null) {
      throw new IllegalArgumentException("Readable and/or Appendable cannot be null.");
    }
    this.ap = ap;
    this.sc = new Scanner(rd);
    this.quit = false;

  }

  // A method to handle writing messages and catching IOException
  protected void writeMessage(String message, boolean newline) throws IllegalStateException {
    try {
      if (newline) {
        ap.append(message).append("\n");
      } else {
        ap.append(message);
      }
    } catch (IOException e) {
      throw new IllegalStateException(e.getMessage());
    }
  }

  @Override
  public <C extends Card> void playGame(RedGameModel<C> model, List<C> deck, boolean shuffle,
                                        int numPalettes, int handSize) {
    checkModel(model, deck, shuffle, numPalettes, handSize);
    SoloRedGameTextView<C> view = new SoloRedGameTextView<>(model, ap);
    try {
      view.render();
    } catch (IOException e) {
      throw new IllegalStateException("Could not render state" + e.getMessage());
    }
    writeMessage("\nNumber of cards in deck: " + model.numOfCardsInDeck(), true);
    while (!model.isGameOver()) {
      try {
        command = getCommand();
      } catch (NoSuchElementException e) {
        throw new IllegalStateException("Cannot process input: " + e.getMessage());
      }

      processCommands(model);

      if (quit) {
        quitMessage(model, view);
        break;
      }
      try {
        view.render();
      } catch (IOException e) {
        throw new IllegalStateException("Could not render state" + e.getMessage());
      }
      writeMessage("\nNumber of cards in deck: " + model.numOfCardsInDeck(), true);
    }
    if (!quit) {
      endGame(model, view);
    }
  }

  private <C extends Card> void processCommands(RedGameModel<C> model) {
    switch (command) {
      case "q":
      case "Q":
        quit = true;
        break;

      case "palette":
        int paletteIndex = getValidInteger(sc);
        if (paletteIndex == -10) {
          quit = true;
          break;
        }
        int cardIndex = getValidInteger(sc);
        if (cardIndex == -10) {
          quit = true;
          break;
        }
        try {
          model.playToPalette(paletteIndex, cardIndex);
          if (!model.isGameOver()) {
            model.drawForHand();
          }
        } catch (IllegalArgumentException | IllegalStateException e) {
          writeMessage("Invalid move. Try again. " + e.getMessage(), true);
        }
        break;
      case "canvas":
        cardIndex = getValidInteger(sc);
        if (cardIndex == -10) {
          quit = true;
          break;
        }
        try {
          model.playToCanvas(cardIndex);

        } catch (IllegalArgumentException | IllegalStateException e) {
          writeMessage("Invalid move. Try again. " + e.getMessage(), true);
        }
        break;
      default:
        writeMessage("Invalid command. Try again.", true);
        break;
    }
  }

  private <C extends Card> void quitMessage(RedGameModel<C> model, SoloRedGameTextView<C> view) {
    writeMessage("Game quit!", true);
    writeMessage("State of game when quit:", true);
    try {
      view.render();
    } catch (IOException e) {
      throw new IllegalStateException("Could not render state" + e.getMessage());
    }
    writeMessage("\nNumber of cards in deck: " + model.numOfCardsInDeck(), false);
  }

  private <C extends Card> void endGame(RedGameModel<C> model, SoloRedGameTextView<C> view) {
    if (model.isGameWon()) {
      writeMessage("Game won.", true);
    } else {
      writeMessage("Game lost.", true);
    }
    try {
      view.render();
    } catch (IOException e) {
      throw new IllegalStateException("Could not render state" + e.getMessage());
    }
    writeMessage("\nNumber of cards in deck: " + model.numOfCardsInDeck(), true);
  }

  private int getValidInteger(Scanner sc) {
    int index;
    while (sc.hasNext()) {
      if (sc.hasNextInt()) {
        index = sc.nextInt();
        if (index > -1) {
          return index - 1;
        }
      } else {
        String input = sc.next();
        if (input.equalsIgnoreCase("q")) {
          return -10;
        }
      }
    }
    throw new IllegalStateException("Could not transmit.");
  }

  private String getCommand() {
    try {
      return sc.next();
    } catch (NoSuchElementException e) {
      throw new IllegalStateException("Cannot process input: " + e.getMessage());
    }
  }

  private <C extends Card> void checkModel(RedGameModel<C> model, List<C> deck, boolean shuffle,
                                           int numPalettes, int handSize) {
    if (model == null) {
      throw new IllegalArgumentException("Invalid model, deck, palettes, or hand size.");
    }
    try {
      model.startGame(deck, shuffle, numPalettes, handSize);
    } catch (IllegalStateException | IllegalArgumentException e) {
      throw new IllegalArgumentException("Game cannot start" + e.getMessage());
    }
  }
}










