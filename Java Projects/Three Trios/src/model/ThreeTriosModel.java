import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Random;
import java.util.Collections;
import java.util.Set;

import cs3500.controllers.TTModelFeatures;


/**
 * Represents the model component of the Three Trios game.
 * This class manages the game state, including the grid, hands, deck, turns, and game logic.
 */
public class ThreeTriosModel implements TTModel {
  private final List<Card> redHand;
  private final List<Card> blueHand;
  private final List<Card> deck;
  private final Random random;
  private boolean gameStarted;
  private boolean gameOver;

  protected Grid grid;
  private Player turn;
  private final Set<TTModelFeatures> listeners;

  /**
   * A no-arg constructor that initializes this class' variables.
   * INVARIANT: The random object is never null
   * INVARIANT: Red always starts
   */
  public ThreeTriosModel() {
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.deck = new ArrayList<>();
    this.random = new Random();
    this.turn = Player.RED;
    this.gameStarted = false;
    this.listeners = new HashSet<>();
  }

  /**
   * A constructor that takes in a random for testing.
   * INVARIANT: The random object is never null
   * INVARIANT: Red always starts
   *
   * @param rand random seed
   */
  public ThreeTriosModel(Random rand) {
    this.random = Objects.requireNonNull(rand);
    this.redHand = new ArrayList<>();
    this.blueHand = new ArrayList<>();
    this.deck = new ArrayList<>();
    this.turn = Player.RED;
    this.gameStarted = false;
    this.listeners = new HashSet<>();

  }

  @Override
  public void addModelListener(TTModelFeatures listener) {
    listeners.add(listener);
  }

  @Override
  public void removeModelListener(TTModelFeatures listener) {
    listeners.remove(listener);
  }

  @Override
  public void notifyTurn(Player player) {
    for (TTModelFeatures listener : listeners) {
      listener.onTurn(player);
    }
  }

  @Override
  public void notifyGameOver(Player winner) {
    for (TTModelFeatures listener : listeners) {
      listener.onGameOver(winner);
    }
  }

  @Override
  public void startGame(String gridConfigPath, String cardConfigPath) {
    if (gameStarted) {
      throw new IllegalStateException("Game has already started");
    }
    try {
      fillGrid(new File(gridConfigPath));
      fillCards(new File(cardConfigPath));
    } catch (IOException ignore) {
      throw new IllegalArgumentException("path to file might be broken!");
    }

    if (this.deck.size() <= this.grid.countCardCells()) {
      throw new IllegalArgumentException("Invalid deck size");
    }
    this.gameStarted = true;
    Collections.shuffle(this.deck, random); //deck is always shuffled to randomly deal cards

    this.dealHand(Player.RED);
    this.dealHand(Player.BLUE);
    notifyTurn(getTurn());
  }

  /**
   * Determines if the game is in progress or over.
   *
   * @throws IllegalStateException if this is the case.
   */
  protected void isGameInProgress() {
    if (!gameStarted || isGameOver()) {
      throw new IllegalStateException("Game has not started yet or is over.");
    }
  }

  /**
   * Deals out a hand to the given player.
   *
   * @param player the player
   */
  private void dealHand(Player player) {
    for (int i = 0; i < (this.grid.countCardCells() + 1) / 2; i++) {
      Card curCard = this.deck.remove(0);
      curCard.setPlayer(player);
      if (player == Player.RED) {
        redHand.add(curCard);
      }
      if (player == Player.BLUE) {
        blueHand.add(curCard);
      }
    }
  }

  @Override
  public Player findWinningPlayer() {
    if (!gameStarted || !gameOver) {
      throw new IllegalStateException("Game has not started yet or isn't over.");
    }
    if (this.grid.playerCardCount(Player.RED) > this.grid.playerCardCount(Player.BLUE)) {
      return Player.RED;
    } else if (this.grid.playerCardCount(Player.RED) < this.grid.playerCardCount(Player.BLUE)) {
      return Player.BLUE;
    }
    return null;
  }

