package cs3500.solored;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.Color;
import cs3500.solored.model.hw02.RedGameModel;

/**
 * represents a mock of a model that always wins.
 */
public class MockSoloRedGameModel implements RedGameModel {
  private final Appendable log;
  public boolean gameLost;

  public MockSoloRedGameModel(Appendable log) {
    this.log = log;
  }

  @Override
  public void startGame(List deck, boolean shuffle, int numPalettes, int handSize) {
    try {
      log.append("startGame called with deck size: ").append(String.valueOf(deck.size()))

              .append(", shuffle: ").append(String.valueOf(shuffle))
              .append(", numPalettes: ").append(String.valueOf(numPalettes))
              .append(", handSize: ").append(String.valueOf(handSize)).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log startGame", e);
    }
  }

  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    try {
      log.append("playToPalette called with paletteIdx: ").append(String.valueOf(paletteIdx))
              .append(", cardIdxInHand: ").append(String.valueOf(cardIdxInHand)).append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log playToPalette", e);
    }
  }

  @Override
  public void playToCanvas(int cardIdxInHand) {
    try {
      log.append("playToCanvas called with cardIdxInHand: ").append(String.valueOf(cardIdxInHand))
              .append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log playToCanvas", e);
    }
  }

  @Override
  public void drawForHand() {
    try {
      log.append("drawForHand called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log drawForHand", e);
    }
  }

  @Override
  public boolean isGameOver() {
    try {
      log.append("isGameOver called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log isGameOver", e);
    }
    return true;
  }

  @Override
  public boolean isGameWon() {
    try {
      log.append("isGameWon called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log isGameWon", e);
    }
    return true;
  }

  @Override
  public int numOfCardsInDeck() {
    try {
      log.append("numOfCardsInDeck called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log numOfCardsInDeck", e);
    }
    return 0;
  }

  @Override
  public int numPalettes() {
    try {
      log.append("numPalettes called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log numPalettes", e);
    }
    return 0;
  }

  @Override
  public int winningPaletteIndex() {
    try {
      log.append("winningPaletteIndex called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log isGameWon", e);

    }
    return 0;
  }

  @Override
  public List<Cards> getHand() {
    try {
      log.append("getHand called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log getHand", e);
    }
    return new ArrayList<>();
  }

  @Override
  public List<Cards> getPalette(int paletteNum) {
    try {
      log.append("getPalette called with paletteNum: ").append(String.valueOf(paletteNum))
              .append("\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log getPalette", e);
    }
    return new ArrayList<>();
  }

  @Override
  public Cards getCanvas() {
    try {
      log.append("getCanvas called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log getCanvas", e);
    }
    return new Cards(Color.RED, 1);
  }

  @Override
  public List<Cards> getAllCards() {
    try {
      log.append("getAllCards called\n");
    } catch (IOException e) {
      throw new RuntimeException("Failed to log getAllCards", e);
    }
    return new ArrayList<>();
  }
}
