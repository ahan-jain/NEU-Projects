/**
 * A concrete representation of a Player's card in the Three Trios Game. It has 4 digits,
 * in every cardinal direction which represents the strength of that card against other cards
 * for that direction, has a unique name and belongs to one of the two players.
 */
public class Card {
  private final String cardName;
  private final AttackValue north;
  private final AttackValue south;
  private final AttackValue east;
  private final AttackValue west;
  private Player player;

  /**
   * A constructor that initializes this card to the provided values.
   *
   * @param cardName the unique string identifier assigned to the card
   * @param north    the number at the top of the card
   * @param south    the number at the bottom of the card
   * @param east     the number on the right side of the card
   * @param west     the number on the left side of the card
   */
  public Card(String cardName, AttackValue north,
              AttackValue south, AttackValue east, AttackValue west, Player player) {
    this.cardName = cardName;
    this.north = north;
    this.south = south;
    this.east = east;
    this.west = west;
    this.player = player;
  }

  /**
   * Constructor for a card that parses a line of the configuration file to create the card.
   *
   * @param line the info that will be parsed to create the card
   */
  public Card(String line) {
    String[] params = line.split(" ");
    this.cardName = params[0];
    this.north = AttackValue.getName(params[1]);
    this.south = AttackValue.getName(params[2]);
    this.east = AttackValue.getName(params[3]);
    this.west = AttackValue.getName(params[4]);
    this.player = null;
  }

  /**
   * Sets this.player to the given value
   *
   * @param player player
   */
  public void setPlayer(Player player) {
    this.player = player;
  }

  /**
   * Flips the card to the other team.
   */
  public void flip() {
    this.player = this.player == Player.BLUE ? Player.RED : Player.BLUE;
  }

  /**
   * Returns the owner of the card.
   *
   * @return the owner of the card
   */
  public Player getPlayer() {
    return this.player;
  }

  /**
   * Returns whether this other wins against the other.
   *
   * @return win or not
   * @throws IllegalArgumentException if direction is not "north", "south", "east" or "west"
   */
  public boolean win(Card that, String direction) {
    switch (direction) {
      case ("north"):
        return this.north.compareTo(that.getSouth()) > 0;
      case ("south"):
        return this.south.compareTo(that.getNorth()) > 0;
      case ("east"):
        return this.east.compareTo(that.getWest()) > 0;
      case ("west"):
        return this.west.compareTo(that.getEast()) > 0;
      default:
        throw new IllegalArgumentException("invalid direction!");
    }
  }

  /**
   * Gets the north value of this card.
   *
   * @return AttackValue north
   */
  public AttackValue getNorth() {
    return this.north;
  }

  /**
   * Gets the south value of this card.
   *
   * @return AttackValue south
   */
  public AttackValue getSouth() {
    return this.south;
  }

  /**
   * Gets the east value of this card.
   *
   * @return AttackValue east
   */
  public AttackValue getEast() {
    return this.east;
  }

  /**
   * Gets the west value of this card.
   *
   * @return AttackValue west
   */
  public AttackValue getWest() {
    return this.west;
  }

  /**
   * Gets the name of this card.
   *
   * @return the unique name of the card
   */
  public String getCardName() {
    return this.cardName;
  }

}
