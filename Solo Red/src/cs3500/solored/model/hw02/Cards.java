package cs3500.solored.model.hw02;

import java.util.Objects;

/**
 * represents a card in the Solo Red Game.
 */
public class Cards implements CardExtension {
  private final int number;
  private final Color color;


  /**
   * represents the constructor of the cards class.
   *
   * @param color  the color of the card
   * @param number the value of the card (between 1 and 7)
   */
  public Cards(Color color, int number) {
    if (number < 1 || number > 7) {
      throw new IllegalArgumentException("Invalid card value");
    }
    this.color = color;
    this.number = number;

  }


  @Override
  public String toString() {
    switch (this.color) {
      case RED:
        return "R" + this.number;
      case ORANGE:
        return "O" + this.number;
      case BLUE:
        return "B" + this.number;
      case INDIGO:
        return "I" + this.number;
      case VIOLET:
        return "V" + this.number;
      default:
        throw new IllegalArgumentException("switch case not supposed to fail!");
    }
  }

  @Override
  public boolean equals(Object o) {
    if (o instanceof Cards) {
      Cards other = (Cards) o;
      return number == other.number && color == other.color;
    }
    else {
      return false;
    }
  }

  @Override
  public int hashCode() {
    return Objects.hash(this.color, this.number);
  }

  @Override
  public int getValue() {
    switch (this.color) {
      case RED:
        return 0;
      case ORANGE:
        return 1;
      case BLUE:
        return 2;
      case INDIGO:
        return 3;
      case VIOLET:
        return 4;
      default:
        throw new IllegalArgumentException("switch case not supposed to fail!");
    }
  }

  @Override
  public int getNumber() {
    return this.number;
  }

  @Override
  public Color getColor() {
    return this.color;
  }

}
