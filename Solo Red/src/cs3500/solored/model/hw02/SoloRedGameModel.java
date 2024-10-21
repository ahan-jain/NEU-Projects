package cs3500.solored.model.hw02;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

/**
 * represents the model of the Solo Red Card Game.
 */
public class SoloRedGameModel implements RedGameModel<Cards> {

  private List<Cards> canvas;
  protected List<Cards> hand;
  protected int handSize;
  protected List<Cards> deck;
  private int numberOfPalettes;
  protected List<List<Cards>> palettes;
  private Random rand;
  protected boolean hasGameStarted;
  private boolean lost;
  protected boolean playedToCanvas;

  private boolean gameOver;

  /**
   * the default constructor that initialises all values.
   */
  public SoloRedGameModel() {
    this.canvas = new ArrayList<>();
    this.canvas.add(new Cards(Color.RED, 1));
    this.hand = new ArrayList<>();
    this.handSize = 0;
    this.palettes = new ArrayList<>();
    this.numberOfPalettes = 0;
    this.deck = new ArrayList<>();
    this.rand = new Random();
    this.hasGameStarted = false;
    this.lost = false;
    this.playedToCanvas = false;
    this.gameOver = false;
  }

  /**
   * the parameterised constructor that contains a random object to test randomness.
   *
   * @param random represents the random object.
   */
  public SoloRedGameModel(Random random) {
    if (random == null) {
      throw new IllegalArgumentException("Random object cannot be null");
    }
    this.rand = random;
    this.canvas = new ArrayList<>();
    this.canvas.add(new Cards(Color.RED, 1));
    this.hand = new ArrayList<>();
    this.handSize = 0;
    this.palettes = new ArrayList<>();
    this.numberOfPalettes = 0;
    this.deck = new ArrayList<>();
    this.hasGameStarted = false;
    this.lost = false;
    this.playedToCanvas = false;
    this.gameOver = false;
  }

  @Override
  public void playToPalette(int paletteIdx, int cardIdxInHand) {
    this.isGameInProgress();
    if (paletteIdx < 0 || paletteIdx > this.numberOfPalettes - 1) {
      throw new IllegalArgumentException("Invalid palette index");
    }
    if (cardIdxInHand < 0 || cardIdxInHand >= this.hand.size()) {
      throw new IllegalArgumentException("Invalid card index");
    }
    if (paletteIdx == this.winningPaletteIndex()) {
      throw new IllegalStateException("playing to a winning palette");
    }

    Cards c = this.hand.remove(cardIdxInHand);
    this.palettes.get(paletteIdx).add(c);

    this.changedWinningPalette(paletteIdx);
    this.playedToCanvas = false;
  }

  private void changedWinningPalette(int paletteIdx) {

    if (paletteIdx != this.winningPaletteIndex()) {
      this.lost = true;
      this.gameOver = true;
    }
  }

  @Override
  public void playToCanvas(int cardIdxInHand) {
    this.isGameInProgress();
    if (cardIdxInHand < 0 || cardIdxInHand >= this.hand.size()) {
      throw new IllegalArgumentException("Invalid card index");
    }
    if (this.playedToCanvas) {
      throw new IllegalStateException("cannot play to canvas again!");
    }
    if (this.hand.size() == 1) {
      throw new IllegalStateException("Only one card in hand");
    }

    Cards c = this.hand.remove(cardIdxInHand);
    this.canvas.add(c);
    this.playedToCanvas = true;
  }

  @Override
  public void drawForHand() {
    this.isGameInProgress();
    while (this.hand.size() < this.handSize) {
      if (this.deck.isEmpty()) {
        break;
      } else {
        Cards card = this.deck.remove(0);
        this.hand.add(card);
      }
    }
  }


  private void makePalettes(int num, List<Cards> c) {

    this.palettes = new ArrayList<>();
    for (int i = 0; i < num; i++) {
      this.palettes.add(new ArrayList<>(Arrays.asList(c.get(i))));
    }


  }

  @Override
  public void startGame(List<Cards> deck, boolean shuffle, int numPalettes, int handSize) {

    if (hasGameStarted || gameOver) {
      throw new IllegalStateException("Game has started or is over.");
    }

    if (deck == null) {
      throw new IllegalArgumentException("Null deck is not allowed");
    }

    if (deck.size() < (numPalettes + handSize)) {
      throw new IllegalArgumentException("Deck size not large enough");
    }

    if (deck.contains(null)) {
      throw new IllegalArgumentException("Null element");
    }


    for (int i = 0; i < deck.size(); i++) {
      for (int j = i + 1; j < deck.size(); j++) {
        if (deck.get(i).equals(deck.get(j))) {
          throw new IllegalArgumentException("non unique element");
        }
      }
    }

    if (numPalettes < 2 || handSize <= 0) {
      throw new IllegalArgumentException("Invalid palette or hand size");
    }


    this.numberOfPalettes = numPalettes;
    this.handSize = handSize;
    if (shuffle) {
      if (rand != null) {
        Collections.shuffle(deck, this.rand);
      } else {
        Collections.shuffle(deck);
      }
    }

    this.deck = new ArrayList<>(deck);
    this.makePalettes(numPalettes, this.deck.subList(0, numPalettes));
    this.deck = this.deck.subList(numPalettes, this.deck.size());
    this.hasGameStarted = true;
    this.lost = false;
    this.gameOver = false;
    this.drawForHand();
  }

