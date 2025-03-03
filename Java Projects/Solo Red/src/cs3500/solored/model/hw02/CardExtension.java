package cs3500.solored.model.hw02;

/**
 * Behaviors for a Card in the Game of RedSeven.
 * Any additional behaviors for cards must be made
 * creating a new interface that extends this one.
 */
public interface CardExtension extends Card {

  /**
   * checks for equality between a card and an Object o.
   * @param o is the Object being compared with a card
   * @return whether they are equal or not
   */
  boolean equals(Object o);

  /**
   * returns the hashcode of an object.
   * @return a number denoting the hashcode
   */
  int hashCode();

  /**
   * returns the greater of two colors of the cards according to the hierarchy.
   * @return a number value according to the colors present
   */
  int getValue();

  /**
   * returns the number of the card.
   * @return the number of the card
   */
  int getNumber();

  /**
   * returns the color of the card.
   * @return the color of the card
   */
  Color getColor();

}
