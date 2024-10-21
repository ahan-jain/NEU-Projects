package cs3500.solored;

import org.junit.Test;

import org.junit.Assert;

import cs3500.solored.model.hw02.Cards;
import cs3500.solored.model.hw02.Color;

/**
 * representes tests for the Cards class.
 */
public class CardsTests {

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidCardNumberTooLow() {
    new Cards(Color.RED, 0);
    new Cards(Color.RED, 8);
  }


  @Test
  public void testValidCardCreation() {
    Cards card = new Cards(Color.RED, 5);
    Assert.assertEquals("R5", card.toString());
  }

  @Test
  public void testToStringMethod() {
    Cards redCard = new Cards(Color.RED, 3);
    Cards blueCard = new Cards(Color.BLUE, 1);
    Cards violetCard = new Cards(Color.VIOLET, 7);

    Assert.assertEquals("R3", redCard.toString());
    Assert.assertEquals("B1", blueCard.toString());
    Assert.assertEquals("V7", violetCard.toString());
  }

  @Test
  public void testEqualsMethod() {
    Cards card1 = new Cards(Color.RED, 4);
    Cards card2 = new Cards(Color.RED, 4);
    Cards card3 = new Cards(Color.BLUE, 4);

    Assert.assertTrue(card1.equals(card2));
    Assert.assertFalse(card1.equals(card3));
  }

  @Test
  public void testHashCodeConsistency() {
    Cards card1 = new Cards(Color.ORANGE, 2);
    Cards card2 = new Cards(Color.ORANGE, 2);

    Assert.assertEquals(card1.hashCode(), card2.hashCode());
  }

  @Test
  public void testGetValue() {
    Cards redCard = new Cards(Color.RED, 4);
    Cards blueCard = new Cards(Color.BLUE, 5);
    Cards violetCard = new Cards(Color.VIOLET, 7);

    Assert.assertEquals(0, redCard.getValue());
    Assert.assertEquals(2, blueCard.getValue());
    Assert.assertEquals(4, violetCard.getValue());
  }

  @Test(expected = NullPointerException.class)
  public void testNullValue() {
    new Cards(null, 4).getValue();
    new Cards(null, 7).getValue();
  }
}