  @Override
  public int numOfCardsInDeck() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    return this.deck.size();
  }

  @Override
  public int numPalettes() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet or is over.");
    }
    return this.numberOfPalettes;
  }

  @Override
  public int winningPaletteIndex() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet.");
    }
    Cards currentRule = this.canvas.get(this.canvas.size() - 1);
    switch (currentRule.getColor()) {
      case RED:
        return this.winningRed();
      case ORANGE:
        return this.winningOrange();
      case BLUE:
        return this.winningBlue();
      case INDIGO:
        return this.winningIndigo();
      case VIOLET:
        return this.winningViolet();
      default:
        throw new IllegalArgumentException("Color not supposed to be invalid!");
    }
  }

  private int winningOrange() {

    List<Integer> frequencyCount = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
    List<Cards> paletteFrequencies = new ArrayList<>();
    List<Cards> repeatedCard = new ArrayList<>();
    List<Integer> highestFrequency = new ArrayList<>();
    for (int i = 0; i < this.numberOfPalettes; i++) {
      List<Cards> palette = this.palettes.get(i);
      for (Cards cards : palette) {
        int val = frequencyCount.get(cards.getNumber() - 1);
        frequencyCount.set(cards.getNumber() - 1, val + 1);
      }
      highestFrequency.add(Collections.max(frequencyCount));
      int maxCardNumber = frequencyCount.lastIndexOf(Collections.max(frequencyCount)) + 1;
      paletteFrequencies.add(getHighestInPalette(repeatedCard, palette, maxCardNumber));
      frequencyCount = new ArrayList<>(Arrays.asList(0, 0, 0, 0, 0, 0, 0));
    }
    return getBestCard(paletteFrequencies, highestFrequency);
  }

  protected Cards getHighestInPalette(List<Cards> repeatedCard,
                                      List<Cards> palette, int maxCardNumber) {
    Cards maxCard;
    for (Cards cards : palette) {
      if (cards.getNumber() == maxCardNumber) {
        repeatedCard.add(cards);
      }
    }
    maxCard = this.getCards(repeatedCard, new Cards(Color.VIOLET, 1));
    return maxCard;
  }

  protected int getBestCard(List<Cards> paletteFrequencies, List<Integer> highestFrequency) {
    int max = Collections.max(highestFrequency);
    for (int x = 0; x < highestFrequency.size(); x++) {
      if (highestFrequency.get(x) != max) {
        paletteFrequencies.set(x, null);
      }
    }
    return paletteFrequencies.indexOf(this.getCards(paletteFrequencies,
            new Cards(Color.VIOLET, 1)));
  }

  private int winningIndigo() {

    int maxRun = 1;
    int longestRun = 1;
    int maxNum;
    Cards maxCard;
    List<Integer> listOfRuns = new ArrayList<>();
    List<Cards> highestRunVal = new ArrayList<>();
    List<Cards> highestRunCards = new ArrayList<>();
    for (int i = 0; i < this.numberOfPalettes; i++) {
      List<Cards> palette = this.palettes.get(i);
      List<Integer> numbers = new ArrayList<>();
      for (Cards card : palette) {
        numbers.add(card.getNumber());
      }
      Collections.sort(numbers);
      maxNum = numbers.get(0);
      longestRun = 1;
      for (int j = 1; j < numbers.size(); j++) {
        if (numbers.get(j) == numbers.get(j - 1) + 1) {
          maxRun++;
          if (maxRun > longestRun) {
            longestRun = maxRun;
          }
          maxNum = numbers.get(j);
        } else if (numbers.get(j) > numbers.get(j - 1) + 1) {
          maxRun = 1;
        }
      }
      if (longestRun == 1) {
        maxNum = Collections.max(numbers);
      }
      listOfRuns.add(longestRun);
      highestRunCards.add(getHighestInPalette(highestRunVal, palette, maxNum));
    }
    maxRun = Collections.max(listOfRuns);
    for (int x = 0; x < listOfRuns.size(); x++) {
      if (listOfRuns.get(x) != maxRun) {
        highestRunCards.set(x, null);
      }
    }
    maxCard = this.getCards(highestRunCards, new Cards(Color.VIOLET, 1));
    return highestRunCards.indexOf(maxCard);
  }

  private int winningRed() {
    List<Cards> listOfMax = new ArrayList<>();
    Cards maxCard;
    for (int i = 0; i < this.numberOfPalettes; i++) {
      List<Cards> palette = this.palettes.get(i);
      maxCard = this.getCards(palette, new Cards(Color.VIOLET, 1));
      listOfMax.add(maxCard);
    }

    maxCard = this.getCards(listOfMax, new Cards(Color.VIOLET, 1));
    return listOfMax.indexOf(maxCard);
  }

  private int winningBlue() {
    List<Integer> listOfUniqueColorPalettes = new ArrayList<>(new ArrayList<>());
    List<Color> listOfUniqueColors = new ArrayList<>();
    int count = 0;
    for (int i = 0; i < this.numberOfPalettes; i++) {
      List<Cards> palette = this.palettes.get(i);
      for (Cards cards : palette) {
        if (!(listOfUniqueColors.contains(cards.getColor()))) {
          listOfUniqueColors.add(cards.getColor());
          count++;
        }
      }
      listOfUniqueColorPalettes.add(count);
      listOfUniqueColors.clear();
      count = 0;
    }
    int maxUniqueColors = Collections.max(listOfUniqueColorPalettes);
    List<Cards> listOfHighestUniqueCards = new ArrayList<>();
    Cards maxCard;
    for (int a = 0; a < listOfUniqueColorPalettes.size(); a++) {
      if (listOfUniqueColorPalettes.get(a) == maxUniqueColors) {
        maxCard = this.getCards(this.palettes.get(a), new Cards(Color.VIOLET, 1));
        listOfHighestUniqueCards.add(maxCard);
      } else {
        listOfHighestUniqueCards.add(null);
      }
    }
    return listOfHighestUniqueCards.indexOf(getCards(listOfHighestUniqueCards,
            new Cards(Color.VIOLET, 1)));
  }

  private int winningViolet() {

    List<Integer> listOfSubFour = new ArrayList<>(new ArrayList<>());
    List<Cards> listOfCards = new ArrayList<>();
    List<Cards> highestSubFour = new ArrayList<>();
    int count = 0;
    Cards maxCard;
    for (int i = 0; i < this.numberOfPalettes; i++) {
      List<Cards> palette = this.palettes.get(i);
      for (Cards cards : palette) {
        if (cards.getNumber() < 4) {
          listOfCards.add(cards);
          count++;
        }
      }
      listOfSubFour.add(count);

      maxCard = this.getCards(listOfCards, new Cards(Color.VIOLET, 1));
      listOfCards.clear();
      highestSubFour.add(maxCard);
      count = 0;
    }
    return getBestCard(highestSubFour, listOfSubFour);
  }

  private Cards getCards(List<Cards> listOfMax, Cards maxCard) {
    for (int i = 0; i < listOfMax.size(); i++) {
      if (listOfMax.get(i) != null) {
        if (listOfMax.get(i).getNumber() > maxCard.getNumber()) {
          maxCard = listOfMax.get(i);
        } else if (listOfMax.get(i).getNumber() == maxCard.getNumber()) {
          if (listOfMax.get(i).getValue() < maxCard.getValue()) {
            maxCard = listOfMax.get(i);
          }
        }
      }
    }
    return maxCard;
  }

  @Override
  public boolean isGameOver() {
    if (!hasGameStarted) {

      throw new IllegalStateException("Game has not started yet or is over.");
    }

    if (this.hand.isEmpty() && this.deck.isEmpty()) {
      gameOver = true;

    }
    return this.lost || gameOver;
  }

  @Override
  public boolean isGameWon() {
    if (!hasGameStarted || !gameOver) {
      throw new IllegalStateException("Game has not started yet or isn't over.");
    }

    return !this.lost && this.isGameOver();
  }

  @Override
  public List<Cards> getHand() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet or is over.");
    }
    return generateList(this.hand);
  }

  @Override
  public List<Cards> getPalette(int paletteNum) {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet or is over.");
    }
    if (paletteNum < 0 || paletteNum > this.numberOfPalettes - 1) {
      throw new IllegalArgumentException("Invalid palette index");
    }

    return generateList(this.palettes.get(paletteNum));
  }

  @Override
  public Cards getCanvas() {
    if (!hasGameStarted) {
      throw new IllegalStateException("Game has not started yet or is over.");
    }
    return this.canvas.get(this.canvas.size() - 1);
  }

  @Override
  public List<Cards> getAllCards() {
    List<Cards> listOfCards = new ArrayList<>();
    for (int i = 1; i <= 7; i++) {
      listOfCards.add(new Cards(Color.RED, i));
      listOfCards.add(new Cards(Color.ORANGE, i));
      listOfCards.add(new Cards(Color.BLUE, i));
      listOfCards.add(new Cards(Color.INDIGO, i));
      listOfCards.add(new Cards(Color.VIOLET, i));
    }
    return listOfCards;
  }

  protected List<Cards> generateList(List<Cards> cardList) {
    List<Cards> copy = new ArrayList<>();
    for (Cards card : cardList) {
      copy.add(new Cards(card.getColor(), card.getNumber()));
    }
    return copy;
  }

  protected void isGameInProgress() {
    if (!hasGameStarted || gameOver) {
      throw new IllegalStateException("Game has not started yet or is over.");
    }
  }

}
