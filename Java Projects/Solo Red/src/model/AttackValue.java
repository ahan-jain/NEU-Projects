import java.util.HashMap;
import java.util.Map;

/**
 * Represents an attack value in the TT game. Four of these are stored by a card.
 */
public enum AttackValue {
  ONE(1),
  TWO(2),
  THREE(3),
  FOUR(4),
  FIVE(5),
  SIX(6),
  SEVEN(7),
  EIGHT(8),
  NINE(9),
  A(10);

  private final int value;
  private static final Map<String, AttackValue> NAME_MAP = new HashMap<>();


  /**
   * A constructor that takes in a value respective to the AttackValue.
   *
   * @param value is the inputted integer corresponding to the AttackValue
   */
  AttackValue(int value) {
    this.value = value;
  }


  /**
   * Outputs the AttackValue based on the inputted string.
   *
   * @param s the string input
   * @return the corresponding AttackValue
   */
  public static AttackValue getName(final String s) {
    for (AttackValue attackValue : AttackValue.values()) {
      NAME_MAP.put(String.valueOf(attackValue.value), attackValue);
    }
    NAME_MAP.put("A", A);
    AttackValue attackValue = NAME_MAP.get(s);
    if (attackValue == null) {
      throw new IllegalArgumentException("Invalid attack value: " + s);
    }
    return attackValue;
  }

  /**
   * Returns the int value of the AttackValue.
   *
   * @return this int
   */
  public int getValue() {
    return this.value;
  }

  /**
   * outputs a string version of the value.
   *
   * @return the value in String form
   */
  public String getStringValue() {
    if (this.value < 10) {
      return String.valueOf(value);
    } else {
      return "A";
    }
  }
}