  /**
   * Creates a grid with the appropriate cells and holes.
   */
  protected void fillGrid(File file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    String line = br.readLine();
    String[] size = line.split(" ");
    this.grid = new Grid(Integer.parseInt(size[0]), Integer.parseInt(size[1]));
    int row = 0;
    try {
      while ((line = br.readLine()) != null) {
        for (int col = 0; col < line.length(); col++) {
          TTCell cell;
          switch (line.charAt(col)) {
            case 'C':
              cell = new CardCell();
              break;
            case 'X':
              cell = new Hole();
              break;
            default:
              throw new IllegalArgumentException("Invalid cell type in file: " + line.charAt(col));
          }
          grid.addCell(row, col, cell);
        }
        row++;
      }
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
    try {
      br.close();
    } catch (IOException e) {
      throw new IllegalArgumentException(e.getMessage());
    }
  }

  /**
   * Fills the deck from the List of provided cards.
   *
   * @param file the file to draw from
   */
  protected void fillCards(File file) throws IOException {
    BufferedReader br = new BufferedReader(new FileReader(file));
    Set<String> checkDupes = new HashSet<>();
    String line;
    while ((line = br.readLine()) != null) {
      this.deck.add(new Card(line));
      String name = line.split(" ")[0];
      checkDupes.add(name);
    }
    if (deck.size() != checkDupes.size()) {
      throw new IllegalArgumentException("duplicate card names!");
    }
  }

  @Override
  public void placeCard(int cardIdxInHand, int row, int col) {
    isGameInProgress();
    if (cardIdxInHand >= blueHand.size() || cardIdxInHand < 0) {
      throw new IllegalArgumentException("card index out of bounds!");
    }
    Card card = this.turn == Player.RED ?
            redHand.get(cardIdxInHand) : blueHand.get(cardIdxInHand);
    this.grid.placeCardHelper(row, col, card);
    if (getTurn() == Player.RED) {
      redHand.remove(cardIdxInHand);
    } else {
      blueHand.remove(cardIdxInHand);
    }
    this.turn = this.turn == Player.RED ? Player.BLUE : Player.RED;
    notifyTurn(this.turn);
  }

  @Override
  public boolean isGameOver() {
    if (!gameStarted) {
      throw new IllegalStateException("Game hasn't started yet!");
    }
    for (TTCell[] tc : this.grid.getGrid()) {
      for (TTCell c : tc) {
        if (c.isEmpty()) {
          gameOver = false;
          return gameOver;
        }
      }
    }
    gameOver = true;
    notifyGameOver(findWinningPlayer());
    return true;
  }

  @Override
  public Player getTurn() {
    if (isGameOver()) {
      this.turn = Player.NONE;
    }
    return this.turn;
  }

  @Override
  public Grid getGrid() {
    return grid;
  }

  @Override
  public List<Card> getHand(Player player) {
    return player == Player.RED ? redHand : blueHand;
  }

  @Override
  public int getScore(Player player) {
    int count = 0;
    for (TTCell[] row : grid.getGrid()) {
      for (TTCell cell : row) {
        if (cell instanceof CardCell && !cell.isEmpty()) {
          Card card = cell.getCard();
          if (card.getPlayer() == player) {
            count++;
          }
        }
      }
    }
    return count + this.getHand(player).size();
  }

  @Override
  public int countFlip(int row, int col, int cardIdxInHand, Player player) {
    Card c = getHand(player).get(cardIdxInHand);
    return grid.countFlip(row, col, c, false);
  }

  @Override
  public Card getContentsAt(int row, int col) {
    return this.grid.getContentsAt(row, col);
  }

  @Override
  public Player getPlayerAt(int row, int col) {
    Card card = getContentsAt(row, col);
    if (card != null) {
      return card.getPlayer();
    } else {
      return null;
    }
  }

}
